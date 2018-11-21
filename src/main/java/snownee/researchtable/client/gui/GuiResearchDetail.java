package snownee.researchtable.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

public class GuiResearchDetail extends GuiScrollingList
{

    public GuiResearchDetail(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight)
    {
        super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected int getSize()
    {
        return 1;
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick)
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean isSelected(int index)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void drawBackground()
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess)
    {
        // TODO Auto-generated method stub

    }

}
