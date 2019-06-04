package snownee.researchtable.core;

import java.util.Collection;
import java.util.Optional;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.util.Util;
import snownee.researchtable.ResearchTable;

public class CriterionResearches implements ICriterion
{
    private Collection<String> researches;
    private int r;

    public CriterionResearches(Collection<String> researches, int requirement)
    {
        this.r = requirement;
        this.researches = researches;
        if (r <= 0)
        {
            r = researches.size();
        }
    }

    @Override
    public boolean matches(EntityPlayer player)
    {
        int c = 0;
        for (String research : researches)
        {
            if (DataStorage.count(player.getName(), research) > 0)
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
        for (String research : researches)
        {
            Optional<Research> result = ResearchList.find(research);
            if (!result.isPresent()) continue;
            if (!first)
            {
                string += Util.color(0) + ", ";
            }
            first = false;
            if (DataStorage.count(player.getName(), research) == 0)
            {
                string += Util.color(0xFFFF0000);
            }
            string += result.get().getTitle();
        }
        string += TextFormatting.RESET;
        string = I18n.format(ResearchTable.MODID + ".gui.needResearches", string);
        if (r != researches.size())
        {
            string += I18n.format(ResearchTable.MODID + ".gui.of", r, researches.size());
        }
        return string;
    }

}
