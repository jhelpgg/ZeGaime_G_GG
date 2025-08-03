package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.formatk3d.data.BLACK_DATA
import fr.khelp.zegaime.formatk3d.data.ColorData
import fr.khelp.zegaime.formatk3d.data.MaterialData
import fr.khelp.zegaime.formatk3d.data.MaterialsMapData
import fr.khelp.zegaime.formatk3d.data.NodeLimitData
import fr.khelp.zegaime.formatk3d.data.NodePositionData
import fr.khelp.zegaime.formatk3d.data.SceneData
import fr.khelp.zegaime.formatk3d.data.TextureData
import fr.khelp.zegaime.formatk3d.data.TextureType
import fr.khelp.zegaime.formatk3d.texturePath
import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.images.ImageFormat
import fr.khelp.zegaime.utils.io.createFile
import fr.khelp.zegaime.utils.io.deleteFull
import java.io.File
import java.io.FileOutputStream

/**
 * Create a scene creator
 * @param directory Directory where scene files will be stored
 * @param create Scene creation lambda
 * @return Scene creator
 */
fun sceneCreator(directory : File, create : SceneCreator.() -> Unit) : SceneCreator
{
    val sceneCreator = SceneCreator(directory)
    sceneCreator.create()
    return sceneCreator
}

/**
 * Scene creator
 * @param directory Directory where scene files will be stored
 */
class SceneCreator(val directory : File)
{
    /** Scene background color */
    var backgroundColor : ColorData = BLACK_DATA
    /** Scene background texture name */
    var backgroundTexture : String? = null
    /** Scene camera position */
    var position : NodePositionData = NodePositionData(x = 0f, y = 0f, z = -5f,
                                                       angleX = 0f, angleY = 0f, angleZ = 0f,
                                                       scaleX = 1f, scaleY = 1f, scaleZ = 1f)
    /** Scene camera position and rotation limits */
    var limits : NodeLimitData =
        NodeLimitData(limitMinX = Float.NEGATIVE_INFINITY, limitMaxX = Float.POSITIVE_INFINITY,
                      limitMinY = Float.NEGATIVE_INFINITY, limitMaxY = Float.POSITIVE_INFINITY,
                      limitMinZ = Float.NEGATIVE_INFINITY, limitMaxZ = Float.POSITIVE_INFINITY,
                      limitMinAngleX = Float.NEGATIVE_INFINITY, limitMaxAngleX = Float.POSITIVE_INFINITY,
                      limitMinAngleY = Float.NEGATIVE_INFINITY, limitMaxAngleY = Float.POSITIVE_INFINITY,
                      limitMinAngleZ = Float.NEGATIVE_INFINITY, limitMaxAngleZ = Float.POSITIVE_INFINITY,
                      limitMinScaleX = Float.NEGATIVE_INFINITY, limitMaxScaleX = Float.POSITIVE_INFINITY,
                      limitMinScaleY = Float.NEGATIVE_INFINITY, limitMaxScaleY = Float.POSITIVE_INFINITY,
                      limitMinScaleZ = Float.NEGATIVE_INFINITY, limitMaxScaleZ = Float.POSITIVE_INFINITY)
    /** Scene textures map */
    private val textures = HashMap<String, TextureData>()
    /** Scene materials map */
    private val materials = HashMap<String, MaterialCreator>()
    /** Scene children creator */
    val nodeChildrenCreator = NodeChildrenCreator()

    /**
     * Store an image from a file
     * @param name Image name
     * @param sourceFile File to store
     */
    fun storeImage(name : String, sourceFile : File)
    {
        this.storeTexture(name, sourceFile, TextureType.IMAGE)
    }

    /**
     * Store an image from a game image
     * @param name Image name
     * @param gameImage Game image to store
     */
    fun storeImage(name : String, gameImage : GameImage)
    {
        val path = texturePath(name)
        val file = File(this.directory, path)
        file.createFile()
        val outputStream = FileOutputStream(file)
        gameImage.save(outputStream, ImageFormat.PNG)
        this.textures[name] = TextureData(path, TextureType.IMAGE)
    }

    /**
     * Store a GIF from a file
     * @param name GIF name
     * @param sourceFile File to store
     */
    fun storeGif(name : String, sourceFile : File)
    {
        this.storeTexture(name, sourceFile, TextureType.GIF)
    }

    /**
     * Store a video from a file
     * @param name Video name
     * @param sourceFile File to store
     */
    fun storeVideo(name : String, sourceFile : File)
    {
        this.storeTexture(name, sourceFile, TextureType.VIDEO)
    }

    /**
     * Create a material
     * @param name Material name
     * @param create Material creation lambda
     */
    fun material(name : String, create : MaterialCreator.() -> Unit)
    {
        val materialCreator = MaterialCreator()
        materialCreator.create()
        this.materials[name] = materialCreator
    }

    /**
     * Define scene children
     * @param children Children creation lambda
     */
    fun children(children : NodeChildrenCreator.() -> Unit)
    {
        this.nodeChildrenCreator.nodes.clear()
        this.nodeChildrenCreator.children()
    }

    /**
     * Convert to scene data for save
     * @return Scene data for save
     */
    operator fun invoke() : SceneData
    {
        val materials = HashMap<String, MaterialData>()

        for ((name, materialCreator) in this.materials)
        {
            materials[name] = this.resoleMaterialData(materialCreator)
        }

        return SceneData(materialsMap = MaterialsMapData(materials),
                         backgroundColor = this.backgroundColor,
                         backgroundTexture = this.backgroundTexture?.let { textureName -> this.textures[textureName] },
                         position = this.position,
                         limits = this.limits,
                         this.nodeChildrenCreator.nodes)
    }

    /**
     * Store a texture from a file
     * @param name Texture name
     * @param sourceFile File to store
     * @param textureType Texture type
     */
    private fun storeTexture(name : String, sourceFile : File, textureType : TextureType)
    {
        val path = texturePath(name)
        val textureFile = File(this.directory, path)

        if (sourceFile.absolutePath != textureFile.absolutePath)
        {
            textureFile.deleteFull()
            sourceFile.copyTo(textureFile)
        }

        this.textures[name] = TextureData(path, textureType)
    }

    /**
     * Resolve material data from material creator
     * @param materialCreator Material creator to resolve
     * @return Resolved material data
     */
    private fun resoleMaterialData(materialCreator : MaterialCreator) : MaterialData =
        MaterialData(colorAmbient = materialCreator.colorAmbient,
                     colorDiffuse = materialCreator.colorDiffuse,
                     colorEmissive = materialCreator.colorEmissive,
                     colorSpecular = materialCreator.colorSpecular,
                     specularLevel = materialCreator.specularLevel,
                     shininess = materialCreator.shininess,
                     transparency = materialCreator.transparency,
                     twoSided = materialCreator.twoSided,
                     sphericRate = materialCreator.sphericRate,
                     textureDiffuse = materialCreator.textureDiffuse?.let { textureName -> this.textures[textureName] },
                     textureSpheric = materialCreator.textureSpheric?.let { textureName -> this.textures[textureName] })
}