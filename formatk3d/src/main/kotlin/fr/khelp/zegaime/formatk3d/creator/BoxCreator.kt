package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.engine3d.dsl.BoxUVCreator
import fr.khelp.zegaime.engine3d.dsl.CrossUVCreator
import fr.khelp.zegaime.engine3d.scene.prebuilt.BoxUV
import fr.khelp.zegaime.formatk3d.data.BoxData
import fr.khelp.zegaime.formatk3d.data.BoxUVData
import fr.khelp.zegaime.formatk3d.data.NodeData
import fr.khelp.zegaime.formatk3d.data.NodeType

/**
 * Creates box information
 *
 * @param name Box name
 */
class BoxCreator(name : String) : NodeWithMaterialCreator(name)
{
    /** UV on the box faces */
    private var boxUV = BoxUV()

    /**
     * Fills the faces' box UV
     *
     * @param create Creates the box faces' UV
     */
    fun boxUV(create : BoxUVCreator.() -> Unit)
    {
        val boxUVCreator = BoxUVCreator()
        boxUVCreator.create()
        this.boxUV = boxUVCreator()
    }

    /**
     * Fills the faces' box UV for cross-texture
     *
     * @param create Creates faces' box UV for cross-texture
     */
    fun crossUV(create : CrossUVCreator.() -> Unit)
    {
        val crossUVCreator = CrossUVCreator()
        crossUVCreator.create()
        this.boxUV = crossUVCreator()
    }

    /**
     * Converts current box information in save data
     *
     * @return Box data for save
     */
    override fun invoke() : NodeData =
        NodeData(name = this.name, nodeType = NodeType.BOX,
                 positionData = this.position, limitData = this.limits,
                 children = this.nodeChildrenCreator.nodes,
                 nodeWithMaterialData = this.nodeWithMaterialData,
                 boxData = BoxData(BoxUVData(this.boxUV)))
}