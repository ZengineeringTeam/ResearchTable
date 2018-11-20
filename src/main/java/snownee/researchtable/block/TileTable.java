package snownee.researchtable.block;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import snownee.kiwi.tile.TileInventoryBase;
import snownee.researchtable.ResearchTableModule;

public class TileTable extends TileInventoryBase
{
    public TileTable()
    {
        super(1, 1);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return index == 0 && stack.getItem() == ResearchTableModule.CARD;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (!stacks.getStackInSlot(0).isEmpty())
        {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            {

            }
        }
        return super.getCapability(capability, facing);
    }
}
