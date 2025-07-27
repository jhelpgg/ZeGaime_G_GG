package fr.khelp.zegaime.engine3d.particles

/**
 * Particle effect is composed of one or several [ParticleNode] that needs play on same particle effect animation.
 *
 * Add [ParticleNode] with [addParticleNode] to create one effect.
 *
 * **Creation example**
 * ```kotlin
 * val particleEffect = ParticleEffect()
 * particleEffect.addParticleNode(myParticleNode)
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * val particleManager = ParticleManager()
 * particleManager.play(particleEffect)
 * ```
 */
class ParticleEffect
{
    private val particleNodes = ArrayList<ParticleNode>()
    private val aliveParticles = ArrayList<Particle>()
    private var statTime : Long = 0L

    /**
     * Add a particle node generation in the particle effect.
     *
     * @param particleNode The particle node to add.
     */
    fun addParticleNode(particleNode : ParticleNode)
    {
        synchronized(this.particleNodes)
        {
            this.particleNodes.add(particleNode)
        }
    }

    /**
     * Starts the particle effect.
     *
     * For internal use only.
     */
    internal fun start()
    {
        synchronized(this.aliveParticles)
        {
            this.aliveParticles.clear()
        }

        synchronized(this.particleNodes)
        {
            for (particleNode in this.particleNodes)
            {
                particleNode.resetEmission()
            }
        }

        this.statTime = System.currentTimeMillis()
        this.update()
    }

    /**
     * Stops the particle effect.
     *
     * For internal use only.
     */
    internal fun stop()
    {
        synchronized(this.aliveParticles)
        {
            this.aliveParticles.clear()
        }
    }

    /**
     * Updates the particle effect.
     *
     * For internal use only.
     *
     * @return `true` if the effect is still alive, `false` otherwise.
     */
    internal fun update() : Boolean
    {
        var alive = false
        val time = System.currentTimeMillis() - this.statTime
        val collector : (Particle) -> Unit = { particle -> this.aliveParticles.add(particle) }

        synchronized(this.particleNodes)
        {
            for (particleNode in this.particleNodes)
            {
                if (particleNode.emitParticle(time, collector))
                {
                    alive = true
                }
            }

            var particle : Particle

            for (index in this.aliveParticles.size - 1 downTo 0)
            {
                particle = this.aliveParticles[index]

                if (particle.update(time))
                {
                    alive = true
                }
                else
                {
                    this.aliveParticles.removeAt(index)
                }
            }
        }

        return alive
    }

    /**
     * Renders the particle effect.
     *
     * For internal use only.
     */
    internal fun render()
    {
        synchronized(this.aliveParticles)
        {
            for (particle in this.aliveParticles)
            {
                particle.draw()
            }
        }
    }
}
