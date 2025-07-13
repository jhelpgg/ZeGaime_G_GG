package fr.khelp.zegaime.utils.ui

import fr.khelp.zegaime.utils.math.centimeterToInch
import fr.khelp.zegaime.utils.math.inchToCentimeter
import fr.khelp.zegaime.utils.math.inchToMillimeter
import fr.khelp.zegaime.utils.math.inchToPica
import fr.khelp.zegaime.utils.math.inchToPoint
import fr.khelp.zegaime.utils.math.millimeterToInch
import fr.khelp.zegaime.utils.math.picaToInch
import fr.khelp.zegaime.utils.math.pointToInch

class Resolution(value : Int, resolutionUnit : ResolutionUnit)
{
    /** Number of pixels per inch  */
    private var pixelPerInch =
        when (resolutionUnit)
        {
            ResolutionUnit.PIXEL_PER_INCH       -> value.toDouble()
            ResolutionUnit.PIXEL_PER_CENTIMETER -> centimeterToInch(value.toDouble())
            else                                -> throw IllegalArgumentException(
                "resolutionUnit not managed : $resolutionUnit")
        }

    /**
     * Obtain resolution in given resolution unit
     *
     * @param resolutionUnit
     * Resolution unit
     * @return Resolution in given resolution unit
     */
    fun getResolution(resolutionUnit : ResolutionUnit) : Int =
        when (resolutionUnit)
        {
            ResolutionUnit.PIXEL_PER_INCH       -> this.pixelPerInch.toInt()
            ResolutionUnit.PIXEL_PER_CENTIMETER -> inchToCentimeter(this.pixelPerInch).toInt()
        }

    /**
     * Number of pixels inside a distance
     *
     * @param value
     * Distance value
     * @param measureUnit
     * Distance unit
     * @return Number of Pixels
     */
    fun numberOfPixels(value : Double, measureUnit : MeasureUnit) : Int
    {
        val valueConverted = when (measureUnit)
        {
            MeasureUnit.CENTIMETER -> centimeterToInch(value)
            MeasureUnit.INCH       -> value
            MeasureUnit.MILLIMETER -> millimeterToInch(value)
            MeasureUnit.PICA       -> picaToInch(value)
            MeasureUnit.POINT      -> pointToInch(value)
        }

        return (this.pixelPerInch * valueConverted).toInt()
    }

    /**
     * Convert a number of pixels to a measure unit
     *
     * @param pixels
     * Number of pixels
     * @param measureUnit
     * Measure to convert
     * @return Converted value
     */
    fun pixelsToMeasure(pixels : Double, measureUnit : MeasureUnit) : Double
    {
        val inch = pixels / this.pixelPerInch
        return when (measureUnit)
        {
            MeasureUnit.CENTIMETER -> inchToCentimeter(inch)
            MeasureUnit.INCH       -> inch
            MeasureUnit.MILLIMETER -> inchToMillimeter(inch)
            MeasureUnit.PICA       -> inchToPica(inch)
            MeasureUnit.POINT      -> inchToPoint(inch)
        }
    }
}