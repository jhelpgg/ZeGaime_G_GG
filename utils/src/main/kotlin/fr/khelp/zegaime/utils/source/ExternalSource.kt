package fr.khelp.zegaime.utils.source

import fr.khelp.zegaime.utils.io.createDirectory
import fr.khelp.zegaime.utils.io.createFile
import fr.khelp.zegaime.utils.io.obtainExternalFile
import fr.khelp.zegaime.utils.io.obtainFile
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URL

class ExternalSource(directoryRelativePath : String = "") : ReferenceSource()
{
    private val baseDirectory = obtainExternalFile(directoryRelativePath)

    init
    {
        if (!this.baseDirectory.createDirectory())
        {
            throw IllegalArgumentException("Can't crete/obtain directory : ${this.baseDirectory.absolutePath}")
        }
    }

    override fun inputStream(path : String) : InputStream = FileInputStream(obtainFile(this.baseDirectory, path))

    override fun outputStream(path : String) : OutputStream
    {
        val file = obtainFile(this.baseDirectory, path)

        if (!file.createFile())
        {
            throw IllegalArgumentException("Can't crete/obtain file : ${file.absolutePath}")
        }

        return FileOutputStream(file)
    }

    override fun url(path : String) : URL = obtainFile(this.baseDirectory, path).toURI()
        .toURL()

    override fun exists(path : String) : Boolean = obtainFile(this.baseDirectory, path).exists()

    override fun internalEquals(referenceSource : ReferenceSource) : Boolean
    {
        val externalSource = referenceSource as ExternalSource
        return this.baseDirectory.absolutePath == externalSource.baseDirectory.absolutePath
    }

    override fun internalHashcode() : Int = this.baseDirectory.absolutePath.hashCode()

    override fun internalDescription() : String = this.baseDirectory.absolutePath
}
