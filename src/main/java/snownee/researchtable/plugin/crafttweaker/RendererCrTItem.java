package snownee.researchtable.plugin.crafttweaker;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import snownee.kiwi.util.Util;
import snownee.researchtable.client.gui.GuiTable;
import snownee.researchtable.client.renderer.ConditionRenderer;
import snownee.researchtable.client.renderer.EventShowItemCondition;

@SideOnly(Side.CLIENT)
public class RendererCrTItem extends ConditionRenderer<ConditionCrTItem> {
    private final NonNullList<ItemStack> stacks;
    @Nullable
    private final String name;

    public RendererCrTItem(ConditionCrTItem condition) {
        stacks = NonNullList.create();
        List<IItemStack> items = condition.ingredient.getItems();
        for (IItemStack stack : items) {
            if (stack.getMetadata() == OreDictionary.WILDCARD_VALUE) {
                Item item = CraftTweakerMC.getItemStack(stack).getItem();
                item.getSubItems(item.getCreativeTab(), stacks);
            } else if (!stack.isEmpty()) {
                stacks.add(CraftTweakerMC.getItemStack(stack));
            }
        }
        MinecraftForge.EVENT_BUS.post(new EventShowItemCondition(stacks));
        name = condition.customName;
    }

    private ItemStack getStack() {
        return stacks.get((int) ((GuiTable.ticks / 30) % stacks.size()));
    }

    @Override
    public void draw(Minecraft mc, int x, int y) {
        if (!stacks.isEmpty()) {
            mc.getRenderItem().renderItemAndEffectIntoGUI(getStack(), x, y);
        }
    }

    @Override
    public String name() {
        if (name != null) {
            return I18n.format(name);
        }
        if (!stacks.isEmpty()) {
            return getStack().getDisplayName();
        }
        return I18n.format("researchtable.gui.unknown_item");
    }

    @Override
    public String format(long number) {
        return Util.formatComma(number);
    }

    public static class Factory implements ConditionRendererFactory<ConditionCrTItem> {

        @Override
        public ConditionRenderer<ConditionCrTItem> get(ConditionCrTItem condition) {
            return new RendererCrTItem(condition);
        }

    }

    @Override
    public FontRenderer getFont() {
        FontRenderer font = null;
        if (!stacks.isEmpty()) {
            ItemStack stack = getStack();
            font = stack.getItem().getFontRenderer(stack);
        }
        if (font == null) {
            font = Minecraft.getMinecraft().fontRenderer;
        }
        // font.setUnicodeFlag(true);
        return font;
    }

    @Override
    public List<String> getTooltip(ITooltipFlag flag) {
        if (!stacks.isEmpty()) {
            return getStack().getTooltip(null, flag);
        } else {
            return Collections.EMPTY_LIST;
        }
    }

}
