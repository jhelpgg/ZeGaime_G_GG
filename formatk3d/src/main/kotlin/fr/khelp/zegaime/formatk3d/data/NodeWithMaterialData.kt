package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.render.TwoSidedRule
import kotlinx.serialization.Serializable

/**
 * Node with material data for save
 * @param material Material name
 * @param materialForSelection Material name for selection
 * @param twoSidedRule Two sided rule
 */
@Serializable
data class NodeWithMaterialData(val material:String,
                                val materialForSelection:String,
                                val twoSidedRule:TwoSidedRule)
