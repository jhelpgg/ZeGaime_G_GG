package fr.khelp.zegaime.engine3d.particles

import fr.khelp.zegaime.animations.interpolation.Interpolation
import fr.khelp.zegaime.animations.interpolation.InterpolationLinear
import fr.khelp.zegaime.engine3d.render.Color4f
import fr.khelp.zegaime.engine3d.render.GRAY
import fr.khelp.zegaime.engine3d.render.Material
import fr.khelp.zegaime.engine3d.scene.ObjectClone
import fr.khelp.zegaime.engine3d.scene.prebuilt.Plane
import fr.khelp.zegaime.engine3d.utils.NodePosition
import fr.khelp.zegaime.engine3d.utils.times
import fr.khelp.zegaime.utils.Time
import java.util.concurrent.atomic.AtomicInteger
import org.lwjgl.opengl.GL11

/**
 * A particle with its information and current state.
 *
 * This class is for internal use of the particle system.
 *
 * @property startTime The time when the particle was created.
 * @property position The initial position of the particle.
 * @property speed The initial speed of the particle.
 * @property acceleration The acceleration of the particle.
 * @property lifeDuration The life duration of the particle.
 * @constructor Creates a new particle.
 */
internal class Particle(private val startTime : Time,
                        private val position : NodePosition,
                        private val speed : NodePosition,
                        private val acceleration : NodePosition,
                        private val lifeDuration : Time)
{
    companion object
    {
        private val basePlane = Plane("Particle base plane")
        private val nextId = AtomicInteger(0)
    }

    private val plane = ObjectClone("Particle_${nextId.getAndIncrement()}", Particle.basePlane)

    /**
     * The material of the particle.
     */
    var material : Material
        get() = this.plane.material
        set(value)
        {
            this.plane.material = value
        }

    private var currentPosition = NodePosition()

    /**
     * The start diffuse color of the particle.
     */
    var diffuseStart = GRAY

    /**
     * The end diffuse color of the particle.
     */
    var diffuseEnd = GRAY

    /**
     * The interpolation for the diffuse color.
     */
    var diffuseInterpolation : Interpolation = InterpolationLinear

    /**
     * The start alpha of the particle.
     */
    var alphaStart = 1.0f

    /**
     * The end alpha of the particle.
     */
    var alphaEnd = 1.0f

    /**
     * The interpolation for the alpha.
     */
    var alphaInterpolation : Interpolation = InterpolationLinear

    /**
     * Draws the particle.
     */
    fun draw()
    {
        GL11.glPushMatrix()
        this.currentPosition.locate()
        this.plane.renderSpecific()
        GL11.glPopMatrix()
    }

    /**
     * Updates the particle.
     *
     * @param timeEffectMilliseconds The current time of the effect in milliseconds.
     * @return `true` if the particle is still alive, `false` otherwise.
     */
    fun update(timeEffectMilliseconds : Long) : Boolean
    {
        val lifeDurationMilliseconds = this.lifeDuration.milliseconds
        var stillAlive = true
        var passTime = timeEffectMilliseconds - this.startTime.milliseconds

        if (passTime > lifeDurationMilliseconds)
        {
            stillAlive = false
            passTime = lifeDurationMilliseconds
        }

        val percent = passTime.toDouble() / lifeDurationMilliseconds
        var factor = this.diffuseInterpolation(percent).toFloat()
        var rotcaf = 1.0f - factor
        val red = this.diffuseStart.red * rotcaf + this.diffuseEnd.red * factor
        val green = this.diffuseStart.green * rotcaf + this.diffuseEnd.green * factor
        val blue = this.diffuseStart.blue * rotcaf + this.diffuseEnd.blue * factor
        val alpha = this.diffuseStart.alpha * rotcaf + this.diffuseEnd.alpha * factor
        this.material.colorDiffuse = Color4f(red, green, blue, alpha)

        factor = this.alphaInterpolation(percent).toFloat()
        rotcaf = 1.0f - factor
        this.material.transparency = this.alphaStart * rotcaf + this.alphaEnd * factor

        val currentSpeed = this.speed + passTime * this.acceleration
        this.currentPosition = this.position + passTime * currentSpeed

        return stillAlive
    }
}
