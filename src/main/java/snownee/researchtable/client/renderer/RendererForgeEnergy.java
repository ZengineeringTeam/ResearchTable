package snownee.researchtable.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.client.gui.element.DrawableResource;
import snownee.kiwi.util.Util;
import snownee.researchtable.ResearchTable;
import snownee.researchtable.client.gui.ConditionRenderer;
import snownee.researchtable.core.ConditionForgeEnergy;

@SideOnly(Side.CLIENT)
public class RendererForgeEnergy extends ConditionRenderer<ConditionForgeEnergy>
{
    private static final RendererForgeEnergy INSTANCE = new RendererForgeEnergy();
    private static final DrawableResource ICON = new DrawableResource(
            new ResourceLocation(ResearchTable.MODID, "textures/gui/energy.png"), 0, 0, 16, 16, 0, 0, 0, 0, 16, 16);

    private RendererForgeEnergy()
    {
    }

    @Override
    public void draw(Minecraft mc, int x, int y)
    {
        ICON.draw(mc, x, y);
    }

    @Override
    public String name()
    {
        return "FE";
    }

    @Override
    public String format(long number)
    {
        return Util.formatComma(number);
    }

    public static class Factory implements ConditionRendererFactory<ConditionForgeEnergy>
    {

        @Override
        public ConditionRenderer<ConditionForgeEnergy> get(ConditionForgeEnergy condition)
        {
            return RendererForgeEnergy.INSTANCE;
        }

    }

}
