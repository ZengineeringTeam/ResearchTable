package snownee.researchtable.plugin.forge;

import java.util.function.Supplier;

import snownee.researchtable.core.ConditionTypes;
import snownee.researchtable.core.ICondition;

public class ConditionForgeEnergy implements ICondition<Long> {
    private final long count;

    public ConditionForgeEnergy(long count) {
        this.count = count;
    }

    @Override
    public long matches(Long e) {
        return e;
    }

    @Override
    public long getGoal() {
        return count;
    }

    @Override
    public Supplier<Class<Long>> getMatchType() {
        return ConditionTypes.ENERGY;
    }
}
