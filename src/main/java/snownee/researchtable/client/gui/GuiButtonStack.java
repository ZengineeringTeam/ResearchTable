package snownee.researchtable.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiButtonStack extends GuiButton {
    private final ItemStack stack;

    public GuiButtonStack(int buttonId, int x, int y, ItemStack stack) {
        super(buttonId, x, y, ComponentResearchList.TAB_WIDTH, ComponentResearchList.TAB_WIDTH, stack.getDisplayName());
        this.stack = stack;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (!visible)
            return;
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        if (isSelected()) {
            Gui.drawRect(x + 2, y + 1, x + width, y + height - 1, 0xFFEEEEEE);
        } else if (isMouseOver()) {
            Gui.drawRect(x + 2, y + 1, x + width, y + height - 1, 0xFFDDDDDD);
        }
        mc.getRenderItem().renderItemIntoGUI(stack, x + width / 2 - 8, y + height / 2 - 8);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            onClick();
            return true;
        }
        return false;
    }

    public void onClick() {
    }

    public boolean isSelected() {
        return false;
    }
}
