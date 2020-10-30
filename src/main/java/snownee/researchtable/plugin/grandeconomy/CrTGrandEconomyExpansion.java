package snownee.researchtable.plugin.grandeconomy;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import snownee.researchtable.plugin.crafttweaker.ResearchBuilder;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ModOnly("grandeconomy")
@ZenRegister
@ZenExpansion("snownee.researchtable.plugin.crafttweaker.ResearchBuilder")
public class CrTGrandEconomyExpansion
{
    @ZenMethod
    public static ResearchBuilder setRewardMoneyGE(ResearchBuilder builder, double money, @Optional boolean showMsg)
    {
        builder.rewards.add(new RewardMoneyGE(money, showMsg));
        return builder;
    }

    @ZenMethod
    public static ResearchBuilder setTriggerMoneyGE(ResearchBuilder builder, double money, @Optional boolean showMsg)
    {
        builder.triggers.add(new RewardMoneyGE(money, showMsg));
        return builder;
    }

    @ZenMethod
    public static ResearchBuilder setRequiredMoneyGE(ResearchBuilder builder, double money)
    {
        builder.criteria.add(new CriterionMoneyGE(money));
        return builder;
    }
}
