package snownee.researchtable.core;

import net.minecraft.item.ItemStack;

public class ResearchCategory {
    public final ItemStack icon;
    public final String nameKey;

    public ResearchCategory(ItemStack icon, String nameKey) {
        this.icon = icon;
        this.nameKey = nameKey;
    }
}
