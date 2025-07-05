package fr.khelp.zegaime.utils.io

import java.io.OutputStream
import java.math.BigInteger

/**
 * Write an integer to the stream
 * @param int Integer to write
 */
fun OutputStream.writeInt(int: Int)
{
    this.write(byteArrayOf(((int shr 24) and 0xFF).toByte(),
                           ((int shr 16) and 0xFF).toByte(),
                           ((int shr 8) and 0xFF).toByte(),
                           (int and 0xFF).toByte()))
}

/**
 * Write a byte array to the stream.
 *
 * It first writes the array size, then the array content
 * @param byteArray Byte array to write
 */
fun OutputStream.writeByteArray(byteArray: ByteArray)
{
    this.writeInt(byteArray.size)
    this.write(byteArray)
}

/**
 * Write a big integer to the stream
 * @param bigInteger Big integer to write
 */
fun OutputStream.writeBigInteger(bigInteger: BigInteger)
{
    this.writeByteArray(bigInteger.toByteArray())
}
