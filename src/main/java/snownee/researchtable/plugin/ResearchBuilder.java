package snownee.researchtable.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import snownee.researchtable.ResearchTable;
import snownee.researchtable.core.Research;
import snownee.researchtable.core.ResearchCategory;
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

    public ResearchBuilder(@Nonnull String name, @Nonnull ResearchCategory category)
    {
        this.name = name;
        this.category = category;
    }

    @ZenMethod
    public void setIcons(@Nonnull IItemStack... items)
    {
        List<ItemStack> actualItems = new ArrayList<>(items.length);
        for (IItemStack item : items)
        {
            actualItems.add(CraftTweakerMC.getItemStack(item));
        }
        icons = ImmutableList.copyOf(actualItems);
    }

    @ZenMethod
    public void setRequiredStages(@Nonnull String... stages)
    {
        requiredStages = ImmutableSet.copyOf(stages);
    }

    @ZenMethod
    public void setRewardStages(@Nonnull String... stages)
    {
        rewardStages = ImmutableSet.copyOf(stages);
    }

    @ZenMethod
    public void setTitle(@Nonnull String title)
    {
        this.title = title;
    }

    @ZenMethod
    public void setDescription(@Nonnull String description)
    {
        this.description = description;
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
        Research research = new Research(name, category, title, description, requiredStages, ImmutableList.of(reward),
                icons);
        // ReseachList.MAP.add(research);
        return true;
    }

}
