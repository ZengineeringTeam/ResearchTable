package snownee.researchtable.plugin.reskillable;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class Helper {
    public static Skill getSkill(String skillName) {
        skillName = skillName.replaceAll(":", ".");
        String[] parts = skillName.split("\\.");
        ResourceLocation rl = parts.length > 1 ? new ResourceLocation(parts[0], skillName.substring(parts[0].length() + 1)) : new ResourceLocation(skillName);
        return ReskillableRegistries.SKILLS.getValue(rl);
    }

    public static int getLevel(EntityPlayer player, Skill skill) {
        PlayerData data = PlayerDataHandler.get(player);
        PlayerSkillInfo skillInfo = data.getSkillInfo(skill);
        return skillInfo.getLevel();
    }
}
