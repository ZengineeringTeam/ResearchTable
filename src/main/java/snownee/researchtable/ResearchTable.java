package snownee.researchtable;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.network.NetworkChannel;
import snownee.researchtable.client.gui.ConditionRenderer;
import snownee.researchtable.network.PacketResearchChanged;
import snownee.researchtable.plugin.crafttweaker.ConditionCrTItem;
import snownee.researchtable.plugin.crafttweaker.CrTStackRenderer;

@Mod(
        modid = ResearchTable.MODID,
        name = ResearchTable.NAME,
        version = "@VERSION_INJECT@",
        acceptedMinecraftVersions = "[1.12, 1.13)"
)
public class ResearchTable
{

    public static final String MODID = "researchtable";
    public static final String NAME = "ResearchTable";

    private static final ResearchTable INSTANCE = new ResearchTable();

    @Mod.InstanceFactory
    public static ResearchTable getInstance()
    {
        return INSTANCE;
    }

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        NetworkChannel.INSTANCE.register(PacketResearchChanged.class);
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void clientPreInit(FMLPreInitializationEvent event)
    {
        if (Loader.isModLoaded("crafttweaker"))
        {
            ConditionRenderer.register(ConditionCrTItem.class, new CrTStackRenderer.Factory());
        }
    }
}
