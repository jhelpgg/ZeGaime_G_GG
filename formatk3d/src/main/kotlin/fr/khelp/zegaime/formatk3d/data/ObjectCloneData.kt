package fr.khelp.zegaime.formatk3d.data

import kotlinx.serialization.Serializable

/**
 * Object clone data for save
 * @param objectReference Object name to clone
 */
@Serializable
data class ObjectCloneData(val objectReference : String)
