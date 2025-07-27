package fr.khelp.zegaime.engine3d.loader.obj.options

import fr.khelp.zegaime.resources.Resources

/**
 * Normals in `obj` files are ignored (if present) and we use a normal map instead.
 *
 * Note:
 * > For it works, the objects in `obj` file must have `(u, v)` coordinates
 * >
 * > We strongly recommend, to use it with `obj` file that contains only one object, since all objects will use the same normal map
 *
 * @property imagePath Normal map image path
 * @property resources Resources where gets the normal map
 */
class ObjUseNormalMap(private val imagePath : String, private val resources : Resources) : ObjOption
{
    /** The normal map */
    val normalMap : NormalMap by lazy {
        val image = this.resources.image(this.imagePath)
        NormalMap(image.width, image.height, image.grabPixels())
    }
}
