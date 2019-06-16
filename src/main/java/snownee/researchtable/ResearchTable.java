package snownee.researchtable;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.network.NetworkChannel;
import snownee.researchtable.client.renderer.ConditionRenderer;
import snownee.researchtable.command.CommandResearch;
import snownee.researchtable.network.PacketResearchChanged;
import snownee.researchtable.network.PacketSyncClient;
import snownee.researchtable.plugin.crafttweaker.ConditionCrTItem;
import snownee.researchtable.plugin.crafttweaker.ConditionCrTLiquid;
import snownee.researchtable.plugin.crafttweaker.RendererCrTItem;
import snownee.researchtable.plugin.crafttweaker.RendererCrTLiquid;

@Mod(
        modid = ResearchTable.MODID, name = ResearchTable.NAME, version = "@VERSION_INJECT@", acceptedMinecraftVersions = "[1.12, 1.13)", useMetadata = true
)
// @EventBusSubscriber(value = Side.CLIENT)
public class ResearchTable
{
    public static final String MODID = "researchtable";
    public static final String NAME = "ResearchTable";

    private static final ResearchTable INSTANCE = new ResearchTable();

    public static String scoreFormattingText;
    public static String scores[];

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
        NetworkChannel.INSTANCE.register(PacketSyncClient.class);
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void clientPreInit(FMLPreInitializationEvent event)
    {
        if (Loader.isModLoaded("crafttweaker"))
        {
            ConditionRenderer.register(ConditionCrTItem.class, new RendererCrTItem.Factory());
            ConditionRenderer.register(ConditionCrTLiquid.class, new RendererCrTLiquid.Factory());
        }
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandResearch());
    }

    // When current GUI is not null, you cannot receive event, I am not sure if it is a bug
    //    @SubscribeEvent
    //    @SideOnly(Side.CLIENT)
    //    public static void onGameStageEvent(GameStageEvent event)
    //    {
    //        if (event.getClass() == GameStageEvent.Check.class || event.getClass() == GameStageEvent.Add.class || event.getClass() == GameStageEvent.Remove.class)
    //        {
    //            return;
    //        }
    //        GuiScreen gui = Minecraft.getMinecraft().currentScreen;
    //        if (gui != null && gui.getClass() == GuiTable.class)
    //        {
    //            ((GuiTable) gui).resetProgress();
    //        }
    //    }
}
