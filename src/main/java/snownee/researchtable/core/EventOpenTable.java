package snownee.researchtable.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import snownee.researchtable.block.TileTable;

@Cancelable
public class EventOpenTable extends PlayerEvent {
    private final TileTable table;

    public EventOpenTable(EntityPlayer player, TileTable table) {
        super(player);
        this.table = table;
    }

    public TileTable getTable() {
        return table;
    }

}
