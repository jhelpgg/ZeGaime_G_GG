package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.geometry.path.element.PathClose
import fr.khelp.zegaime.engine3d.geometry.path.element.PathCubic
import fr.khelp.zegaime.engine3d.geometry.path.element.PathElement
import fr.khelp.zegaime.engine3d.geometry.path.element.PathLineTo
import fr.khelp.zegaime.engine3d.geometry.path.element.PathMove
import fr.khelp.zegaime.engine3d.geometry.path.element.PathQuadratic
import java.util.Objects
import kotlinx.serialization.Serializable

@Serializable
data class PathElementData(val pathElementType : PathElementType, val parameters : FloatArray)
{
    companion object
    {
        fun create(pathElement : PathElement) : PathElementData =
            when (pathElement)
            {
                PathClose        ->
                    PathElementData(PathElementType.CLOSE,
                                    parameters = FloatArray(size = 0))

                is PathMove      ->
                    PathElementData(PathElementType.MOVE,
                                    parameters = floatArrayOf(pathElement.x, pathElement.y))

                is PathLineTo    ->
                    PathElementData(PathElementType.LINE,
                                    parameters = floatArrayOf(pathElement.x, pathElement.y))

                is PathQuadratic ->
                    PathElementData(PathElementType.QUADRATIC,
                                    parameters = floatArrayOf(pathElement.controlX, pathElement.controlY,
                                                              pathElement.x, pathElement.y))

                is PathCubic     ->
                    PathElementData(PathElementType.CUBIC,
                                    parameters = floatArrayOf(pathElement.control1X, pathElement.control1Y,
                                                              pathElement.control2X, pathElement.control2Y,
                                                              pathElement.x, pathElement.y))
            }
    }

    val pathElement : PathElement by lazy {
        when (this.pathElementType)
        {
            PathElementType.CLOSE     ->
                PathClose

            PathElementType.MOVE      ->
                PathMove(x = this.parameters[0], y = this.parameters[1])

            PathElementType.LINE      ->
                PathLineTo(x = this.parameters[0], y = this.parameters[1])

            PathElementType.QUADRATIC ->
                PathQuadratic(controlX = this.parameters[0], controlY = this.parameters[1],
                              x = this.parameters[2], y = this.parameters[3])

            PathElementType.CUBIC     ->
                PathCubic(control1X = this.parameters[0], control1Y = this.parameters[1],
                          control2X = this.parameters[2], control2Y = this.parameters[3],
                          x = this.parameters[4], y = this.parameters[5])
        }
    }

    override fun equals(other : Any?) : Boolean =
        when
        {
            this === other                             -> true

            null == other || other !is PathElementData -> false

            else                                       ->
                this.pathElementType == other.pathElementType && this.parameters.contentEquals(other.parameters)
        }

    override fun hashCode() : Int =
        Objects.hash(this.pathElementType, this.parameters.contentHashCode())
}
