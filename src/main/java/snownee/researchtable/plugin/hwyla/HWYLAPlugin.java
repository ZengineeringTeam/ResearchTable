package snownee.researchtable.plugin.hwyla;

import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import snownee.researchtable.block.BlockTable;

@WailaPlugin
public class HWYLAPlugin implements IWailaPlugin {

    @Override
    public void register(IWailaRegistrar registrar) {
        TableInfoProvider provider = new TableInfoProvider();
        registrar.registerBodyProvider(provider, BlockTable.class);
        registrar.registerNBTProvider(provider, BlockTable.class);
    }

}
