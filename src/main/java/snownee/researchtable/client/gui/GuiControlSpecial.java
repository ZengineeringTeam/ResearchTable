package snownee.researchtable.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.client.gui.GuiControl;
import snownee.kiwi.client.gui.IMessageHandler;
import snownee.kiwi.client.gui.component.Component;

@SideOnly(Side.CLIENT)
public class GuiControlSpecial extends GuiControl {

    public GuiControlSpecial(Minecraft mc, int width, int height, IMessageHandler messageHandler) {
        super(mc, width, height, messageHandler);
    }

    @Override
    public void addComponent(Component component) {
        if (components.size() > 2) {
            components.add(components.size() - 3, component);
        } else {
            components.add(component);
        }
    }

}
