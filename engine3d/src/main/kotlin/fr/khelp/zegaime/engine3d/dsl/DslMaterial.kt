package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.render.Material

@MaterialDSL
fun material(create:Material.() ->Unit) : Material
{
    val material = Material()
    material.create()
    return material
}

@MaterialDSL
fun Material.edit(edit:Material.() ->Unit)
{
    this.edit()
}