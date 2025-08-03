package fr.khelp.zegaime.engine3d.format

import fr.khelp.zegaime.engine3d.render.BLACK
import fr.khelp.zegaime.engine3d.render.GRAY
import fr.khelp.zegaime.engine3d.render.LIGHT_GRAY
import fr.khelp.zegaime.engine3d.render.Material
import fr.khelp.zegaime.resources.Resources

class MaterialK3D
{
    /**Ambient color*/
    var colorAmbient = BLACK

    /**Diffuse color*/
    var colorDiffuse = GRAY

    /**Emissive color*/
    var colorEmissive = LIGHT_GRAY

    /**Specular color*/
    var colorSpecular = LIGHT_GRAY

    /**Influence of specular color*/
    var specularLevel = 0.1f

    /**Shininess*/
    var shininess = 12

    /**Transparency (0 full transparent, 1 opaque)*/
    var transparency = 1f

    /**Indicates if have to render the two parts*/
    var twoSided = false

    /**Spherical texture influence*/
    var sphericRate = 1f

    /**Diffuse texture*/
    var textureDiffuse : TextureDescription? = null

    /**Spherical texture*/
    var textureSpheric : TextureDescription? = null

    fun resolve(resources : Resources) : Material
    {
        val material = Material()
        material.colorAmbient = this.colorAmbient
        material.colorDiffuse = this.colorDiffuse
        material.colorEmissive = this.colorEmissive
        material.colorSpecular = this.colorSpecular
        material.specularLevel = this.specularLevel
        material.shininess = this.shininess
        material.transparency = this.transparency
        material.twoSided = this.twoSided
        material.sphericRate = this.sphericRate
        material.textureDiffuse = this.textureDiffuse?.fromResources(resources)
        material.textureSpheric = this.textureSpheric?.fromResources(resources)
        return material
    }
}