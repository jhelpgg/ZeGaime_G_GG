package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.geometry.path.Path
import fr.khelp.zegaime.engine3d.render.Color4f
import fr.khelp.zegaime.engine3d.render.DEFAULT_WIRE_FRAME_COLOR
import fr.khelp.zegaime.engine3d.render.Material
import fr.khelp.zegaime.engine3d.scene.prebuilt.Revolution
import fr.khelp.zegaime.engine3d.utils.NodePosition
import fr.khelp.zegaime.engine3d.utils.position

@PrebuiltDSL
fun Revolution.edit(pathFiller : RevolutionCreator.() -> Unit)
{
    val revolutionCreator = RevolutionCreator(this)
    revolutionCreator.pathFiller()
    revolutionCreator()
}

@PrebuiltDSL
fun revolution(id : String, create : RevolutionCreator.() -> Unit) : Revolution
{
    val revolutionCreator = RevolutionCreator(Revolution(id))
    revolutionCreator.create()
    return revolutionCreator()
}

@PrebuiltDSL
class RevolutionCreator(private val revolution : Revolution)
{
    var position = NodePosition()
    var material : Material = Material()
    var materialForSelection : Material = Material()
    var wireColor : Color4f = DEFAULT_WIRE_FRAME_COLOR

    var precision : Int = 5
        set(value)
        {
            field = value.coerceIn(2, 12)
        }

    var angle : Float = 360f
        set(value)
        {
            field = value.coerceIn(1f, 360f)
        }

    var rotationPrecision : Int = 12
        set(value)
        {
            field = value.coerceIn(3, 32)
        }

    var start : Float = 0f

    var end : Float = 1f

    var multiplierU : Float = 1f

    private var path = Path()

    @PathDSL
    fun path(pathFiller : Path.() -> Unit)
    {
        this.path.clear()
        this.path.pathFiller()
    }

    @MaterialDSL
    fun materialCreate(create : Material.() -> Unit)
    {
        this.material = material(create)
    }

    @MaterialDSL
    fun materialForSelectionCreate(create : Material.() -> Unit)
    {
        this.materialForSelection = material(create)
    }

    internal operator fun invoke() : Revolution
    {
        this.revolution.path(this.precision,
                             this.angle, this.rotationPrecision,
                             this.start, this.end,
                             this.multiplierU,
                             this.path)
        this.revolution.position(this.position)
        this.revolution.material = this.material
        this.revolution.materialForSelection = this.materialForSelection
        this.revolution.wireColor = this.wireColor
        return this.revolution
    }
}