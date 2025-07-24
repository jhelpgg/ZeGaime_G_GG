package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.geometry.path.Path

fun path(pathFiller : Path.() -> Unit) : Path
{
    val path = Path()
    path.pathFiller()
    return path
}