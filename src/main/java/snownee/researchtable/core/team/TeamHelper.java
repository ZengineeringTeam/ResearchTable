package snownee.researchtable.core.team;

import javax.annotation.Nonnull;

import net.minecraftforge.fml.common.Loader;
import snownee.researchtable.plugin.togetherforever.TeamProviderTF;

public class TeamHelper
{
    @Nonnull
    public static TeamProvider provider;

    static
    {
        if (Loader.isModLoaded("togetherforever"))
        {
            provider = TeamProviderTF.INSTANCE;
        }
        else
        {
            provider = TeamProvider.Stub.INSTANCE;
        }
    }

}
