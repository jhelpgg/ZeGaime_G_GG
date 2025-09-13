package fr.khelp.zegaime.engine3d.scene.prebuilt

import fr.khelp.zegaime.engine3d.dsl.face
import fr.khelp.zegaime.engine3d.dsl.mesh
import fr.khelp.zegaime.engine3d.geometry.path.Path
import fr.khelp.zegaime.engine3d.scene.Object3D
import fr.khelp.zegaime.engine3d.utils.scalarProduct
import fr.khelp.zegaime.utils.collections.lists.forEachReversed
import fr.khelp.zegaime.utils.collections.lists.inverted
import kotlin.math.max
import kotlin.math.min

/**
 * 3D object that repeats a path along another path to create a 3D mesh
 *
 * @param id Object unique identifier
 * @param pathToRepeat Path to repeat along the follow path
 * @param pathToRepeatPrecision Number of segments used to approximate the path to repeat. Value between 2 and 12
 * @param pathToFollow Path that defines how the repeated path is placed in 3D space
 * @param pathToFollowPrecision Number of segments used to approximate the follow path. Value between 2 and 12
 * @param uMinimum Minimum U texture coordinate for a repeated path
 * @param uMaximum Maximum U texture coordinate for a repeated path
 * @param vMinimum Minimum V texture coordinate for a follow path
 * @param vMaximum Maximum V texture coordinate for a follow path
 * @param closed If true, creates end caps to close the mesh
 */
class RepeatPath(id : String,
                 pathToRepeat : Path, pathToRepeatPrecision : Int = 5,
                 pathToFollow : Path, pathToFollowPrecision : Int = 5,
                 uMinimum : Float = 0f, uMaximum : Float = 1f,
                 vMinimum : Float = 0f, vMaximum : Float = 1f,
                 closed : Boolean = true) : Object3D(id)
{
    init
    {
        val repeatPrecision = pathToRepeatPrecision.coerceIn(2, 12)
        val followPrecision = pathToFollowPrecision.coerceIn(2, 12)

        val repeatLines = pathToRepeat.path(repeatPrecision, uMinimum, uMaximum)
        val followLines = pathToFollow.path(followPrecision, vMinimum, vMaximum)

        mesh {
            for (repeat in repeatLines)
            {
                for (follow in followLines)
                {
                    val normal =
                        scalarProduct(xBefore = repeat.x1 + follow.x1, yBefore = repeat.y1, zBefore = follow.y1,
                                      xCenter = repeat.x1 + follow.x2, yCenter = repeat.y1, zCenter = follow.y2,
                                      xAfter = repeat.x2 + follow.x2, yAfter = repeat.y2, zAfter = follow.y2)
                    face {
                        add(x = repeat.x1 + follow.x1, y = repeat.y1, z = follow.y1,
                            uvU = repeat.information1, uvV = follow.information1,
                            normalX = normal.x, normalY = normal.y, normalZ = normal.z)

                        add(x = repeat.x1 + follow.x2, y = repeat.y1, z = follow.y2,
                            uvU = repeat.information1, uvV = follow.information2,
                            normalX = normal.x, normalY = normal.y, normalZ = normal.z)

                        add(x = repeat.x2 + follow.x2, y = repeat.y2, z = follow.y2,
                            uvU = repeat.information2, uvV = follow.information2,
                            normalX = normal.x, normalY = normal.y, normalZ = normal.z)

                        add(x = repeat.x2 + follow.x1, y = repeat.y2, z = follow.y1,
                            uvU = repeat.information2, uvV = follow.information1,
                            normalX = normal.x, normalY = normal.y, normalZ = normal.z)
                    }
                }
            }

            if (closed)
            {
                var minX = Float.POSITIVE_INFINITY
                var minY = Float.POSITIVE_INFINITY
                var maxX = Float.NEGATIVE_INFINITY
                var maxY = Float.NEGATIVE_INFINITY

                for (line in repeatLines)
                {
                    minX = min(minX, line.x1)
                    minY = min(minY, line.y1)
                    maxX = max(maxX, line.x1)
                    maxY = max(maxY, line.y1)
                }

                val deltaX = maxX - minX
                val deltaY = maxY - minY

                var xx = followLines[0].x1
                var z = followLines[0].y1

                face {
                    for (line in repeatLines)
                    {
                        add(line.x1 + xx, line.y1, z,
                            (line.x1 - minX) / deltaX, (line.y1 - minY) / deltaY,
                            0f, 0f, 1f)
                    }
                }

                xx = followLines[followLines.size - 1].x2
                z = followLines[followLines.size - 1].y2

                face {
                    for (line in repeatLines.inverted())
                    {
                        add(line.x1 + xx, line.y1, z,
                            (line.x1 - minX) / deltaX, (line.y1 - minY) / deltaY,
                            0f, 0f, -1f)
                    }
                }
            }
        }
    }
}