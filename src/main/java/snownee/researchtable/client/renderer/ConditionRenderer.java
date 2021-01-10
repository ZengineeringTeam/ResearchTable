package snownee.researchtable.client.renderer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.researchtable.core.ICondition;
import snownee.researchtable.plugin.forge.ConditionForgeEnergy;
import snownee.researchtable.plugin.forge.RendererForgeEnergy;

@SideOnly(Side.CLIENT)
public abstract class ConditionRenderer<T extends ICondition> {
    private static final Map<Class, ConditionRendererFactory> MAP = new HashMap<>();

    static {
        ConditionRenderer.register(ConditionForgeEnergy.class, new RendererForgeEnergy.Factory());
    }

    public static <T extends ICondition> void register(Class<T> clazz, ConditionRendererFactory<T> renderer) {
        MAP.put(clazz, renderer);
    }

    @Nullable
    public static <T extends ICondition> ConditionRenderer<T> get(T condition) {
        ConditionRendererFactory<T> factory = MAP.get(condition.getClass());
        if (factory != null) {
            return factory.get(condition);
        }
        return null;
    }

    public abstract void draw(Minecraft mc, int x, int y);

    public abstract String name();

    public abstract String format(long number);

    public abstract FontRenderer getFont();

    public abstract List<String> getTooltip(ITooltipFlag flag);

    public static interface ConditionRendererFactory<T extends ICondition> {
        ConditionRenderer<T> get(T condition);
    }
}
