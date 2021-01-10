package snownee.researchtable.plugin.forge;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.client.AdvancedFontRenderer;
import snownee.kiwi.client.gui.element.DrawableResource;
import snownee.kiwi.util.Util;
import snownee.researchtable.ResearchTable;
import snownee.researchtable.client.renderer.ConditionRenderer;

@SideOnly(Side.CLIENT)
public class RendererForgeEnergy extends ConditionRenderer<ConditionForgeEnergy> {
    private static final RendererForgeEnergy INSTANCE = new RendererForgeEnergy();
    private static final DrawableResource ICON = new DrawableResource(new ResourceLocation(ResearchTable.MODID, "textures/gui/energy.png"), 0, 0, 16, 16, 0, 0, 0, 0, 16, 16);

    private RendererForgeEnergy() {
    }

    @Override
    public void draw(Minecraft mc, int x, int y) {
        ICON.draw(mc, x, y);
    }

    @Override
    public String name() {
        return I18n.format(ResearchTable.MODID + ".gui.fe");
    }

    @Override
    public String format(long number) {
        return Util.formatComma(number);
    }

    public static class Factory implements ConditionRendererFactory<ConditionForgeEnergy> {

        @Override
        public ConditionRenderer<ConditionForgeEnergy> get(ConditionForgeEnergy condition) {
            return RendererForgeEnergy.INSTANCE;
        }

    }

    @Override
    public FontRenderer getFont() {
        return AdvancedFontRenderer.INSTANCE;
    }

    @Override
    public List<String> getTooltip(ITooltipFlag flag) {
        return null;
    }

}
