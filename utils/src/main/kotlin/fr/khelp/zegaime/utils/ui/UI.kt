package fr.khelp.zegaime.utils.ui

import fr.khelp.zegaime.utils.math.computeIntersectedArea
import java.awt.Component
import java.awt.Container
import java.awt.Cursor
import java.awt.Dimension
import java.awt.GraphicsConfiguration
import java.awt.GraphicsDevice
import java.awt.GraphicsEnvironment
import java.awt.Point
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.awt.Window
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.JFrame
import javax.swing.KeyStroke
import javax.swing.UIManager
import kotlin.math.max
import kotlin.math.min

/**
 * Special character for delete
 */
const val CHARACTER_DELETE = '\b'

/**
 * Special character for escape
 */
const val CHARACTER_ESCAPE = (0x1B).toChar()

/**
 * Character unicode for the smiley :)
 */
val SMILEY_HAPPY = 0x263A.toChar()

/**
 * Character unicode for the smiley :(
 */
val SMILEY_SAD = 0x2639.toChar()

/**
 * Current toolkit
 */
val TOOLKIT : Toolkit = Toolkit.getDefaultToolkit()

/**
 * Current graphics environment
 */
val GRAPHICS_ENVIRONMENT : GraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment()

/**
 * Current screen resolution
 */
val SCREEN_RESOLUTION : Resolution = Resolution(TOOLKIT.screenResolution, ResolutionUnit.PIXEL_PER_INCH)

/**
 * Identity transform
 */
val AFFINE_TRANSFORM = AffineTransform()

/**
 * Flatness to use
 */
val FLATNESS = 0.01

/**
 * Font render context
 */
val FONT_RENDER_CONTEXT = FontRenderContext(AFFINE_TRANSFORM, true, true)

/**
 * List of graphics devices
 */
val GRAPHICS_DEVICES : Array<GraphicsDevice> = GRAPHICS_ENVIRONMENT.screenDevices

/**
 * Robot for simulate keyboard and mouse events
 */
private val ROBOT : Robot? =
    try
    {
        Robot()
    }
    catch (_ : Exception)
    {
        null
    }

/**
 * Empty image
 */
private lateinit var EMPTY_IMAGE : BufferedImage
private val emptyImageNotCreated = AtomicBoolean(true)

/**
 * Invisible cursor
 */
val INVISIBLE_CURSOR : Cursor by lazy {
    val dimension = TOOLKIT.getBestCursorSize(32, 32)

    if ((dimension == null) || (dimension.width < 1) || (dimension.height < 1))
    {
        Cursor.getDefaultCursor()
    }
    else
    {
        val bufferedImage = BufferedImage(dimension.width, dimension.height,
                                          BufferedImage.TYPE_INT_ARGB)
        bufferedImage.flush()
        Toolkit.getDefaultToolkit()
            .createCustomCursor(bufferedImage,
                                Point(dimension.width / 2, dimension.height / 2),
                                "Invisible")
    }
}

/**
 * Center a window on its screen
 *
 * @param window Widow to center
 */
fun centerOnScreen(window : Window)
{
    var dimension = window.getSize()
    var screen = computeScreenRectangle(window)
    window.setLocation(screen.x + ((screen.width - dimension.width) / 2),
                       screen.y + ((screen.height - dimension.height) / 2))
}

/**
 * Compute the rectangle of the screen where is a window
 *
 * @param window Window we looking for its screen
 * @return Screen's rectangle
 */
