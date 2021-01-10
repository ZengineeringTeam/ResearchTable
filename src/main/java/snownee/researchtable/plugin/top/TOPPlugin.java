package snownee.researchtable.plugin.top;

import java.util.function.Function;

import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import snownee.kiwi.IModule;
import snownee.kiwi.KiwiModule;
import snownee.researchtable.ResearchTable;

@KiwiModule(modid = ResearchTable.MODID, name = "theoneprobe", dependency = "theoneprobe", optional = true)
public class TOPPlugin implements IModule {

    @Override
    public void preInit() {
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "snownee.researchtable.plugin.top.TOPPlugin$GetTheOneProbe");
    }

    public static class GetTheOneProbe implements Function<ITheOneProbe, Void> {

        @Override
        public Void apply(ITheOneProbe probe) {
            probe.registerProvider(new TableInfoProvider());
            return null;
        }

    }

}
