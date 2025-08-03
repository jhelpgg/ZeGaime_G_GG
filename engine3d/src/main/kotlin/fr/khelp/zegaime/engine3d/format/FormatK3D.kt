package fr.khelp.zegaime.engine3d.format

import fr.khelp.zegaime.resources.Resources
import fr.khelp.zegaime.utils.source.DirectorySource
import java.io.File

/**
 * KHelp file format for 3D
 */
class FormatK3D(val name:String, val directory:File)
{
    companion object
    {
        private const val TEXTURES_DIRECTORY_NAME = "textures"
        private const val MATERIALS_DIRECTORY_NAME = "materials"

        fun textureRelativePath(textureName:String) : String =
            "$TEXTURES_DIRECTORY_NAME/$textureName"

        fun materialRelativePath(materialName:String) : String =
            "$MATERIALS_DIRECTORY_NAME/$materialName"
    }

    private val resourcesDirectory = File(this.directory, "resources")
    private val texturesDirectory = File(this.resourcesDirectory, TEXTURES_DIRECTORY_NAME)
    private val materialsDirectory = File(this.resourcesDirectory, MATERIALS_DIRECTORY_NAME)
    val resources = Resources(DirectorySource(this.resourcesDirectory))

    val textures : TexturesK3D = TexturesK3D(this.texturesDirectory)
    val materialsK3D : MaterialsK3D = MaterialsK3D(this.materialsDirectory)
}