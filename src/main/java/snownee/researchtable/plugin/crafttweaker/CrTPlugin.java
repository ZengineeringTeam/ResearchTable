package snownee.researchtable.plugin.crafttweaker;

import javax.annotation.Nonnull;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import snownee.researchtable.core.ResearchCategory;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.ResearchTable")
@ZenRegister
public class CrTPlugin
{
    @ZenMethod
    public static ResearchBuilder builder(@Nonnull String name, @Nonnull ResearchCategory category)
    {
        return new ResearchBuilder(name, category);
    }

    @ZenMethod
    public static ResearchCategory addCategory(@Nonnull IItemStack stack)
    {
        return new ResearchCategory(CraftTweakerMC.getItemStack(stack));
    }

    @ZenMethod
    public static boolean remove(@Nonnull String name)
    {
        return false;
        // return ReseachList.MAP.removeIf(e -> e.getName().equals(name));
    }
}
