package fr.khelp.zegaime.game.characters

import fr.khelp.zegaime.engine3d.dsl.path
import fr.khelp.zegaime.engine3d.geometry.path.Path
import fr.khelp.zegaime.engine3d.render.BLACK
import fr.khelp.zegaime.engine3d.render.Color4f
import fr.khelp.zegaime.engine3d.render.Material
import fr.khelp.zegaime.engine3d.render.Texture
import fr.khelp.zegaime.engine3d.resources.Eye
import fr.khelp.zegaime.engine3d.resources.Mouth
import fr.khelp.zegaime.engine3d.scene.Node
import fr.khelp.zegaime.engine3d.scene.prebuilt.Plane
import fr.khelp.zegaime.engine3d.scene.prebuilt.Revolution
import fr.khelp.zegaime.engine3d.scene.prebuilt.Sphere
import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.images.color.Color
import fr.khelp.zegaime.images.color.TRANSPARENT
import fr.khelp.zegaime.images.color.argb
import fr.khelp.zegaime.images.color.base.COLOR_PINK_0100
import fr.khelp.zegaime.images.drawImage
import fr.khelp.zegaime.images.drawImageCenter
import fr.khelp.zegaime.images.setColor
import java.awt.BasicStroke
import java.awt.geom.Path2D

/**
 * Base for head node
 *
 * @param id Node ID
 */
class HeadBase(id : String) : Node(id)
{
    companion object
    {
        /** Ear main path */
        private val earPath : Path2D.Double by lazy {
            val path = Path2D.Double()
            path.moveTo(0.0, 256.0 - 64.0)
            path.curveTo(512.0, 0.0,
                         512.0, 512.0,
                         0.0, 256.0 + 64.0)
            path.closePath()
            path
        }

        /** Inside ear path */
        private val insideEarPath : Path2D.Double by lazy {
            val path = Path2D.Double()
            path.moveTo(0.0, 256.0 - 64.0)
            path.curveTo(512.0 - 128.0, 64.0,
                         512.0 - 128.0, 512.0 - 64.0,
                         0.0, 256.0 + 64.0)
            path.closePath()
            path
        }

        /** Nose revolution path */
        private val nosePath : Path =
            path {
                move(0f, 0.5f)
                quadratic(0.5f, -0.25f, 0f, -0.5f)
            }
    }

    /** Left eye */
    var leftEye : Eye = Eye.RED_2
        set(value)
        {
            field = value
            this.refresh()
        }

    /** Right eye */
    var rightEye : Eye = Eye.GREEN_1
        set(value)
        {
            field = value
            this.refresh()
        }

    /** Mouth */
    var mouth : Mouth = Mouth.SMILE_BIG_3
        set(value)
        {
            field = value
            this.refresh()
        }

    /** Skin colo */
    var skinColor : Color = COLOR_PINK_0100.argb
        set(value)
        {
            field = value
            this.refresh()
        }

    /** Image used in face */
    private val imageFace = GameImage(512, 512)

    /** Image used in ears */
    private val imageEar = GameImage(512, 512)

    /** Texture used for face */
    private val textureFace = Texture(this.imageFace)

    /** Texture used in ears */
    private val textureEar = Texture(this.imageEar)

    /** Skin material */
    private val skinMaterial = Material()

    init
    {
        val base = Sphere("${id}.base", 16, 16)
        base.scaleY = 1.234f
        base.material.textureDiffuse = this.textureFace
        base.materialForSelection = base.material
        this.addChild(base)

        this.skinMaterial.colorDiffuse = BLACK
        this.skinMaterial.colorEmissive = Color4f(this.skinColor)

        val nose = Revolution("${id}.nose", path = nosePath)
        nose.scaleX = 0.456f
        nose.scaleY = 0.345f
        nose.scaleZ = 0.345f
        nose.x = -0.987f
        nose.material = this.skinMaterial
        nose.materialForSelection = nose.material
        this.addChild(nose)

        val rightEar = Plane("${id}.rightEar")
        rightEar.scaleX = 0.345f
        rightEar.scaleY = 1f
        rightEar.scaleZ = 0.345f
        rightEar.z = -0.87f
        rightEar.x = -0.5f
        rightEar.angleY = 45f
        rightEar.material.textureDiffuse = this.textureEar
        rightEar.materialForSelection = rightEar.material
        this.addChild(rightEar)

        val leftEar = Plane("${id}.leftEar")
        leftEar.scaleX = 0.345f
        leftEar.scaleY = 1f
        leftEar.scaleZ = 0.345f
        leftEar.z = 0.87f
        leftEar.x = -0.5f
        leftEar.angleY = -45f
        leftEar.material = rightEar.material
        leftEar.materialForSelection = leftEar.material
        this.addChild(leftEar)

        this.refresh()
    }

    /**
     * Updates head textures and materials
     */
    private fun refresh()
    {
        this.skinMaterial.colorEmissive = Color4f(this.skinColor)
        this.imageFace.clear(this.skinColor)

        this.imageFace.draw { graphics2D ->
            graphics2D.drawImage(96 - 32, 224 - 32, 64, 64, this.leftEye.image)
            graphics2D.drawImage(160 - 32, 224 - 32, 64, 64, this.rightEye.image)
            graphics2D.drawImageCenter(128, 375, this.mouth.image)
        }

        this.imageEar.clear(TRANSPARENT)

        this.imageEar.draw { graphics2D ->
            graphics2D.setColor(this.skinColor)
            graphics2D.fill(HeadBase.earPath)

            graphics2D.stroke = BasicStroke(4f)
            graphics2D.color = java.awt.Color.BLACK
            graphics2D.draw(HeadBase.earPath)

            graphics2D.stroke = BasicStroke(1f)
            graphics2D.color = java.awt.Color.GRAY
            graphics2D.draw(HeadBase.insideEarPath)
        }
    }
}