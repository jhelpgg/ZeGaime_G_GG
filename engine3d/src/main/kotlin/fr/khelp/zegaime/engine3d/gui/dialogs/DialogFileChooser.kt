package fr.khelp.zegaime.engine3d.gui.dialogs

import fr.khelp.zegaime.engine3d.gui.GUI
import fr.khelp.zegaime.engine3d.gui.component.GUIComponent
import fr.khelp.zegaime.engine3d.gui.component.GUIComponentPanel
import fr.khelp.zegaime.engine3d.gui.component.GUIComponentScroll
import fr.khelp.zegaime.engine3d.gui.component.GUIComponentText
import fr.khelp.zegaime.engine3d.gui.component.GUIComponentTextImage
import fr.khelp.zegaime.engine3d.gui.component.GUIComponentToggleButton
import fr.khelp.zegaime.engine3d.gui.component.GUIDialog
import fr.khelp.zegaime.engine3d.gui.dsl.buttonText
import fr.khelp.zegaime.engine3d.gui.dsl.dialogAbsolute
import fr.khelp.zegaime.engine3d.gui.layout.absolute.GUIAbsoluteConstraint
import fr.khelp.zegaime.engine3d.gui.layout.absolute.GUIAbsoluteLayout
import fr.khelp.zegaime.engine3d.gui.style.ImageTextRelativePosition
import fr.khelp.zegaime.engine3d.gui.style.background.StyleBackgroundColor
import fr.khelp.zegaime.engine3d.resources.Resources3D
import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.images.TextAlignment
import fr.khelp.zegaime.images.VerticalAlignment
import fr.khelp.zegaime.images.color.BLACK
import fr.khelp.zegaime.images.color.WHITE
import fr.khelp.zegaime.images.color.base.Cyan
import fr.khelp.zegaime.images.drawText
import fr.khelp.zegaime.images.font.DEFAULT_FONT_SMALL
import fr.khelp.zegaime.images.gif.GIF
import fr.khelp.zegaime.images.gif.isGIF
import fr.khelp.zegaime.images.pcx.PCX
import fr.khelp.zegaime.images.pcx.isPCX
import fr.khelp.zegaime.images.setColor
import fr.khelp.zegaime.resources.CANCEL
import fr.khelp.zegaime.resources.OK
import fr.khelp.zegaime.resources.ResourcesText
import fr.khelp.zegaime.utils.collections.cache.Cache
import fr.khelp.zegaime.utils.collections.iterations.select
import fr.khelp.zegaime.utils.collections.iterations.selectInstance
import fr.khelp.zegaime.utils.extensions.ellipseIfMoreThan
import fr.khelp.zegaime.utils.io.isVirtualLink
import fr.khelp.zegaime.utils.io.outsideDirectory
import java.awt.BasicStroke
import java.awt.Dimension
import java.io.File
import java.io.FileInputStream
import java.util.Optional
import kotlin.math.max

/**
 * Dialog for choose a file
 *
 * @param gui GUI where dialog is draws
 */
