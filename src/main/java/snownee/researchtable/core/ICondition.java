package snownee.researchtable.core;

public interface ICondition<T>
{
    Class<T> getMatchClass();

    long matches(T e);

    long getGoal();
}
