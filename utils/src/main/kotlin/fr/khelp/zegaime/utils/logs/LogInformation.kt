package fr.khelp.zegaime.utils.logs

import java.io.PrintStream
import java.util.Arrays

/**
 * Information for printing a log message
 * @param logLevel Log level
 * @param stackTraceElement Stack trace element where the log is called
 * @param message Message to print
 * @param throwable Throwable to print its stack trace
 * @param printStream Stream where print the log
 */
data class LogInformation(val logLevel : LogLevel = LogLevel.VERBOSE,
                          val stackTraceElement : StackTraceElement,
                          val message : Array<out Any?>? = null,
                          val throwable : Throwable? = null,
                          val printStream : PrintStream = System.out)
{
    /**
     * Indicates in other object equals to this debug information
     */
    override fun equals(other : Any?) : Boolean
    {
        if (this === other) return true
        if (other !is LogInformation) return false

        if (this.logLevel != other.logLevel) return false

        if (this.stackTraceElement != other.stackTraceElement) return false

        if (!Arrays.equals(this.message, other.message)) return false

        if (this.throwable != other.throwable) return false

        if (this.printStream != other.printStream) return false

        return true
    }

    /**
     * Hash code
     */
    override fun hashCode() : Int
    {
        var result = this.logLevel.hashCode()
        result = 31 * result + this.stackTraceElement.hashCode()
        result = 31 * result + (this.message?.let { it.contentHashCode() } ?: 0)
        result = 31 * result + (this.throwable?.hashCode() ?: 0)
        result = 31 * result + this.printStream.hashCode()
        return result
    }
}