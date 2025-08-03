package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.engine3d.dsl.FaceUVCreator
import fr.khelp.zegaime.engine3d.scene.prebuilt.FaceUV
import fr.khelp.zegaime.formatk3d.data.FaceUVData
import fr.khelp.zegaime.formatk3d.data.NodeData
import fr.khelp.zegaime.formatk3d.data.NodeType
import fr.khelp.zegaime.formatk3d.data.PlaneData

class PlaneCreator(name : String) : NodeWithMaterialCreator(name)
{
    private var faceUV = FaceUV()

    fun faceUV(create : FaceUVCreator.() -> Unit)
    {
        val faceUVCreator = FaceUVCreator()
        faceUVCreator.create()
        this.faceUV = faceUVCreator()
    }

    override fun invoke() : NodeData =
        NodeData(name = this.name, nodeType = NodeType.PLANE,
                 positionData = this.position, limitData = this.limits,
                 children = this.nodeChildrenCreator.nodes,
                 nodeWithMaterialData = this.nodeWithMaterialData,
                 planeData = PlaneData(FaceUVData(this.faceUV)))
}