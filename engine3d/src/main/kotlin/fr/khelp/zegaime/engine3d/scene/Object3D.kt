package fr.khelp.zegaime.engine3d.scene

import fr.khelp.zegaime.engine3d.geometry.Mesh
import fr.khelp.zegaime.engine3d.geometry.Point3D
import fr.khelp.zegaime.engine3d.geometry.VirtualBox
import fr.khelp.zegaime.engine3d.render.Color4f
import fr.khelp.zegaime.engine3d.render.DEFAULT_WIRE_FRAME_COLOR
import org.lwjgl.opengl.GL11

open class Object3D(id : String) : NodeWithMaterial(id)
{
    val mesh : Mesh = Mesh()
    var showWire : Boolean = false
    var wireColor : Color4f = DEFAULT_WIRE_FRAME_COLOR
    override val center : Point3D get() = this.mesh.center
    override val virtualBox : VirtualBox get() = this.mesh.virtualBox

    /**Polygons list's OpenGL ID*/
    private var idList = -1

    /**Indicates if the polygons list have to be reconstruct in OpenGL memory*/
    private var needReconstructTheList = true

    constructor(id : String, mesh : Mesh) : this(id)
    {
        this.mesh.copy(mesh)
    }

    constructor(id : String, object3D : Object3D) : this(id, object3D.mesh)

    fun refresh()
    {
        this.needReconstructTheList = true
    }

    override fun renderSpecific()
    {
        this.material { material -> material.renderMaterial(this) }
    }

    override fun renderSpecificPicking()
    {
        this.drawObject()
    }

    internal fun drawObject()
    {
        // If no list is create or actual list needs to be update
        if (this.idList < 0 || this.needReconstructTheList)
        {
            this.needReconstructTheList = false

            // Delete old list
            if (this.idList >= 0)
            {
                GL11.glDeleteLists(this.idList, 1)
            }
            // Create list
            this.idList = GL11.glGenLists(1)
            GL11.glNewList(this.idList, GL11.GL_COMPILE)
            try
            {
                this.mesh.render()
            }
            catch (e : Exception)
            {
                e.printStackTrace()
                this.needReconstructTheList = true
            }
            catch (e : Error)
            {
                e.printStackTrace()
                this.needReconstructTheList = true
            }

            GL11.glEndList()
        }
        // Draw the list
        GL11.glCallList(this.idList)
    }
}
