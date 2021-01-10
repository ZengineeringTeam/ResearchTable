package snownee.researchtable.plugin.grandeconomy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import snownee.kiwi.IModule;
import snownee.kiwi.KiwiModule;
import snownee.researchtable.ResearchTable;
import snownee.researchtable.core.EventOpenTable;
import the_fireplace.grandeconomy.api.GrandEconomyApi;

@KiwiModule(modid = ResearchTable.MODID, name = "grandeconomy", dependency = "grandeconomy")
public class GrandEconomyPlugin implements IModule {
    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onOpen(EventOpenTable event) {
        if (!event.getEntityPlayer().world.isRemote) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setDouble("money", GrandEconomyApi.getBalance(event.getEntityPlayer().getUniqueID(), true));
            tag.setString("singular", GrandEconomyApi.getCurrencyName(1));
            tag.setString("multiple", GrandEconomyApi.getCurrencyName(99));
            event.getTable().getData().setTag("grandeconomy", tag);
            event.getTable().hasChanged = true;
        }
    }
}
