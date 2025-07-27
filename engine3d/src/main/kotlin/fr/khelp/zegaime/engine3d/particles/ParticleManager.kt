package fr.khelp.zegaime.engine3d.particles

import org.lwjgl.opengl.GL11

/**
 * Manages the particle effects.
 *
 * This class is responsible for playing, stopping and rendering the particle effects.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * It is created by the `Window3D` class.
 *
 * **Standard usage:**
 * ```kotlin
 * val particleManager = window3D.particleManager
 * particleManager.play(myParticleEffect)
 * ```
 *
 * @constructor Creates a new particle manager. For internal use only.
 */
class ParticleManager internal constructor()
{
    private val particleEffects = ArrayList<ParticleEffect>()

    /**
     * Launch a particle effect.
     *
     * @param particleEffect The particle effect to play.
     */
    fun play(particleEffect: ParticleEffect)
    {
        particleEffect.start()

        synchronized(this.particleEffects)
        {
            this.particleEffects.add(particleEffect)
        }
    }

    /**
     * Stop a particle effect.
     *
     * @param particleEffect The particle effect to stop.
     */
    fun stop(particleEffect: ParticleEffect)
    {
        synchronized(this.particleEffects)
        {
            if (this.particleEffects.remove(particleEffect))
            {
                particleEffect.stop()
            }
        }
    }

    /**
     * Draws the particle effects.
     *
     * For internal use only.
     */
    internal fun draw()
    {
        GL11.glDisable(GL11.GL_DEPTH_TEST)

        synchronized(this.particleEffects) {
            for (index in this.particleEffects.size - 1 downTo 0)
            {
                val particleEffect = this.particleEffects[index]
                val alive = particleEffect.update()
                particleEffect.render()

                if (!alive)
                {
                    this.particleEffects.removeAt(index)
                }
            }
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST)
    }
}
