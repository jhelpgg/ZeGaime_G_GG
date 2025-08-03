package fr.khelp.zegaime.images

import fr.khelp.zegaime.images.color.ARGB
import fr.khelp.zegaime.images.color.argb
import fr.khelp.zegaime.utils.io.createFile
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

fun main()
{
    val file = File("C:\\Users\\jhelp\\Pictures\\4f66eb83176299235eac9a07333aa3d0.jpg")
    val fileOut = File("C:\\Users\\jhelp\\Pictures\\testTest.png")
    fileOut.createFile()
    var inputStream = FileInputStream(file)
    val outputStream = FileOutputStream(fileOut)
    val source = GameImage.load(inputStream)
    var pixels = source.grabPixels()

    for (index in 0 until 10)
    {
        val argb = pixels[index].argb
        val argb2 = ARGB(argb.alpha, (argb.red and 0xF0) or index, argb.green, argb.blue)
        pixels[index] = argb2.argb
    }

    source.putPixels(0, 0, source.width, source.height, pixels)
    source.save(outputStream, ImageFormat.PNG)
    outputStream.flush()
    outputStream.close()
    inputStream.close()

    inputStream = FileInputStream(fileOut)
    val transformed = GameImage.load(inputStream)
    pixels = transformed.grabPixels()

    for (index in 0 until 10)
    {
        val argb = pixels[index].argb
        println("$index => ${argb.red and 0x0F}")
    }
}
