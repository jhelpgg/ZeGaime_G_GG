package fr.khelp.zegaime.utils.logs

import fr.khelp.zegaime.utils.extensions.day
import fr.khelp.zegaime.utils.extensions.hour
import fr.khelp.zegaime.utils.extensions.millisecond
import fr.khelp.zegaime.utils.extensions.minute
import fr.khelp.zegaime.utils.extensions.month
import fr.khelp.zegaime.utils.extensions.second
import fr.khelp.zegaime.utils.extensions.year
import fr.khelp.zegaime.utils.tasks.synchro.Mutex
import java.io.PrintStream
import java.text.DecimalFormat
import java.util.GregorianCalendar

/**
 * Function to print debug message
 */
private val printFunction : (LogInformation?) -> Unit =
    { logInformation ->

        if (logInformation != null)
        {
            printDate(logInformation.printStream)
            logInformation.printStream.print(logInformation.logLevel.header)
            printTrace(logInformation.stackTraceElement, logInformation.printStream, true)

            if (logInformation.message == null)
            {
                logInformation.printStream.println("null")
            }
            else
            {
                for (part in logInformation.message)
                {
                    printObject(part, logInformation.printStream)
                }

                logInformation.printStream.println()
            }

            printTrace(logInformation.throwable, logInformation.printStream)
        }
    }

/**
 * Header used for mark
 */
private const val MARK_HEADER = "*=> MARK\n"

/**
 * Format integer to show at least 2 digits
 */
private val NUMBER2 = DecimalFormat("00")

/**
 * Format integer to show at least 3 digits
 */
private val NUMBER3 = DecimalFormat("000")

/**
 * Current debug level
 */
private var LOG_LEVEL = LogLevel.VERBOSE

/**
 * Synchronization mutex
 */
private val mutex = Mutex()

/**
 * Private a message
 *
 * @param logLevel  Debug level
 * @param message     Message to print
 * @param throwable   Throwable to print trace
 * @param printStream Stream where print
 */
private fun print(logLevel : LogLevel, message : Array<out Any?>?, throwable : Throwable?, printStream : PrintStream)
{
    if (logLevel.order > LOG_LEVEL.order)
    {
        return
    }

    val logInformation = LogInformation(logLevel,
                                        Throwable().stackTrace[2],
                                        message, throwable,
                                        printStream)
    mutex { printFunction(logInformation) }
}

/**
 * Print current date
 *
 * @param printStream Stream where print
 */
internal fun printDate(printStream : PrintStream)
{
    val gregorianCalendar = GregorianCalendar()
    printStream.print(gregorianCalendar.year)
    printStream.print("-")
    printStream.print(NUMBER2.format(gregorianCalendar.month.toLong()))
    printStream.print("-")
    printStream.print(NUMBER2.format(gregorianCalendar.day.toLong()))
    printStream.print(" ")
    printStream.print(NUMBER2.format(gregorianCalendar.hour.toLong()))
    printStream.print(":")
    printStream.print(NUMBER2.format(gregorianCalendar.minute.toLong()))
    printStream.print(":")
    printStream.print(NUMBER2.format(gregorianCalendar.second.toLong()))
    printStream.print(":")
    printStream.print(NUMBER3.format(gregorianCalendar.millisecond.toLong()))
    printStream.print(" ")
}

/**
 * Print an object
 *
 * @param objectToPrint      Object to print
 * @param printStream Stream where print
 */
internal fun printObject(objectToPrint : Any?, printStream : PrintStream)
{
    if (objectToPrint == null)
    {
        printStream.print("null")
        return
    }

    val clazz = objectToPrint.javaClass

    if (!clazz.isArray)
    {
        if (objectToPrint is Iterable<*>)
        {
            printStream.print("{")
            var first = true

            for (element in (objectToPrint as Iterable<*>?)!!)
            {
                if (!first)
                {
                    printStream.print("; ")
                }

                printObject(element, printStream)
                first = false
            }

            printStream.print("}")
            return
        }

        if (objectToPrint is Map<*, *>)
        {
            printStream.print("{")
            var first = true

            for ((key, value) in objectToPrint)
            {
                if (!first)
                {
                    printStream.print(" | ")
                }

                printObject(key, printStream)
                printStream.print("=")
                printObject(value, printStream)
                first = false
            }

            printStream.print("}")
            return
        }

        printStream.print(objectToPrint)
        return
    }

    printStream.print("[")
    val componentType = clazz.componentType

    if (componentType.isPrimitive)
    {
        if (Boolean::class.javaPrimitiveType == componentType)
        {
            val array = objectToPrint as BooleanArray?
            val length = array!!.size

            if (length > 0)
            {
                printStream.print(array[0])

                for (i in 1 until length)
                {
                    printStream.print(", ")
                    printStream.print(array[i])
                }
            }
        }
        else if (Char::class.javaPrimitiveType == componentType)
        {
            val array = objectToPrint as CharArray?
            val length = array!!.size

            if (length > 0)
            {
                printStream.print(array[0])

                for (i in 1 until length)
                {
                    printStream.print(", ")
                    printStream.print(array[i])
                }
            }
        }
        else if (Byte::class.javaPrimitiveType == componentType)
        {
            val array = objectToPrint as ByteArray?
            val length = array!!.size

            if (length > 0)
            {
                printStream.print(array[0].toInt())

                for (i in 1 until length)
                {
                    printStream.print(", ")
                    printStream.print(array[i].toInt())
                }
            }
        }
        else if (Short::class.javaPrimitiveType == componentType)
        {
            val array = objectToPrint as ShortArray?
            val length = array!!.size

            if (length > 0)
            {
                printStream.print(array[0].toInt())

                for (i in 1 until length)
                {
                    printStream.print(", ")
                    printStream.print(array[i].toInt())
                }
            }
        }
        else if (Int::class.javaPrimitiveType == componentType)
        {
            val array = objectToPrint as IntArray?
            val length = array!!.size

            if (length > 0)
            {
                printStream.print(array[0])

                for (i in 1 until length)
                {
                    printStream.print(", ")
                    printStream.print(array[i])
                }
            }
        }
        else if (Long::class.javaPrimitiveType == componentType)
        {
            val array = objectToPrint as LongArray?
            val length = array!!.size

            if (length > 0)
            {
                printStream.print(array[0])

                for (i in 1 until length)
                {
                    printStream.print(", ")
                    printStream.print(array[i])
                }
            }
        }
        else if (Float::class.javaPrimitiveType == componentType)
        {
            val array = objectToPrint as FloatArray?
            val length = array!!.size

            if (length > 0)
            {
                printStream.print(array[0])

                for (i in 1 until length)
                {
                    printStream.print(", ")
                    printStream.print(array[i])
                }
            }
        }
        else if (Double::class.javaPrimitiveType == componentType)
        {
            val array = objectToPrint as DoubleArray?
            val length = array!!.size

            if (length > 0)
            {
                printStream.print(array[0])

                for (i in 1 until length)
                {
                    printStream.print(", ")
                    printStream.print(array[i])
                }
            }
        }
    }
    else
    {
        @Suppress("UNCHECKED_CAST")
        val array = objectToPrint as Array<Any>?
        val length = array!!.size

        if (length > 0)
        {
            printObject(array[0], printStream)

            for (i in 1 until length)
            {
                printStream.print(", ")
                printObject(array[i], printStream)
            }
        }
    }

    printStream.print("]")
}

