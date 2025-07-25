package fr.khelp.zegaime.engine3d.particles

import org.lwjgl.opengl.GL11

class ParticleManager internal constructor()
{
    private val particleEffects = ArrayList<ParticleEffect>()

    /**
     * Launch a particle effect
     */
    fun play(particleEffect : ParticleEffect)
    {
        particleEffect.start()

        synchronized(this.particleEffects)
        {
            this.particleEffects.add(particleEffect)
        }
    }

    /**
     * Stop a particle effect
     */
    fun stop(particleEffect : ParticleEffect)
    {
        synchronized(this.particleEffects)
        {
            if (this.particleEffects.remove(particleEffect))
            {
                particleEffect.stop()
            }
        }
    }

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