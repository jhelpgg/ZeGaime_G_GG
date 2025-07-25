package fr.khelp.zegaime.engine3d.particles

import fr.khelp.zegaime.animations.interpolation.Interpolation
import fr.khelp.zegaime.animations.interpolation.InterpolationLinear
import fr.khelp.zegaime.engine3d.render.Color4f
import fr.khelp.zegaime.engine3d.render.GRAY
import fr.khelp.zegaime.engine3d.render.Texture
import fr.khelp.zegaime.engine3d.utils.NodePosition
import fr.khelp.zegaime.utils.Time
import fr.khelp.zegaime.utils.extensions.milliseconds
import fr.khelp.zegaime.utils.math.random
import kotlin.math.max

/**
 * Particle node emits one or several [Particle]
 *
 * [Particle]s can be emits on same time or one after other.
 * To regulate emission :
 * * *startEmissionFrame* : Number of frames to wait before launch the first [Particle] after [ParticleEffect] is launch
 * * *endEmissionFrame* : Number of frames to wait for stop emitting [Particle] after [ParticleEffect] is launch
 * So [Particle] are emits between *startEmissionFrame* and *endEmissionFrame* :
 * * If they are equals, all particles are emits in same time.
 * * Else, particles are emitting regularly during the laps time.
 *
 * Each [Particle] will have the given life time, they disappear after the defined number of frame.
 *
 * [Particle]s can start at same position or in position choose randomly inside a box, with [setPosition].
 * If use the one coordinates version, all [Particle]s start at same position.
 * If use the two coordinates version, [Particle]s start position are chose randomly inside box defined by the two coordinates
 *
 * [Particle]s have a speed direction, this direction can be the same or chose inside a box, like position. With [setSpeedDirection]
 *
 * [Particle]s have an acceleration direction, this direction can be the same or chose inside a box, like position. With [setAccelerationDirection]
 *
 * [Particle]s rotation can change during animation [setAngle], [setSpeedRotation] and [setAccelerationRotation] regulate respectively the start angle, speed angle change and acceleration of angle.
 *
 * [Particle]s scale can change during animation [setScale], [setSpeedScale] and [setAccelerationScale] regulate respectively the start scale, speed cale direction and acceleration scale direction.
 *
 * [texture] defines the texture used by [Particle]s. If `null` no texture and only the diffuse color
 *
 * [Particle]s' diffuse color can change during animation and start/end can choose randomly depends on number arguments involved in [setDiffuseColor].
 * The change color is not necessary linear, an interpolation method can be set with [diffuseInterpolation]
 *
 * [Particle]s' alpha value can change during animation [setAlpha] defines the start and the end value.
 * The way of the change can be choose with [alphaInterpolation]
 */
