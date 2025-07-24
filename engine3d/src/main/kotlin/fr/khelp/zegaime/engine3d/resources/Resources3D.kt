package fr.khelp.zegaime.engine3d.resources

import fr.khelp.zegaime.resources.Resources
import fr.khelp.zegaime.resources.ResourcesText
import fr.khelp.zegaime.utils.source.ClassSource

object Resources3D
{
    val resources = Resources(ClassSource(Resources3D::class.java))
    val resourcesText : ResourcesText = this.resources.resourcesText("texts/engineTexts")
}