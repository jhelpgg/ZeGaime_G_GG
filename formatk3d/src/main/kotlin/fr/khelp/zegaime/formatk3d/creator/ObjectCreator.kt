package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.engine3d.geometry.Mesh
import fr.khelp.zegaime.formatk3d.data.ColorData
import fr.khelp.zegaime.formatk3d.data.DEFAULT_WIRE_FRAME_COLOR_DATA
import fr.khelp.zegaime.formatk3d.data.FaceData
import fr.khelp.zegaime.formatk3d.data.MeshData
import fr.khelp.zegaime.formatk3d.data.NodeData
import fr.khelp.zegaime.formatk3d.data.NodeType
import fr.khelp.zegaime.formatk3d.data.ObjectData

class ObjectCreator(name : String) : NodeWithMaterialCreator(name)
{
    var showWire : Boolean = false
    var wireColor : ColorData = DEFAULT_WIRE_FRAME_COLOR_DATA

    private var mesh : MeshData = MeshData(emptyList<FaceData>())

    fun mesh(create : Mesh.() -> Unit)
    {
        val mesh = Mesh()
        mesh.create()
        this.mesh = MeshData(mesh)
    }

    override operator fun invoke() : NodeData =
        NodeData(name = this.name, nodeType = NodeType.OBJECT,
                 positionData = this.position, limitData = this.limits,
                 children = this.nodeChildrenCreator.nodes,
                 nodeWithMaterialData = this.nodeWithMaterialData,
                 objectData = ObjectData(mesh = this.mesh,
                                         showWire = this.showWire,
                                         wireColor = this.wireColor))
}