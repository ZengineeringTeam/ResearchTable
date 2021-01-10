package snownee.researchtable.plugin.reskillable;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.event.LevelUpEvent;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.toast.ToastHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import snownee.researchtable.core.IReward;

public class RewardSkillLevelUp implements IReward {
    protected final Skill skill;

    public RewardSkillLevelUp(String skillName) {
        skill = Helper.getSkill(skillName);
        if (skill == null) {
            throw new NullPointerException("Unknown skill name " + skillName);
        }
    }

    @Override
    public void earn(World world, BlockPos pos, EntityPlayer player) {
        PlayerData data = PlayerDataHandler.get(player);
        PlayerSkillInfo skillInfo = data.getSkillInfo(skill);
        int oldLevel = skillInfo.getLevel();
        if (!MinecraftForge.EVENT_BUS.post(new LevelUpEvent.Pre(player, skill, oldLevel + 1, oldLevel))) {
            skillInfo.levelUp();
            data.saveAndSync();
            MinecraftForge.EVENT_BUS.post(new LevelUpEvent.Post(player, skill, skillInfo.getLevel(), oldLevel));
            if (player instanceof EntityPlayerMP) {
                ToastHelper.sendSkillToast((EntityPlayerMP) player, skill, skillInfo.getLevel());
            }
        }
    }
}
