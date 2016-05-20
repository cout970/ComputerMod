package com.cout970.computer.gui

import com.cout970.computer.util.vector.Vec2d
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.inventory.Container
import net.minecraft.util.ResourceLocation
import java.io.IOException

/**
 * Created by cout970 on 20/05/2016.
 */
abstract class GuiBase(cont: Container) : GuiContainer(cont), IGui {

    val components = mutableListOf<IComponent>()

    override fun initGui() {
        super.initGui()
        components.clear()
        initComponents()
    }

    abstract fun initComponents()

    abstract fun getBackground() : ResourceLocation?

    open fun getBackTexSize() = Vec2d(256, 256)

    open fun getBackgroundEnd() = Vec2d(176, 166)

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        val background = getBackground()
        if(background != null) {
            bindTexture(background)
            drawScaledCustomSizeModalRect(getStart(), getSize(), Vec2d(), getBackgroundEnd(), getBackTexSize())
        }
        components.forEach { it.drawFirstLayer(this, Vec2d(mouseX, mouseY), partialTicks) }
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        components.forEach { it.drawSecondLayer(this, Vec2d(mouseX, mouseY)) }
    }

    @Throws(IOException::class)
    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        var block = false;
        for (it in components) {
            if (it.onMouseClick(this, Vec2d(mouseX, mouseY), mouseButton)) {
                block = true
                break;
            }
        }
        if (!block) {
            super.mouseClicked(mouseX, mouseY, mouseButton)
        }
    }

    override fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
        var block = false;
        for (it in components) {
            if (it.onMouseClickMove(this, Vec2d(mouseX, mouseY), clickedMouseButton, timeSinceLastClick)) {
                block = true
                break;
            }
        }
        if (!block) {
            super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick)
        }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int){
        components.forEach { it.onMouseReleased(this, Vec2d(mouseX, mouseY), state) }
        super.mouseReleased(mouseX, mouseY, state)
    }

    @Throws(IOException::class)
    override fun keyTyped(typedChar: Char, keyCode: Int){
        var block = false;
        for (it in components) {
            if (it.onKeyTyped(this, typedChar, keyCode)) {
                block = true
                break;
            }
        }
        if (!block) {
            super.keyTyped(typedChar, keyCode)
        }
    }

    override fun onGuiClosed(){
        components.forEach { it.onGuiClosed(this) }
        super.onGuiClosed()
    }

    override fun bindTexture(res: ResourceLocation) {
        Minecraft.getMinecraft().renderEngine.bindTexture(res)
    }

    override fun drawHoveringText(textLines: List<String>, pos: Vec2d){
        super.drawHoveringText(textLines, pos.getXi(), pos.getYi())
    }

    override fun getSize(): Vec2d = Vec2d(xSize, ySize)

    override fun getStart(): Vec2d = Vec2d(guiLeft, guiTop)

    override fun getWindowSize(): Vec2d = Vec2d(width, height)

    override fun drawCenteredString(text: String, pos: Vec2d, color: Int) {
        drawCenteredString(fontRendererObj, text, pos.getXi(), pos.getYi(), color)
    }

    override fun drawString(text: String, pos: Vec2d, color: Int) {
        drawString(fontRendererObj, text, pos.getXi(), pos.getYi(), color)
    }

    override fun drawHorizontalLine(startX: Int, endX: Int, y: Int, color: Int){
        super.drawHorizontalLine(startX, endX, y, color)
    }

    override fun drawVerticalLine(x: Int, startY: Int, endY: Int, color: Int){
        super.drawVerticalLine(x, startY, endY, color)
    }

    override fun drawRect(start: Vec2d, end: Vec2d, color: Int) {
        drawRect(start.getXi(), start.getYi(), end.getXi(), end.getYi(), color)
    }

    override fun drawGradientRect(start: Vec2d, end: Vec2d, startColor: Int, endColor: Int) {
        drawGradientRect(start.getXi(), start.getYi(), end.getXi(), end.getYi(), startColor, endColor)
    }

    override fun drawTexturedModalRect(pos: Vec2d, size: Vec2d, texture: Vec2d) {
        drawTexturedModalRect(pos.getXi(), pos.getYi(), texture.getXi(), texture.getYi(), size.getXi(), size.getYi())
    }

    override fun drawTexturedModalRect(pos: Vec2d, size: Vec2d, textureSprite: TextureAtlasSprite) {
        drawTexturedModalRect(pos.getXi(), pos.getYi(), textureSprite, size.getXi(), size.getYi())
    }

    override fun drawModalRectWithCustomSizedTexture(pos: Vec2d, size: Vec2d, uv: Vec2d, textureSize: Vec2d) {
        drawModalRectWithCustomSizedTexture(pos.getXi(), pos.getYi(), uv.getXf(), uv.getYf(), size.getXi(),
                size.getYi(), textureSize.getXf(), textureSize.getYf())
    }

    override fun drawScaledCustomSizeModalRect(pos: Vec2d, size: Vec2d, uvMin: Vec2d, uvMax: Vec2d, textureSize: Vec2d) {
        drawScaledCustomSizeModalRect(pos.getXi(), pos.getYi(), uvMin.getXf(), uvMin.getYf(), uvMax.getXi(),
                uvMax.getYi(), size.getXi(), size.getYi(), textureSize.getXf(), textureSize.getYf())
    }
}