package com.cout970.computer.gui.components

import com.cout970.computer.gui.*
import com.cout970.computer.util.resource
import com.cout970.computer.util.vector.Vec2d
import net.minecraft.util.ResourceLocation

/**
 * Created by cout970 on 20/05/2016.
 */
class SimpleButton(ID: Int, pos: Vec2d, listener: IButtonListener?,
                   val uv: Vec2d
) : AbstractStateButtonComponent(ID, pos, Vec2d(16, 16), listener, null, resource("textures/gui/buttons.png")){

    override  fun getOffset(): Vec2d {
        when(state){
            ButtonState.HOVER -> return uv + Vec2d(0, 16)
            ButtonState.DOWN -> return uv + Vec2d(0, 32)
            else -> return uv
        }
    }
}

abstract class AbstractStateButtonComponent(ID: Int = 0, pos: Vec2d, size: Vec2d = Vec2d(16, 16),
                                  listener: IButtonListener? = null, sound: ResourceLocation? = null,
                                  texture: ResourceLocation) : AbstractButton(ID, pos, size, listener, sound, texture) {

    var state: ButtonState = ButtonState.NORMAL

    override fun drawFirstLayer(gui: IGui, mouse: Vec2d, partialTicks: Float) {
        if(isInside(mouse, pos, size)){
            if(isMouseButtonDown(MOUSE_BUTTON_LEFT)){
                state = ButtonState.DOWN
            }else {
                state = ButtonState.HOVER
            }
        }else{
            state = ButtonState.NORMAL
        }
        super.drawFirstLayer(gui, mouse, partialTicks)
    }
}

open class AbstractButton(
        val ID: Int,
        var pos: Vec2d,
        val size: Vec2d,
        var listener: IButtonListener?,
        var sound: ResourceLocation?,
        var texture: ResourceLocation
) : IComponent {

    override fun drawFirstLayer(gui: IGui, mouse: Vec2d, partialTicks: Float) {
        gui.bindTexture(texture)
        gui.drawTexturedModalRect(pos, size, getOffset())
    }

    override fun onMouseClick(gui: IGui, mouse: Vec2d, mouseButton: Int): Boolean {
        if (isInside(mouse, pos, size)) {
            var press: Boolean
            if (listener != null) {
                press = listener?.onPress(this, mouse, mouseButton) ?: false
            } else {
                press = true
            }
            if (press) {
                playSound()
                return true
            }
        }
        return false
    }

    open fun getOffset(): Vec2d = Vec2d()

    open fun playSound() = {}
}

interface IButtonListener {
    fun onPress(button: AbstractButton, mouse: Vec2d, mouseButton: Int): Boolean;
}

enum class ButtonState(val parent: ButtonState?) {
    NORMAL(null),
    HOVER(NORMAL),
    DOWN(HOVER),
    DISABLED(null),
    DISABLED_HOVER(DISABLED),
    DISABLED_DOWN(DISABLED_HOVER),
    ACTIVE(null),
    ACTIVE_HOVER(ACTIVE),
    ACTIVE_DOWN(ACTIVE_HOVER),
    HIGHLIGHT(null);
}