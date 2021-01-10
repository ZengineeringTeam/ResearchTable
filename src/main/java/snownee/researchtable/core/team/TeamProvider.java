package snownee.researchtable.core.team;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import javax.annotation.Nullable;

public interface TeamProvider {
    @Nullable
    UUID getOwner(UUID player);

    Collection<UUID> getMembers(UUID player);

    @Nullable
    String getTeamName(UUID player);

    public static enum Stub implements TeamProvider {
        INSTANCE;

        @Override
        public UUID getOwner(UUID player) {
            return null;
        }

        @Override
        public Collection<UUID> getMembers(UUID player) {
            return Collections.singleton(player);
        }

        @Override
        public String getTeamName(UUID player) {
            return null;
        }

    }
}
