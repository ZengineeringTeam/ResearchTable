package snownee.researchtable.client.gui;

import java.util.Collections;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.client.AdvancedFontRenderer;
import snownee.kiwi.client.gui.GuiControl;
import snownee.kiwi.client.gui.component.ComponentList;
import snownee.kiwi.util.Util;
import snownee.researchtable.ModConfig;
import snownee.researchtable.core.DataStorage;
import snownee.researchtable.core.Research;
import snownee.researchtable.core.ResearchCategory;
import snownee.researchtable.core.ResearchList;

@SideOnly(Side.CLIENT)
public class ComponentResearchList extends ComponentList {
    public static final int TAB_WIDTH = 28;

    final List<Research> researches = Lists.newLinkedList();
    final List<GuiButtonStack> btns = Lists.newLinkedList();
    ResearchCategory category;

    private final int slotHeight;
    private final boolean showTabs;

    public ComponentResearchList(GuiControl parent, int width, int height, int left, int top, int entryHeight, int screenWidth, int screenHeight, boolean showTabs) {
        super(parent, width, height, showTabs ? left + TAB_WIDTH : left, top, screenWidth, screenHeight);
        this.slotHeight = entryHeight;
        this.showTabs = showTabs;
    }

    public void setCategory(ResearchCategory category) {
        if (category == null) {
            return;
        }
        this.category = category;
        researches.clear();

        List<Research> researchesAvailable = Lists.newLinkedList();
        List<Research> researchesUnavailable = Lists.newLinkedList();
        List<Research> researchesCompleted = Lists.newLinkedList();

        for (Research research : ResearchList.LIST.values()) {
            if (research.getCategory() != category) {
                continue;
            }
            if (research.canResearch(parent.mc.player, GuiTable.data)) {
                researchesAvailable.add(research);
            } else {
                if (DataStorage.count(parent.mc.player.getGameProfile().getId(), research) > 0) {
                    if (!ModConfig.hideCompletedResearch) {
                        researchesCompleted.add(research);
                    }
                } else {
                    if (!ModConfig.hideUnavailableResearch) {
                        researchesUnavailable.add(research);
                    }
                }
            }
        }
        researches.addAll(researchesAvailable);
        researches.addAll(researchesUnavailable);
        researches.addAll(researchesCompleted);
        if (showTabs) {
            if (btns.isEmpty()) {
                int btnLeft = left - TAB_WIDTH;
                int btnTop = top;
                int id = 114514;
                for (ResearchCategory category2 : ResearchList.CATEGORIES) {
                    GuiButtonStack btn = new GuiButtonStack(id++, btnLeft, btnTop, category2.icon) {
                        @Override
                        public void onClick() {
                            parent.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                            setCategory(category2);
                        }

                        @Override
                        public boolean isSelected() {
                            return ComponentResearchList.this.category == category2;
                        }

                        @Override
                        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
                            super.drawButton(mc, mouseX, mouseY, partialTicks);
                            if (category2.nameKey != null && visible && hovered) {
                                String tooltip = category2.nameKey;
                                if (I18n.hasKey(tooltip)) {
                                    tooltip = I18n.format(tooltip);
                                }
                                setTooltip(Collections.singletonList(tooltip), null);
                            }
                        }
                    };
                    btns.add(btn);
                    btnTop += TAB_WIDTH;
                }
            }
        }
    }

    @Override
    protected int getSize() {
        return researches.size();
    }

    @Override
    protected void elementClicked(int index, int mouseX, int mouseY, boolean doubleClick) {
        sendMessage(index, 0);
    }

    @Override
    protected void drawBackground() {
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        boolean hovering = slotIdx == hoveringIndex;
        Gui.drawRect(left, slotTop, left + width, slotTop + slotHeight - 1, hovering ? 0xFFDDDDDD : 0xFFEEEEEE);
        Research research = researches.get(slotIdx);
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        GlStateManager.disableLighting();
        RenderHelper.enableGUIStandardItemLighting();
        renderItem.renderItemAndEffectIntoGUI(research.getIcon(), offsetX + 6, offsetY + slotTop + 1);
        renderItem.renderItemOverlayIntoGUI(parent.mc.fontRenderer, research.getIcon(), offsetX + 6, offsetY + slotTop + 1, null);
        String title = research.getTitle();
        if (!research.canResearch(Minecraft.getMinecraft().player, GuiTable.data)) {
            title = Util.color(0x808080) + title;
        }
        AdvancedFontRenderer.INSTANCE.drawString(title, offsetX + 32, offsetY + slotTop + 6, 0x000000);
    }

    @Override
    protected void drawGradientRect(int left, int top, int right, int bottom, int color1, int color2) {
        super.drawGradientRect(left, top, right, bottom, color1, color2);
    }

    @Override
    protected int getSlotHeight(int index) {
        return slotHeight;
    }

    @Override
    public void handleMouseInput(int mouseX, int mouseY) {
        super.handleMouseInput(mouseX, mouseY);
        if (Mouse.isButtonDown(0) && Mouse.getEventButtonState()) {
            for (GuiButtonStack btn : btns) {
                if (btn.isMouseOver()) {
                    btn.mousePressed(parent.mc, mouseX, mouseY);
                }
            }
        }
    }

    @Override
    public void drawScreen(int x, int y, int mouseX, int mouseY, float arg4) {
        super.drawScreen(x, y, mouseX, mouseY, arg4);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        for (GuiButtonStack btn : btns) {
            btn.drawButton(parent.mc, mouseX, mouseY, 0);
        }
        GlStateManager.popMatrix();
    }

}
