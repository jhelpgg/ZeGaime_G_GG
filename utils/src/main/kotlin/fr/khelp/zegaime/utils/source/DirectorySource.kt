package fr.khelp.zegaime.utils.source

import fr.khelp.zegaime.utils.io.createDirectory
import fr.khelp.zegaime.utils.io.createFile
import fr.khelp.zegaime.utils.io.obtainFile
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URL

class DirectorySource(private val directory : File) : ReferenceSource()
{
    init
    {
        if (!this.directory.createDirectory())
        {
            throw IllegalArgumentException("Can't create/obtain directory : ${this.directory.absolutePath}")
        }
    }

    override fun inputStream(path : String) : InputStream = FileInputStream(obtainFile(this.directory, path))

    override fun outputStream(path : String) : OutputStream
    {
        val file = obtainFile(this.directory, path)

        if (!file.createFile())
        {
            throw IllegalArgumentException("Can't create/obtain file : ${file.absolutePath}")
        }

        return FileOutputStream(file)
    }

    override fun url(path : String) : URL = obtainFile(this.directory, path).toURI()
        .toURL()

    override fun exists(path : String) : Boolean = obtainFile(this.directory, path).exists()

    override fun internalEquals(referenceSource : ReferenceSource) : Boolean
    {
        val directorySource = referenceSource as DirectorySource
        return this.directory.absolutePath == directorySource.directory.absolutePath
    }

    override fun internalHashcode() : Int = this.directory.absolutePath.hashCode()

    override fun internalDescription() : String = this.directory.absolutePath
}
