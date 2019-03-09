package snownee.researchtable.core;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Research
{
    private static final ItemStack DEFAULT_ICON = new ItemStack(Blocks.GRASS);

    private final String name;
    private final ResearchCategory category;
    private final String title;
    private final String description;
    private final Set<String> stages;
    private final Set<String> researches;
    @Nullable
    private final List<ItemStack> icons;
    private final List<ICondition> conditions;
    private final Collection<IReward> rewards;
    private final int maxCount;

    public Research(String name, ResearchCategory category, String title, String description, Set<String> stages, Set<String> researches, List<IReward> rewards, List<ICondition> conditions, @Nullable List<ItemStack> icons, int maxCount)
    {
        this.name = name;
        this.category = category;
        this.title = title;
        this.description = description;
        this.stages = stages;
        this.researches = researches;
        this.rewards = rewards;
        this.conditions = conditions;
        this.icons = icons;
        this.maxCount = maxCount;
    }

    public String getName()
    {
        return name;
    }

    public ResearchCategory getCategory()
    {
        return category;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

    public ItemStack getIcon()
    {
        if (icons == null || icons.isEmpty())
        {
            return DEFAULT_ICON;
        }
        else
        {
            return icons.get(0);
        }
    }

    public List<ICondition> getConditions()
    {
        return Collections.unmodifiableList(conditions);
    }

    public boolean canResearch(EntityPlayer player)
    {
        return GameStageHelper.hasAllOf(player, stages) && DataStorage.count(player.getName(), this) < getMaxCount() && DataStorage.hasAllOf(player.getName(), researches);
    }

    public Set<String> getStages()
    {
        return Collections.unmodifiableSet(stages);
    }

    public Set<String> getRequiredResearchNames()
    {
        return Collections.unmodifiableSet(researches);
    }

    public void complete(World world, BlockPos pos, EntityPlayer player)
    {
        rewards.forEach(e -> e.earn(world, pos, player));
    }

    @Override
    public String toString()
    {
        return "Research@" + getName();
    }

    public int getMaxCount()
    {
        return maxCount;
    }
}
