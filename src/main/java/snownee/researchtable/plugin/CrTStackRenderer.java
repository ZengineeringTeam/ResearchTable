package snownee.researchtable.plugin;

import java.util.List;

import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import snownee.researchtable.client.gui.ConditionRenderer;

public class CrTStackRenderer extends ConditionRenderer<ConditionCrTStack>
{
    private final NonNullList<ItemStack> stacks;

    public CrTStackRenderer(ConditionCrTStack condition)
    {
        stacks = NonNullList.create();
        List<IItemStack> items = condition.ingredient.getItems();
        for (IItemStack stack : items)
        {
            if (stack.getMetadata() == OreDictionary.WILDCARD_VALUE)
            {
                Item item = CraftTweakerMC.getItemStack(stack).getItem();
                item.getSubItems(item.getCreativeTab(), stacks);
            }
            else if (!stack.isEmpty())
            {
                stacks.add(CraftTweakerMC.getItemStack(stack));
            }
        }
    }

    @Override
    public void draw(Minecraft mc, int x, int y)
    {
        if (!stacks.isEmpty())
        {
            mc.getRenderItem().renderItemAndEffectIntoGUI(
                    stacks.get((int) ((Minecraft.getSystemTime() / 1500) % stacks.size())), x, y);
        }
    }

    @Override
    public String name()
    {
        if (!stacks.isEmpty())
        {
            return stacks.get((int) ((Minecraft.getSystemTime() / 1500) % stacks.size())).getDisplayName();
        }
        return ItemStack.EMPTY.getDisplayName();
    }

    public static class Factory implements ConditionRendererFactory<ConditionCrTStack>
    {

        @Override
        public ConditionRenderer<ConditionCrTStack> get(ConditionCrTStack condition)
        {
            return new CrTStackRenderer(condition);
        }

    }

}
