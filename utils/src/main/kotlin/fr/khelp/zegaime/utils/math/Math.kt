package fr.khelp.zegaime.utils.math

import java.awt.Rectangle
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/**
 * Double precision, the "zero"
 */
val EPSILON = maxOf(Double.MIN_VALUE,
                    abs(Math.E - exp(1.0)),
                    abs(Math.PI - acos(-1.0)))

/**
 * Float precision, the "zero"
 */
val EPSILON_FLOAT = maxOf(Float.MIN_VALUE,
                          abs(Math.E.toFloat() - exp(1.0).toFloat()),
                          abs(Math.PI.toFloat() - acos(-1.0).toFloat()))

const val CENTIMETER_IN_PICA = 6.0 / 2.54

/** One centimeter in point  */
const val CENTIMETER_IN_POINT = 72.0 / 2.54

/** One grade in degree  */
const val GRADE_IN_DEGREE = 0.9

/** One grade in radian  */
var GRADE_IN_RADIAN = PI / 200.0

/** One inch in centimeter  */
const val INCH_IN_CENTIMETER = 2.54

/** One inch in millimeter  */
const val INCH_IN_MILLIMETER = 25.4

/** One inch in pica  */
const val INCH_IN_PICA = 6.0

/** One inch in point  */
const val INCH_IN_POINT = 72.0

/** One millimeter in point  */
const val MILLIMETER_IN_POINT = 72.0 / 25.4

/** One pica in millimeter  */
const val PICA_IN_MILLIMETER = 25.4 / 6.0

/** One pica in point  */
const val PICA_IN_POINT = 12.0

/**
 * Indicates if given value can be considered as zero
 *
 * @param value Value to test
 * @return `true` if given value can be considered as zero
 */
fun isNul(value : Double) = abs(value) <= EPSILON

/**
 * Indicates if given value can be considered as zero
 *
 * @param value Value to test
 * @return `true` if given value can be considered as zero
 */
fun isNul(value : Float) = abs(value) <= EPSILON_FLOAT

/**
 * Compare two floats.
 *
 * It returns:
 * * **< 0** if value1 < value2
 * * **0** if value1 ~ value2
 * * **> 0** if value1 > value2
 * @param value1 First value
 * @param value2 Second value
 * @return Comparison result
 */
fun compare(value1 : Float, value2 : Float) : Int
{
    val diff = value1 - value2

    return when
    {
        isNul(diff) -> 0
        diff > 0f   -> 1
        else        -> -1
    }
}

/**
 * Compare two doubles.
 *
 * It returns:
 * * **< 0** if value1 < value2
 * * **0** if value1 ~ value2
 * * **> 0** if value1 > value2
 * @param value1 First value
 * @param value2 Second value
 * @return Comparison result
 */
fun compare(value1 : Double, value2 : Double) : Int
{
    val diff = value1 - value2

    return when
    {
        isNul(diff) -> 0
        diff > 0.0  -> 1
        else        -> -1
    }
}

/**
 * Indicates if two given real can be considered as equals
 *
 * @param value1 First real
 * @param value2 Second real
 * @return `true` if two given real can be considered as equals
 */
fun equals(value1 : Double, value2 : Double) = isNul(value1 - value2)

/**
 * Indicates if two given real can be considered as equals
 *
 * @param value1 First real
 * @param value2 Second real
 * @return `true` if two given real can be considered as equals
 */
fun equals(value1 : Float, value2 : Float) = isNul(value1 - value2)

/**
 * Compute the minimum of two characters
 * @param char1 First character
 * @param char2 Second character
 * @return The minimum
 */
fun min(char1 : Char, char2 : Char) : Char =
    if (char1 <= char2)
    {
        char1
    }
    else
    {
        char2
    }

/**
 * Compute the maximum of two characters
 * @param char1 First character
 * @param char2 Second character
 * @return The maximum
 */
fun max(char1 : Char, char2 : Char) : Char =
    if (char1 >= char2)
    {
        char1
    }
    else
    {
        char2
    }

/**
 * Compute the sign of an integer
 * @param int Integer to get the sign
 * @return The sign
 */
fun sign(int : Int) : Int =
    when
    {
        int < 0  -> -1
        int == 0 -> 0
        else     -> 1
    }