fun computeScreenRectangle(window : Window) : Rectangle
{
    val windowBounds = window.getBounds()

    var graphicsConfiguration = GRAPHICS_DEVICES[0].defaultConfiguration
    var screenBounds = graphicsConfiguration.bounds
    var areaMax = computeIntersectedArea(windowBounds, screenBounds)

    var totalWidth = screenBounds.x + screenBounds.width
    var totalHeight = screenBounds.y + screenBounds.height

    var bounds : Rectangle
    var area : Int
    var cg : GraphicsConfiguration

    for (i in 1 until GRAPHICS_DEVICES.size)
    {
        cg = GRAPHICS_DEVICES[i].getDefaultConfiguration()
        bounds = cg.bounds
        area = computeIntersectedArea(windowBounds, bounds)

        totalWidth = max(totalWidth, bounds.x + bounds.width)
        totalHeight = max(totalHeight, bounds.y + bounds.height)

        if (area > areaMax)
        {
            graphicsConfiguration = cg
            screenBounds = bounds
            areaMax = area
        }
    }

    val margin = TOOLKIT.getScreenInsets(graphicsConfiguration)

    val screenRectangle = Rectangle(screenBounds)
    screenRectangle.x = margin.left
    screenRectangle.y = margin.top
    screenRectangle.width = totalWidth - margin.left - margin.right
    screenRectangle.height = totalHeight - margin.top - margin.bottom

    return screenRectangle
}

fun screenRectangle(screenIndex : Int) : Rectangle = GRAPHICS_DEVICES[screenIndex].defaultConfiguration.bounds

/**
 * Change a window of screen
 *
 * @param window      Widow to translate
 * @param screenIndex Destination screen
 */
fun changeScreen(window : Window, screenIndex : Int)
{
    checkScreenIndex(screenIndex)

    val sourceScreen = computeScreenRectangle(window)
    val x = window.x - sourceScreen.x
    val y = window.y - sourceScreen.y

    val graphicsConfiguration = GRAPHICS_DEVICES[screenIndex].defaultConfiguration
    val destinationScreen = graphicsConfiguration.bounds
    val insets = TOOLKIT.getScreenInsets(graphicsConfiguration)

    window.setLocation(x + destinationScreen.x + insets.left, //
                       y + destinationScreen.y + insets.top)
}

/**
 * Check if a screen index is valid
 *
 * @param screenIndex Screen index to check
 */
private fun checkScreenIndex(screenIndex : Int)
{
    if ((screenIndex < 0) || (screenIndex >= GRAPHICS_DEVICES.size))
    {
        throw IllegalArgumentException(
            "You have ${GRAPHICS_DEVICES.size} screens so the screen index must be in [0, ${GRAPHICS_DEVICES.size}[ not $screenIndex")
    }
}

/**
 * Compute the maximum dimension of a component
 *
 * @param component Component to compute it's maximum size
 * @return Maximum size
 */
fun computeMaximumDimension(component : Component) : Dimension
{
    if (component is Container)
    {
        val layoutManager = component.layout
        var dimension : Dimension? = null

        if (layoutManager != null)
        {
            dimension = layoutManager.preferredLayoutSize(component)
        }

        if (component.componentCount < 1 || dimension == null)
        {
            dimension = component.getMaximumSize()

            if (dimension == null)
            {
                return Dimension(128, 128)
            }

            return Dimension(max(128, dimension.width), max(128, dimension.height))
        }

        return dimension
    }

    return component.maximumSize
}

/**
 * Compute the minimum dimension of a component
 *
 * @param component Component to compute it's minimum size
 * @return Minimum size
 */
fun computeMinimumDimension(component : Component) : Dimension
{
    if (component is Container)
    {
        val layoutManager = component.layout
        var dimension : Dimension? = null

        if (layoutManager != null)
        {
            dimension = layoutManager.preferredLayoutSize(component)
        }

        if (component.componentCount < 1 || dimension == null)
        {
            dimension = component.getMinimumSize()

            if (dimension == null)
            {
                return Dimension(1, 1)
            }

            return Dimension(max(1, dimension.width), max(1, dimension.height))
        }

        return dimension
    }

    return component.minimumSize
}

/**
 * Compute the preferred dimension of a component
 *
 * @param component Component to compute it's preferred size
 * @return Preferred size
 */
