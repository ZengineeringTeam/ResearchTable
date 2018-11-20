package snownee.researchtable.core;

import java.util.Set;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RewardUnlockStages implements IReward
{
    private final Set<String> stages;

    public RewardUnlockStages(Set<String> stages)
    {
        this.stages = stages;
    }

    @Override
    public void earn(World world, BlockPos pos, EntityPlayer player)
    {
        stages.forEach(e -> GameStageHelper.addStage(player, e));
    }

}
