package snownee.researchtable.client.renderer;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EventShowItemCondition extends Event {
    public final NonNullList<ItemStack> stacks;

    public EventShowItemCondition(NonNullList<ItemStack> stacks) {
        this.stacks = stacks;
    }
}
