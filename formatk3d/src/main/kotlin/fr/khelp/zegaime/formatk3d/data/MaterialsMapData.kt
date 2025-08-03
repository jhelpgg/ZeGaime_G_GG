package fr.khelp.zegaime.formatk3d.data

import kotlinx.serialization.Serializable

@Serializable
data class MaterialsMapData(val materials : Map<String, MaterialData>)
