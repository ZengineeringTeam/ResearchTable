package snownee.researchtable.plugin.itemstages;

import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.itemstages.ItemStages;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.IModule;
import snownee.kiwi.KiwiModule;
import snownee.researchtable.ResearchTable;
import snownee.researchtable.client.renderer.EventShowItemCondition;

@KiwiModule(modid = ResearchTable.MODID, name = "itemstages", dependency = "itemstages", optional = true)
public class ItemStagesPlugin implements IModule {
    @Override
    @SideOnly(Side.CLIENT)
    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onCondition(EventShowItemCondition event) {
        event.stacks.removeIf(stack -> {
            String itemsStage = ItemStages.getStage(stack);
            if (itemsStage != null) {
                return !GameStageHelper.hasStage(Minecraft.getMinecraft().player, itemsStage);
            }
            return false;
        });
    }
}
