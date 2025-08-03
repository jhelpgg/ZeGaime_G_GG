package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.engine3d.geometry.Mesh
import fr.khelp.zegaime.formatk3d.data.ColorData
import fr.khelp.zegaime.formatk3d.data.DEFAULT_WIRE_FRAME_COLOR_DATA
import fr.khelp.zegaime.formatk3d.data.FaceData
import fr.khelp.zegaime.formatk3d.data.MeshData
import fr.khelp.zegaime.formatk3d.data.NodeData
import fr.khelp.zegaime.formatk3d.data.NodeType
import fr.khelp.zegaime.formatk3d.data.ObjectData

/**
 * Object 3D creator
 * @param name Object name
 */
class ObjectCreator(name : String) : NodeWithMaterialCreator(name)
{
    /** Indicates if wire frame is shown */
    var showWire : Boolean = false
    /** Wire frame color */
    var wireColor : ColorData = DEFAULT_WIRE_FRAME_COLOR_DATA

    /** Object mesh */
    private var mesh : MeshData = MeshData(emptyList<FaceData>())

    /**
     * Define the object mesh
     * @param create Mesh creation lambda
     */
    fun mesh(create : Mesh.() -> Unit)
    {
        val mesh = Mesh()
        mesh.create()
        this.mesh = MeshData(mesh)
    }

    /**
     * Convert to node data for save
     * @return Node data for save
     */
    override operator fun invoke() : NodeData =
        NodeData(name = this.name, nodeType = NodeType.OBJECT,
                 positionData = this.position, limitData = this.limits,
                 children = this.nodeChildrenCreator.nodes,
                 nodeWithMaterialData = this.nodeWithMaterialData,
                 objectData = ObjectData(mesh = this.mesh,
                                         showWire = this.showWire,
                                         wireColor = this.wireColor))
}