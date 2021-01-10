package snownee.researchtable.plugin.crafttweaker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import snownee.researchtable.ResearchTable;
import snownee.researchtable.core.CriterionResearchCount;
import snownee.researchtable.core.CriterionResearches;
import snownee.researchtable.core.CriterionScore;
import snownee.researchtable.core.ICondition;
import snownee.researchtable.core.ICriterion;
import snownee.researchtable.core.IReward;
import snownee.researchtable.core.Research;
import snownee.researchtable.core.ResearchCategory;
import snownee.researchtable.core.ResearchList;
import snownee.researchtable.core.RewardExecute;
import snownee.researchtable.core.RewardItems;
import snownee.researchtable.plugin.forge.ConditionForgeEnergy;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("ResearchTable.Builder")
@ZenRegister
public class ResearchBuilder {
    private static final String KEY_NO_TITLE = ResearchTable.MODID + ".title.missing";
    private static final String KEY_NO_DESCRIPTION = ResearchTable.MODID + ".description.missing";

    private final String name;
    private final ResearchCategory category;
    public List<ICriterion> criteria = new LinkedList<>();
    public List<IReward> triggers = new LinkedList<>();
    public List<IReward> rewards = new LinkedList<>();
    public List<ICondition> conditions = new ArrayList<>(4);
    public List<ItemStack> icons;
    private String title;
    private String description;
    private int maxCount = 1;

    public ResearchBuilder(@Nonnull String name, @Nonnull ResearchCategoryWrapper category) {
        this.name = name;
        this.category = category.category;
    }

    @ZenMethod
    public ResearchBuilder setIcons(@Nonnull IIngredient... items) {
        NonNullList<ItemStack> actualItems = NonNullList.create();
        for (IIngredient item : items) {
            for (IItemStack iItemStack : item.getItems()) {
                if (iItemStack != null) {
                    ItemStack stack = CraftTweakerMC.getItemStack(iItemStack);
                    if (stack.getMetadata() == OreDictionary.WILDCARD_VALUE) {
                        stack.getItem().getSubItems(stack.getItem().getCreativeTab(), actualItems);
                    } else {
                        actualItems.add(stack);
                    }
                }
            }
        }
        icons = ImmutableList.copyOf(actualItems);
        return this;
    }

    @ZenMethod
    public ResearchBuilder setRequiredResearches(@Nonnull String... researches) {
        Set<String> set = ImmutableSet.copyOf(researches);
        criteria.add(new CriterionResearches(set, set.size()));
        return this;
    }

    @ZenMethod
    public ResearchBuilder setOptionalResearches(int amount, @Nonnull String... researches) {
        Set<String> set = ImmutableSet.copyOf(researches);
        criteria.add(new CriterionResearches(set, amount));
        return this;
    }

    @ZenMethod
    public ResearchBuilder setRequiredScore(String score, String failingText, int min, @Optional int max) {
        if (max < min)
            max = min;
        criteria.add(new CriterionScore(score, min, max, failingText));
        return this;
    }

    @ZenMethod
    public ResearchBuilder setRewardCommands(@Nonnull String... commands) {
        rewards.add(new RewardExecute(commands));
        return this;
    }

    @ZenMethod
    public ResearchBuilder setRewardItems(@Nonnull IItemStack... items) {
        NonNullList<ItemStack> rawItems = NonNullList.from(ItemStack.EMPTY, CraftTweakerMC.getItemStacks(items));
        rewards.add(new RewardItems(rawItems));
        return this;
    }

    @ZenMethod
    public ResearchBuilder setTriggerCommands(@Nonnull String... commands) {
        triggers.add(new RewardExecute(commands));
        return this;
    }

    @ZenMethod
    public ResearchBuilder setTriggerItems(@Nonnull IItemStack... items) {
        NonNullList<ItemStack> rawItems = NonNullList.from(ItemStack.EMPTY, CraftTweakerMC.getItemStacks(items));
        triggers.add(new RewardItems(rawItems));
        return this;
    }

    @ZenMethod
    public ResearchBuilder setTitle(@Nonnull String title) {
        this.title = title;
        return this;
    }

    @ZenMethod
    public ResearchBuilder setDescription(@Nonnull String description) {
        this.description = description;
        return this;
    }

    @ZenMethod
    public ResearchBuilder addCondition(@Nonnull IIngredient... ingredients) {
        for (IIngredient ingredient : ingredients) {
            if (ingredient instanceof ILiquidStack) {
                conditions.add(new ConditionCrTLiquid((ILiquidStack) ingredient));
            } else {
                conditions.add(new ConditionCrTItem(ingredient));
            }
        }
        return this;
    }

    @ZenMethod
    public ResearchBuilder addCondition(@Nonnull IIngredient ingredient, long amount, @Optional String customName) {
        conditions.add(new ConditionCrTItem(ingredient, amount).setCustomName(customName));
        return this;
    }

    @ZenMethod
    public ResearchBuilder addCondition(@Nonnull ILiquidStack ingredient, long amount) {
        conditions.add(new ConditionCrTLiquid(ingredient, amount));
        return this;
    }

    @ZenMethod
    public ResearchBuilder addEnergyCondition(long amount) {
        conditions.add(new ConditionForgeEnergy(amount));
        return this;
    }

    @ZenMethod
    public ResearchBuilder setMaxCount(int count) {
        this.maxCount = count;
        return this;
    }

    @ZenMethod
    public ResearchBuilder setNoMaxCount() {
        return setMaxCount(0);
    }

    @ZenMethod
    public boolean build() {
        if (title == null) {
            title = KEY_NO_TITLE;
        }
        if (description == null) {
            description = KEY_NO_DESCRIPTION;
        }
        if (maxCount > 0) {
            criteria.add(new CriterionResearchCount(name, maxCount));
        }
        Research research = new Research(name, category, title, description, criteria, triggers, rewards, conditions, icons);
        return ResearchList.add(research);
    }

}
