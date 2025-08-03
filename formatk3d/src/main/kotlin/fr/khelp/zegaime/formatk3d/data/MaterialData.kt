package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.render.Material
import fr.khelp.zegaime.resources.Resources
import kotlinx.serialization.Serializable

/**
 * Material data for save
 * @param colorAmbient Ambient color
 * @param colorDiffuse Diffuse color
 * @param colorEmissive Emissive color
 * @param colorSpecular Specular color
 * @param specularLevel Specular level
 * @param shininess Shininess
 * @param transparency Transparency
 * @param twoSided Two sided
 * @param sphericRate Spheric rate
 * @param textureDiffuse Diffuse texture
 * @param textureSpheric Spheric texture
 */
@Serializable
data class MaterialData(val colorAmbient : ColorData,
                        val colorDiffuse : ColorData,
                        val colorEmissive : ColorData,
                        val colorSpecular : ColorData,
                        val specularLevel : Float,
                        val shininess : Int,
                        val transparency : Float,
                        val twoSided : Boolean,
                        val sphericRate : Float,
                        val textureDiffuse : TextureData?,
                        val textureSpheric : TextureData?)
{
    /**
     * Convert to material
     * @param resources Resources to use
     * @return The material
     */
    fun material(resources : Resources) : Material
    {
        val material = Material()
        material.colorAmbient = this.colorAmbient.color
        material.colorDiffuse = this.colorDiffuse.color
        material.colorEmissive = this.colorEmissive.color
        material.colorSpecular = this.colorSpecular.color
        material.specularLevel = this.specularLevel
        material.shininess = this.shininess
        material.transparency = this.transparency
        material.twoSided = this.twoSided
        material.sphericRate = this.sphericRate
        material.textureDiffuse = this.textureDiffuse?.texture(resources)
        material.textureSpheric = this.textureSpheric?.texture(resources)
        return material
    }
}
