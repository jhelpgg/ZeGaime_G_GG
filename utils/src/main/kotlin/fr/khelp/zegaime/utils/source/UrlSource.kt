package fr.khelp.zegaime.utils.source

import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.net.URLConnection

class UrlSource(private val urlBase : String) : ReferenceSource()
{
    override fun inputStream(path : String) : InputStream
    {
        val connection = this.createConnection(path)
        connection.doInput = true
        connection.connect()
        return connection.getInputStream()
    }

    override fun outputStream(path : String) : OutputStream
    {
        val connection = this.createConnection(path)
        connection.doOutput = true
        connection.connect()
        return connection.getOutputStream()
    }

    override fun url(path : String) : URL
    {
        val urlPath =
            when
            {
                this.urlBase.endsWith('/') && path.startsWith('/') -> this.urlBase + path.substring(1)
                this.urlBase.endsWith('/') || path.startsWith('/') -> this.urlBase + path
                else                                               -> this.urlBase + "/" + path
            }
        return URL(urlPath)
    }

    override fun exists(path : String) : Boolean =
        try
        {
            val stream = this.inputStream(path)
            stream.read()
            stream.close()
            true
        }
        catch (_ : Exception)
        {
            false
        }

    private fun createConnection(path : String) : URLConnection
    {
        val urlPath =
            when
            {
                this.urlBase.endsWith('/') && path.startsWith('/') -> this.urlBase + path.substring(1)
                this.urlBase.endsWith('/') || path.startsWith('/') -> this.urlBase + path
                else                                               -> this.urlBase + "/" + path
            }

        return URL(urlPath).openConnection()
    }

    override fun internalEquals(referenceSource : ReferenceSource) : Boolean
    {
        val urlSource = referenceSource as UrlSource
        return this.urlBase == urlSource.urlBase
    }

    override fun internalHashcode() : Int = this.urlBase.hashCode()

    override fun internalDescription() : String = this.urlBase
}
