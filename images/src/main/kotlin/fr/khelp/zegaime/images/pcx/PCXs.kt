package fr.khelp.zegaime.images.pcx

import fr.khelp.zegaime.utils.extensions.and
import fr.khelp.zegaime.utils.io.treatInputStream
import java.awt.Dimension
import java.io.File
import java.io.FileInputStream

/**
 * Original PCX manufacturer code.
 */
val MANUFACTURER_ZSOFT = 10.toByte()

/**
 * PCX version Paintbrush v2.5.
 */
val VERSION_PAINTBRUSH_V_2_5 = 0.toByte()

/**
 * PCX version Paintbrush v2.5 unofficial version.
 */
val VERSION_PAINTBRUSH_V_2_5_UNOFFICAL = 1.toByte()

/**
 * PCX version Paintbrush v2.8 w palette information.
 */
val VERSION_PAINTBRUSH_V_2_8_W = 2.toByte()

/**
 * PCX version Paintbrush v2.8 w/o palette information.
 */
val VERSION_PAINTBRUSH_V_2_8_WO = 3.toByte()

/**
 * PCX version Paintbrush v3.0+.
 */
val VERSION_PAINTBRUSH_V_3_0 = 5.toByte()

/**
 * PCX version Paintbrush/Windows.
 */
val VERSION_PAINTBRUSH_WINDOWS = 4.toByte()

/**
 * Computes the size of a PCX image.
 *
 * If the given file is not a PCX image file, `null` is returned.
 *
 * @param file The PCX image file.
 * @return The PCX image size, or `null` if the given file is not a valid PCX image file.
 */
fun computePcxSize(file: File?): Dimension?
{
    if (file == null || !file.exists() || file.isDirectory || !file.canRead())
    {
        return null
    }

    var dimension: Dimension? = null
    treatInputStream({ FileInputStream(file) },
                     { inputStream ->
                         val pcx = PCX()
                         pcx.readHeader(inputStream)
                         dimension = Dimension(pcx.width, pcx.height)
                     },
                     {})
    return dimension
}

/**
 * Indicates if a file is a PCX image file.
 *
 * @param file The file to test.
 * @return `true` if the file is a PCX image file, `false` otherwise.
 */
fun isPCX(file: File) = computePcxSize(file) != null

/**
 * Converts a manufacturer code to its name.
 *
 * @param manufacturer The manufacturer code.
 * @return The manufacturer name.
 */
fun manufacturerToString(manufacturer: Byte) =
    when (manufacturer)
    {
        MANUFACTURER_ZSOFT -> "ZSoft"
        else -> "Manufacturer_${manufacturer and 0xFF}"
    }

/**
 * Converts a version code to its name.
 *
 * @param version The version code.
 * @return The version name.
 */
fun versionToString(version: Byte) =
    when (version)
    {
        VERSION_PAINTBRUSH_V_2_5, VERSION_PAINTBRUSH_V_2_5_UNOFFICAL -> "Paintbrush v2.5"
        VERSION_PAINTBRUSH_V_2_8_W -> "Paintbrush v2.8 w palette information"
        VERSION_PAINTBRUSH_V_2_8_WO -> "Paintbrush v2.8 w/o palette information"
        VERSION_PAINTBRUSH_WINDOWS -> "Paintbrush/Windows"
        VERSION_PAINTBRUSH_V_3_0 -> "Paintbrush v3.0+"
        else -> "More than Paintbrush v3.0+ (${version and 0xFF})"
    }