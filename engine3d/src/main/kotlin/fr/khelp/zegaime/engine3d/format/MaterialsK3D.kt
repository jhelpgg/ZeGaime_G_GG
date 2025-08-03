package fr.khelp.zegaime.engine3d.format

import fr.khelp.zegaime.utils.io.createDirectory
import fr.khelp.zegaime.utils.io.createFile
import java.io.File
import java.io.FileOutputStream

class MaterialsK3D(private val directory : File)
{
    init
    {
        if (!this.directory.createDirectory())
        {
            throw IllegalArgumentException("Can't create/obtain directory : ${this.directory.absolutePath}")
        }
    }

    private val materials = HashMap<String, MaterialK3D>()

    fun store(name : String, materialK3D : MaterialK3D)
    {
        this.materials[name] = materialK3D
        val file = File(this.directory, name)
        file.createFile()
        val outputStream = FileOutputStream(file)
        outputStream.writeMaterialK3D(materialK3D)
        outputStream.flush()
        outputStream.close()
    }

    @Throws(MaterialNotFoundException::class)
    fun obtain(name : String) : MaterialK3D =
        this.materials[name] ?: throw MaterialNotFoundException(name)
}