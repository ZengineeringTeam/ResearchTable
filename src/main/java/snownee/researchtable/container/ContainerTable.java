package snownee.researchtable.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import snownee.researchtable.block.TileTable;

public class ContainerTable extends Container
{
    private final TileTable tile;
    private final InventoryPlayer inventory;

    public ContainerTable(TileTable tile, InventoryPlayer inventory)
    {
        this.tile = tile;
        this.inventory = inventory;

        for (int l = 0; l < 3; ++l)
        {
            for (int j1 = 0; j1 < 9; ++j1)
            {
                this.addSlotToContainer(new Slot(inventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1)
        {
            this.addSlotToContainer(new Slot(inventory, i1, 8 + i1 * 18, 161));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        if (tile.hasWorld())
        {
            return playerIn.getDistanceSq((double) tile.getPos().getX() + 0.5D, (double) tile.getPos().getY() + 0.5D,
                    (double) tile.getPos().getZ() + 0.5D) <= 64.0D;
        }
        else
        {
            return false;
        }
    }

}
