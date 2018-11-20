package snownee.researchtable.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import snownee.kiwi.item.ItemMod;

public class ItemCard extends ItemMod
{

    public ItemCard(String name)
    {
        super(name);
        setCreativeTab(CreativeTabs.MISC);
    }

    public CardHandler getHandler(ItemStack stack)
    {
        return new CardHandler(stack);
    }

}
