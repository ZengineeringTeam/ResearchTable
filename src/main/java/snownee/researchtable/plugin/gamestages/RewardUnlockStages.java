package snownee.researchtable.plugin.gamestages;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import snownee.researchtable.core.IReward;

public class RewardUnlockStages implements IReward {
    private final Set<String> stages;

    public RewardUnlockStages(String... stages) {
        this.stages = ImmutableSet.copyOf(stages);
    }

    @Override
    public void earn(World world, BlockPos pos, EntityPlayer player) {
        stages.forEach(e -> GameStageHelper.addStage(player, e));
        GameStageHelper.syncPlayer(player);
    }

}
