package snownee.researchtable;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
}
