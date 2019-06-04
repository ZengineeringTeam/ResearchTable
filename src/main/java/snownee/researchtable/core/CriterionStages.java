package snownee.researchtable.core;

import java.util.Collection;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.util.Util;
import snownee.researchtable.ResearchTable;

public class CriterionStages implements ICriterion
{
    private Collection<String> stages;
    private int r;

    public CriterionStages(Collection<String> stages, int requirement)
    {
        this.r = requirement;
        this.stages = stages;
        if (r <= 0)
        {
            r = stages.size();
        }
    }

    @Override
    public boolean matches(EntityPlayer player)
    {
        int c = 0;
        for (String stage : stages)
        {
            if (GameStageHelper.hasStage(player, stage))
            {
                ++c;
            }
        }
        return c >= r;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getFailingText(EntityPlayer player)
    {
        String string = "";
        boolean first = true;
        for (String stage : stages)
        {
            if (!first)
            {
                string += Util.color(0) + ", ";
            }
            first = false;
            if (!GameStageHelper.hasStage(player, stage))
            {
                string += Util.color(0xFFFF0000);
            }
            string += stage;
        }
        string += TextFormatting.RESET;
        string = I18n.format(ResearchTable.MODID + ".gui.needStages", string);
        if (r != stages.size())
        {
            string += I18n.format(ResearchTable.MODID + ".gui.of", r, stages.size());
        }
        return string;
    }

}