fun computePreferredDimension(component : Component) : Dimension
{
    if (component is Container)
    {
        val layoutManager = component.layout
        var dimension : Dimension? = null

        if (layoutManager != null)
        {
            dimension = layoutManager.preferredLayoutSize(component)
        }

        if (component.componentCount < 1 || dimension == null)
        {
            dimension = component.getPreferredSize()

            if (dimension == null)
            {
                return Dimension(16, 16)
            }

            return Dimension(max(16, dimension.width), max(16, dimension.height))
        }

        return dimension
    }

    return component.preferredSize
}

/**
 * Create key-stroke short-cut for given key combination
 *
 * @param character Character
 * @param control   Indicates if control should be down (default `false`)
 * @param alt       Indicates if alt should be down (default `false`)
 * @param shift     Indicates if shift should be down (default `false`)
 * @param altGraph  Indicates if alt graph should be down (default `false`)
 * @param meta      Indicates if meta should be down (default `false`)
 * @return Created key-stroke short-cut for given key combination
 */
fun createKeyStroke(character : Char,
                    control : Boolean = false,
                    alt : Boolean = false,
                    shift : Boolean = false,
                    altGraph : Boolean = false,
                    meta : Boolean = false) : KeyStroke
{
    var modifiers = 0

    if (control)
    {
        modifiers = modifiers or InputEvent.CTRL_DOWN_MASK
    }

    if (alt)
    {
        modifiers = modifiers or InputEvent.ALT_DOWN_MASK
    }

    if (shift)
    {
        modifiers = modifiers or InputEvent.SHIFT_DOWN_MASK
    }

    if (altGraph)
    {
        modifiers = modifiers or InputEvent.ALT_GRAPH_DOWN_MASK
    }

    if (meta)
    {
        modifiers = modifiers or InputEvent.META_DOWN_MASK
    }

    return KeyStroke.getKeyStroke(charToKeyCodeForShortCut(character), modifiers)
}

/**
 * Compute the key code to use for short-cut that use a given character.
 *
 * It is possible to use [CHARACTER_DELETE] or [CHARACTER_ESCAPE] character if you want build short-cut
 * for respectively delete key, escape key
 *
 * @param character Character to compute the key code to use
 * @return Computed key code
 */
fun charToKeyCodeForShortCut(character : Char) : Int =
    when (character)
    {
        '0'              -> KeyEvent.VK_NUMPAD0
        '1'              -> KeyEvent.VK_NUMPAD1
        '2'              -> KeyEvent.VK_NUMPAD2
        '3'              -> KeyEvent.VK_NUMPAD3
        '4'              -> KeyEvent.VK_NUMPAD4
        '5'              -> KeyEvent.VK_NUMPAD5
        '6'              -> KeyEvent.VK_NUMPAD6
        '7'              -> KeyEvent.VK_NUMPAD7
        '8'              -> KeyEvent.VK_NUMPAD8
        '9'              -> KeyEvent.VK_NUMPAD9
        '+'              -> KeyEvent.VK_ADD
        '-'              -> KeyEvent.VK_SUBTRACT
        '*'              -> KeyEvent.VK_MULTIPLY
        '/'              -> KeyEvent.VK_DIVIDE
        '.'              -> KeyEvent.VK_PERIOD
        CHARACTER_ESCAPE -> KeyEvent.VK_ESCAPE
        CHARACTER_DELETE -> KeyEvent.VK_BACK_SPACE
        '\n'             -> KeyEvent.VK_ENTER
        '\t'             -> KeyEvent.VK_TAB
        else             -> KeyEvent.getExtendedKeyCodeForChar(character.code)
    }

/**
 * Obtain frame parent of a container
 *
 * @param container Container to get its parent
 * @return Parent frame
 */
fun getFrameParent(container : Container) : JFrame?
{
    var containerTest : Container? = container

    while (containerTest != null)
    {
        if (containerTest is JFrame)
        {
            return containerTest
        }

        containerTest = containerTest.parent
    }

    return null
}

/**
 * Give the relative position of a component for an other one
 *
 * @param component Component to search its position
 * @param parent    A component ancestor
 * @return Relative position of {@code null} if parent is not an ancestor of component
 */
