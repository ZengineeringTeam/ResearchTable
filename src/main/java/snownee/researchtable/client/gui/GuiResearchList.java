package snownee.researchtable.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;
import snownee.kiwi.client.AdvancedFontRenderer;
import snownee.researchtable.core.ReseachList;
import snownee.researchtable.core.Research;
import snownee.researchtable.core.ResearchCategory;

public class GuiResearchList extends GuiScrollingList
{
    private final List<Research> researches = new ArrayList<>();
    private final Minecraft mc;

    public GuiResearchList(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight)
    {
        super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
        this.mc = client;
    }

    public void setCategory(ResearchCategory category)
    {
        researches.clear();
        researches.addAll(
                ReseachList.LIST.stream().filter(e -> e.getCategory().equals(category)).collect(Collectors.toList()));
    }

    @Override
    protected int getSize()
    {
        return researches.size();
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
        Gui.drawRect(left, slotTop, left + listWidth, slotTop + slotHeight - 2, 0xFFEEEEEE);
        Research research = researches.get(slotIdx);
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        GlStateManager.disableLighting();
        RenderHelper.enableGUIStandardItemLighting();
        renderItem.renderItemAndEffectIntoGUI(research.getIcon(), left + 6, slotTop + 6);
        renderItem.renderItemOverlayIntoGUI(mc.fontRenderer, research.getIcon(), left + 6, slotTop + 6, null);
        AdvancedFontRenderer.INSTANCE.drawString(research.getTitle(), left + 32, slotTop + 10, 0x000000);
    }

    @Override
    protected void drawGradientRect(int left, int top, int right, int bottom, int color1, int color2)
    {
        super.drawGradientRect(left, top, right, bottom, color1, color2);
    }

}
