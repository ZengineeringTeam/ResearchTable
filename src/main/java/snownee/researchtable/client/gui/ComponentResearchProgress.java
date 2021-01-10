package snownee.researchtable.client.gui;

import java.util.List;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.client.AdvancedFontRenderer;
import snownee.kiwi.client.gui.GuiControl;
import snownee.kiwi.client.gui.component.Component;
import snownee.kiwi.client.gui.element.DrawableResource;
import snownee.kiwi.util.Util;
import snownee.researchtable.client.renderer.ConditionRenderer;
import snownee.researchtable.core.ICondition;

@SideOnly(Side.CLIENT)
public class ComponentResearchProgress extends Component {
    private static final DrawableResource SUCCESS_ICON = new DrawableResource(new ResourceLocation("textures/gui/container/beacon.png"), 91, 224, 14, 12);

    private final ICondition condition;
    private ConditionRenderer renderer;
    private boolean researching;
    private long total;
    private long target;
    private double current;

    public ComponentResearchProgress(GuiControl parent, int width, ICondition condition) {
        super(parent, width, 25);
        this.condition = condition;
        this.renderer = ConditionRenderer.get(condition);
        this.total = condition.getGoal();
        if (total <= 0) {
            total = 1;
        }
    }

    public void resetRenderer() {
        this.renderer = ConditionRenderer.get(condition);
    }

    public void setResearching(boolean researching) {
        this.researching = researching;
    }

    public void setProgress(long progress) {
        this.target = progress;
    }

    @Override
    public void drawScreen(int offsetX, int offsetY, int relMouseX, int relMouseY, float partialTicks) {
        int left = offsetX;
        int right = left + width - 8;
        int top = offsetY;
        int bottom = top + height;

        if (researching) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.setTranslation(0, 0, 0);

            double progress = total == 0 ? 0 : target / (double) total;
            if (progress != current) {
                current += partialTicks * 0.03;
                current = Math.min(current, progress);
            }

            Gui.drawRect(left, top, left + (int) (width * current), bottom, 0x33339933);

            if (progress == 1) {
                GlStateManager.color(1, 1, 1, 1);
                SUCCESS_ICON.draw(parent.mc, right - 20, offsetY + 7);
            } else {
                AdvancedFontRenderer.INSTANCE.drawString((int) (progress * 100) + "%", right - 15, offsetY + 10, 0);
            }

        } else if (renderer != null) {
            String text = renderer.format(condition.getGoal());
            AdvancedFontRenderer.INSTANCE.drawString(text, right - 10 - AdvancedFontRenderer.INSTANCE.getStringWidth(text), offsetY + 7, 0);
        }

        left = offsetX + 25;
        top = offsetY + 15;
        bottom = offsetY + 19;

        if (renderer != null) {
            Tessellator.getInstance().getBuffer().setTranslation(0, 0, 0);
            GlStateManager.color(1, 1, 1, 1);
            renderer.draw(parent.mc, offsetX + 5, offsetY + 4);
            String text = renderer.name();
            if (researching) {
                text += " (" + Util.formatCompact(target) + "/" + Util.formatCompact(total) + ")";
            }
            AdvancedFontRenderer.INSTANCE.drawString(text, left + 2, offsetY + 7, 0);
            if (GuiTable.isInRegion(5, 4, 21, 20, relMouseX, relMouseY)) {
                List<String> tooltip = renderer.getTooltip(parent.mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
                if (tooltip != null) {
                    setTooltip(tooltip, renderer.getFont());
                }
            }
        }
    }

}