/**
 * Compute the sign of a long
 * @param long Long to get the sign
 * @return The sign
 */
fun sign(long : Long) : Int =
    when
    {
        long < 0L  -> -1
        long == 0L -> 0
        else       -> 1
    }

/**
 * Compute the sign of a float
 * @param float Float to get the sign
 * @return The sign
 */
fun sign(float : Float) : Int =
    when
    {
        isNul(float) -> 0
        float < 0f   -> -1
        else         -> 1
    }

/**
 * Compute the sign of a double
 * @param double Double to get the sign
 * @return The sign
 */
fun sign(double : Double) : Int =
    when
    {
        isNul(double) -> 0
        double < 0.0  -> -1
        else          -> 1
    }

/**
 * Compute the square of a float
 * @param number Float to square
 * @return The square
 */
fun square(number : Float) : Float = number * number

/**
 * Compute the square of a double
 * @param number Double to square
 * @return The square
 */
fun square(number : Double) : Double = number * number

/**
 * Compute the intersected area of two rectangles
 * @param rectangle1 First rectangle
 * @param rectangle2 Second rectangle
 * @return The intersected area
 */
fun computeIntersectedArea(rectangle1 : Rectangle, rectangle2 : Rectangle) : Int
{
    val xMin1 = rectangle1.x
    val xMax1 = rectangle1.x + rectangle1.width
    val yMin1 = rectangle1.y
    val yMax1 = rectangle1.y + rectangle1.height
    val xMin2 = rectangle2.x
    val xMax2 = rectangle2.x + rectangle2.width
    val yMin2 = rectangle2.y
    val yMax2 = rectangle2.y + rectangle2.height

    if (xMin1 > xMax2 || yMin1 > yMax2 || xMin2 > xMax1 || yMin2 > yMax1)
    {
        return 0
    }

    val xMin = max(xMin1, xMin2)
    val xMax = min(xMax1, xMax2)

    if (xMin >= xMax)
    {
        return 0
    }

    val yMin = max(yMin1, yMin2)
    val yMax = min(yMax1, yMax2)

    return if (yMin >= yMax)
    {
        0
    }
    else (xMax - xMin) * (yMax - yMin)
}

/**
 * Convert centimeter to inch
 *
 * @param centimeter
 * Centimeter to convert
 * @return Converted inch
 */
fun centimeterToInch(centimeter : Double) : Double
{
    return centimeter / INCH_IN_CENTIMETER
}

/**
 * Convert centimeter to millimeter
 *
 * @param centimeter
 * Centimeter to convert
 * @return Converted millimeter
 */
fun centimeterToMillimeter(centimeter : Double) : Double
{
    return centimeter * 10.0
}

/**
 * Convert centimeter to pica
 *
 * @param centimeter
 * Centimeter to convert
 * @return Converted pica
 */
fun centimeterToPica(centimeter : Double) : Double
{
    return centimeter * CENTIMETER_IN_PICA
}

/**
 * Convert centimeter to point
 *
 * @param centimeter
 * Centimeter to convert
 * @return Converted point
 */
fun centimeterToPoint(centimeter : Double) : Double
{
    return centimeter * CENTIMETER_IN_POINT
}

/**
 * Convert degree to grade
 *
 * @param degree
 * Degree to convert
 * @return Converted grade
 */
fun degreeToGrade(degree : Double) : Double
{
    return degree * GRADE_IN_DEGREE
}

/**
 * Convert degree to radian
 *
 * @param degree
 * Degree to convert
 * @return Converted radian
 */
fun degreeToRadian(degree : Double) : Double = degree * PI / 180.0

/**
 * Convert grade to degree
 *
 * @param grade
 * Grade to convert
 * @return Converted degree
 */
fun gradeToDegree(grade : Double) : Double = grade / GRADE_IN_DEGREE

/**
 * Convert grade to radian
 *
 * @param grade
 * Grade to convert
 * @return Converted radian
 */
fun gradeToRadian(grade : Double) : Double = grade * Math.PI / 200.0

/**
 * Convert inch to centimeter
 *
 * @param inch
 * Inch to convert
 * @return Converted centimeter
 */
