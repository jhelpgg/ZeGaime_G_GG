package fr.khelp.zegaime.utils.source

import java.io.InputStream
import java.io.OutputStream
import java.net.URL

sealed class ReferenceSource
{
    abstract fun inputStream(path : String) : InputStream
    abstract fun outputStream(path : String) : OutputStream
    abstract fun url(path : String) : URL
    abstract fun exists(path : String) : Boolean

    override fun equals(other : Any?) : Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || this.javaClass != other.javaClass)
        {
            return false
        }

        return this.internalEquals(other as ReferenceSource)
    }

    override fun hashCode() : Int = this.javaClass.name.hashCode() + 31 * this.internalHashcode()

    override fun toString() : String = "${this.javaClass.name} : ${this.internalDescription()}"

    protected abstract fun internalEquals(referenceSource : ReferenceSource) : Boolean
    protected abstract fun internalHashcode() : Int
    protected abstract fun internalDescription() : String
}
