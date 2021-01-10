package snownee.researchtable.plugin.reskillable;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.event.LevelUpEvent;
import codersafterdark.reskillable.api.toast.ToastHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class RewardSetSkillLevel extends RewardSkillLevelUp {
    private final int newLevel;

    public RewardSetSkillLevel(String skillName, int newLevel) {
        super(skillName);
        this.newLevel = newLevel;
    }

    @Override
    public void earn(World world, BlockPos pos, EntityPlayer player) {
        PlayerData data = PlayerDataHandler.get(player);
        PlayerSkillInfo skillInfo = data.getSkillInfo(skill);
        int oldLevel = skillInfo.getLevel();
        if (!MinecraftForge.EVENT_BUS.post(new LevelUpEvent.Pre(player, skill, newLevel, oldLevel))) {
            skillInfo.setLevel(newLevel);
            data.saveAndSync();
            MinecraftForge.EVENT_BUS.post(new LevelUpEvent.Post(player, skill, newLevel, oldLevel));
            if (player instanceof EntityPlayerMP) {
                ToastHelper.sendSkillToast((EntityPlayerMP) player, skill, newLevel);
            }
        }
    }
}
