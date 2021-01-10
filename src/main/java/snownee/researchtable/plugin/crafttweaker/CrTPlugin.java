package snownee.researchtable.plugin.crafttweaker;

import javax.annotation.Nonnull;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import snownee.researchtable.ResearchTable;
import snownee.researchtable.core.ResearchCategory;
import snownee.researchtable.core.ResearchList;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.ResearchTable")
@ZenRegister
public class CrTPlugin {
    @ZenMethod
    public static ResearchBuilder builder(@Nonnull String name, @Nonnull ResearchCategoryWrapper category) {
        return new ResearchBuilder(name, category);
    }

    @ZenMethod
    public static ResearchCategoryWrapper addCategory(@Nonnull IItemStack stack, @Optional String name) {
        return new ResearchCategoryWrapper(new ResearchCategory(CraftTweakerMC.getItemStack(stack), name));
    }

    @ZenMethod
    public static boolean remove(@Nonnull String name) {
        return ResearchList.LIST.remove(name) != null;
    }

    @ZenMethod
    public static void removeAll() {
        ResearchList.LIST.clear();
    }

    @ZenMethod
    public static void scoreIndicator(String formattingText, String... scores) {
        ResearchTable.scoreFormattingText = formattingText;
        ResearchTable.scores = scores;
    }
}
