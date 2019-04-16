package snownee.researchtable.client.gui;

import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.typesafe.config.ConfigException.Null;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import snownee.kiwi.client.gui.GuiControl;
import snownee.kiwi.client.gui.component.Component;
import snownee.kiwi.client.gui.component.ComponentText;
import snownee.kiwi.util.Util;
import snownee.researchtable.ResearchTable;
import snownee.researchtable.client.gui.ComponentButtonList.State;
import snownee.researchtable.core.DataStorage;
import snownee.researchtable.core.ICondition;
import snownee.researchtable.core.Research;
import snownee.researchtable.core.ResearchList;

public class ComponentResearchDetail extends GuiList
{
    @Nullable
    private Research displaying;
    GuiControl control;
    private ComponentText text;
    private ComponentText info;
    private ComponentButtonList buttons;
    @Nullable
    Research researching;

    public ComponentResearchDetail(GuiControl control, int width, int height, int left, int top, int screenWidth, int screenHeight)
    {
        super(control, width, height, left, top, screenWidth, screenHeight);
        setDrawBackground(false);
        setDrawScrollBar(false);
        this.control = new GuiControlSpecial(control.mc, width, height, control);
        this.control.offsetX = left;
        this.control.offsetY = top;
        this.info = new ComponentText(this.control, width, 5, 5);
        this.control.addComponent(this.info);
        this.buttons = new ComponentButtonList(this.control, width, 15);
        this.control.addComponent(this.buttons);
        this.text = new ComponentText(this.control, width, 5, 5);
        this.control.addComponent(this.text);
    }

    @Nullable
    public Research getResearch()
    {
        return displaying;
    }

    public void setResearch(@Nonnull Research displaying, boolean canComplete)
    {
        if (this.displaying != displaying)
        {
            this.displaying = displaying;
            for (int i = control.getComponentSize(null) - 4; i >= 0; --i)
            {
                // Last two components must be button list and text. remove others
                control.removeComponent(i);
            }
            text.setText(displaying.getDescription());
            for (ICondition condition : displaying.getConditions())
            {
                ComponentResearchProgress progress = new ComponentResearchProgress(control, width, condition);
                progress.setResearching(displaying == researching);
                control.addComponent(progress);
            }
        }
        updateResearching(canComplete);
    }

    @Override
    protected void elementClicked(int index, int mouseX, int mouseY, boolean doubleClick)
    {
        if (index >= 0 && index < control.getComponentSize(null))
        {
            control.getComponent(index).handleMouseInput(mouseX, mouseY);
        }
    }

    @Override
    protected void drawBackground()
    {
    }

    @Override
    protected int getSize()
    {
        return control.getComponentSize(null);
    }

    @Override
    protected int getSlotHeight(int index)
    {
        if (index >= 0 && index < control.getComponentSize(null))
        {
            Component component = control.getComponent(index);
            return component.visible ? component.height : 0;
        }
        return 0;
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess)
    {
        if (slotIdx >= 0 && slotIdx < control.getComponentSize(null))
        {
            Component component = control.getComponent(slotIdx);
            if (component.visible)
            {
                component.drawScreen(offsetX, slotTop + offsetY, mouseX - left, mouseY - slotTop, control.mc.getRenderPartialTicks());
            }
        }
    }

    @Override
    public void onDestroy()
    {
        control.onDestroy();
        super.onDestroy();
    }

    public void updateResearching(boolean canComplete)
    {
        visible = displaying != null;
        if (this.researching == this.displaying)
        {
            buttons.visible = true;
            if (this.displaying != null) // Researching
            {
                if (canComplete)
                {
                    buttons.states[0] = State.INVISIBLE;
                    buttons.texts[1] = "complete";
                }
                else
                {
                    buttons.states[0] = State.NORMAL;
                    buttons.texts[1] = "cancel";
                }
            }
        }
        else
        {
            buttons.visible = this.researching == null;
            if (buttons.visible)
            {
                buttons.states[0] = State.INVISIBLE;
                buttons.states[1] = displaying.canResearch(parent.mc.player) ? State.NORMAL : State.DISABLED;
                buttons.texts[1] = "research";
            }
        }

        if (displaying != null && researching != displaying && !displaying.canResearch(control.mc.player))
        {
            String string = TextFormatting.RESET.toString();
            Set<String> stages = displaying.getStages();
            Set<String> researches = displaying.getRequiredResearchNames();
            boolean wrap = false;
            if (!GameStageHelper.hasAllOf(control.mc.player, stages))
            {
                wrap = true;
                boolean first = true;
                for (String stage : stages)
                {
                    if (!first)
                    {
                        string += Util.color(0) + ", ";
                    }
                    first = false;
                    if (!GameStageHelper.hasStage(control.mc.player, stage))
                    {
                        string += Util.color(0xFFFF0000);
                    }
                    string += stage;
                }
                string += TextFormatting.RESET;
                string = I18n.format(ResearchTable.MODID + ".gui.needStages", string);
            }
            if (!DataStorage.hasAllOf(control.mc.player.getName(), researches))
            {
                if (wrap) string += "\n";
                wrap = true;
                boolean first = true;
                String s = "";
                for (String research : researches)
                {
                    Optional<Research> result = ResearchList.find(research);
                    if (!result.isPresent()) continue;
                    if (!first)
                    {
                        s += Util.color(0) + ", ";
                    }
                    first = false;
                    if (DataStorage.count(control.mc.player.getName(), research) == 0)
                    {
                        s += Util.color(0xFFFF0000);
                    }
                    s += result.get().getTitle();
                }
                string += TextFormatting.RESET;
                string += I18n.format(ResearchTable.MODID + ".gui.needResearches", s);
            }
            if (DataStorage.count(control.mc.player.getName(), displaying) >= displaying.getMaxCount())
            {
                if (wrap) string += "\n";
                wrap = true;
                string += Util.color(0xFFFF0000) + I18n.format(ResearchTable.MODID + ".gui.maxCount");
            }
            info.setText(string);
            info.visible = true;
        }
        else
        {
            info.visible = false;
        }
    }

}
