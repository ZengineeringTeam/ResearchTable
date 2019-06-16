package snownee.researchtable.plugin.reskillable;

import crafttweaker.CraftTweakerAPI;
import net.minecraftforge.fml.common.Loader;
import snownee.kiwi.IModule;
import snownee.kiwi.KiwiModule;
import snownee.researchtable.ResearchTable;

@KiwiModule(modid = ResearchTable.MODID, name = "gamestages", dependency = "gamestages", optional = true)
public class ReskillablePlugin implements IModule
{
    @Override
    public void preInit()
    {
        if (Loader.isModLoaded("crafttweaker"))
        {
            CraftTweakerAPI.registerClass(CrTReskillableExpansion.class);
        }
    }
}
