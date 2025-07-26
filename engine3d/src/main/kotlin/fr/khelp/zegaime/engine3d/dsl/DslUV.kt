package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.scene.prebuilt.BoxUV
import fr.khelp.zegaime.engine3d.scene.prebuilt.CrossUV
import fr.khelp.zegaime.engine3d.scene.prebuilt.FaceUV

@UvDSL
fun faceUV(create : FaceUVCreator.() -> Unit) : FaceUV
{
    val faceUVCreator = FaceUVCreator()
    faceUVCreator.create()
    return faceUVCreator()
}

@UvDSL
class FaceUVCreator
{
    var minU : Float = 0f
    var maxU : Float = 1f

    var minV : Float = 0f
    var maxV : Float = 1f

    operator fun invoke() : FaceUV =
        FaceUV(this.minU, this.maxU, this.minV, this.maxV)
}

@UvDSL
fun boxUV(create : BoxUVCreator.() -> Unit) : BoxUV
{
    val boxUVCreator = BoxUVCreator()
    boxUVCreator.create()
    return boxUVCreator()
}

@UvDSL
class BoxUVCreator
{
    private var faceCreator = FaceUVCreator()
    private var backCreator = FaceUVCreator()
    private var topCreator = FaceUVCreator()
    private var bottomCreator = FaceUVCreator()
    private var leftCreator = FaceUVCreator()
    private var rightCreator = FaceUVCreator()

    fun face(create : FaceUVCreator.() -> Unit)
    {
        this.faceCreator.create()
    }

    fun back(create : FaceUVCreator.() -> Unit)
    {
        this.backCreator.create()
    }

    fun top(create : FaceUVCreator.() -> Unit)
    {
        this.topCreator.create()
    }

    fun bottom(create : FaceUVCreator.() -> Unit)
    {
        this.bottomCreator.create()
    }

    fun left(create : FaceUVCreator.() -> Unit)
    {
        this.leftCreator.create()
    }

    fun right(create : FaceUVCreator.() -> Unit)
    {
        this.rightCreator.create()
    }

    internal operator fun invoke() : BoxUV =
        BoxUV(face = this.faceCreator(), back = this.backCreator(),
              top = this.topCreator(), bottom = this.backCreator(),
              left = this.leftCreator(), right = this.rightCreator())
}

@UvDSL
fun crossUV(create : CrossUVCreator.() -> Unit) : CrossUV
{
    val crossUVCreator = CrossUVCreator()
    crossUVCreator.create()
    return crossUVCreator()
}

@UvDSL
class CrossUVCreator
{
    var u1 : Float = 1f / 3f
    var u2 : Float = 2f / 3f

    var v1 : Float = 0.25f
    var v2 : Float = 0.5f
    var v3 : Float = 0.75f

    internal operator fun invoke() : CrossUV =
        CrossUV(u1 = this.u1, u2 = this.u2,
                v1 = this.v1, v2 = this.v2, v3 = this.v3)
}