class DialogFileChooser internal constructor(gui : GUI)
{
    companion object
    {
        /** Thumbnails size */
        private const val THUMBNAIL_SIZE = 128

        /** Default image use if a thumbnail not loads */
        private val DEFAULT_IMAGE = GameImage.DARK_LIGHT.resize(THUMBNAIL_SIZE, THUMBNAIL_SIZE)

        /** Folder image */
        private val FOLDER_IMAGE =
            Resources3D.resources.imageThumbnail("icons/folder.png", THUMBNAIL_SIZE, THUMBNAIL_SIZE)

        /** Managed images extensions */
        private val IMAGE_EXTENSIONS = arrayOf<String>("jpg", "jpeg", "png", "gif", "bmp", "pcx")

        /** Thumbnails cache */
        private val thumbnailCache = Cache<String, GameImage>(64) { name ->
            val file = File(name)

            if (file.exists())
            {
                try
                {
                    when
                    {
                        isGIF(file) ->
                        {
                            val gif = GIF(FileInputStream(file))
                            gif.image(0)
                        }

                        isPCX(file) ->
                        {
                            val pcx = PCX(FileInputStream(file))
                            pcx.createImage()
                        }

                        else        ->
                            GameImage.loadThumbnail(FileInputStream(file), THUMBNAIL_SIZE, THUMBNAIL_SIZE)
                    }
                }
                catch (_ : Exception)
                {
                    DEFAULT_IMAGE
                }
            }
            else
            {
                val image = BASE_GENERIC_ICON.copy()

                image.draw { graphics2D ->
                    graphics2D.setColor(BLACK)
                    graphics2D.font = DEFAULT_FONT_SMALL.font
                    graphics2D.drawText((THUMBNAIL_SIZE - DEFAULT_FONT_SMALL.stringWidth(name)) shr 1,
                                        (THUMBNAIL_SIZE - DEFAULT_FONT_SMALL.fontHeight) shr 1,
                                        name, TextAlignment.LEFT)
                }

                image
            }
        }

        /**
         * Icon base for a generic file
         */
        private val BASE_GENERIC_ICON : GameImage by lazy {
            val image = GameImage(THUMBNAIL_SIZE, THUMBNAIL_SIZE)
            val xLeft = THUMBNAIL_SIZE shr 3
            val xRight = THUMBNAIL_SIZE - xLeft
            val width = xRight - xLeft
            val fold = width shr 2
            val xs = intArrayOf(xLeft + fold, xLeft + fold, xLeft)
            val ys = intArrayOf(0, fold, fold)

            image.draw { graphics2D ->
                graphics2D.setColor(WHITE)
                graphics2D.fillRect(xLeft + fold, 0, width - fold, fold)
                graphics2D.fillRect(xLeft, fold, width, THUMBNAIL_SIZE - fold)
                graphics2D.fillPolygon(xs, ys, 3)

                graphics2D.setColor(BLACK)
                graphics2D.stroke = BasicStroke(3f)
                graphics2D.drawLine(xLeft + fold, 0, xRight, 0)
                graphics2D.drawLine(xRight, 0, xRight, THUMBNAIL_SIZE)
                graphics2D.drawLine(xRight, THUMBNAIL_SIZE, xLeft, THUMBNAIL_SIZE)
                graphics2D.drawLine(xLeft, THUMBNAIL_SIZE, xLeft, fold)
                graphics2D.drawPolygon(xs, ys, 3)
            }

            image
        }

        /**
         * Computes a file thumbnail
         */
        private fun thumbnail(file : File) : GameImage
        {
            val extension = file.extension.lowercase()

            if (extension in IMAGE_EXTENSIONS)
            {
                return thumbnailCache[file.absolutePath]
            }

            return thumbnailCache[extension]
        }
    }

    /**
     * Current directory
     */
    var directory : File = outsideDirectory
        set(value)
        {
            if (!value.exists())
            {
                return
            }

            val directory =
                if (value.isDirectory)
                {
                    value
                }
                else
                {
                    value.parentFile ?: return
                }

            if (!directory.canRead() || !directory.canWrite())
            {
                return
            }

            field = directory
            this.update()
        }

    /**
     * Current selected file
     */
    var currentFile : Optional<File> = Optional.empty<File>()
        private set

    /**
     * Callback called when a file is selected
     */
    var onSelectFile : (File) -> Unit = {}

    /** Scroll component */
    private val scroll : GUIComponentScroll

    /** Text where the directory path is writen */
    private val textPath = GUIComponentText()

    /** Layout that carrys the files */
    private val absoluteLayout = GUIAbsoluteLayout()

    /** Dialog instance */
    private val dialog : GUIDialog<GUIAbsoluteConstraint, GUIAbsoluteLayout>

    /** List of actions to cancel if change of directory */
    private val cancelList = ArrayList<() -> Unit>()

    /** OK button */
    private val buttonOk = buttonText(OK)

    /** Width in the file list */
    private val fileListWidth = gui.width

    init
    {
        val panel = GUIComponentPanel<GUIAbsoluteConstraint, GUIAbsoluteLayout>(this.absoluteLayout)
        this.scroll = GUIComponentScroll(panel)

        this.buttonOk.click = this::okButton
        this.buttonOk.enabled = false
        val buttonCancel = buttonText(CANCEL)
        buttonCancel.click = this::cancelButton
        this.textPath.textAlignment = TextAlignment.CENTER
        this.textPath.verticalAlignment = VerticalAlignment.CENTER
        val textPreferred = this.textPath.preferredSize()
        val okPreferred = this.buttonOk.preferredSize()
        val cancelPreferred = buttonCancel.preferredSize()
        val y1 = textPreferred.height + 8
        val y2 = gui.height - max(okPreferred.height, cancelPreferred.height) - 8
        val middle = gui.width shr 1
        val xOk = (middle - okPreferred.width) shr 1
        val xCancel = middle + ((middle - cancelPreferred.width) shr 1)

        this.dialog = gui.dialogAbsolute {
            this@DialogFileChooser.textPath.with(GUIAbsoluteConstraint(0, 4, gui.width, y1 - 4))
            this@DialogFileChooser.scroll.with(GUIAbsoluteConstraint(0, y1, gui.width, y2 - y1))
            this@DialogFileChooser.buttonOk.with(GUIAbsoluteConstraint(xOk, y2 + 4,
                                                                       okPreferred.width, okPreferred.height))
            buttonCancel.with(GUIAbsoluteConstraint(xCancel, y2 + 4, cancelPreferred.width, cancelPreferred.height))
        }

        this.update()
    }

