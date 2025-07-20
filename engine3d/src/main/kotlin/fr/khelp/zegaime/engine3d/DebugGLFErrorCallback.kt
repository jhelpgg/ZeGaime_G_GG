package fr.khelp.zegaime.engine3d

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWErrorCallbackI
import org.lwjgl.system.APIUtil

/**
 * Map of error codes and their name
 */
private val ERROR_CODES = APIUtil.apiClassTokens(
    { _, value -> value in 0x10001..0x1ffff },
    null,
    GLFW::class.java)

object DebugGLFErrorCallback : GLFWErrorCallbackI
{
    override fun invoke(error : Int, description : Long)
    {
        val errorType = ERROR_CODES[error]
        val message = GLFWErrorCallback.getDescription(description)
        fr.khelp.zegaime.utils.logs.error("GLF -", errorType, "- ", message)
    }
}