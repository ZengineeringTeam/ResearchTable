package snownee.researchtable.plugin;

import javax.annotation.Nonnull;

import crafttweaker.annotations.ZenRegister;
import snownee.researchtable.core.ReseachList;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.ResearchTable")
@ZenRegister
public class CrTPlugin
{
    @ZenMethod
    public static ResearchBuilder builder(@Nonnull String name)
    {
        return new ResearchBuilder(name);
    }

    @ZenMethod
    public static boolean remove(@Nonnull String name)
    {
        return ReseachList.LIST.removeIf(e -> e.getName().equals(name));
    }
}
