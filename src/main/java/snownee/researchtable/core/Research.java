package snownee.researchtable.core;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Research {
    private static final ItemStack DEFAULT_ICON = new ItemStack(Blocks.GRASS);

    private final String name;
    private final ResearchCategory category;
    private final String title;
    private final String description;
    @Nullable
    private final List<ItemStack> icons;
    private final List<ICondition> conditions;
    private final Collection<ICriterion> criteria;
    private final Collection<IReward> triggers;
    private final Collection<IReward> rewards;

    public Research(String name, ResearchCategory category, String title, String description, Collection<ICriterion> criteria, Collection<IReward> triggers, Collection<IReward> rewards, List<ICondition> conditions, @Nullable List<ItemStack> icons) {
        this.name = name;
        this.category = category;
        this.title = title;
        this.description = description;
        this.criteria = criteria;
        this.triggers = triggers;
        this.rewards = rewards;
        this.conditions = conditions;
        this.icons = icons;
    }

    public String getName() {
        return name;
    }

    public ResearchCategory getCategory() {
        return category;
    }

    public String getTitleRaw() {
        return title;
    }

    @SideOnly(Side.CLIENT)
    public String getTitle() {
        return I18n.hasKey(title) ? I18n.format(title) : title;
    }

    public String getDescriptionRaw() {
        return description;
    }

    @SideOnly(Side.CLIENT)
    public String getDescription() {
        return I18n.hasKey(description) ? I18n.format(description) : description;
    }

    public ItemStack getIcon() {
        if (icons == null || icons.isEmpty()) {
            return DEFAULT_ICON;
        } else {
            return icons.get(0);
        }
    }

    public List<ICondition> getConditions() {
        return Collections.unmodifiableList(conditions);
    }

    public boolean canResearch(EntityPlayer player, NBTTagCompound data) {
        return criteria.stream().allMatch(c -> c.matches(player, data));
    }

    public Collection<ICriterion> getCriteria() {
        return Collections.unmodifiableCollection(criteria);
    }

    public Collection<IReward> getTriggers() {
        return Collections.unmodifiableCollection(triggers);
    }

    public void complete(World world, BlockPos pos, EntityPlayer player) {
        rewards.forEach(e -> e.earn(world, pos, player));
    }

    @Override
    public String toString() {
        return "Research@" + getName();
    }

    public void start(World world, BlockPos pos, EntityPlayer player) {
        triggers.forEach(r -> r.earn(world, pos, player));
    }
}
