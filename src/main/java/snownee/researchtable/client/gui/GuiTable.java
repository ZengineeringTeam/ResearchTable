package snownee.researchtable.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import snownee.researchtable.block.TileTable;
import snownee.researchtable.container.ContainerTable;

public class GuiTable extends GuiContainer
{

    public GuiTable(TileTable tile, InventoryPlayer inventory)
    {
        super(new ContainerTable(tile, inventory));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        // TODO Auto-generated method stub

    }

}
