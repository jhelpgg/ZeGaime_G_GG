package fr.khelp.zegaime.formatk3d.data

import kotlinx.serialization.Serializable

/**
 * Materials map for save
 * @param materials Map of materials
 */
@Serializable
data class MaterialsMapData(val materials : Map<String, MaterialData>)
