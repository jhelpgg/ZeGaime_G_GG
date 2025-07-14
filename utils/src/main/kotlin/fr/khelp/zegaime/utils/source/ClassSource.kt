package fr.khelp.zegaime.utils.source

import java.io.InputStream
import java.io.OutputStream
import java.net.URL

class ClassSource(private val classReference : Class<*> = ReferenceSource::class.java) : ReferenceSource()
{
    override fun inputStream(path : String) : InputStream =
        this.classReference.getResourceAsStream(path)
        ?: throw IllegalArgumentException("Can't retrieve path '$path' from class ${this.classReference.name}")

    override fun outputStream(path : String) : OutputStream =
        throw UnsupportedOperationException("Can't write inside ClassSource reference")

    override fun url(path : String) : URL =
        this.classReference.getResource(path)
        ?: throw IllegalArgumentException("Can't retrieve path '$path' from class ${this.classReference.name}")

    override fun exists(path : String) : Boolean = this.classReference.getResource(path) != null

    override fun internalEquals(referenceSource : ReferenceSource) : Boolean
    {
        val classSource = referenceSource as ClassSource
        return this.classReference.name == classSource.classReference.name
    }

    override fun internalHashcode() : Int = this.classReference.name.hashCode()

    override fun internalDescription() : String = this.classReference.name
}