class ParticleNode(private val numberParticle : Int,
                   private val lifeTime : Time,
                   private val startEmission : Time = 0L.milliseconds,
                   private val stopEmission : Time = startEmission)
{
    /**Texture used by [Particle]s*/
    var texture : Texture? = null

    /**Way alpha evolve during animation*/
    var alphaInterpolation : Interpolation = InterpolationLinear

    /**Way diffuse color evolve during animation*/
    var diffuseInterpolation : Interpolation = InterpolationLinear

    private var position1 = NodePosition()
    private var position2 = NodePosition()
    private var speed1 = NodePosition(scaleX = 0f, scaleY = 0f, scaleZ = 0f)
    private var speed2 = NodePosition(scaleX = 0f, scaleY = 0f, scaleZ = 0f)
    private var acceleration1 = NodePosition(scaleX = 0f, scaleY = 0f, scaleZ = 0f)
    private var acceleration2 = NodePosition(scaleX = 0f, scaleY = 0f, scaleZ = 0f)

    private var alphaStart = 1.0f
    private var alphaEnd = 1.0f

    private var diffuseColorStart1 = GRAY
    private var diffuseColorStart2 = GRAY
    private var diffuseColorEnd1 = GRAY
    private var diffuseColorEnd2 = GRAY

    private var alreadyEmited : Int = 0
    private var lastEmittedTime : Long = 0L
    private val stepTimeEmission =
        (this.stopEmission - this.startEmission) / this.numberParticle

    /**
     * [Particle]s start position box definition.
     *
     * Note :
     * > If use one coordination version or same coordinates for both, the box is reduced to one point, so all [Particle] will starts at same point
     */
    fun setPosition(x1 : Float, y1 : Float, z1 : Float,
                    x2 : Float = x1, y2 : Float = y1, z2 : Float = z1)
    {
        this.position1 = this.position1.copy(x = x1, y = y1, z = z1)
        this.position2 = this.position2.copy(x = x2, y = y2, z = z2)
    }

    /**
     * [Particle]s start angle definition in degree.
     *
     * Note :
     * > If use one angle version or same angle value for both, all [Particle]s will start with the same angle
     */
    fun setAngle(angle1 : Float,
                 angle2 : Float = angle1)
    {
        this.position1 = this.position1.copy(angleZ = angle1)
        this.position2 = this.position2.copy(angleZ = angle2)
    }

    /**
     * [Particle]s start scale box definition.
     *
     * Note :
     * > If use one scale version or same scale values for both, the box is reduced to one point, so all [Particle] will starts with same scale
     */
    fun setScale(scaleX1 : Float, scaleY1 : Float, scaleZ1 : Float,
                 scaleX2 : Float = scaleX1, scaleY2 : Float = scaleY1, scaleZ2 : Float = scaleZ1)
    {
        this.position1 = this.position1.copy(scaleX = scaleX1, scaleY = scaleY1, scaleZ = scaleZ1)
        this.position2 = this.position2.copy(scaleX = scaleX2, scaleY = scaleY2, scaleZ = scaleZ2)
    }

    /**
     * [Particle]s start scale value
     *
     * Note :
     * > If use one scale version or same scale values for both, all [Particle] will starts with same scale
     */
    fun setScale(scale1 : Float, scale2 : Float = scale1)
    {
        this.position1 = this.position1.copy(scaleX = scale1, scaleY = scale1, scaleZ = scale1)
        this.position2 = this.position2.copy(scaleX = scale2, scaleY = scale2, scaleZ = scale2)
    }

    /**
     * [Particle]s start speed direction box definition.
     *
     * Note :
     * > If use one coordination version or same coordinates for both, the box is reduced to one vector, so all [Particle] will starts with same speed
     */
    fun setSpeedDirection(x1 : Float, y1 : Float, z1 : Float,
                          x2 : Float = x1, y2 : Float = y1, z2 : Float = z1)
    {
        this.speed1 = this.speed1.copy(x = x1, y = y1, z = z1)
        this.speed2 = this.speed2.copy(x = x2, y = y2, z = z2)
    }

    /**
     * [Particle]s speed for rotation angle
     *
     * Note :
     * > If use one rotation angle value version or same rotation angle value for both, all [Particle] will have same speed for rotation angle
     */
    fun setSpeedRotation(angle1 : Float,
                         angle2 : Float = angle1)
    {
        this.speed1 = this.speed1.copy(angleZ = angle1)
        this.speed2 = this.speed2.copy(angleZ = angle2)
    }

    /**
     * [Particle]s start speed for scale direction box definition.
     *
     * Note :
     * > If use one coordination version or same coordinates for both, the box is reduced to one vector, so all [Particle] will starts with same speed for scale
     */
    fun setSpeedScale(scaleX1 : Float, scaleY1 : Float, scaleZ1 : Float,
                      scaleX2 : Float = scaleX1, scaleY2 : Float = scaleY1, scaleZ2 : Float = scaleZ1)
    {
        this.speed1 = this.speed1.copy(scaleX = scaleX1, scaleY = scaleY1, scaleZ = scaleZ1)
        this.speed2 = this.speed2.copy(scaleX = scaleX2, scaleY = scaleY2, scaleZ = scaleZ2)
    }

    /**
     * [Particle]s speed for scale in all direction
     *
     * Note :
     * > If use one scale value version or same scale value for both, all [Particle] will have same speed for scale
     */
    fun setSpeedScale(scale1 : Float, scale2 : Float = scale1)
    {
        this.speed1 = this.speed1.copy(scaleX = scale1, scaleY = scale1, scaleZ = scale1)
        this.speed2 = this.speed2.copy(scaleX = scale2, scaleY = scale2, scaleZ = scale2)
    }

    /**
     * [Particle]s acceleration direction box definition.
     *
     * Note :
     * > If use one coordination version or same coordinates for both, the box is reduced to one vector, so all [Particle] will have same acceleration
     */
    fun setAccelerationDirection(x1 : Float, y1 : Float, z1 : Float,
                                 x2 : Float = x1, y2 : Float = y1, z2 : Float = z1)
    {
        this.acceleration1 = this.acceleration1.copy(x = x1, y = y1, z = z1)
        this.acceleration2 = this.acceleration2.copy(x = x2, y = y2, z = z2)
    }

    /**
     * [Particle]s acceleration for rotation angle
     *
     * Note :
     * > If use one rotation angle value version or same rotation angle value for both, all [Particle] will have same acceleration for rotation angle
     */
    fun setAccelerationRotation(angle1 : Float,
                                angle2 : Float = angle1)
    {
        this.acceleration1 = this.acceleration1.copy(angleZ = angle1)
        this.acceleration2 = this.acceleration2.copy(angleZ = angle2)
    }

    /**
     * [Particle]s acceleration for scale direction box definition.
     *
     * Note :
     * > If use one coordination version or same coordinates for both, the box is reduced to one vector, so all [Particle] will have same acceleration  for scale
     */
    fun setAccelerationScale(scaleX1 : Float, scaleY1 : Float, scaleZ1 : Float,
                             scaleX2 : Float = scaleX1,
                             scaleY2 : Float = scaleY1,
                             scaleZ2 : Float = scaleZ1)
    {
        this.acceleration1 = this.acceleration1.copy(scaleX = scaleX1, scaleY = scaleY1, scaleZ = scaleZ1)
        this.acceleration2 = this.acceleration2.copy(scaleX = scaleX2, scaleY = scaleY2, scaleZ = scaleZ2)
    }

    /**
     * [Particle]s acceleration for scale in all direction
     *
     * Note :
     * > If use one scale value version or same scale value for both, all [Particle] will have same acceleration  for scale
     */
    fun setAccelerationScale(scale1 : Float, scale2 : Float = scale1)
    {
        this.acceleration1 = this.acceleration1.copy(scaleX = scale1, scaleY = scale1, scaleZ = scale1)
        this.acceleration2 = this.acceleration2.copy(scaleX = scale2, scaleY = scale2, scaleZ = scale2)
    }

    /**
     * Define diffuse color for particle
     *
     * Diffuse color at start of animation is choose between *diffuseColorStart1* and *diffuseColorStart2*.
     * So if they are the same, all [Particle] will start with same color
     *
     * Diffuse color at end of animation is choose between *diffuseColorEnd1* and *diffuseColorEnd2*.
     * So if they are the same, all [Particle] will end with same color
     *
     * To regulate how change progress see [diffuseInterpolation]
     */
    fun setDiffuseColor(diffuseColorStart1 : Color4f,
                        diffuseColorEnd1 : Color4f = diffuseColorStart1,
                        diffuseColorStart2 : Color4f = diffuseColorStart1,
                        diffuseColorEnd2 : Color4f = diffuseColorEnd1)
    {
        this.diffuseColorStart1 = diffuseColorStart1
        this.diffuseColorEnd1 = diffuseColorEnd1
        this.diffuseColorStart2 = diffuseColorStart2
        this.diffuseColorEnd2 = diffuseColorEnd2
    }

    /**
     * Define alpha value.
     *
     * To regulate how change progress see [alphaInterpolation]
     *
     * @param alphaStart Value of alpha at animation start
     * @param alphaEnd  Value of alpha at animation end
     */
    fun setAlpha(alphaStart : Float, alphaEnd : Float = alphaStart)
    {
        this.alphaStart = alphaStart
        this.alphaEnd = alphaEnd
    }

    internal fun resetEmission()
    {
        this.alreadyEmited = 0
        this.lastEmittedTime = 0L
    }

    internal fun emitParticle(time : Long, collector : (Particle) -> Unit) : Boolean
    {
        if (this.lastEmittedTime > this.stopEmission.milliseconds || this.alreadyEmited >= this.numberParticle)
        {
            this.lastEmittedTime = time
            return false
        }

        if (time < this.startEmission.milliseconds)
        {
            this.lastEmittedTime = time
            return true
        }

        var emissionTime = max(this.startEmission.milliseconds, this.lastEmittedTime)

        if (emissionTime >= this.stopEmission.milliseconds)
        {
            var particle : Particle

            for (number in this.alreadyEmited until this.numberParticle)
            {
                particle = Particle(this.stopEmission,
                                    this.randomPosition(),
                                    this.randomSpeed(),
                                    this.randomAcceleration(),
                                    this.lifeTime)
                particle.material.textureDiffuse = this.texture
                particle.alphaStart = this.alphaStart
                particle.alphaEnd = this.alphaEnd
                particle.alphaInterpolation = this.alphaInterpolation
                particle.diffuseStart = this.randomDiffuseColorStart()
                particle.diffuseEnd = this.randomDiffuseColorEnd()
                particle.diffuseInterpolation = this.diffuseInterpolation
                collector(particle)
            }

            this.alreadyEmited = this.numberParticle
            this.lastEmittedTime = emissionTime
        }
        else
        {
            while (emissionTime <= time && this.alreadyEmited < this.numberParticle)
            {
                collector(Particle(emissionTime.milliseconds,
                                   this.randomPosition(),
                                   this.randomSpeed(),
                                   this.randomAcceleration(),
                                   this.lifeTime))
                this.alreadyEmited++
                emissionTime += this.stepTimeEmission.milliseconds
                this.lastEmittedTime = emissionTime
            }
        }

        return true
    }

    private fun randomPosition() : NodePosition = this.random(this.position1, this.position2)

    private fun randomSpeed() : NodePosition = this.random(this.speed1, this.speed2)

    private fun randomAcceleration() : NodePosition =
        this.random(this.acceleration1, this.acceleration2)

    private fun random(position1 : NodePosition, position2 : NodePosition) : NodePosition =
        NodePosition(random(position1.x, position2.x),
                     random(position1.y, position2.y),
                     random(position1.z, position2.z),
                     random(position1.angleX, position2.angleX),
                     random(position1.angleY, position2.angleY),
                     random(position1.angleZ, position2.angleZ),
                     random(position1.scaleX, position2.scaleX),
                     random(position1.scaleY, position2.scaleY),
                     random(position1.scaleZ, position2.scaleZ))

    private fun randomDiffuseColorStart() : Color4f =
        this.randomColor(this.diffuseColorStart1, this.diffuseColorStart2)

    private fun randomDiffuseColorEnd() : Color4f =
        this.randomColor(this.diffuseColorEnd1, this.diffuseColorEnd2)

    private fun randomColor(color1 : Color4f, color2 : Color4f) : Color4f =
        Color4f(random(color1.red, color2.red),
                random(color1.green, color2.green),
                random(color1.blue, color2.blue),
                random(color1.alpha, color2.alpha))
}