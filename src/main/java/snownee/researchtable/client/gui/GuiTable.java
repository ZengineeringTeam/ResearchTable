package snownee.researchtable.client.gui;

import java.io.IOException;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.List;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.client.AdvancedFontRenderer;
import snownee.kiwi.client.gui.GuiContainerMod;
import snownee.kiwi.client.gui.GuiControl;
import snownee.kiwi.client.gui.component.Component;
import snownee.kiwi.client.gui.component.ComponentPanel;
import snownee.kiwi.client.gui.element.DrawableResource;
import snownee.kiwi.network.NetworkChannel;
import snownee.kiwi.util.NBTHelper;
import snownee.researchtable.ModConfig;
import snownee.researchtable.ResearchTable;
import snownee.researchtable.block.TileTable;
import snownee.researchtable.container.ContainerTable;
import snownee.researchtable.core.Research;
import snownee.researchtable.core.ResearchList;
import snownee.researchtable.network.PacketResearchChanged;
import snownee.researchtable.network.PacketResearchChanged.Action;

@SideOnly(Side.CLIENT)
public class GuiTable extends GuiContainerMod {
    public static float ticks;
    public static NBTTagCompound data;
    private final TileTable table;
    private ComponentResearchDetail detail;
    private ComponentResearchList researchList;
    private DrawableResource globe;
    private List<String> scoreText;
    private int listWidth = ModConfig.guiListWidth;

    public GuiTable(TileTable tile, InventoryPlayer inventory) {
        super(new ContainerTable(tile, inventory));
        this.table = tile;
        data = table.getData();
        fontRenderer = AdvancedFontRenderer.INSTANCE;
        AdvancedFontRenderer.INSTANCE.setUnicodeFlag(true);
        if (ModConfig.guiListAutoWidth) {
            int titleWidth = ResearchList.LIST.values().stream().map(Research::getTitle).mapToInt(fontRenderer::getStringWidth).max().orElse(0);
            listWidth = Math.max(listWidth, 40 + titleWidth);
        }
    }

