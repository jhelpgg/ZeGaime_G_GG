package fr.khelp.zegaime.engine3d.loader.obj

/**
 * `obj` vertex description
 *
 * @property point Point index
 * @property uv UV index
 * @property normal Normal index
 */
internal data class ObjVertex(val point : Int, val uv : Int, val normal : Int)
