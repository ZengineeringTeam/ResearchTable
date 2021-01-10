package snownee.researchtable.plugin.crafttweaker;

import java.util.function.Supplier;

import crafttweaker.api.liquid.ILiquidStack;
import net.minecraftforge.fluids.FluidStack;
import snownee.researchtable.core.ConditionTypes;
import snownee.researchtable.core.ICondition;

public class ConditionCrTLiquid implements ICondition<FluidStack> {
    final FluidStack fluid;
    final long count;

    public ConditionCrTLiquid(ILiquidStack ingredient) {
        this(ingredient, ingredient.getAmount());
    }

    public ConditionCrTLiquid(ILiquidStack ingredient, long count) {
        this.count = count;
        Object raw = ingredient.withAmount(1).getInternal();
        if (!(raw instanceof FluidStack)) // FluidStack does not have final!
        {
            throw new IllegalArgumentException("Ingredient is not liquid: " + ingredient);
        }
        this.fluid = (FluidStack) raw;
    }

    @Override
    public long matches(FluidStack e) {
        if (fluid.isFluidEqual(e)) {
            return e.amount;
        }
        return 0;
    }

    @Override
    public long getGoal() {
        return count;
    }

    @Override
    public Supplier<Class<FluidStack>> getMatchType() {
        return ConditionTypes.FLUID;
    }

    public FluidStack getFluid() {
        return fluid;
    }

}
