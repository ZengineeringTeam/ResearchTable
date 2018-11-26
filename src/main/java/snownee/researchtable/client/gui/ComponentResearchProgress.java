package snownee.researchtable.client.gui;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import snownee.kiwi.client.AdvancedFontRenderer;
import snownee.kiwi.client.gui.GuiControl;
import snownee.kiwi.client.gui.component.Component;
import snownee.researchtable.core.ICondition;

public class ComponentResearchProgress extends Component
{
    private final ICondition condition;
    private final ConditionRenderer renderer;
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
        int right = offsetX + 130;
        int top = offsetY + 15;
        int bottom = offsetY + 19;

        if (renderer != null)
        {
            renderer.draw(parent.mc, offsetX + 5, offsetY + 4);
            AdvancedFontRenderer.INSTANCE.drawString(renderer.name(), left, offsetY + 5, 0);
        }

        double progress = total == 0 ? 0 : target / (double) total;
        if (progress != current)
        {
            current += partialTicks * 0.03;
            current = Math.min(current, progress);
        }
        AdvancedFontRenderer.INSTANCE.drawString((int) (progress * 100) + "%", offsetX + 133, offsetY + 10, 0);
        // int nameWidth = AdvancedFontRenderer.INSTANCE.getStringWidth(name);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.setTranslation(0, 0, 0);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
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

}
