package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.geometry.path.Path
import kotlinx.serialization.Serializable

/**
 * Path data for save
 * @param pathElements Path elements
 */
@Serializable
data class PathData(val pathElements : List<PathElementData>)
{
    companion object
    {
        /**
         * Create a new instance of PathData from a Path
         * @param path Path to copy
         * @return The new instance
         */
        fun create(path : Path) : PathData
        {
            val pathElements = ArrayList<PathElementData>()

            for (index in 0 until path.size)
            {
                pathElements.add(PathElementData.create(path[index]))
            }

            return PathData(pathElements)
        }
    }

    /**
     * The Path
     */
    val path : Path by lazy {
        val path = Path()

        for (pathElement in this.pathElements)
        {
            when (pathElement.pathElementType)
            {
                PathElementType.CLOSE     ->
                    path.close()

                PathElementType.MOVE      ->
                    path.move(x = pathElement.parameters[0], y = pathElement.parameters[1])

                PathElementType.LINE      ->
                    path.line(x = pathElement.parameters[0], y = pathElement.parameters[1])

                PathElementType.QUADRATIC ->
                    path.quadratic(controlX = pathElement.parameters[0], controlY = pathElement.parameters[1],
                                   x = pathElement.parameters[2], y = pathElement.parameters[3])

                PathElementType.CUBIC     ->
                    path.cubic(control1X = pathElement.parameters[0], control1Y = pathElement.parameters[1],
                               control2X = pathElement.parameters[2], control2Y = pathElement.parameters[3],
                               x = pathElement.parameters[4], y = pathElement.parameters[5])
            }
        }

        path
    }
}
