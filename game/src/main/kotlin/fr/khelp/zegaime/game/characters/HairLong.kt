package fr.khelp.zegaime.game.characters

import fr.khelp.zegaime.engine3d.render.Texture
import fr.khelp.zegaime.engine3d.render.TwoSidedRule
import fr.khelp.zegaime.engine3d.scene.Node
import fr.khelp.zegaime.engine3d.scene.prebuilt.Sphere
import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.images.color.TRANSPARENT
import fr.khelp.zegaime.images.color.base.BaseColor
import fr.khelp.zegaime.images.color.base.Brown
import fr.khelp.zegaime.images.setColor
import fr.khelp.zegaime.utils.math.random
import java.awt.BasicStroke
import java.awt.Point
import java.awt.geom.Path2D

/**
 * Long hair node
 *
 * @param id Node ID
 */
class HairLong(id : String) : Node(id)
{
    companion object
    {
        /** Hair main path */
        private val hairPath : Path2D.Double by lazy {
            val path = Path2D.Double()
            path.moveTo(0.0, 0.0)
            path.lineTo(512.0, 0.0)

            path.lineTo(512.0, 500.0)
            path.curveTo(288.0, 500.0,
                         288.0, 416.0,
                         224.0, 352.0)

            path.lineTo(224.0, 244.0)
            path.curveTo(160.0, 170.0,
                         96.0, 170.0,
                         48.0, 244.0)

            path.lineTo(48.0, 352.0)
            path.curveTo(16.0, 416.0,
                         16.0, 500.0,
                         0.0, 500.0)

            path.closePath()
            path
        }

        /** Light reflect */
        private val polygones : Array<Pair<IntArray, IntArray>> =
            arrayOf(
                Pair(intArrayOf(100, 85, 125),
                     intArrayOf(210, 80, 80)),

                Pair(intArrayOf(150, 195, 210),
                     intArrayOf(208, 95, 100)),

                Pair(intArrayOf(16, 10, 20),
                     intArrayOf(400, 80, 100)),

                Pair(intArrayOf(210, 200, 222),
                     intArrayOf(400, 80, 100)),

                Pair(intArrayOf(300, 295, 305),
                     intArrayOf(404, 100, 123)),

                Pair(intArrayOf(400, 395, 404),
                     intArrayOf(404, 88, 123))
                   )

        /** Hair volume */
        private val lines : Array<Pair<Point, Point>> =
            Array<Pair<Point, Point>>(32) { index ->
                when (index)
                {
                    0    -> Point(100, 210) to Point(105, 80)

                    1    -> Point(150, 205) to Point(200, 102)

                    else ->
                    {
                        val x1 = random(10, 500)
                        val x2 = x1 + random(-8, 8)
                        val y1 = random(0, 32)
                        val y2 = if (x2 in 60..210) random(170, 200) else random(400, 500)
                        Point(x1, y1) to Point(x2, y2)
                    }
                }
            }
    }

    /** Hair color */
    var color : BaseColor<*> = Brown.BROWN_0500
        set(value)
        {
            if (field != value)
            {
                field = value
                this.refresh()
            }
        }

    /** Hair texture image */
    private val image = GameImage(512, 512)

    /** Hair texture */
    private val texture = Texture(this.image)

    init
    {
        val base = Sphere("${id}.base", 16, 16)
        base.scaleY = 1.5f
        base.material.textureDiffuse = this.texture
        base.twoSidedRule = TwoSidedRule.FORCE_TWO_SIDE
        this.addChild(base)

        this.refresh()
    }

    /**
     * Updates hair texture
     */
    private fun refresh()
    {
        val lighter = this.color.lighter.color
        val color = this.color.color
        val darker = this.color.darker.color

        this.image.clear(TRANSPARENT)

        this.image.draw { graphics2D ->
            graphics2D.setColor(color)
            graphics2D.fill(HairLong.hairPath)

            graphics2D.setColor(lighter)

            for ((xs, ys) in HairLong.polygones)
            {
                graphics2D.fillPolygon(xs, ys, xs.size)
            }

            graphics2D.setColor(darker)
            graphics2D.stroke = BasicStroke(2f)

            for ((start, end) in HairLong.lines)
            {
                graphics2D.drawLine(start.x, start.y, end.x, end.y)
            }
        }
    }
}