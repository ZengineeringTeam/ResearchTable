package snownee.researchtable.client.gui;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.ResourceLocation;
import snownee.kiwi.client.AdvancedFontRenderer;
import snownee.kiwi.client.gui.GuiControl;
import snownee.kiwi.client.gui.component.Component;
import snownee.kiwi.client.gui.element.DrawableResource;
import snownee.kiwi.util.Util;
import snownee.researchtable.client.renderer.ConditionRenderer;
import snownee.researchtable.core.ICondition;

public class ComponentResearchProgress extends Component
{
    private static final DrawableResource SUCCESS_ICON = new DrawableResource(new ResourceLocation("textures/gui/container/beacon.png"), 91, 224, 14, 12);

    private final ICondition condition;
    private ConditionRenderer renderer;
    private boolean researching;
    private long total;
    private long target;
    private double current;

    public ComponentResearchProgress(GuiControl parent, int width, ICondition condition)
    {
        super(parent, width, 25);
        this.condition = condition;
        this.renderer = ConditionRenderer.get(condition);
        this.total = condition.getGoal();
        if (total <= 0)
        {
            total = 1;
        }
    }

    public void resetRenderer()
    {
        this.renderer = ConditionRenderer.get(condition);
    }

    public void setResearching(boolean researching)
    {
        this.researching = researching;
    }

    public void setProgress(long progress)
    {
        this.target = progress;
    }

    @Override
    public void drawScreen(int offsetX, int offsetY, int relMouseX, int relMouseY, float partialTicks)
    {
        int left = offsetX + 25;
        int right = offsetX + width - 20;
        int top = offsetY + 15;
        int bottom = offsetY + 19;

        if (renderer != null)
        {
            Tessellator.getInstance().getBuffer().setTranslation(0, 0, 0);
            GlStateManager.color(1, 1, 1, 1);
            renderer.draw(parent.mc, offsetX + 5, offsetY + 4);
            String text = renderer.name();
            if (researching)
            {
                text += " (" + Util.formatCompact(target) + "/" + Util.formatCompact(total) + ")";
            }
            AdvancedFontRenderer.INSTANCE.drawString(text, left, offsetY + (researching ? 5 : 7), 0);
            if (GuiTable.isInRegion(5, 4, 21, 20, relMouseX, relMouseY))
            {
                List<String> tooltip = renderer.getTooltip(parent.mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
                if (tooltip != null)
                {
                    setTooltip(tooltip, renderer.getFont());
                }
            }
        }

        if (researching)
        {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.setTranslation(0, 0, 0);

            double progress = total == 0 ? 0 : target / (double) total;
            if (progress != current)
            {
                current += partialTicks * 0.03;
                current = Math.min(current, progress);
            }
            if (current == 1)
            {
                GlStateManager.color(1, 1, 1, 1);
                SUCCESS_ICON.draw(parent.mc, right + 4, offsetY + 7);
            }
            else
            {
                AdvancedFontRenderer.INSTANCE.drawString((int) (progress * 100) + "%", right + 5, offsetY + 10, 0);
            }

            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.color(0.2F, 0.2F, 0.2F, 1);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
            bufferbuilder.pos(left, bottom, 0.0D).endVertex();
            bufferbuilder.pos(right, bottom, 0.0D).endVertex();
            bufferbuilder.pos(right, top, 0.0D).endVertex();
            bufferbuilder.pos(left, top, 0.0D).endVertex();
            tessellator.draw();

            ++top;
            ++left;
            --bottom;
            --right;
            int x = left + (int) (current * (right - left));

            GlStateManager.color(0, 0.8F, 0.2F, 1);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
            bufferbuilder.pos(left, bottom, 0.0D).endVertex();
            bufferbuilder.pos(x, bottom, 0.0D).endVertex();
            bufferbuilder.pos(x, top, 0.0D).endVertex();
            bufferbuilder.pos(left, top, 0.0D).endVertex();
            tessellator.draw();
            GlStateManager.color(0.4F, 0.4F, 0.4F, 1);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
            bufferbuilder.pos(x, bottom, 0.0D).endVertex();
            bufferbuilder.pos(right, bottom, 0.0D).endVertex();
            bufferbuilder.pos(right, top, 0.0D).endVertex();
            bufferbuilder.pos(x, top, 0.0D).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
        else if (renderer != null)
        {
            String text = renderer.format(condition.getGoal());
            AdvancedFontRenderer.INSTANCE.drawString(text, right + 10 - AdvancedFontRenderer.INSTANCE.getStringWidth(text), offsetY + 7, 0);
        }
    }

}
