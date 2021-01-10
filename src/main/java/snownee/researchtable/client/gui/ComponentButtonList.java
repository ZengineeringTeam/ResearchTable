package snownee.researchtable.client.gui;

import java.util.Collections;

import org.lwjgl.input.Mouse;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.client.AdvancedFontRenderer;
import snownee.kiwi.client.gui.GuiControl;
import snownee.kiwi.client.gui.component.Component;
import snownee.kiwi.client.gui.element.DrawableNineSlice;
import snownee.researchtable.ResearchTable;

@SideOnly(Side.CLIENT)
public class ComponentButtonList extends Component {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/beacon.png");
    private static final DrawableNineSlice DRAWABLE_NORMAL;
    private static final DrawableNineSlice DRAWABLE_CLICKED;
    private static final DrawableNineSlice DRAWABLE_DISABLED;
    private static final DrawableNineSlice DRAWABLE_HOVERED;
    private static final DrawableNineSlice[] DRAWABLES;
    private State[] states = new State[] { State.INVISIBLE, State.NORMAL };
    private String[] texts = new String[2];
    private int[] widths = new int[2];
    private boolean cancel;

    static {
        DRAWABLE_NORMAL = new DrawableNineSlice(TEXTURE, 0, 219, 22, 22, 1, 1, 1, 1);
        DRAWABLE_CLICKED = new DrawableNineSlice(TEXTURE, 22, 219, 22, 22, 1, 1, 1, 1);
        DRAWABLE_DISABLED = new DrawableNineSlice(TEXTURE, 44, 219, 22, 22, 1, 1, 1, 1);
        DRAWABLE_HOVERED = new DrawableNineSlice(TEXTURE, 66, 219, 22, 22, 1, 1, 1, 1);
        DRAWABLES = new DrawableNineSlice[] { DRAWABLE_NORMAL, DRAWABLE_CLICKED, DRAWABLE_DISABLED, DRAWABLE_HOVERED };
    }

    public enum State {
        NORMAL, CLICKED, DISABLED, HOVERED, INVISIBLE
    }

    public ComponentButtonList(GuiControl parent, int width, int height) {
        super(parent, width, height);
        setText(0, "submit");
        setText(1, "research");
    }

    public void setText(int index, String key) {
        String text = I18n.format(ResearchTable.MODID + ".gui.button." + key);
        texts[index] = text;
        widths[index] = Math.max(AdvancedFontRenderer.INSTANCE.getStringWidth(text) + 4, 40);
        if (index == 1) {
            cancel = key.equals("cancel");
        }
    }

    public void setState(int index, State state) {
        states[index] = state;
    }

    @Override
    public void drawScreen(int offsetX, int offsetY, int relMouseX, int relMouseY, float partialTicks) {
        offsetY += 4;
        int x = width;
        for (int i = states.length - 1; i >= 0; i--) {
            if (states[i] == State.INVISIBLE) {
                continue;
            }
            x -= widths[i];
            if (states[i] != State.DISABLED) {
                if (GuiTable.isInRegion(x, 4, x + widths[i], 16, relMouseX, relMouseY)) {
                    if (Mouse.isButtonDown(0)) {
                        states[i] = State.CLICKED;
                    } else {
                        states[i] = State.HOVERED;
                        if (i == 1 && cancel) {
                            setTooltip(Collections.singletonList(I18n.format("researchtable.gui.button.shift")), AdvancedFontRenderer.INSTANCE);
                        }
                    }
                } else {
                    states[i] = State.NORMAL;
                }
            }
            Tessellator.getInstance().getBuffer().setTranslation(0, 0, 0);
            GlStateManager.color(1, 1, 1, 1);
            DrawableNineSlice drawable = DRAWABLES[states[i].ordinal()];
            drawable.setWidth(widths[i]);
            drawable.setHeight(12);
            drawable.draw(parent.mc, x + offsetX, offsetY);
            int stringWidth = AdvancedFontRenderer.INSTANCE.getStringWidth(texts[i]);
            AdvancedFontRenderer.INSTANCE.drawString(texts[i], x + offsetX + (widths[i] - stringWidth) / 2, offsetY + 2, states[i] == State.DISABLED ? 0x999999 : 0);
            x -= 5;
        }
    }

    @Override
    public void handleMouseInput(int relMouseX, int relMouseY) {
        int x = width;
        for (int i = states.length - 1; i >= 0; i--) {
            if (states[i] == State.INVISIBLE) {
                continue;
            }
            x -= widths[i];
            if (states[i] != State.DISABLED && GuiTable.isInRegion(x, 4, x + widths[i], 16, relMouseX, relMouseY)) {
                sendMessage(i, states[i].ordinal());
            }
            x -= 5;
        }
    }

}
