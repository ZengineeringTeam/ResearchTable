package snownee.researchtable.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

public class RewardItems implements IReward {
    private final NonNullList<ItemStack> items;

    public RewardItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public void earn(World world, BlockPos pos, EntityPlayer player) {
        for (ItemStack item : items)
            ItemHandlerHelper.giveItemToPlayer(player, item);
    }

}
