package snownee.researchtable.core;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ConditionTypes {
    private ConditionTypes() {
    }

    public static final Supplier<Class<ItemStack>> ITEM = () -> ItemStack.class;
    public static final Supplier<Class<FluidStack>> FLUID = () -> FluidStack.class;
    public static final Supplier<Class<Long>> ENERGY = () -> Long.class;
}
