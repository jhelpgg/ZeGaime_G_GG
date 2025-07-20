package fr.khelp.zegaime.engine3d.scene

import fr.khelp.zegaime.engine3d.geometry.Point3D
import fr.khelp.zegaime.engine3d.geometry.VirtualBox

class ObjectClone(id : String, internal val reference : Object3D) : NodeWithMaterial(id)
{
    override val center : Point3D get() = this.reference.center
    override val virtualBox : VirtualBox get() = this.reference.virtualBox

    init
    {
        this.twoSidedRule = this.reference.twoSidedRule
    }

    override fun renderSpecific()
    {
        this.material { material -> material.renderMaterial(this.reference) }
    }

    override fun renderSpecificPicking()
    {
        this.reference.drawObject()
    }
}
