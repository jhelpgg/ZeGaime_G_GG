package fr.khelp.zegaime.engine3d.resources

import fr.khelp.zegaime.resources.Resources
import fr.khelp.zegaime.resources.ResourcesText
import fr.khelp.zegaime.utils.source.ClassSource

/**
 * Provides access to the 3D resources.
 *
 * @property resources The resources' manager.
 * @property resourcesText The text resources manager.
 */
object Resources3D
{
    /**
     * The resources' manager.
     */
    val resources = Resources(ClassSource(Resources3D::class.java))

    /**
     * The text resources manager.
     */
    val resourcesText : ResourcesText = this.resources.resourcesText("texts/engineTexts")
}
