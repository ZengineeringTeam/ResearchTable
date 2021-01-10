package snownee.researchtable.plugin.gamestages;

import java.util.Collection;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.util.Util;
import snownee.researchtable.ResearchTable;
import snownee.researchtable.core.ICriterion;

public class CriterionStages implements ICriterion {
    private final Collection<String> stages;
    private final int r;

    public CriterionStages(Collection<String> stages, int requirement) {
        this.r = requirement > 0 ? requirement : stages.size();
        this.stages = stages;
    }

    @Override
    public boolean matches(EntityPlayer player, NBTTagCompound data) {
        int c = 0;
        for (String stage : stages) {
            if (GameStageHelper.hasStage(player, stage)) {
                ++c;
            }
        }
        return c >= r;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getFailingText(EntityPlayer player, NBTTagCompound data) {
        String string = "";
        boolean first = true;
        for (String stage : stages) {
            if (!first) {
                string += Util.color(0) + ", ";
            }
            first = false;
            if (!GameStageHelper.hasStage(player, stage)) {
                string += Util.color(0xFFFF0000);
            }
            string += stage;
        }
        string += TextFormatting.RESET;
        if (r == stages.size()) {
            string = I18n.format(ResearchTable.MODID + ".gui.requiredStage", string);
        } else {
            string = I18n.format(ResearchTable.MODID + ".gui.optionalStage", string);
            string += I18n.format(ResearchTable.MODID + ".gui.of", r, stages.size());
        }
        return string;
    }

}
