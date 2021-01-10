package snownee.researchtable.plugin.togetherforever;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

import com.buuz135.togetherforever.api.IPlayerInformation;
import com.buuz135.togetherforever.api.ITogetherTeam;
import com.buuz135.togetherforever.api.TogetherForeverAPI;
import com.buuz135.togetherforever.api.event.TeamEvent;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import snownee.researchtable.core.team.TeamHelper;
import snownee.researchtable.core.team.TeamProvider;

public enum TeamProviderTF implements TeamProvider {
    INSTANCE;

    private TeamProviderTF() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerAdd(TeamEvent.PlayerAdd event) {
        if (TeamHelper.provider == this) {
            TeamHelper.onPlayerAdd(event.getPlayerInformation().getUUID(), event.getTogetherTeam().getOwner());
        }
    }

    @Override
    public UUID getOwner(UUID player) {
        ITogetherTeam team = TogetherForeverAPI.getInstance().getPlayerTeam(player);
        return team == null ? null : team.getOwner();
    }

    @Override
    public Collection<UUID> getMembers(UUID player) {
        ITogetherTeam team = TogetherForeverAPI.getInstance().getPlayerTeam(player);
        if (team == null) {
            return Collections.singleton(player);
        }
        return team.getPlayers().stream().map(IPlayerInformation::getUUID).collect(Collectors.toSet());
    }

    @Override
    public String getTeamName(UUID player) {
        ITogetherTeam team = TogetherForeverAPI.getInstance().getPlayerTeam(player);
        return team == null ? null : team.getTeamName();
    }

}
