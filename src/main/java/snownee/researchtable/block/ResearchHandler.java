package snownee.researchtable.block;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ResearchHandler implements IItemHandler
{
    private final ItemStack stack;

    ResearchHandler(ItemStack stack)
    {
        this.stack = stack;
    }

    @Override
    public int getSlots()
    {
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return 0;
    }

}
