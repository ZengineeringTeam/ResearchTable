package snownee.researchtable.client.gui;

import org.lwjgl.input.Mouse;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import snownee.kiwi.client.AdvancedFontRenderer;
import snownee.kiwi.client.gui.GuiControl;
import snownee.kiwi.client.gui.component.Component;
import snownee.kiwi.client.gui.element.DrawableNineSlice;
import snownee.researchtable.ResearchTable;

public class ComponentButtonList extends Component
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/beacon.png");
    private static final DrawableNineSlice DRAWABLE_NORMAL;
    private static final DrawableNineSlice DRAWABLE_CLICKED;
    private static final DrawableNineSlice DRAWABLE_DISABLED;
    private static final DrawableNineSlice DRAWABLE_HOVERED;
    private static final DrawableNineSlice[] DRAWABLES;
    public State[] states = new State[] { State.INVISIBLE, State.NORMAL };
    public String[] texts = new String[] { "submit", "research" };

    static
    {
        DRAWABLE_NORMAL = new DrawableNineSlice(TEXTURE, 0, 219, 22, 22, 1, 1, 1, 1);
        DRAWABLE_CLICKED = new DrawableNineSlice(TEXTURE, 22, 219, 22, 22, 1, 1, 1, 1);
        DRAWABLE_DISABLED = new DrawableNineSlice(TEXTURE, 44, 219, 22, 22, 1, 1, 1, 1);
        DRAWABLE_HOVERED = new DrawableNineSlice(TEXTURE, 66, 219, 22, 22, 1, 1, 1, 1);
        DRAWABLES = new DrawableNineSlice[] { DRAWABLE_NORMAL, DRAWABLE_CLICKED, DRAWABLE_DISABLED, DRAWABLE_HOVERED };
    }

    public enum State
    {
        NORMAL, CLICKED, DISABLED, HOVERED, INVISIBLE
    }

    public ComponentButtonList(GuiControl parent, int width, int height)
    {
        super(parent, width, height);
    }

    @Override
    public void drawScreen(int offsetX, int offsetY, int relMouseX, int relMouseY, float partialTicks)
    {
        offsetX += 60;
        for (int i = 0; i < states.length; i++)
        {
            if (states[i] == State.INVISIBLE)
            {
                continue;
            }
            if (states[i] != State.DISABLED)
            {
                if (GuiTable.isInRegion(60 + 45 * i, 0, 60 + 45 * i + 40, 12, relMouseX, relMouseY))
                {
                    if (Mouse.isButtonDown(0))
                    {
                        states[i] = State.CLICKED;
                    }
                    else
                    {
                        states[i] = State.HOVERED;
                    }
                }
                else
                {
                    states[i] = State.NORMAL;
                }
            }
            Tessellator.getInstance().getBuffer().setTranslation(0, 0, 0);
            GlStateManager.color(1, 1, 1, 1);
            DrawableNineSlice drawable = DRAWABLES[states[i].ordinal()];
            drawable.setWidth(40);
            drawable.setHeight(12);
            drawable.draw(parent.mc, offsetX + 45 * i, offsetY);
            String text = I18n.format(ResearchTable.MODID + ".gui.button." + texts[i]);
            int width = AdvancedFontRenderer.INSTANCE.getStringWidth(text);
            AdvancedFontRenderer.INSTANCE.drawString(text, offsetX + 45 * i + 20 - width / 2, offsetY + 2,
                    states[i] == State.DISABLED ? 0x999999 : 0);
        }
    }

    @Override
    public void handleMouseInput(int relMouseX, int relMouseY)
    {
        for (int i = 0; i < states.length; i++)
        {
            if (states[i] != State.DISABLED && states[i] != State.INVISIBLE
                    && GuiTable.isInRegion(60 + 45 * i, 0, 60 + 45 * i + 40, 12, relMouseX, relMouseY))
            {
                sendMessage(i, states[i].ordinal());
            }
        }
    }

}
