package snownee.researchtable.core;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ResearchCategory
{
    public static final ResearchCategory GENERAL = new ResearchCategory(new ItemStack(Items.DIAMOND));

    private final ItemStack icon;

    public ResearchCategory(ItemStack icon)
    {
        this.icon = icon;
    }
}