fun inchToCentimeter(inch : Double) : Double = inch * INCH_IN_CENTIMETER

/**
 * Convert inch to millimeter
 *
 * @param inch
 * Inch to convert
 * @return Converted millimeter
 */
fun inchToMillimeter(inch : Double) : Double = inch * INCH_IN_MILLIMETER

/**
 * Convert inch to pica
 *
 * @param inch
 * Inch to convert
 * @return Converted pica
 */
fun inchToPica(inch : Double) : Double = inch * INCH_IN_PICA

/**
 * Convert inch to point
 *
 * @param inch
 * Inch to convert
 * @return Converted point
 */
fun inchToPoint(inch : Double) : Double = inch * INCH_IN_POINT

/**
 * Convert millimeter to centimeter
 *
 * @param millimeter
 * Millimeter to convert
 * @return Converted centimeter
 */
fun millimeterToCentimeter(millimeter : Double) : Double = millimeter * 0.1

/**
 * Convert millimeter to inch
 *
 * @param millimeter
 * Millimeter to convert
 * @return Converted inch
 */
fun millimeterToInch(millimeter : Double) : Double = millimeter / INCH_IN_MILLIMETER

/**
 * Convert millimeter to pica
 *
 * @param millimeter
 * Millimeter to convert
 * @return Converted pica
 */
fun millimeterToPica(millimeter : Double) : Double = millimeter / PICA_IN_MILLIMETER

/**
 * Convert millimeter to point
 *
 * @param millimeter
 * Millimeter to convert
 * @return Converted point
 */
fun millimeterToPoint(millimeter : Double) : Double = millimeter * MILLIMETER_IN_POINT

/**
 * Convert pica to centimeter
 *
 * @param pica
 * Pica to convert
 * @return Converted centimeter
 */
fun picaToCentimeter(pica : Double) : Double = pica / CENTIMETER_IN_PICA

/**
 * Convert pica to inch
 *
 * @param pica
 * Pica to convert
 * @return Converted inch
 */
fun picaToInch(pica : Double) : Double = pica / INCH_IN_PICA

/**
 * Convert pica to millimeter
 *
 * @param pica
 * Pica to convert
 * @return Converted millimeter
 */
fun picaToMillimeter(pica : Double) : Double = pica * PICA_IN_MILLIMETER

/**
 * Convert pica to point
 *
 * @param pica
 * Pica to convert
 * @return Converted point
 */
fun picaToPoint(pica : Double) : Double = pica * PICA_IN_POINT

/**
 * Convert point to centimeter
 *
 * @param point
 * Point to convert
 * @return Converted centimeter
 */
fun pointToCentimeter(point : Double) : Double = point / CENTIMETER_IN_POINT

/**
 * Convert point to inch
 *
 * @param point
 * Point to convert
 * @return Converted inch
 */
fun pointToInch(point : Double) : Double = point / INCH_IN_POINT

/**
 * Convert point to millimeter
 *
 * @param point
 * Point to convert
 * @return Converted millimeter
 */
fun pointToMillimeter(point : Double) : Double = point / MILLIMETER_IN_POINT

/**
 * Convert point to point
 *
 * @param point
 * Point to convert
 * @return Converted point
 */
fun pointToPica(point : Double) : Double = point / PICA_IN_POINT

/**
 * Convert radian to degree
 *
 * @param radian
 * Radian to convert
 * @return Converted degree
 */
fun radianToDegree(radian : Double) : Double = radian * 180.0 / Math.PI
fun radianToDegree(radian : Float) : Float = radian * 180.0f / Math.PI.toFloat()

/**
 * Convert radian to grade
 *
 * @param radian
 * Radian to convert
 * @return Converted grade
 */
fun radianToGrade(radian : Double) : Double = radian * 200.0 / Math.PI

/**
 * Calculates the Gaussian value for the given 3x3 matrix of integer values.
 *
 * @return The calculated Gaussian value.
 */
fun gauss(c00 : Int, c10 : Int, c20 : Int,
          c01 : Int, c11 : Int, c21 : Int,
          c02 : Int, c12 : Int, c22 : Int) =
    (c00 + (c10 shl 1) + c20 +
     (c01 shl 1) + (c11 shl 2) + (c21 shl 1) +
     c02 + (c12 shl 1) + c22) shr 4

