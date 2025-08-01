package fr.khelp.zegaime.engine3d.utils

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.DoubleBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer

/**Textures maximum width*/
val MAX_WIDTH = 4096

/**Textures maximum height*/
val MAX_HEIGHT = 4096

/**Maximum buffer size*/
val MAX_DIMENSION = MAX_WIDTH * MAX_HEIGHT

/**Maximum size in bytes*/
val MAX_DIMENSION_IN_BYTES = MAX_DIMENSION shl 2

/**Byte buffer for communicating with OpenGL*/
val TEMPORARY_BYTE_BUFFER : ByteBuffer =
    ByteBuffer.allocateDirect(MAX_DIMENSION_IN_BYTES).order(ByteOrder.nativeOrder())

/**Int buffer for communicating with OpenGL*/
val TEMPORARY_INT_BUFFER : IntBuffer = TEMPORARY_BYTE_BUFFER.asIntBuffer()

/**Float buffer for communicating with OpenGL*/
val TEMPORARY_FLOAT_BUFFER : FloatBuffer = TEMPORARY_BYTE_BUFFER.asFloatBuffer()

/**Double buffer for communicating with OpenGL*/
val TEMPORARY_DOUBLE_BUFFER : DoubleBuffer = TEMPORARY_BYTE_BUFFER.asDoubleBuffer()

/**
 * Fill a byte array with the buffer.
 *
 * @param array Array to fill
 */
fun fillBuffer(array : ByteArray)
{
    TEMPORARY_BYTE_BUFFER.rewind()
    TEMPORARY_BYTE_BUFFER.get(array)
    TEMPORARY_BYTE_BUFFER.rewind()
}

/**
 * Fill a double array with the buffer.
 *
 * @param array Array to fill
 */
fun fillBuffer(array : DoubleArray)
{
    TEMPORARY_DOUBLE_BUFFER.rewind()
    TEMPORARY_DOUBLE_BUFFER.get(array)
    TEMPORARY_DOUBLE_BUFFER.rewind()
}

/**
 * Fill a float array with the buffer.
 *
 * @param array Array to fill
 */
fun fillBuffer(array : FloatArray)
{
    TEMPORARY_FLOAT_BUFFER.rewind()
    TEMPORARY_FLOAT_BUFFER.get(array)
    TEMPORARY_FLOAT_BUFFER.rewind()
}

/**
 * Fill an int array with the buffer.
 *
 * @param array Array to fill
 */
fun fillBuffer(array : IntArray)
{
    TEMPORARY_INT_BUFFER.rewind()
    TEMPORARY_INT_BUFFER.get(array)
    TEMPORARY_INT_BUFFER.rewind()
}

/**
 * Transfer a byte array to the buffer.
 *
 * @param array Array to transfer
 * @return The buffer filled
 */
fun transferByte(array : ByteArray) : ByteBuffer
{
    TEMPORARY_BYTE_BUFFER.rewind()
    TEMPORARY_BYTE_BUFFER.put(array)
    TEMPORARY_BYTE_BUFFER.rewind()
    return TEMPORARY_BYTE_BUFFER
}

/**
 * Transfer a float array to the buffer.
 *
 * @param array Array to transfer
 * @return The buffer filled
 */
fun transferFloat(vararg array : Float) : FloatBuffer
{
    TEMPORARY_FLOAT_BUFFER.rewind()
    TEMPORARY_FLOAT_BUFFER.put(array)
    TEMPORARY_FLOAT_BUFFER.rewind()
    return TEMPORARY_FLOAT_BUFFER
}

/**
 * Transfer an int array to the buffer.
 *
 * @param array Array to transfer
 * @return The buffer filled
 */
fun transferInteger(vararg array : Int) : IntBuffer
{
    TEMPORARY_INT_BUFFER.rewind()
    TEMPORARY_INT_BUFFER.put(array)
    TEMPORARY_INT_BUFFER.rewind()
    return TEMPORARY_INT_BUFFER
}
