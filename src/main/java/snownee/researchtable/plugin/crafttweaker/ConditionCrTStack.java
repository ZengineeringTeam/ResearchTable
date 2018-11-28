package snownee.researchtable.plugin.crafttweaker;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import snownee.researchtable.core.ICondition;

public class ConditionCrTStack implements ICondition<ItemStack>
{
    final IIngredient ingredient;
    final int count;

    public ConditionCrTStack(IIngredient ingredient)
    {
        this.count = ingredient.getAmount();
        this.ingredient = ingredient.amount(1);

    }

    @Override
    public long matches(ItemStack e)
    {
        if (!e.isEmpty() && ingredient.matchesExact(CraftTweakerMC.getIItemStack(e).amount(1)))
        {
            return e.getCount();
        }
        return 0;
    }

    @Override
    public long getGoal()
    {
        return count;
    }

    @Override
    public Class<ItemStack> getMatchClass()
    {
        return ItemStack.class;
    }

}