/**
 * Calculates the area of a triangle given the coordinates of its three vertices.
 *
 * @param x1 the x-coordinate of vertex 1
 * @param y1 the y-coordinate of vertex 1
 * @param x2 the x-coordinate of vertex 2
 * @param y2 the y-coordinate of vertex 2
 * @param x3 the x-coordinate of vertex 3
 * @param y3 the y-coordinate of vertex 3
 * @return the area of the triangle
 */
fun area(x1 : Double, y1 : Double, x2 : Double, y2 : Double, x3 : Double, y3 : Double) : Double =
    abs(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0

/**
 * Calculates the area of a triangle given the coordinates of its three vertices.
 *
 * @param x1 the x-coordinate of vertex 1
 * @param y1 the y-coordinate of vertex 1
 * @param x2 the x-coordinate of vertex 2
 * @param y2 the y-coordinate of vertex 2
 * @param x3 the x-coordinate of vertex 3
 * @param y3 the y-coordinate of vertex 3
 * @return the area of the triangle
 */
fun area(x1 : Float, y1 : Float, x2 : Float, y2 : Float, x3 : Float, y3 : Float) : Float =
    abs(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2f

/**
 * Determines whether three given points are in a trigonometric order.
 * That is to say if follow the path 1, 2, then 3, it wil turn in the same way as the trigonometric circle (counter clock wize)
 *
 * @param x1 the x-coordinate of the first point
 * @param y1 the y-coordinate of the first point
 * @param x2 the x-coordinate of the second point
 * @param y2 the y-coordinate of the second point
 * @param x3 the x-coordinate of the third point
 * @param y3 the y-coordinate of the third point
 * @return true if the points are in a trigonometric order, false otherwise
 */
fun isTrigonometricWay(x1 : Float, y1 : Float, x2 : Float, y2 : Float, x3 : Float, y3 : Float) : Boolean
{
    val vx21 = x1 - x2
    val vy21 = y1 - y2
    val vx23 = x3 - x2
    val vy23 = y3 - y2
    /*
        | vx21 vx23 |
        | vy21 vy23 | = (vx21 * vy23) - (vy21*vx23)
     */
    return (vx21 * vy23) - (vy21 * vx23) <= 0f
}
/**
 * 2! / (index! * (2 - index)!)
 * index=0 => 2 / (1 * 2) = 1
 * index=1 => 2 / (1 * 1) = 2
 * index=2 => 2 / (2 * 1) = 1
 */
private val combinationDouble2 = doubleArrayOf(1.0, 2.0, 1.0)


/**
 * 3! / (index! * (3 - index)!)
 * index=0 => 6 / (1 * 6) = 1
 * index=1 => 6 / (1 * 2) = 3
 * index=2 => 6 / (2 * 1) = 3
 * index=3 => 6 / (6 * 1) = 1
 */
private val combinationDouble3 = doubleArrayOf(1.0, 3.0, 3.0, 1.0)

private fun BernoulliDouble2(index : Int, t : Double) =
    combinationDouble2[index] * t.pow(index.toDouble()) * (1f - t).pow((2 - index).toDouble())

private fun BernoulliDouble3(index : Int, t : Double) =
    combinationDouble3[index] * t.pow(index.toDouble()) * (1f - t).pow((3 - index).toDouble())

/**
 * Compute cubic invoke at a given time
 *
 * @param cp Current value
 * @param p1 First control value
 * @param p2 Second control value
 * @param p3 End value
 * @param t  Interpolation time
 * @return Interpolated value
 */
fun cubic(cp : Double, p1 : Double, p2 : Double, p3 : Double, t : Double) =
    BernoulliDouble3(0, t) * cp +
    BernoulliDouble3(1, t) * p1 +
    BernoulliDouble3(2, t) * p2 +
    BernoulliDouble3(3, t) * p3

fun quadratic(cp : Double, p1 : Double, p2 : Double, t : Double) =
    BernoulliDouble2(0, t) * cp +
    BernoulliDouble2(1, t) * p1 +
    BernoulliDouble2(2, t) * p2
