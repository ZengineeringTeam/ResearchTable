package snownee.researchtable.core;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RewardUnlockStages implements IReward
{
    private final Set<String> stages;

    public RewardUnlockStages(String... stages)
    {
        this.stages = ImmutableSet.copyOf(stages);
    }

    @Override
    public void earn(World world, BlockPos pos, EntityPlayer player)
    {
        stages.forEach(e -> GameStageHelper.addStage(player, e));
    }

}
