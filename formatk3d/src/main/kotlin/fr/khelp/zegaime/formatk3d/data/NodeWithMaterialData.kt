package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.render.TwoSidedRule
import kotlinx.serialization.Serializable

@Serializable
data class NodeWithMaterialData(val material:String,
                                val materialForSelection:String,
                                val twoSidedRule:TwoSidedRule)
