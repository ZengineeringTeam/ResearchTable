package snownee.researchtable.core.team;

import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraftforge.fml.common.Loader;
import snownee.researchtable.core.DataStorage;
import snownee.researchtable.plugin.togetherforever.TeamProviderTF;

public class TeamHelper {
    static {
        if (Loader.isModLoaded("togetherforever")) {
            provider = TeamProviderTF.INSTANCE;
        } else {
            provider = TeamProvider.Stub.INSTANCE;
        }
    }

    @Nonnull
    public static TeamProvider provider;

    public static void onPlayerAdd(UUID uuid, UUID owner) {
        DataStorage.onPlayerAdd(owner, owner);
    }
}
