package fr.khelp.zegaime.formatk3d

import fr.khelp.zegaime.formatk3d.creator.SceneCreator
import fr.khelp.zegaime.formatk3d.data.SceneData
import fr.khelp.zegaime.utils.io.createFile
import fr.khelp.zegaime.utils.io.unzip
import fr.khelp.zegaime.utils.io.zip
import java.io.File
import kotlinx.serialization.json.Json

/** JSON instance for save/load scene information */
private val JSON : Json = Json {
    this.prettyPrint = true
    this.explicitNulls = false
    this.allowSpecialFloatingPointValues = true
}

/** Subdirectory where textures are stored */
const val TEXTURES_DIRECTORY = "textures"

/**
 * Creates texture relative path from its name
 *
 * @param name Texture name
 *
 * @return Texture relative path
 */
fun texturePath(name : String) : String =
    "$TEXTURES_DIRECTORY/$name"

/**
 * Saves a scene description in K3D format
 *
 * @param file File where save the scene description
 */
fun SceneCreator.save(file : File)
{
    file.createFile()
    val json = JSON.encodeToString(this())
    val sceneFile = File(this.directory, "scene.json")
    sceneFile.createFile()
    sceneFile.writeText(json)
    zip(this.directory, file, onlyContentIfDirectory = true)
}

/**
 * Loads scene description and its files from a K3D format file
 *
 * @param file K3D file to load
 * @param directoryDestination Directory where deploy the scene files
 *
 * @return Scene description loaded
 */
fun loadScene(file : File, directoryDestination : File) : SceneData
{
    unzip(directoryDestination, file)
    val sceneFile = File(directoryDestination, "scene.json")
    val json = sceneFile.readText()
    return JSON.decodeFromString<SceneData>(json)
}
