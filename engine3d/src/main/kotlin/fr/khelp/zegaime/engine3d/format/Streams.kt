package fr.khelp.zegaime.engine3d.format

import fr.khelp.zegaime.engine3d.render.Color4f
import fr.khelp.zegaime.utils.io.readBoolean
import fr.khelp.zegaime.utils.io.readFloat
import fr.khelp.zegaime.utils.io.readInt
import fr.khelp.zegaime.utils.io.readString
import fr.khelp.zegaime.utils.io.writeBoolean
import fr.khelp.zegaime.utils.io.writeFloat
import fr.khelp.zegaime.utils.io.writeInt
import fr.khelp.zegaime.utils.io.writeString
import java.io.InputStream
import java.io.OutputStream

fun InputStream.readColor4f() : Color4f
{
    val red = this.readFloat()
    val green = this.readFloat()
    val blue = this.readFloat()
    val alpha = this.readFloat()
    return Color4f(red, green, blue, alpha)
}

fun OutputStream.writeColor4f(color : Color4f)
{
    this.writeFloat(color.red)
    this.writeFloat(color.green)
    this.writeFloat(color.blue)
    this.writeFloat(color.alpha)
}

fun InputStream.readTextureType() : TextureType =
    TextureType.valueOf(this.readString())

fun OutputStream.writeTextureType(textureType : TextureType)
{
    this.writeString(textureType.name)
}

fun InputStream.readTextureDescription() : TextureDescription
{
    val type = this.readTextureType()
    val name = this.readString()
    return TextureDescription(type, name)
}

fun OutputStream.writeTextureDescription(textureDescription : TextureDescription)
{
    this.writeTextureType(textureDescription.type)
    this.writeString(textureDescription.name)
}

fun InputStream.readMaterialK3D() : MaterialK3D
{
    val materialK3D = MaterialK3D()
    materialK3D.colorAmbient = this.readColor4f()
    materialK3D.colorDiffuse = this.readColor4f()
    materialK3D.colorEmissive = this.readColor4f()
    materialK3D.colorSpecular = this.readColor4f()
    materialK3D.specularLevel = this.readFloat()
    materialK3D.shininess = this.readInt()
    materialK3D.transparency = this.readFloat()
    materialK3D.twoSided = this.readBoolean()
    materialK3D.sphericRate = this.readFloat()

    if (this.readBoolean())
    {
        materialK3D.textureDiffuse = this.readTextureDescription()
    }

    if (this.readBoolean())
    {
        materialK3D.textureSpheric = this.readTextureDescription()
    }

    return materialK3D
}

fun OutputStream.writeMaterialK3D(materialK3D : MaterialK3D)
{
    this.writeColor4f(materialK3D.colorAmbient)
    this.writeColor4f(materialK3D.colorDiffuse)
    this.writeColor4f(materialK3D.colorEmissive)
    this.writeColor4f(materialK3D.colorSpecular)
    this.writeFloat(materialK3D.specularLevel)
    this.writeInt(materialK3D.shininess)
    this.writeFloat(materialK3D.transparency)
    this.writeFloat(materialK3D.sphericRate)

    val textureDiffuse = materialK3D.textureDiffuse
    this.writeBoolean(textureDiffuse != null)

    if (textureDiffuse != null)
    {
        this.writeTextureDescription(textureDiffuse)
    }

    val textureSpheric = materialK3D.textureSpheric
    this.writeBoolean(textureSpheric != null)

    if (textureSpheric != null)
    {
        this.writeTextureDescription(textureSpheric)
    }
}