    /**
     * Shows the dialog
     */
    fun show()
    {
        this.dialog.show()
    }

    /**
     * Closes the dialog
     */
    fun close()
    {
        this.dialog.close()
    }

    /**
     * Updates the dialog content
     */
    private fun update()
    {
        for (cancel in this.cancelList)
        {
            cancel()
        }

        this.cancelList.clear()

        this.currentFile = Optional.empty<File>()
        this.textPath.keyText = ResourcesText.standardTextKey(this.directory.absolutePath)
        this.buttonOk.enabled = false
        this.absoluteLayout.clear()
        var x = 0
        var y = 0
        var maxHeight = 0
        var component : GUIComponentToggleButton
        var preferred : Dimension

        val parent = this.directory.parentFile

        if (parent != null)
        {
            component = this.fileComponent(parent, isParentDirectory = true)
            preferred = component.preferredSize()
            this.absoluteLayout.add(component, GUIAbsoluteConstraint(x, y, preferred.width, preferred.height))
            x = preferred.width
            maxHeight = preferred.height
        }

        val children = this.directory.listFiles()

        if (children != null)
        {
            for (child in children)
            {
                if (child.isVirtualLink)
                {
                    continue
                }

                component = this.fileComponent(child, isParentDirectory = false)
                preferred = component.preferredSize()

                if (x + preferred.width > this.fileListWidth)
                {
                    x = 0
                    y += maxHeight
                    maxHeight = 0
                }

                this.absoluteLayout.add(component, GUIAbsoluteConstraint(x, y, preferred.width, preferred.height))

                x += preferred.width
                maxHeight = max(maxHeight, preferred.height)
            }
        }

        this.scroll.update()
    }

    /**
     * Called on ok button click
     */
    private fun okButton()
    {
        this.currentFile.ifPresent(this.onSelectFile)

        if (this.currentFile.isPresent)
        {
            this.dialog.close()
        }
    }

    /**
     * Called on cancel button clicked
     */
    private fun cancelButton()
    {
        this.dialog.close()
    }

    /**
     * Creates component for a file
     *
     * @param file File to get its component
     * @param isParentDirectory Indicates if the file corresponds to go to parent directory
     *
     * @return Computed component
     */
    private fun fileComponent(file : File, isParentDirectory : Boolean) : GUIComponentToggleButton
    {
        val icon = if (file.isDirectory) DialogFileChooser.FOLDER_IMAGE else DialogFileChooser.thumbnail(file)
        val text = if (isParentDirectory) ".." else file.name.ellipseIfMoreThan(16)

        val notSelected = GUIComponentTextImage()
        notSelected.keyText = ResourcesText.standardTextKey(text)
        notSelected.image = icon
        notSelected.textAlignment = TextAlignment.CENTER
        notSelected.imageSize = DialogFileChooser.THUMBNAIL_SIZE
        notSelected.imageTextRelativePosition = ImageTextRelativePosition.IMAGE_ABOVE_OF_TEXT

        val selected = GUIComponentTextImage()
        selected.keyText = ResourcesText.standardTextKey(text)
        selected.image = icon
        selected.textAlignment = TextAlignment.CENTER
        selected.imageSize = DialogFileChooser.THUMBNAIL_SIZE
        selected.imageTextRelativePosition = ImageTextRelativePosition.IMAGE_ABOVE_OF_TEXT
        selected.borderColor = Cyan.CYAN_0050.color
        selected.background = StyleBackgroundColor(Cyan.CYAN_A700)

        val toggle = GUIComponentToggleButton(notSelected, notSelected, selected, selected)
        toggle.select(false)
        val cancel = toggle.selected.register { selected ->
            if (selected)
            {
                if (file.isDirectory)
                {
                    this.directory = file
                }
                else
                {
                    this.buttonOk.enabled = true
                    this.currentFile = Optional.of<File>(file)
                    this.unSelectOther(toggle)
                }
            }
        }

        this.cancelList.add(cancel)
        return toggle
    }

    /**
     * Unselects other components than specified one
     *
     * @param keepSelected Component to keep selected
     */
    private fun unSelectOther(keepSelected : GUIComponentToggleButton)
    {
        for (componentToggleButton in this.absoluteLayout.components()
            .select { component -> component != keepSelected }
            .selectInstance<GUIComponent, GUIComponentToggleButton>())
        {
            componentToggleButton.select(false)
        }

        keepSelected.select(true)
    }
}