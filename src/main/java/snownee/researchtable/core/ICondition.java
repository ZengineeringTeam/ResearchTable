package snownee.researchtable.core;

import java.util.function.Supplier;

public interface ICondition<T> {
    Supplier<Class<T>> getMatchType();

    long matches(T e);

    long getGoal();
}
