package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.particles.ParticleEffect
import fr.khelp.zegaime.engine3d.particles.ParticleNode
import fr.khelp.zegaime.utils.Time
import fr.khelp.zegaime.utils.extensions.milliseconds
import fr.khelp.zegaime.utils.math.max
import kotlin.math.max

/**
 * Creates a particle effect using the DSL.
 *
 * **Usage example:**
 * ```kotlin
 * val particleEffect = particleEffect {
 *     nodeInstant(100, 1.seconds) {
 *         // ...
 *     }
 * }
 * ```
 *
 * @param create The lambda function to create the particle effect.
 * @return The created particle effect.
 */
@ParticleDSL
fun particleEffect(create: ParticleEffectCreator.() -> Unit): ParticleEffect
{
    val particleEffectCreator = ParticleEffectCreator()
    particleEffectCreator.create()
    return particleEffectCreator.particleEffect
}

/**
 * DSL creator for particle effects.
 *
 * @property particleEffect The created particle effect.
 * @constructor Creates a new particle effect creator.
 */
@ParticleDSL
class ParticleEffectCreator
{
    /**
     * The created particle effect.
     */
    internal val particleEffect = ParticleEffect()

    /**
     * Adds a particle node with an instantaneous emission.
     *
     * @param numberParticle The number of particles to emit.
     * @param lifeTime The lifetime of the particles.
     * @param fill The lambda function to configure the particle node.
     */
    @ParticleDSL
    fun nodeInstant(numberParticle: Int,
                    lifeTime: Time,
                    fill: ParticleNode.() -> Unit)
    {
        this.node(numberParticle, lifeTime, 0.milliseconds, 0.milliseconds, fill)
    }

    /**
     * Adds a particle node with an instantaneous emission at a specific time.
     *
     * @param numberParticle The number of particles to emit.
     * @param lifeTime The lifetime of the particles.
     * @param instantEmission The time of the emission.
     * @param fill The lambda function to configure the particle node.
     */
    @ParticleDSL
    fun nodeInstant(numberParticle: Int,
                    lifeTime: Time,
                    instantEmission: Time,
                    fill: ParticleNode.() -> Unit)
    {
        this.node(numberParticle, lifeTime, instantEmission, instantEmission, fill)
    }

    /**
     * Adds a particle node with a continuous emission.
     *
     * @param numberParticle The number of particles to emit.
     * @param lifeTime The lifetime of the particles.
     * @param stopEmission The time when the emission stops.
     * @param fill The lambda function to configure the particle node.
     */
    @ParticleDSL
    fun node(numberParticle: Int,
             lifeTime: Time,
             stopEmission: Time,
             fill: ParticleNode.() -> Unit)
    {
        this.node(numberParticle, lifeTime, 0.milliseconds, stopEmission, fill)
    }

    /**
     * Adds a particle node with a continuous emission between two times.
     *
     * @param numberParticle The number of particles to emit.
     * @param lifeTime The lifetime of the particles.
     * @param startEmission The time when the emission starts.
     * @param stopEmission The time when the emission stops.
     * @param fill The lambda function to configure the particle node.
     */
    @ParticleDSL
    fun node(numberParticle: Int,
             lifeTime: Time,
             startEmission: Time,
             stopEmission: Time,
             fill: ParticleNode.() -> Unit)
    {
        val numberParticleLocal = max(1, numberParticle)
        val lifeTimeLocal = max(1.milliseconds, lifeTime)
        val stopEmissionLocal = max(stopEmission, startEmission)
        val particleNode = ParticleNode(numberParticleLocal, lifeTimeLocal, startEmission, stopEmissionLocal)
        particleNode.fill()
        this.particleEffect.addParticleNode(particleNode)
    }
}