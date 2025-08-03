package fr.jhelp.zegaime.main

import fr.khelp.zegaime.engine3d.window3DFull
import fr.khelp.zegaime.formatk3d.creator.sceneCreator
import fr.khelp.zegaime.formatk3d.filler.fill
import fr.khelp.zegaime.formatk3d.loadScene
import fr.khelp.zegaime.formatk3d.save
import fr.khelp.zegaime.utils.io.deleteFull
import fr.khelp.zegaime.utils.io.outsideDirectory
import java.io.File

/**
 * Main function
 */
fun main()
{
    val baseDirectory = File(outsideDirectory, "testK3D")
    val directory = File(baseDirectory, "sceneSource")
    val sceneCreator =
        sceneCreator(directory) {
            val witchFile = File("C:\\Users\\jhelp\\Pictures\\0bdc2852ac0b3c53d466b731a81ccc04.jpg")
            storeImage("witch.jpg", witchFile)

            material("witch") {
                textureDiffuse = "witch.jpg"
            }

            children {
                box("box") {
                    material = "witch"
                }
            }
        }

    val sceneK3d = File(baseDirectory, "scene.k3d")
    sceneCreator.save(sceneK3d)

    val destination = File(baseDirectory, "sceneDestination")
    destination.deleteFull()
    val sceneData = loadScene(sceneK3d, destination)
    window3DFull("Test") {
        scene.fill(destination, sceneData)
    }
}
