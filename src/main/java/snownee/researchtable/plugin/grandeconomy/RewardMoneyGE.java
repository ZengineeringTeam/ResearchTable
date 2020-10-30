package snownee.researchtable.plugin.grandeconomy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import snownee.researchtable.core.IReward;
import the_fireplace.grandeconomy.api.GrandEconomyApi;

public class RewardMoneyGE implements IReward
{
    private final double money;
    private final boolean showMsg;

    public RewardMoneyGE(double money, boolean showMsg)
    {
        this.money = money;
        this.showMsg = showMsg;
    }

    @Override
    public void earn(World world, BlockPos pos, EntityPlayer player)
    {
        if (money >= 0)
        {
            GrandEconomyApi.addToBalance(player.getUniqueID(), money, showMsg);
        }
        else
        {
            GrandEconomyApi.takeFromBalance(player.getUniqueID(), -money, showMsg);
        }
    }
}
