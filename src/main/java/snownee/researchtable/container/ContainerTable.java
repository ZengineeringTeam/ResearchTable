package snownee.researchtable.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import snownee.researchtable.block.TileTable;

public class ContainerTable extends Container {
    private final TileTable tile;
    private final InventoryPlayer inventory;

    public ContainerTable(TileTable tile, InventoryPlayer inventory) {
        this.tile = tile;
        this.inventory = inventory;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        if (tile.hasWorld()) {
            return playerIn.getDistanceSq(tile.getPos().getX() + 0.5D, tile.getPos().getY() + 0.5D, tile.getPos().getZ() + 0.5D) <= 64.0D;
        } else {
            return false;
        }
    }

    @Override
    public void detectAndSendChanges() {
        if (tile.hasChanged) {
            for (IContainerListener listener : listeners) {
                if (listener instanceof EntityPlayerMP) {
                    ((EntityPlayerMP) listener).connection.sendPacket(tile.getUpdatePacket());
                }
            }
            tile.hasChanged = false; // server
        }
    }

}
