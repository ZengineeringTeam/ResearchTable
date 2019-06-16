package snownee.researchtable.plugin.reskillable;

import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.util.Util;
import snownee.researchtable.ResearchTable;
import snownee.researchtable.core.ICriterion;
import snownee.researchtable.plugin.reskillable.CrTReskillableExpansion.Helper;

public class CriterionSkill implements ICriterion
{
    private Skill skill;
    private int r;

    public CriterionSkill(String skillName, int requirementLevel)
    {
        this.r = requirementLevel;
        skill = Helper.getSkill(skillName);
        if (skill == null)
        {
            throw new NullPointerException("Unknown skill name " + skillName);
        }
    }

    @Override
    public boolean matches(EntityPlayer player)
    {
        int lv = Helper.getLevel(player, skill);
        return r == 0 ? lv == 0 : lv >= r;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getFailingText(EntityPlayer player)
    {
        return I18n.format(ResearchTable.MODID + ".gui.needSkill", skill.getName(), r, Util.color(0xFF0000) + I18n.format(ResearchTable.MODID + ".gui.youHave", Helper.getLevel(player, skill)));
    }

}
