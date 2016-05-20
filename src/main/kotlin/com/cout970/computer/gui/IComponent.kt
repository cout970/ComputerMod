package com.cout970.computer.gui

import com.cout970.computer.util.vector.Vec2d

/**
 * Created by cout970 on 20/05/2016.
 */
interface IComponent {

    fun drawFirstLayer(gui: IGui, mouse: Vec2d, partialTicks: Float)

    fun drawSecondLayer(gui: IGui, mouse: Vec2d) = {}

    //returns true if this should block the event in others components
    fun onMouseClick(gui: IGui, mouse: Vec2d, mouseButton: Int): Boolean = false

    //called when the mouse moves while one button is pressed
    //returns true if this should block the event in others components
    fun onMouseClickMove(gui: IGui, mouse: Vec2d, clickedMouseButton: Int, timeSinceLastClick: Long): Boolean = false

    fun onMouseReleased(gui: IGui, mouse: Vec2d, state: Int) = {}

    //returns true if this should block the event in others components
    fun onKeyTyped(gui: IGui, typedChar: Char, keyCode: Int): Boolean = false

    fun onGuiClosed(gui: IGui) = {}
}