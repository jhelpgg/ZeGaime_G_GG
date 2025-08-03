package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.engine3d.geometry.path.Path
import fr.khelp.zegaime.formatk3d.data.NodeData
import fr.khelp.zegaime.formatk3d.data.NodeType
import fr.khelp.zegaime.formatk3d.data.PathData
import fr.khelp.zegaime.formatk3d.data.RevolutionData

/**
 * Revolution creator
 * @param name Revolution name
 */
class RevolutionCreator(name : String) : NodeWithMaterialCreator(name)
{
    /** Revolution precision */
    var precision : Int = 5
        set(value)
        {
            field = value.coerceIn(2, 12)
        }

    /** Revolution angle */
    var angle : Float = 360f
        set(value)
        {
            field = value.coerceIn(1f, 360f)
        }

    /** Revolution rotation precision */
    var rotationPrecision : Int = 12
        set(value)
        {
            field = value.coerceIn(3, 32)
        }

    /** Revolution start position in path */
    var start : Float = 0f
    /** Revolution end position in path */
    var end : Float = 1f
    /** Revolution texture U multiplier */
    var multiplierU : Float = 1f

    /** Path to follow for revolution */
    private val path = Path()

    /**
     * Define the revolution path
     * @param fill Path creation lambda
     */
    fun path(fill : Path.() -> Unit)
    {
        this.path.clear()
        this.path.fill()
    }

    /**
     * Convert to node data for save
     * @return Node data for save
     */
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