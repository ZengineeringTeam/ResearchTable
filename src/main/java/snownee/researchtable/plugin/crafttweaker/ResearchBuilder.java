package snownee.researchtable.plugin.crafttweaker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import snownee.researchtable.ResearchTable;
import snownee.researchtable.core.ConditionForgeEnergy;
import snownee.researchtable.core.ICondition;
import snownee.researchtable.core.Research;
import snownee.researchtable.core.ResearchCategory;
import snownee.researchtable.core.ResearchList;
import snownee.researchtable.core.RewardUnlockStages;
import stanhebben.zenscript.annotations.ZenMethod;

public class ResearchBuilder
{
    private static final String KEY_NO_TITLE = ResearchTable.MODID + ".title.missing";
    private static final String KEY_NO_DESCRIPTION = ResearchTable.MODID + ".description.missing";

    private final String name;
    private final ResearchCategory category;
    private Set<String> requiredStages;
    private Set<String> rewardStages;
    private List<ItemStack> icons;
    private String title;
    private String description;
    private List<ICondition> conditions = new ArrayList<>(4);

    public ResearchBuilder(@Nonnull String name, @Nonnull ResearchCategory category)
    {
        this.name = name;
        this.category = category;
    }

    @ZenMethod
    public ResearchBuilder setIcons(@Nonnull IItemStack... items)
    {
        List<ItemStack> actualItems = new ArrayList<>(items.length);
        for (IItemStack item : items)
        {
            actualItems.add(CraftTweakerMC.getItemStack(item));
        }
        icons = ImmutableList.copyOf(actualItems);
        return this;
    }

    @ZenMethod
    public ResearchBuilder setRequiredStages(@Nonnull String... stages)
    {
        requiredStages = ImmutableSet.copyOf(stages);
        return this;
    }

    @ZenMethod
    public ResearchBuilder setRewardStages(@Nonnull String... stages)
    {
        rewardStages = ImmutableSet.copyOf(stages);
        return this;
    }

    @ZenMethod
    public ResearchBuilder setTitle(@Nonnull String title)
    {
        this.title = title;
        return this;
    }

    @ZenMethod
    public ResearchBuilder setDescription(@Nonnull String description)
    {
        this.description = description;
        return this;
    }

    @ZenMethod
    public ResearchBuilder addCondition(@Nonnull IIngredient ingredient)
    {
        conditions.add(new ConditionCrTItem(ingredient));
        return this;
    }

    @ZenMethod
    public ResearchBuilder addEnergyCondition(long amount)
    {
        conditions.add(new ConditionForgeEnergy(amount));
        return this;
    }

    @ZenMethod
    public boolean build()
    {
        RewardUnlockStages reward = new RewardUnlockStages(rewardStages != null ? rewardStages : Collections.EMPTY_SET);
        if (title == null)
        {
            title = KEY_NO_TITLE;
        }
        if (description == null)
        {
            description = KEY_NO_DESCRIPTION;
        }
        if (requiredStages == null)
        {
            requiredStages = Collections.EMPTY_SET;
        }
        Research research = new Research(name, ResearchCategory.GENERAL, title, description, requiredStages,
                ImmutableList.of(reward), conditions, icons);
        return ResearchList.LIST.add(research);
    }

}
