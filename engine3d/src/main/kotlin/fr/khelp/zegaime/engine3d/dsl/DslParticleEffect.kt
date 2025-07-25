package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.particles.ParticleEffect
import fr.khelp.zegaime.engine3d.particles.ParticleNode
import fr.khelp.zegaime.utils.Time
import fr.khelp.zegaime.utils.extensions.milliseconds
import fr.khelp.zegaime.utils.math.max
import kotlin.math.max

@ParticleDSL
fun particleEffect(create : ParticleEffectCreator.() -> Unit) : ParticleEffect
{
    val particleEffectCreator = ParticleEffectCreator()
    particleEffectCreator.create()
    return particleEffectCreator.particleEffect
}

@ParticleDSL
class ParticleEffectCreator
{
    val particleEffect = ParticleEffect()

    @ParticleDSL
    fun nodeInstant(numberParticle : Int,
                    lifeTime : Time,
                    fill : ParticleNode.() -> Unit)
    {
        this.node(numberParticle, lifeTime, 0.milliseconds, 0.milliseconds, fill)
    }

    @ParticleDSL
    fun nodeInstant(numberParticle : Int,
                    lifeTime : Time,
                    instantEmission : Time,
                    fill : ParticleNode.() -> Unit)
    {
        this.node(numberParticle, lifeTime, instantEmission, instantEmission, fill)
    }

    @ParticleDSL
    fun node(numberParticle : Int,
             lifeTime : Time,
             stopEmission : Time,
             fill : ParticleNode.() -> Unit)
    {
        this.node(numberParticle, lifeTime, 0.milliseconds, stopEmission, fill)
    }

    @ParticleDSL
    fun node(numberParticle : Int,
             lifeTime : Time,
             startEmission : Time,
             stopEmission : Time,
             fill : ParticleNode.() -> Unit)
    {
        val numberParticleLocal = max(1, numberParticle)
        val lifeTimeLocal = max(1.milliseconds, lifeTime)
        val stopEmissionLocal = max(stopEmission, startEmission)
        val particleNode = ParticleNode(numberParticleLocal, lifeTimeLocal, startEmission, stopEmissionLocal)
        particleNode.fill()
        this.particleEffect.addParticleNode(particleNode)
    }
}
