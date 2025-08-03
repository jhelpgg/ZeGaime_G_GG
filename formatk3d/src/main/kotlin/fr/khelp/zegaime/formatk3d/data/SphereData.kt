package fr.khelp.zegaime.formatk3d.data

import kotlinx.serialization.Serializable

/**
 * Sphere data
 *
 * @property slice Number of slices
 * @property slack Number of slacks
 * @property multiplierU Number time texture is repeated along `U`. If under 1, only an image part is used
 * @property multiplierV Number time texture is repeated along `V`. If under 1, only an image part is used
 */
@Serializable
data class SphereData(val slice : Int, val slack : Int,
                      val multiplierU : Float, val multiplierV : Float)
