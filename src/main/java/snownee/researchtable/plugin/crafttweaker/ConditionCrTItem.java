package snownee.researchtable.plugin.crafttweaker;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import snownee.researchtable.core.ConditionTypes;
import snownee.researchtable.core.ICondition;

public class ConditionCrTItem implements ICondition<ItemStack> {
    final IIngredient ingredient;
    final long count;
    @Nullable
    String customName;

    public ConditionCrTItem(IIngredient ingredient) {
        this(ingredient, ingredient.getAmount());
    }

    public ConditionCrTItem(IIngredient ingredient, long count) {
        this.count = count;
        this.ingredient = ingredient.amount(1);
    }

    @Override
    public long matches(ItemStack e) {
        if (!e.isEmpty() && ingredient.matches(CraftTweakerMC.getIItemStack(e).amount(1))) {
            return e.getCount();
        }
        return 0;
    }

    @Override
    public long getGoal() {
        return count;
    }

    @Override
    public Supplier<Class<ItemStack>> getMatchType() {
        return ConditionTypes.ITEM;
    }

    public ConditionCrTItem setCustomName(String name) {
        customName = name;
        return this;
    }
}
