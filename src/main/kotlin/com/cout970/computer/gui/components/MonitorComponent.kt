package com.cout970.computer.gui.components

import com.cout970.computer.api.IPeripheralMonitor
import com.cout970.computer.gui.*
import com.cout970.computer.util.resource
import com.cout970.computer.util.vector.Vec2d
import org.lwjgl.opengl.GL11

/**
 * Created by cout970 on 20/05/2016.
 */

class MonitorComponent(val monitor: IPeripheralMonitor) : IComponent {

    val TEXTURE = resource("textures/gui/monitor_text.png")
    var pressedKeyNum = -1
    var pressedKeyCode: Int = 0

    override fun drawFirstLayer(gui: IGui, mouse: Vec2d, partialTicks: Float) {
        if (pressedKeyNum != -1 && !isKeyPressed(pressedKeyNum)) {
            pressedKeyNum = -1
            monitor.onKeyRelease(pressedKeyCode)
        }

        gui.bindTexture(TEXTURE)
        GL11.glColor4f(0.0f, 1.0f, 0.0f, 1.0f)
        val cursor = monitor.cursorPos
        val lines = monitor.lines
        val columns = monitor.columns
        for (line in 0..lines - 1) {
            for (column in 0..columns - 1) {
                var character = monitor.getChar(line * columns + column) and 0xFF
                if (line * columns + column == cursor && monitor.world.worldTime % 20 >= 10) {
                    character = character xor 128
                }
                if (character != 32) {
                    gui.drawModalRectWithCustomSizedTexture(gui.getStart() + Vec2d(15 + column * 4, 15 + line * 4),
                            Vec2d(4, 4), Vec2d((character and 15) * 4, (character shr 4) * 4), Vec2d(64, 64))
                }
            }
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
    }

    override fun onKeyTyped(gui: IGui, typedChar: Char, keyCode: Int): Boolean{

        if (typedChar.toInt() == 27) return false
        var shift = 0
        if (isShiftKeyDown()) shift = shift or 64
        if (isCtrlKeyDown()) shift = shift or 32
        when (typedChar.toByte().toInt()) {
            199 -> sendKey(132 or shift, keyCode)
            200 -> sendKey(128 or shift, keyCode)
            201, 202, 204, 206,
            209 -> sendKey(typedChar.toInt(), keyCode)
            203 -> sendKey(130 or shift, keyCode)
            205 -> sendKey(131 or shift, keyCode)
            207 -> sendKey(133 or shift, keyCode)
            208 -> sendKey(129 or shift, keyCode)
            210 -> sendKey(134 or shift, keyCode)
            0 -> if (keyCode != 54 && keyCode != 42 && keyCode != 56 && keyCode != 184 && keyCode != 29 &&
                    keyCode != 221 && keyCode != 157 && (keyCode < 59 || keyCode > 70) && keyCode != 87 &&
                    keyCode != 88 && keyCode != 197 && keyCode != 183 && keyCode != 0) {
                sendKey(keyCode, keyCode)
            }
            else -> if (typedChar.toInt() > 0 && typedChar.toInt() <= 127) {
                sendKey(typedChar.toInt(), keyCode)
            }
        }
        return true
    }

    fun sendKey(key: Int, num: Int) {
        pressedKeyNum = num
        pressedKeyCode = key
        monitor.onKeyPressed(key)
    }
}