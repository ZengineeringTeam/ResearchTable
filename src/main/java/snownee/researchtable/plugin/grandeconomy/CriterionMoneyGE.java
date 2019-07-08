package snownee.researchtable.plugin.grandeconomy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.util.NBTHelper;
import snownee.researchtable.core.ICriterion;

public class CriterionMoneyGE implements ICriterion
{
    private final long money;

    public CriterionMoneyGE(long money)
    {
        this.money = money;
    }

    @Override
    public boolean matches(EntityPlayer player, NBTTagCompound data)
    {
        NBTHelper helper = NBTHelper.of(data);
        long balance = helper.getLong("grandeconomy.money");
        return balance >= money;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getFailingText(EntityPlayer player, NBTTagCompound data)
    {
        return "123";
        //return I18n.format(ResearchTable.MODID + ".gui.needMoney", money, r, Util.color(0xFF0000) + I18n.format(ResearchTable.MODID + ".gui.youHave", Helper.getLevel(player, skill)));
    }

}