fun getLocationOn(component : Component, parent : Component) : Point?
{
    val point = Point()
    var componentTest : Component? = component

    while ((componentTest != null) && (componentTest != parent))
    {
        point.translate(componentTest.x, componentTest.y)
        componentTest = componentTest.parent
    }

    if (componentTest == null)
    {
        return null
    }

    return point
}

/**
 * Give bounds of a screen
 *
 * @param screen Screen index
 * @return Screen bounds
 */
fun getScreenBounds(screen : Int) : Rectangle
{
    val bounds = GRAPHICS_DEVICES[screen].defaultConfiguration.bounds
    val insets = TOOLKIT.getScreenInsets(GRAPHICS_DEVICES[screen].defaultConfiguration)

    if (bounds.x < insets.left)
    {
        insets.left -= bounds.x
    }

    bounds.x += insets.left
    bounds.y += insets.top
    bounds.width -= insets.left + insets.right
    bounds.height -= insets.top + insets.bottom

    return bounds
}

/**
 * Screen identifier
 *
 * @param screenIndex Screen index
 * @return Screen identifier
 */
fun getScreenIdentifier(screenIndex : Int) : String
{
    checkScreenIndex(screenIndex)

    val stringBuffer = StringBuilder()
    stringBuffer.append(System.getProperty("java.vendor"))
    stringBuffer.append(" | ")
    stringBuffer.append(GRAPHICS_DEVICES[screenIndex].getIDstring())
    stringBuffer.append(" | ")
    stringBuffer.append(screenIndex)
    return stringBuffer.toString()
}

/**
 * Initialize GUI with operating system skin, call it before create any frame
 */
fun initializeGUI()
{
    try
    {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    }
    catch (_ : Exception)
    {
    }
}

/**
 * Place the mouse over the middle of a component
 *
 * @param component Component mouse go over
 */
fun locateMouseOver(component : Component)
{
    if ((!component.isValid) || (!component.isVisible))
    {
        return
    }

    val dimension = component.size
    locateMouseOver(component, dimension.width / 2, dimension.height / 2)
}

/**
 * Place the mouse over a component
 *
 * @param component Component mouse go over
 * @param x         X relative to component up-left corner
 * @param y         Y relative to component up-left corner
 */
fun locateMouseOver(component : Component, x : Int, y : Int)
{
    if ((!component.isValid) || (!component.isVisible))
    {
        return
    }

    val position = component.locationOnScreen
    locateMouseAt(position.x + x, position.y + y)
}

/**
 * Place the mouse on a location on the screen
 *
 * @param x X screen position
 * @param y Y screen position
 */
fun locateMouseAt(x : Int, y : Int)
{
    ROBOT?.mouseMove(x, y)
}

/**
 * Number of screen
 *
 * @return Number of screen
 */
fun numberOfScreen() : Int
{
    return GRAPHICS_DEVICES.size
}

/**
 * Obtain index of the screen where is the window
 *
 * @param window Considered window
 * @return Screen index
 */
fun obtainScreenIndex(window : Window) : Int
{
    val windowBounds = window.bounds
    var graphicsConfiguration = GRAPHICS_DEVICES[0].defaultConfiguration
    var bounds = graphicsConfiguration.bounds
    var areaMax = computeIntersectedArea(windowBounds, bounds)
    var screenIndex = 0
    var area : Int

    for (i in 1 until GRAPHICS_DEVICES.size)
    {
        graphicsConfiguration = GRAPHICS_DEVICES[i].getDefaultConfiguration()
        bounds = graphicsConfiguration.getBounds()
        area = computeIntersectedArea(windowBounds, bounds)

        if (area > areaMax)
        {
            screenIndex = i
            areaMax = area
        }
    }

    return screenIndex
}

/**
 * Put a window in it's pack size<br>
 * Size is automatic limited to the window's screen
 *
 * @param window Window to pack
 */
fun packedSize(window : Window)
{
    window.pack()
    val dimension = window.size
    val screen = computeScreenRectangle(window)

    if (dimension.width > screen.width)
    {
        dimension.width = screen.width
    }

    if (dimension.height > screen.height)
    {
        dimension.height = screen.height
    }

    window.size = dimension
}

