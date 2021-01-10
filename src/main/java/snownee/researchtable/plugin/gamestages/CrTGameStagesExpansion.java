package snownee.researchtable.plugin.gamestages;

import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableSet;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import snownee.researchtable.plugin.crafttweaker.ResearchBuilder;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ModOnly("gamestages")
@ZenRegister
@ZenExpansion("ResearchTable.Builder")
public class CrTGameStagesExpansion {
    @ZenMethod
    public static ResearchBuilder setRewardStages(ResearchBuilder builder, @Nonnull String... stages) {
        builder.rewards.add(new RewardUnlockStages(stages));
        return builder;
    }

    @ZenMethod
    public static ResearchBuilder setTriggerStages(ResearchBuilder builder, @Nonnull String... stages) {
        builder.triggers.add(new RewardUnlockStages(stages));
        return builder;
    }

    @ZenMethod
    public static ResearchBuilder setRequiredStages(ResearchBuilder builder, @Nonnull String... stages) {
        Set<String> set = ImmutableSet.copyOf(stages);
        builder.criteria.add(new CriterionStages(set, set.size()));
        return builder;
    }

    @ZenMethod
    public static ResearchBuilder setOptionalStages(ResearchBuilder builder, int amount, @Nonnull String... stages) {
        Set<String> set = ImmutableSet.copyOf(stages);
        builder.criteria.add(new CriterionStages(set, amount));
        return builder;
    }
}
