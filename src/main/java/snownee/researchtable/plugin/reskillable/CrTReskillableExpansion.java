package snownee.researchtable.plugin.reskillable;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import snownee.researchtable.plugin.crafttweaker.ResearchBuilder;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ModOnly("reskillable")
@ZenRegister
@ZenExpansion("ResearchTable.Builder")
public class CrTReskillableExpansion {
    @ZenMethod
    public static ResearchBuilder setRewardSkill(ResearchBuilder builder, String skillName) {
        builder.rewards.add(new RewardSkillLevelUp(skillName));
        return builder;
    }

    @ZenMethod
    public static ResearchBuilder setTriggerSkill(ResearchBuilder builder, String skillName) {
        builder.triggers.add(new RewardSkillLevelUp(skillName));
        return builder;
    }

    @ZenMethod
    public static ResearchBuilder setRewardSkill(ResearchBuilder builder, String skillName, int newLevel) {
        builder.rewards.add(new RewardSetSkillLevel(skillName, newLevel));
        return builder;
    }

    @ZenMethod
    public static ResearchBuilder setTriggerSkill(ResearchBuilder builder, String skillName, int newLevel) {
        builder.triggers.add(new RewardSetSkillLevel(skillName, newLevel));
        return builder;
    }

    @ZenMethod
    public static ResearchBuilder setRequiredSkill(ResearchBuilder builder, String skill, int level) {
        builder.criteria.add(new CriterionSkill(skill, level));
        return builder;
    }
}
