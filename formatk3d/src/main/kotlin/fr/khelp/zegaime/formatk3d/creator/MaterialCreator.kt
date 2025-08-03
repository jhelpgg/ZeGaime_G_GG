package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.formatk3d.data.BLACK_DATA
import fr.khelp.zegaime.formatk3d.data.ColorData
import fr.khelp.zegaime.formatk3d.data.GRAY_DATA
import fr.khelp.zegaime.formatk3d.data.LIGHT_GRAY_DATA

/**
 * Material creator
 */
class MaterialCreator
{
    /** Ambient color */
    var colorAmbient : ColorData = BLACK_DATA

    /**Diffuse color*/
    var colorDiffuse : ColorData = GRAY_DATA

    /**Emissive color*/
    var colorEmissive : ColorData = LIGHT_GRAY_DATA

    /**Specular color*/
    var colorSpecular : ColorData = LIGHT_GRAY_DATA

    /**Influence of specular color*/
    var specularLevel : Float = 0.1f

    /**Shininess*/
    var shininess : Int = 12

    /**Transparency (0 full transparent, 1 opaque)*/
    var transparency : Float = 1f

    /**Indicates if have to render the two parts*/
    var twoSided : Boolean = false

    /**Spherical texture influence*/
    var sphericRate : Float = 1f

    /**Diffuse texture*/
    var textureDiffuse : String? = null

    /**Spherical texture*/
    var textureSpheric : String? = null
}