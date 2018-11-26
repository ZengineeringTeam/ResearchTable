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
    @Nullable
    private final List<ItemStack> icons;
    private final List<ICondition> conditions;
    private final Collection<IReward> rewards;

    public Research(String name, ResearchCategory category, String title, String description, Set<String> stages, List<IReward> rewards, List<ICondition> conditions, @Nullable List<ItemStack> icons)
    {
        this.name = name;
        this.category = category;
        this.title = title;
        this.description = description;
        this.stages = stages;
        this.rewards = rewards;
        this.conditions = conditions;
        this.icons = icons;
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
        return GameStageHelper.hasAllOf(player, stages);
    }

    public Set<String> getStages()
    {
        return Collections.unmodifiableSet(stages);
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
}
