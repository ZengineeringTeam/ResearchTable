package snownee.researchtable.client.gui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.client.gui.GuiControl;
import snownee.kiwi.client.gui.component.Component;
import snownee.kiwi.client.gui.component.ComponentList;
import snownee.kiwi.client.gui.component.ComponentText;
import snownee.researchtable.client.gui.ComponentButtonList.State;
import snownee.researchtable.core.ICondition;
import snownee.researchtable.core.ICriterion;
import snownee.researchtable.core.Research;

@SideOnly(Side.CLIENT)
public class ComponentResearchDetail extends ComponentList {
    @Nullable
    private Research displaying;
    GuiControl control;
    private ComponentText text;
    private ComponentText info;
    private ComponentButtonList buttons;
    @Nullable
    Research researching;

    public ComponentResearchDetail(GuiControl control, int width, int height, int left, int top, int screenWidth, int screenHeight) {
        super(control, width, height, left, top, screenWidth, screenHeight);
        setDrawBackground(false);
        setDrawScrollBar(false);
        this.control = new GuiControlSpecial(control.mc, width, height, control);
        this.control.offsetX = left;
        this.control.offsetY = top;
        this.info = new ComponentText(this.control, width, 5, 5);
        this.control.addComponent(this.info);
        this.buttons = new ComponentButtonList(this.control, width - 8, 20);
        this.control.addComponent(this.buttons);
        this.text = new ComponentText(this.control, width, 5, 5);
        this.control.addComponent(this.text);
    }

    @Nullable
    public Research getResearch() {
        return displaying;
    }

    public void setResearch(@Nonnull Research displaying, boolean canComplete) {
        if (this.displaying != displaying) {
            this.displaying = displaying;
            for (int i = control.getComponentSize(null) - 4; i >= 0; --i) {
                // Last two components must be button list and text. remove others
                control.removeComponent(i);
            }
            text.setText(displaying.getDescription());
            for (ICondition condition : displaying.getConditions()) {
                ComponentResearchProgress progress = new ComponentResearchProgress(control, width, condition);
                progress.setResearching(displaying == researching);
                control.addComponent(progress);
            }
        }
        updateResearching(canComplete);
    }

    @Override
    protected void elementClicked(int index, int mouseX, int mouseY, boolean doubleClick) {
        if (index >= 0 && index < control.getComponentSize(null)) {
            control.getComponent(index).handleMouseInput(mouseX, mouseY);
        }
    }

    @Override
    protected void drawBackground() {
    }

    @Override
    protected int getSize() {
        return control.getComponentSize(null);
    }

    @Override
    protected int getSlotHeight(int index) {
        if (index >= 0 && index < control.getComponentSize(null)) {
            Component component = control.getComponent(index);
            return component.visible ? component.height : 0;
        }
        return 0;
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        if (slotIdx >= 0 && slotIdx < control.getComponentSize(null)) {
            Component component = control.getComponent(slotIdx);
            if (component.visible) {
                component.drawScreen(offsetX, slotTop + offsetY, mouseX - left, mouseY - slotTop, control.mc.getRenderPartialTicks());
            }
        }
    }

    @Override
    public void onDestroy() {
        control.onDestroy();
        super.onDestroy();
    }

    public void updateResearching(boolean canComplete) {
        visible = displaying != null;
        if (this.researching == this.displaying) {
            buttons.visible = true;
            if (this.displaying != null) // Researching
            {
                if (canComplete) {
                    buttons.setState(0, State.INVISIBLE);
                    buttons.setText(1, "complete");
                } else {
                    buttons.setState(0, State.NORMAL);
                    buttons.setText(1, "cancel");
                }
            }
        } else {
            buttons.visible = this.researching == null;
            if (buttons.visible) {
                buttons.setState(0, State.INVISIBLE);
                buttons.setState(1, displaying.canResearch(parent.mc.player, GuiTable.data) ? State.NORMAL : State.DISABLED);
                buttons.setText(1, "research");
            }
        }

        if (displaying != null && researching != displaying && !displaying.canResearch(control.mc.player, GuiTable.data)) {
            String string = "";
            boolean wrap = false;
            for (ICriterion criterion : displaying.getCriteria()) {
                if (criterion.matches(control.mc.player, GuiTable.data))
                    continue;
                if (wrap)
                    string += "\n";
                string += TextFormatting.RESET + criterion.getFailingText(control.mc.player, GuiTable.data);
                wrap = true;
            }
            info.setText(string);
            info.visible = true;
        } else {
            info.visible = false;
        }
    }

}
