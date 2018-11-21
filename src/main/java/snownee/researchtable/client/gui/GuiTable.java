package snownee.researchtable.client.gui;

import java.io.IOException;
import java.util.Collections;

import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.GuiPageButtonList.GuiResponder;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.researchtable.block.TileTable;
import snownee.researchtable.container.ContainerTable;
import snownee.researchtable.core.ReseachList;
import snownee.researchtable.core.Research;
import snownee.researchtable.core.ResearchCategory;

@SideOnly(Side.CLIENT)
public class GuiTable extends GuiContainer implements GuiResponder
{
    private GuiResearchList researchList;

    public GuiTable(TileTable tile, InventoryPlayer inventory)
    {
        super(new ContainerTable(tile, inventory));
        xSize = 256;
        ySize = 128;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        researchList = new GuiResearchList(mc, (int) (xSize * 0.4), ySize, 32, xSize - 32, 20, 30, width, height);
        ReseachList.LIST.add(new Research("hello", ResearchCategory.GENERAL, "hello", "hi", Collections.EMPTY_SET,
                Collections.EMPTY_LIST, null));
        researchList.setCategory(ResearchCategory.GENERAL);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        researchList.drawScreen(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
        // this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);
    }

    public void handleMouseInput() throws IOException
    {
        int mouseX = Mouse.getEventX() * width / mc.displayWidth;
        int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;
        super.handleMouseInput();
        researchList.handleMouseInput(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
    }

    @Override
    public void setEntryValue(int id, boolean value)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setEntryValue(int id, float value)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setEntryValue(int id, String value)
    {
        // TODO Auto-generated method stub

    }

}
