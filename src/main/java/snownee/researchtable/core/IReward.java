package snownee.researchtable.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IReward {
    void earn(World world, BlockPos pos, EntityPlayer player);
}
