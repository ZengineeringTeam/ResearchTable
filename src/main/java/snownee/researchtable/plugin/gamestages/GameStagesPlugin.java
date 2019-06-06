package snownee.researchtable.plugin.gamestages;

import crafttweaker.CraftTweakerAPI;
import net.minecraftforge.fml.common.Loader;
import snownee.kiwi.IModule;
import snownee.kiwi.KiwiModule;
import snownee.researchtable.ResearchTable;

@KiwiModule(modid = ResearchTable.MODID, name = "gamestages", dependency = "gamestages", optional = true)
public class GameStagesPlugin implements IModule
{
    @Override
    public void preInit()
    {
        if (Loader.isModLoaded("crafttweaker"))
        {
            CraftTweakerAPI.registerClass(CrTGameStagesExpansion.class);
        }
    }
}
