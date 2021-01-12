package snownee.researchtable.plugin.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import snownee.researchtable.client.gui.GuiTable;

@JEIPlugin
public class JeiPlugin implements IModPlugin {

    @Override
    public void register(IModRegistry registry) {
        registry.addGuiScreenHandler(GuiTable.class, $ -> null);
    }

}
