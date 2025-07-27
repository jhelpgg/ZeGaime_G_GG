package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.geometry.path.Path

/**
 * Creates a path using the DSL.
 *
 * **Usage example**
 * ```kotlin
 * val path = path {
 *     moveTo(0f, 0f)
 *     lineTo(1f, 1f)
 *     quadraticTo(2f, 0f, 3f, 1f)
 *     cubicTo(4f, 2f, 5f, 0f, 6f, 1f)
 *     close()
 * }
 * ```
 *
 * @param pathFiller The lambda function to create the path.
 * @return The created path.
 */
fun path(pathFiller : Path.() -> Unit) : Path
{
    val path = Path()
    path.pathFiller()
    return path
}
