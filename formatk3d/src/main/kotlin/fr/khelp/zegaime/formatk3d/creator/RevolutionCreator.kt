package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.engine3d.geometry.path.Path
import fr.khelp.zegaime.formatk3d.data.NodeData
import fr.khelp.zegaime.formatk3d.data.NodeType
import fr.khelp.zegaime.formatk3d.data.PathData
import fr.khelp.zegaime.formatk3d.data.RevolutionData

class RevolutionCreator(name : String) : NodeWithMaterialCreator(name)
{
    var precision : Int = 5
        set(value)
        {
            field = value.coerceIn(2, 12)
        }

    var angle : Float = 360f
        set(value)
        {
            field = value.coerceIn(1f, 360f)
        }

    var rotationPrecision : Int = 12
        set(value)
        {
            field = value.coerceIn(3, 32)
        }

    var start : Float = 0f
    var end : Float = 1f
    var multiplierU : Float = 1f

    private val path = Path()

    fun path(fill : Path.() -> Unit)
    {
        this.path.clear()
        this.path.fill()
    }

    override fun invoke() : NodeData =
        NodeData(name = this.name, nodeType = NodeType.REVOLUTION,
                 positionData = this.position, limitData = this.limits,
                 children = this.nodeChildrenCreator.nodes,
                 nodeWithMaterialData = this.nodeWithMaterialData,
                 revolutionData = RevolutionData(precision = this.precision,
                                                 angle = this.angle, rotationPrecision = this.rotationPrecision,
                                                 start = this.start, end = this.end,
                                                 multiplierU = this.multiplierU,
                                                 path = PathData.create(this.path)))
}