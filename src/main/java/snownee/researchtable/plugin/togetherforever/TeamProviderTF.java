package snownee.researchtable.plugin.togetherforever;

import java.util.UUID;

import com.buuz135.togetherforever.api.ITogetherTeam;
import com.buuz135.togetherforever.api.TogetherForeverAPI;

import snownee.researchtable.core.team.TeamProvider;

public enum TeamProviderTF implements TeamProvider
{
    INSTANCE;

    @Override
    public UUID getOwner(UUID player)
    {
        ITogetherTeam team = TogetherForeverAPI.getInstance().getPlayerTeam(player);
        return team == null ? null : team.getOwner();
    }
}
