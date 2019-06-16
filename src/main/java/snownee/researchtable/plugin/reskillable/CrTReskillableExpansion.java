package snownee.researchtable.plugin.reskillable;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import snownee.researchtable.plugin.crafttweaker.ResearchBuilder;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenExpansion("snownee.researchtable.plugin.crafttweaker.ResearchBuilder")
public class CrTReskillableExpansion
{
    @ZenMethod
    public static ResearchBuilder setRewardSkill(ResearchBuilder builder, String skillName)
    {
        builder.rewards.add(new RewardSkillLevelUp(skillName));
        return builder;
    }

    @ZenMethod
    public static ResearchBuilder setTriggerSkill(ResearchBuilder builder, String skillName)
    {
        builder.triggers.add(new RewardSkillLevelUp(skillName));
        return builder;
    }

    @ZenMethod
    public static ResearchBuilder setRewardSkill(ResearchBuilder builder, String skillName, int newLevel)
    {
        builder.rewards.add(new RewardSetSkillLevel(skillName, newLevel));
        return builder;
    }

    @ZenMethod
    public static ResearchBuilder setTriggerSkill(ResearchBuilder builder, String skillName, int newLevel)
    {
        builder.triggers.add(new RewardSetSkillLevel(skillName, newLevel));
        return builder;
    }

    @ZenMethod
    public static ResearchBuilder setRequiredSkill(ResearchBuilder builder, String skill, int level)
    {
        builder.criteria.add(new CriterionSkill(skill, level));
        return builder;
    }

    public static class Helper
    {
        public static Skill getSkill(String skillName)
        {
            skillName = skillName.replaceAll(":", ".");
            String[] parts = skillName.split("\\.");
            ResourceLocation rl = parts.length > 1 ? new ResourceLocation(parts[0], skillName.substring(parts[0].length() + 1)) : new ResourceLocation(skillName);
            return ReskillableRegistries.SKILLS.getValue(rl);
        }

        public static int getLevel(EntityPlayer player, Skill skill)
        {
            PlayerData data = PlayerDataHandler.get(player);
            PlayerSkillInfo skillInfo = data.getSkillInfo(skill);
            return skillInfo.getLevel();
        }
    }
}
