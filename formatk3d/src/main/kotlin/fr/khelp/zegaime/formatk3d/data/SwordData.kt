package fr.khelp.zegaime.formatk3d.data

import kotlinx.serialization.Serializable

/**
 * Data to store sword information
 *
 * @property size Sword size
 * @property baseMaterial Material name of base sword part. If empty, the default material is used
 * @property bladeMaterial Material name of blade sword part. If empty, the default material is used
 */
@Serializable
data class SwordData(val size : Float,
                     val baseMaterial : String,
                     val bladeMaterial : String)
