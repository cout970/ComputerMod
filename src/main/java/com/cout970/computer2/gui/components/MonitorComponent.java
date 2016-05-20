package com.cout970.computer2.gui.components;

import com.cout970.computer2.ComputerMod;
import com.cout970.computer.api.IPeripheralMonitor;
import net.darkaqua.blacksmith.api.common.gui.IGui;
import net.darkaqua.blacksmith.api.common.gui.IGuiComponent;
import net.darkaqua.blacksmith.api.common.util.ResourceReference;
import net.darkaqua.blacksmith.api.common.util.vectors.Vect2d;
import net.darkaqua.blacksmith.api.common.util.vectors.Vect2i;
import org.lwjgl.opengl.GL11;

/**
 * Created by cout970 on 07/02/2016.
 */
public class MonitorComponent implements IGuiComponent {

    private static final ResourceReference texture = new ResourceReference(ComputerMod.MOD_ID, "textures/gui/monitor_text.png");
    private IPeripheralMonitor monitor;
    private int pressedKeyNum = -1;
    private int pressedKeyCode;

    public MonitorComponent(IPeripheralMonitor monitor) {
        this.monitor = monitor;
    }

    public IPeripheralMonitor getMonitor() {
        return monitor;
    }

    @Override
    public void renderBackground(IGui gui, Vect2i mouse, float partialTicks) {
        if (pressedKeyNum != -1 && !gui.isKeyPressed(pressedKeyNum)){
            pressedKeyNum = -1;
            monitor.onKeyRelease(pressedKeyCode);
        }

        gui.getGuiRenderer().bindTexture(texture);
        GL11.glColor4f(0.0F, 1.0F, 0.0F, 1.0F);
        int cursor = monitor.getCursorPos();
        int lines = monitor.getLines();
        int columns = monitor.getColumns();
        for (int line = 0; line < lines; line++) {
            for (int column = 0; column < columns; column++) {
                int character = monitor.getChar(line * columns + column) & 255;
                if (line * columns + column == cursor && monitor.getWorldRef().getWorldTime() % 20 >= 10) {
                    character ^= 128;
                }
                if (character != 32) {
                    gui.getGuiRenderer().drawRectangleWithCustomSizedTexture(gui.getGuiStartingPoint().add(15 + column * 4, 15 + line * 4),
                            new Vect2i(4, 4), new Vect2d((character & 15) * 4, (character >> 4) * 4), new Vect2d(64, 64));
                }
            }
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void renderForeground(IGui gui, Vect2i mouse) {}

    @Override
    public void onMouseClick(IGui gui, Vect2i mouse, MouseButton button) {}

    @Override
    public boolean onKeyPressed(IGui gui, int num, char key) {
        if (key == 27) return false;
        int shift = 0;
        if (gui.isShiftKeyPressed()) shift |= 64;
        if (gui.isCtrlKeyPressed()) shift |= 32;
        switch (key) {
            case 199:
                sendKey(132 | shift, num);
                break;
            case 200:
                sendKey(128 | shift, num);
                break;
            case 201:
            case 202:
            case 204:
            case 206:
            case 209:
            default:
                if (key > 0 && key <= 127) {
                    sendKey(key, num);
                }
                break;

            case 203:
                sendKey(130 | shift, num);
                break;
            case 205:
                sendKey(131 | shift, num);
                break;
            case 207:
                sendKey(133 | shift, num);
                break;
            case 208:
                sendKey(129 | shift, num);
                break;
            case 210:
                sendKey(134 | shift, num);
                break;
            case 0:
                if (num != 54 && num != 42 && num != 56 && num != 184 && num != 29 && num != 221 && num != 157 && (num < 59 || num > 70) && num != 87 && num != 88 && num != 197 && num != 183 && num != 0) {
                    sendKey(num, num);
                }
                break;
        }
        return true;
    }

    public void sendKey(int key, int num) {
        pressedKeyNum = num;
        pressedKeyCode = key;
        monitor.onKeyPressed(key);
    }
}
