package snownee.researchtable.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ICriterion
{
    boolean matches(EntityPlayer player);

    @SideOnly(Side.CLIENT)
    String getFailingText(EntityPlayer player);
}
