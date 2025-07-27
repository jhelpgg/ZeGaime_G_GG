package fr.khelp.zegaime.engine3d.render

import fr.khelp.zegaime.engine3d.scene.Object3D
import org.lwjgl.opengl.GL11

/**
 * Represents the material of a 3D object.
 *
 * A material defines the visual properties of an object, such as its color, shininess, and texture.
 *
 * **Creation example:**
 * ```kotlin
 * val material = Material()
 * material.colorDiffuse = RED
 * material.shininess = 128
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * myObject3D.material = material
 * ```
 *
 * @property colorAmbient The ambient color of the material.
 * @property colorDiffuse The diffuse color of the material.
 * @property colorEmissive The emissive color of the material.
 * @property colorSpecular The specular color of the material.
 * @property specularLevel The influence of the specular color.
 * @property shininess The shininess of the material.
 * @property transparency The transparency of the material (0.0 for fully transparent, 1.0 for fully opaque).
 * @property twoSided Indicates if the material should be rendered on both sides of the faces.
 * @property sphericRate The influence of the spherical texture.
 * @property textureDiffuse The diffuse texture of the material.
 * @property textureSpheric The spherical texture of the material.
 * @constructor Creates a new material with default settings.
 */
class Material
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
    var textureDiffuse : Texture? = null

    /**Spherical texture*/
    var textureSpheric : Texture? = null

    /**
     * Reset all settings to put as default.
     */
    fun originalSettings()
    {
        this.colorAmbient = BLACK
        this.colorDiffuse = GRAY
        this.colorEmissive = LIGHT_GRAY
        this.colorSpecular = LIGHT_GRAY
        this.specularLevel = 0.1f
        this.shininess = 12
        this.transparency = 1f
        this.twoSided = false
        this.sphericRate = 1f
        this.textureSpheric = null
        this.textureDiffuse = null
    }

    /**
     * Do settings for 2D.
     */
    fun settingAsFor2D()
    {
        this.colorEmissive = WHITE
        this.specularLevel = 1f
        this.shininess = 128
        this.colorDiffuse = WHITE
        this.colorSpecular = BLACK
        this.colorAmbient = WHITE
        this.twoSided = true
    }

    /**
     * Prepare material for OpenGL render.
     *
     * Use by the renderer, don't call it directly.
     */
    internal fun prepareMaterial()
    {
        if (this.twoSided)
        {
            GL11.glDisable(GL11.GL_CULL_FACE)
        }
        else
        {
            GL11.glEnable(GL11.GL_CULL_FACE)
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D)
        this.colorDiffuse.glColor4f(this.transparency)
        //
        GL11.glMaterialfv(GL11.GL_FRONT_AND_BACK, GL11.GL_DIFFUSE, this.colorDiffuse.putInFloatBuffer())
        //
        GL11.glMaterialfv(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, this.colorEmissive.putInFloatBuffer())
        //
        GL11.glMaterialfv(GL11.GL_FRONT_AND_BACK, GL11.GL_SPECULAR,
                          this.colorSpecular.putInFloatBuffer(this.specularLevel))
        //
        GL11.glMaterialfv(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT, this.colorAmbient.putInFloatBuffer())
        //
        GL11.glMateriali(GL11.GL_FRONT_AND_BACK, GL11.GL_SHININESS, this.shininess)
    }

    /**
     * Render the material for a 3D object.
     *
     * For internal use only.
     *
     * @param object3D Object to render.
     */
    internal fun renderMaterial(object3D : Object3D)
    {
        this.prepareMaterial()
        //
        if (this.textureDiffuse != null)
        {
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            this.textureDiffuse?.bind()
            object3D.drawObject()
            GL11.glDisable(GL11.GL_TEXTURE_2D)
        }
        else
        {
            object3D.drawObject()
        }

        if (this.textureSpheric != null)
        {
            val transparency = this.transparency
            this.transparency *= this.sphericRate
            //
            this.prepareMaterial()
            GL11.glDepthFunc(GL11.GL_LEQUAL)
            //
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            GL11.glEnable(GL11.GL_TEXTURE_GEN_S)
            GL11.glEnable(GL11.GL_TEXTURE_GEN_T)
            //
            GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP)
            GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP)
            //
            this.textureSpheric?.bind()
            object3D.drawObject()
            //
            GL11.glDisable(GL11.GL_TEXTURE_GEN_T)
            GL11.glDisable(GL11.GL_TEXTURE_GEN_S)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            //
            GL11.glDepthFunc(GL11.GL_LESS)
            //
            this.transparency = transparency
        }

        if (object3D.showWire)
        {
            GL11.glDisable(GL11.GL_LIGHTING)
            object3D.wireColor.glColor4f()
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE)
            GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE.toFloat())
            object3D.drawObject()
            GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE.toFloat())
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL)
            GL11.glEnable(GL11.GL_LIGHTING)
        }
    }
}