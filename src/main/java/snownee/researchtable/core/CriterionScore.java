package snownee.researchtable.core;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.util.NBTHelper;

public class CriterionScore implements ICriterion {
    private final String s;
    private final int min;
    private final int max;
    private final String failingText;

    public CriterionScore(String score, int min, int max, String failingText) {
        this.min = min;
        this.max = max;
        this.s = score;
        this.failingText = failingText;
    }

    @Override
    public boolean matches(EntityPlayer player, NBTTagCompound data) {
        int i = NBTHelper.of(data).getInt("score." + s, 0);
        return i >= min && i <= max;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getFailingText(EntityPlayer player, NBTTagCompound data) {
        return I18n.format(failingText);
    }

}
