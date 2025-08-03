package fr.khelp.zegaime.formatk3d.data

import kotlinx.serialization.Serializable

/**
 * Information that describes a scene
 *
 * @property materialsMap Materials declared in the scene
 * @property backgroundColor Scene main background color
 * @property backgroundTexture Scene background texture. If `null`, no background texture
 * @property position Scene position relative to scene viewer
 * @property limits Navigation limits
 * @property nodes Nodes directly children of the scene
 */
@Serializable
data class SceneData(val materialsMap : MaterialsMapData,
                     val backgroundColor : ColorData,
                     val backgroundTexture : TextureData?,
                     val position : NodePositionData,
                     val limits : NodeLimitData,
                     val nodes : List<NodeData>)
