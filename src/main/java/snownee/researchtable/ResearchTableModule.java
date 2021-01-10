package snownee.researchtable;

import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import snownee.kiwi.IModule;
import snownee.kiwi.KiwiModule;
import snownee.researchtable.block.BlockTable;
import snownee.researchtable.block.TileTable;
import snownee.researchtable.network.GuiHandler;

@KiwiModule(modid = ResearchTable.MODID)
public class ResearchTableModule implements IModule {
    public static final BlockTable TABLE = new BlockTable("table", Material.IRON);

    @Override
    public void preInit() {
        NetworkRegistry.INSTANCE.registerGuiHandler(ResearchTable.getInstance(), new GuiHandler());
    }

    @Override
    public void init() {
        GameRegistry.registerTileEntity(TileTable.class, new ResourceLocation(ResearchTable.MODID, "table"));
    }
}
