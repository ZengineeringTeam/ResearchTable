package snownee.researchtable.core;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.util.Util;
import snownee.researchtable.ResearchTable;

public class CriterionResearchCount implements ICriterion {
    private final String id;
    private final int c;

    public CriterionResearchCount(String id, int count) {
        this.id = id;
        this.c = count;
    }

    @Override
    public boolean matches(EntityPlayer player, NBTTagCompound data) {
        return DataStorage.count(player.getGameProfile().getId(), id) < c;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getFailingText(EntityPlayer player, NBTTagCompound data) {
        return I18n.format(ResearchTable.MODID + ".gui.maxCount", c, Util.color(0xFFFF0000) + c + TextFormatting.RESET);
    }

}
