package snownee.researchtable.client.gui;

import java.io.IOException;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.client.AdvancedFontRenderer;
import snownee.kiwi.client.gui.GuiContainerMod;
import snownee.kiwi.client.gui.GuiControl;
import snownee.kiwi.client.gui.component.Component;
import snownee.kiwi.client.gui.component.ComponentPanel;
import snownee.kiwi.network.NetworkChannel;
import snownee.researchtable.block.TileTable;
import snownee.researchtable.container.ContainerTable;
import snownee.researchtable.core.ResearchCategory;
import snownee.researchtable.network.PacketResearchChanged;
import snownee.researchtable.network.PacketResearchChanged.Action;

@SideOnly(Side.CLIENT)
public class GuiTable extends GuiContainerMod
{
    private final TileTable table;
    private ComponentResearchDetail detail;
    private ComponentResearchList researchList;

    public GuiTable(TileTable tile, InventoryPlayer inventory)
    {
        super(new ContainerTable(tile, inventory));
        this.table = tile;
        xSize = 256;
        ySize = 158;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        AdvancedFontRenderer.INSTANCE.setUnicodeFlag(true);
        ComponentPanel panel = new ComponentPanel(control, xSize, ySize);
        researchList = new ComponentResearchList(panel.control, (int) ((xSize - 8) * 0.4), ySize - 8, 0, 0, 20, width,
                height);
        // ResearchList.LIST.clear();
        //        int r = new Random().nextInt(6) + 1;
        //        List<ICondition> conditions = new ArrayList<>(8);
        //        conditions.add(new ConditionCrTStack(CraftTweakerMC.getOreDict("blockGlass").amount(1000)));
        //        for (int i = 0; i < r; i++)
        //        {
        //            conditions.add(new ConditionCrTStack(CraftTweakerMC.getIItemStack(new ItemStack(Items.CLAY_BALL, 256))));
        //        }
        //        ResearchList.LIST.add(new Research("hello", ResearchCategory.GENERAL, "hello", "À²À²À²",
        //                ImmutableSet.of("stageA", "stageB"), Collections.EMPTY_LIST, conditions, null));
        researchList.setCategory(ResearchCategory.GENERAL);
        detail = new ComponentResearchDetail(panel.control, (int) ((xSize - 8) * 0.6), ySize - 8,
                researchList.left + researchList.width, 0, width, height);
        detail.visible = false;
        detail.researching = table.getResearch();
        if (detail.researching != null)
        {
            detail.setResearch(detail.researching, table.canComplete());
            table.hasChanged = true;
        }
        control.addComponent(panel);
        panel.control.addComponent(researchList);
        panel.control.addComponent(detail);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        if (table.hasChanged)
        {
            detail.researching = table.getResearch();
            if (detail != null)
            {
                detail.updateResearching(table.canComplete());
                if (detail.getResearch() != null)
                {
                    List<ComponentResearchProgress> progresses = detail.control
                            .getComponents(ComponentResearchProgress.class);
                    boolean flag = table.getResearch() == detail.getResearch();
                    for (int i = 0; i < progresses.size(); ++i)
                    {
                        ComponentResearchProgress progress = progresses.get(i);
                        progress.setProgress(flag ? table.getProgress(i) : 0);
                        progress.setResearching(flag);
                    }
                }
            }
            table.hasChanged = false;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public static boolean isInRegion(int left, int top, int right, int bottom, int x, int y)
    {
        return x >= left && x < right && y >= top && y < bottom;
    }

    @Override
    public int messageReceived(GuiControl control, Component component, NBTTagCompound data)
    {
        return 0;
    }

    @Override
    public int messageReceived(GuiControl control, Component component, int param1, int param2)
    {
        if (component.getClass() == ComponentButtonList.class)
        {
            if (!table.hasPermission(mc.player))
            {
                return 0;
            }
            if (param1 == 0) // param1 == button id
            {
                if (table.getResearch() == detail.getResearch())
                {
                    PacketResearchChanged packet = new PacketResearchChanged(table.getPos(), table.getResearch(),
                            Action.SUBMIT);
                    NetworkChannel.INSTANCE.sendToServer(packet);
                }
            }
            else if (param1 == 1) // param1 == button id
            {
                if (table.getResearch() == null) // no research doing
                {
                    if (detail.getResearch() != null)
                    {
                        PacketResearchChanged packet = new PacketResearchChanged(table.getPos(), detail.getResearch(),
                                Action.START);
                        NetworkChannel.INSTANCE.sendToServer(packet);
                        return 0;
                    }
                }
                else
                {
                    if (detail.getResearch() == table.getResearch())
                    {
                        Action action = table.canComplete() ? Action.COMPLETE : Action.STOP;
                        if (action == Action.STOP && !GuiScreen.isShiftKeyDown())
                        {
                            // TODO: toast
                            return 0;
                        }
                        PacketResearchChanged packet = new PacketResearchChanged(table.getPos(), table.getResearch(),
                                action);
                        NetworkChannel.INSTANCE.sendToServer(packet);
                        return 0;
                    }
                }
            }
        }
        else if (component.getClass() == ComponentResearchList.class)
        {
            if (detail != null)
            {
                detail.setResearch(researchList.researches.get(param1), table.canComplete()); // param1 == index
                table.hasChanged = true;
            }
        }
        return 0;
    }

    @Override
    public void onGuiClosed()
    {
        researchList = null;
        detail = null;
        super.onGuiClosed();
    }

}
