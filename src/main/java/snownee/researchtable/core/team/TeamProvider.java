package snownee.researchtable.core.team;

import java.util.UUID;

import javax.annotation.Nullable;

public interface TeamProvider
{
    @Nullable
    UUID getOwner(UUID player);

    public static enum Stub implements TeamProvider
    {
        INSTANCE;

        @Override
        public UUID getOwner(UUID player)
        {
            return null;
        }
    }
}
