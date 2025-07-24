package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.geometry.path.Path
import fr.khelp.zegaime.engine3d.scene.prebuilt.Revolution

fun Revolution.edit(pathFiller : RevolutionCreator.() -> Unit)
{
    val revolutionCreator = RevolutionCreator(this)
    revolutionCreator.pathFiller()
    revolutionCreator()
}

fun revolution(id : String, create : RevolutionCreator.() -> Unit) : Revolution
{
    val revolutionCreator = RevolutionCreator(Revolution(id))
    revolutionCreator.create()
    return revolutionCreator()
}

class RevolutionCreator(private val revolution : Revolution)
{
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

    operator fun invoke() : Revolution
    {
        this.revolution.path(this.precision,
                             this.angle, this.rotationPrecision,
                             this.start, this.end,
                             this.multiplierU,
                             this.path)
        return this.revolution
    }
}