/**
 * Make a screen shot
 *
 * @return Screen shot
 */
fun screenShot() : BufferedImage
{
    var xMin = 0
    var xMax = 0
    var yMin = 0
    var yMax = 0
    var bounds : Rectangle

    for (graphicsDevice in GRAPHICS_DEVICES)
    {
        bounds = graphicsDevice.defaultConfiguration.bounds

        xMin = min(xMin, -bounds.x)
        xMax = max(xMax, -bounds.x + bounds.width)
        yMin = min(yMin, -bounds.y)
        yMax = max(yMax, -bounds.y + bounds.height)
    }

    val size = Toolkit.getDefaultToolkit().screenSize
    val width = max(xMax - xMin, size.width)
    val height = max(yMax - yMin, size.height)

    if (ROBOT == null)
    {
        if (emptyImageNotCreated.compareAndSet(true, false))
        {
            EMPTY_IMAGE = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        }

        val pixels = IntArray(width * height)
        EMPTY_IMAGE.setRGB(0, 0, width, height, pixels, 0, width)
        return EMPTY_IMAGE
    }

    return ROBOT.createScreenCapture(Rectangle(xMin, yMin, width, height))
}

/**
 * Search JFrame parent of a component
 *
 * @param component Component search it's JFram parent
 * @return JFrame parent or {@code null} if component haven't a JFrame parent
 */
fun searchFrameParent(component : Component) : JFrame?
{
    var componentTested : Component? = component

    while (componentTested != null)
    {
        if ((componentTested is JFrame))
        {
            return componentTested
        }

        componentTested = if ((componentTested is Window))
        {
            componentTested.owner
        }
        else
        {
            componentTested.parent
        }
    }

    return null
}

/**
 * Simulate a key press
 *
 * @param keyCode Key code
 */
fun simulateKeyPress(keyCode : Int)
{
    ROBOT?.keyPress(keyCode)
}

/**
 * Simulate a key release
 *
 * @param keyCode Key code
 */
fun simulateKeyRelease(keyCode : Int)
{
    ROBOT?.keyRelease(keyCode)
}

/**
 * Simulate (If system allow it) a mouse click
 *
 * @param time Duration of down state
 */
fun simulateMouseClick(time : Int)
{
    if (ROBOT == null)
    {
        return
    }

    ROBOT.mousePress(InputEvent.BUTTON1_MASK)
    Thread.sleep(time.toLong())
    ROBOT.mouseRelease(InputEvent.BUTTON1_MASK or InputEvent.BUTTON2_MASK or InputEvent.BUTTON3_MASK)
}

/**
 * Simulate a mouse press
 *
 * @param button Mouse buttons
 */
fun simulateMousePress(button : Int)
{
    ROBOT?.mousePress(button)
}

/**
 * Simulate a mouse release
 *
 * @param button Mouse buttons
 */
fun simulateMouseRelease(button : Int)
{
    ROBOT?.mouseRelease(button)
}

/**
 * Simulate mouse wheel move
 *
 * @param tick Number of "notches" to move the mouse wheel Negative values indicate movement up/away from the user,
 *             positive values indicate movement down/towards the user.
 */
fun simulateMouseWheel(tick : Int)
{
    ROBOT?.mouseWheel(tick)
}

/**
 * Simulate a release then press mouse button
 *
 * @param time Time between release and press
 */
fun simulateReleasedPressed(time : Int)
{
    if (ROBOT == null)
    {
        return
    }

    ROBOT.mouseRelease(InputEvent.BUTTON1_MASK or InputEvent.BUTTON2_MASK or InputEvent.BUTTON3_MASK)
    Thread.sleep(time.toLong())
    ROBOT.mousePress(InputEvent.BUTTON1_MASK)
}

/**
 * Make widow take all it's screen
 *
 * @param window Window to maximize
 */
fun takeAllScreen(window : Window)
{
    window.bounds = computeScreenRectangle(window)
}