/**
 * Print a stack trace element
 *
 * @param stackTraceElement Stack trace element
 * @param printStream       Stream where print
 * @param somethingFollow   Indicates if something will be print in same line or not
 */
internal fun printTrace(stackTraceElement : StackTraceElement, printStream : PrintStream, somethingFollow : Boolean)
{
    printStream.print(stackTraceElement.className)
    printStream.print(".")
    printStream.print(stackTraceElement.methodName)
    printStream.print(" at ")
    printStream.print(stackTraceElement.lineNumber)

    if (somethingFollow)
    {
        printStream.print(": ")
    }
    else
    {
        printStream.println()
    }
}

/**
 * Print a complete trace
 *
 * @param throwable   Throwable to print its trace
 * @param printStream Stream where print
 */
internal fun printTrace(throwable : Throwable?, printStream : PrintStream)
{
    var throwableLocal = throwable

    while (throwableLocal != null)
    {
        printStream.println(throwableLocal.toString())

        for (stackTraceElement in throwableLocal.stackTrace)
        {
            printStream.print("   ")
            printTrace(stackTraceElement, printStream, false)
        }

        throwableLocal = throwableLocal.cause

        if (throwableLocal != null)
        {
            printStream.println("Caused by:")
        }
    }
}

/**
 * Change debug level
 *
 * @param logLevel New debug level
 */
fun setLevel(logLevel : LogLevel)
{
    LOG_LEVEL = logLevel
}

/**
 * Current debug level
 *
 * @return Current debug level
 */
fun getLevel() : LogLevel = LOG_LEVEL

/**
 * Print to do message
 *
 * @param todoMessage Message to print
 */
fun todo(vararg todoMessage : Any?)
{
    val message = arrayOfNulls<Any>(todoMessage.size + 2)
    message[0] = "-TODO- "
    System.arraycopy(todoMessage, 0, message, 1, todoMessage.size)
    message[message.size - 1] = " -TODO-"
    print(LogLevel.INFORMATION, message, null, System.out)
}

/**
 * Print message follow by the call stack trace
 *
 * @param message Message to print
 */
fun trace(vararg message : Any?)
{
    print(LogLevel.DEBUG, message, Throwable(), System.out)
}

/**
 * Print verbose message
 *
 * @param message Message to print
 */
fun verbose(vararg message : Any?)
{
    print(LogLevel.VERBOSE, message, null, System.out)
}

/**
 * Print warning message
 *
 * @param message Message to print
 */
fun warning(vararg message : Any?)
{
    print(LogLevel.WARNING, message, null, System.err)
}

/**
 * Print debug message
 *
 * @param message Message to print
 */
fun debug(vararg message : Any?)
{
    print(LogLevel.DEBUG, message, null, System.out)
}

/**
 * Print error message
 *
 * @param message Message to print
 */
fun error(vararg message : Any?)
{
    print(LogLevel.ERROR, message, null, System.err)
}

/**
 * Print exception message
 *
 * @param throwable Error/exception trace
 * @param message   Message to print
 */
fun exception(throwable : Throwable, vararg message : Any?)
{
    print(LogLevel.ERROR, message, throwable, System.err)
}

/**
 * Print information message
 *
 * @param message Message to print
 */
fun information(vararg message : Any?)
{
    print(LogLevel.INFORMATION, message, null, System.out)
}

/**
 * Print a mark
 *
 * @param mark Mark to print
 */
fun mark(mark : String)
{
    val size = mark.length + 12
    val message = StringBuilder(MARK_HEADER.length + 3 * size + 2)

    message.append(MARK_HEADER)

    for (i in 0 until size)
    {
        message.append('*')
    }

    message.append("\n***   ")
    message.append(mark)
    message.append("   ***\n")

    for (i in 0 until size)
    {
        message.append('*')
    }

    print(LogLevel.INFORMATION, arrayOf(message.toString()), null, System.out)
}