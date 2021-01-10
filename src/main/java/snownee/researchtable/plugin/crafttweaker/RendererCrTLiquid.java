package snownee.researchtable.plugin.crafttweaker;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.client.AdvancedFontRenderer;
import snownee.kiwi.util.Util;
import snownee.researchtable.client.renderer.ConditionRenderer;

@SideOnly(Side.CLIENT)
public class RendererCrTLiquid extends ConditionRenderer<ConditionCrTLiquid> {
    private final FluidStack fluid;

    public RendererCrTLiquid(ConditionCrTLiquid condition) {
        this.fluid = condition.getFluid();
    }

    @Override
    public void draw(Minecraft mc, int x, int y) {
        TextureMap textureMapBlocks = mc.getTextureMapBlocks();
        ResourceLocation still = fluid.getFluid().getStill(fluid);
        TextureAtlasSprite sprite = null;
        if (still != null) {
            sprite = textureMapBlocks.getTextureExtry(still.toString());
        }
        if (sprite == null) {
            sprite = textureMapBlocks.getMissingSprite();
        }

        int color = fluid.getFluid().getColor(fluid);
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        GlStateManager.color(red, green, blue, 1.0F);

        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        double uMin = sprite.getMinU();
        double uMax = sprite.getMaxU();
        double vMin = sprite.getMinV();
        double vMax = sprite.getMaxV();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(x, y + 16, 0).tex(uMin, vMax).endVertex();
        bufferBuilder.pos(x + 16, y + 16, 0).tex(uMax, vMax).endVertex();
        bufferBuilder.pos(x + 16, y, 0).tex(uMax, vMin).endVertex();
        bufferBuilder.pos(x, y, 0).tex(uMin, vMin).endVertex();
        tessellator.draw();

        GlStateManager.color(1, 1, 1, 1);
    }

    @Override
    public String name() {
        return fluid.getLocalizedName();
    }

    @Override
    public String format(long number) {
        if (number >= 10000) {
            return Util.formatComma(number / 1000) + "B";
        } else {
            return Util.formatComma(number) + "mB";
        }
    }

    public static class Factory implements ConditionRendererFactory<ConditionCrTLiquid> {

        @Override
        public ConditionRenderer<ConditionCrTLiquid> get(ConditionCrTLiquid condition) {
            return new RendererCrTLiquid(condition);
        }

    }

    @Override
    public FontRenderer getFont() {
        return AdvancedFontRenderer.INSTANCE;
    }

    @Override
    public List<String> getTooltip(ITooltipFlag flag) {
        List<String> tooltip = Lists.newArrayList(fluid.getLocalizedName());
        if (flag.isAdvanced()) {
            String s = TextFormatting.GRAY + fluid.getFluid().getName();
            if (fluid.tag != null) {
                s += fluid.tag;
            }
            tooltip.add(s);
        }
        return tooltip;
    }

}
