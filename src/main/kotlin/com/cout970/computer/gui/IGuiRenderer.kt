package com.cout970.computer.gui

import com.cout970.computer.util.vector.Vec2d
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.ResourceLocation

/**
 * Created by cout970 on 20/05/2016.
 */
interface IGuiRenderer {

    //bind texture
    fun bindTexture(res: ResourceLocation)

    //string render
    fun drawHoveringText(textLines: List<String>, pos: Vec2d)

    fun drawCenteredString(text: String, pos: Vec2d, color: Int)

    fun drawString(text: String, pos: Vec2d, color: Int)

    //shape render
    fun drawHorizontalLine(startX: Int, endX: Int, y: Int, color: Int)

    fun drawVerticalLine(x: Int, startY: Int, endY: Int, color: Int)

    fun drawRect(start: Vec2d, end: Vec2d, color: Int)

    fun drawGradientRect(start: Vec2d, end: Vec2d, startColor: Int, endColor: Int)

    //texture render
    fun drawTexturedModalRect(pos: Vec2d, size: Vec2d, texture : Vec2d)

    fun drawTexturedModalRect(pos: Vec2d, size: Vec2d, textureSprite: TextureAtlasSprite)

    fun drawModalRectWithCustomSizedTexture(pos: Vec2d, size: Vec2d, uv: Vec2d, textureSize : Vec2d)

    fun drawScaledCustomSizeModalRect(pos: Vec2d, size: Vec2d, uvMin: Vec2d, uvMax: Vec2d, textureSize : Vec2d)
}