package fr.khelp.zegaime.images.pcx

import fr.khelp.zegaime.utils.extensions.and
import fr.khelp.zegaime.utils.io.treatInputStream
import java.awt.Dimension
import java.io.File
import java.io.FileInputStream

/**
 * Original PCX manufacturer code
 */
val MANUFACTURER_ZSOFT = 10.toByte()

/**
 * PCX version Paintbrush v2.5
 */
val VERSION_PAINTBRUSH_V_2_5 = 0.toByte()

/**
 * PCX version Paintbrush v2.5 unofficial version
 */
val VERSION_PAINTBRUSH_V_2_5_UNOFFICAL = 1.toByte()

/**
 * PCX version Paintbrush v2.8 w palette information
 */
val VERSION_PAINTBRUSH_V_2_8_W = 2.toByte()

/**
 * PCX version Paintbrush v2.8 w/o palette information
 */
val VERSION_PAINTBRUSH_V_2_8_WO = 3.toByte()

/**
 * PCX version Paintbrush v3.0+
 */
val VERSION_PAINTBRUSH_V_3_0 = 5.toByte()

/**
 * PCX version Paintbrush/Windows
 */
val VERSION_PAINTBRUSH_WINDOWS = 4.toByte()

/**
 * Compute size of an PCX image.
 *
 * If the given file is not a PCX image file, `null` is return
 *
 * @param file Image PCX file
 * @return PCX image size OR `null` if given file not a valid PCX image file
 */
fun computePcxSize(file : File?) : Dimension?
{
    if (file == null || !file.exists() || file.isDirectory || !file.canRead())
    {
        return null
    }

    var dimension : Dimension? = null
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
 * Indicates if a file is a PCX image file
 *
 * @param file Tested file
 * @return `true` if the file is a PCX image file
 */
fun isPCX(file : File) = computePcxSize(file) != null

/**
 * Convert manufacturer code to its name
 *
 * @param manufacturer Manufacturer code
 * @return Manufacturer name
 */
fun manufacturerToString(manufacturer : Byte) =
    when (manufacturer)
    {
        MANUFACTURER_ZSOFT -> "ZSoft"
        else               -> "Manufacturer_${manufacturer and 0xFF}"
    }

/**
 * Convert a version code to its name
 *
 * @param version Version code
 * @return Version name
 */
fun versionToString(version : Byte) =
    when (version)
    {
        VERSION_PAINTBRUSH_V_2_5, VERSION_PAINTBRUSH_V_2_5_UNOFFICAL -> "Paintbrush v2.5"
        VERSION_PAINTBRUSH_V_2_8_W                                   -> "Paintbrush v2.8 w palette information"
        VERSION_PAINTBRUSH_V_2_8_WO                                  -> "Paintbrush v2.8 w/o palette information"
        VERSION_PAINTBRUSH_WINDOWS                                   -> "Paintbrush/Windows"
        VERSION_PAINTBRUSH_V_3_0                                     -> "Paintbrush v3.0+"
        else                                                         -> "More than Paintbrush v3.0+ (${version and 0xFF})"
    }