    @Override
    public void initGui() {
        int guiDetailWidth;
        if (ModConfig.guiFullScreen) {
            xSize = width + 8;
            ySize = height + 8;
            guiDetailWidth = width - listWidth;
        } else {
            guiDetailWidth = ModConfig.guiDetailWidth;
            xSize = listWidth + guiDetailWidth + 8;
            ySize = ModConfig.guiHeight;
        }

        data = table.getData();
        super.initGui();
        ComponentPanel panel = new ComponentPanel(control, xSize, ySize);
        boolean showTabs = ResearchList.CATEGORIES.size() > 1;
        researchList = new ComponentResearchList(panel.control, listWidth, ySize - 8, 0, 0, 20, width, height, showTabs);
        // ResearchList.LIST.clear();
        //        int r = new Random().nextInt(6) + 1;
        //        List<ICondition> conditions = new ArrayList<>(8);
        //        conditions.add(new ConditionCrTStack(CraftTweakerMC.getOreDict("blockGlass").amount(1000)));
        //        for (int i = 0; i < r; i++)
        //        {
        //            conditions.add(new ConditionCrTStack(CraftTweakerMC.getIItemStack(new ItemStack(Items.CLAY_BALL, 256))));
        //        }
        //        ResearchList.LIST.add(new Research("hello", ResearchCategory.GENERAL, "hello", "������",
        //                ImmutableSet.of("stageA", "stageB"), Collections.EMPTY_LIST, conditions, null));
        if (!ResearchList.CATEGORIES.isEmpty()) {
            researchList.setCategory(ResearchList.CATEGORIES.get(0));
        }
        if (showTabs) {
            guiDetailWidth -= ComponentResearchList.TAB_WIDTH;
        }
        Research displaying = null;
        if (detail != null) {
            displaying = detail.getResearch();
        }
        detail = new ComponentResearchDetail(panel.control, guiDetailWidth, ySize - 8, researchList.left + listWidth, 0, width, height);
        detail.visible = false;
        detail.researching = table.getResearch();
        if (displaying != null) {
            detail.setResearch(displaying, table.canComplete());
            table.hasChanged = true;
        } else if (detail.researching != null) {
            detail.setResearch(detail.researching, table.canComplete());
            table.hasChanged = true;
        }
        control.addComponent(panel);
        panel.control.addComponent(researchList);
        panel.control.addComponent(detail);

        if (ResearchTable.scoreFormattingText != null) {
            boolean failed = false;
            Integer[] values = new Integer[ResearchTable.scores.length];
            int i = 0;
            NBTHelper helper = NBTHelper.of(data);
            for (String s : ResearchTable.scores) {
                values[i] = helper.getInt("score." + s, 0);
                ++i;
            }
            if (!failed) {
                String string = ResearchTable.scoreFormattingText;
                if (I18n.hasKey(string)) {
                    string = I18n.format(ResearchTable.scoreFormattingText, (Object[]) values);
                } else {
                    try {
                        string = String.format(ResearchTable.scoreFormattingText, (Object[]) values);
                    } catch (IllegalFormatException var5) {
                        string = "Format error: " + string;
                    }
                }
                scoreText = Arrays.asList(string.split("\\n"));
                globe = new DrawableResource(new ResourceLocation(ResearchTable.MODID, "textures/gui/globe.png"), 0, 0, 11, 10, 0, 0, 0, 0, 11, 10);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (!GuiScreen.isCtrlKeyDown()) {
            ticks += partialTicks;
        }
        if (table.hasChanged) {
            data = table.getData();
            if (detail != null) {
                detail.researching = table.getResearch();
                detail.updateResearching(table.canComplete());
                researchList.setCategory(researchList.category);
                if (detail.getResearch() != null) {
                    List<ComponentResearchProgress> progresses = detail.control.getComponents(ComponentResearchProgress.class);
                    boolean flag = table.getResearch() == detail.getResearch();
                    for (int i = 0; i < progresses.size(); ++i) {
                        ComponentResearchProgress progress = progresses.get(i);
                        progress.setProgress(flag ? table.getProgress(i) : 0);
                        progress.setResearching(flag);
                    }
                }
            }
            table.hasChanged = false;
        }
        drawScreenInternal(mouseX, mouseY, partialTicks);
        if (globe != null && scoreText != null) {
            GlStateManager.color(1, 1, 1, 1);
            RenderHelper.enableGUIStandardItemLighting();
            int x = (width + xSize) / 2 - 18;
            int y = (height + ySize) / 2 - 18;
            globe.draw(mc, x, y);
            if (isInRegion(x, y, x + 11, y + 11, mouseX, mouseY)) {
                drawHoveringText(scoreText, mouseX, mouseY);
            }
        }
    }

    private void drawScreenInternal(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        control.drawScreen(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (tooltip != null && !tooltip.isEmpty()) {
            if (tooltipFont == null) {
                tooltipFont = fontRenderer;
            }
            drawHoveringText(tooltip, mouseX, mouseY, tooltipFont);
        }
        this.tooltip = null;
        this.tooltipFont = null;
    }

    public static boolean isInRegion(int left, int top, int right, int bottom, int x, int y) {
        return x >= left && x < right && y >= top && y < bottom;
    }

    @Override
    public int messageReceived(GuiControl control, Component component, NBTTagCompound data) {
        return 0;
    }

    @Override
    public int messageReceived(GuiControl control, Component component, int param1, int param2) {
        if (component.getClass() == ComponentButtonList.class) {
            if (!table.hasPermission(mc.player)) {
                return 0;
            }
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            if (param1 == 0) // param1 == button id
            {
                if (table.getResearch() == detail.getResearch()) {
                    PacketResearchChanged packet = new PacketResearchChanged(table.getPos(), table.getResearch(), Action.SUBMIT);
                    NetworkChannel.INSTANCE.sendToServer(packet);
                }
            } else if (param1 == 1) // param1 == button id
            {
                if (table.getResearch() == null) // no research doing
                {
                    if (detail.getResearch() != null) {
                        PacketResearchChanged packet = new PacketResearchChanged(table.getPos(), detail.getResearch(), Action.START);
                        NetworkChannel.INSTANCE.sendToServer(packet);
                        return 0;
                    }
                } else {
                    if (detail.getResearch() == table.getResearch()) {
                        Action action = table.canComplete() ? Action.COMPLETE : Action.STOP;
                        if (action == Action.STOP && !GuiScreen.isShiftKeyDown()) {
                            return 0;
                        }
                        PacketResearchChanged packet = new PacketResearchChanged(table.getPos(), table.getResearch(), action);
                        NetworkChannel.INSTANCE.sendToServer(packet);
                        return 0;
                    }
                }
            }
        } else if (component.getClass() == ComponentResearchList.class) {
            if (detail != null) {
                detail.setResearch(researchList.researches.get(param1), table.canComplete()); // param1 == index
                table.hasChanged = true;
            }
        }
        return 0;
    }

    public void resetProgress() {
        if (detail == null)
            return;
        List<ComponentResearchProgress> components = detail.control.getComponents(ComponentResearchProgress.class);
        for (ComponentResearchProgress component : components) {
            component.resetRenderer();
        }
    }

    @Override
    public void onGuiClosed() {
        researchList = null;
        detail = null;
        data = null;
        super.onGuiClosed();
    }

}
