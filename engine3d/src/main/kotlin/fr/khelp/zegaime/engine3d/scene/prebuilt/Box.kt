package fr.khelp.zegaime.engine3d.scene.prebuilt

import fr.khelp.zegaime.engine3d.dsl.face
import fr.khelp.zegaime.engine3d.dsl.mesh
import fr.khelp.zegaime.engine3d.render.TwoSidedRule
import fr.khelp.zegaime.engine3d.scene.Object3D

class Box(id : String, boxUV : BoxUV = BoxUV()) : Object3D(id)
{
    init
    {
        this.twoSidedRule = TwoSidedRule.FORCE_ONE_SIDE
        this.boxUV(boxUV)
    }

    fun boxUV(boxUV : BoxUV)
    {
        mesh {
            this.clear()

            // Face
            face {
                add(-0.5f, 0.5f, 0.5f,
                    boxUV.face.minU, boxUV.face.minV,
                    0f, 0f, -1f)
                add(0.5f, 0.5f, 0.5f,
                    boxUV.face.maxU, boxUV.face.minV,
                    0f, 0f, -1f)
                add(0.5f, -0.5f, 0.5f,
                    boxUV.face.maxU, boxUV.face.maxV,
                    0f, 0f, -1f)
                add(-0.5f, -0.5f, 0.5f,
                    boxUV.face.minU, boxUV.face.maxV,
                    0f, 0f, -1f)
            }

            // Top
            face {
                add(-0.5f, 0.5f, -0.5f,
                    boxUV.top.minU, boxUV.top.minV,
                    0f, -1f, 0f)
                add(0.5f, 0.5f, -0.5f,
                    boxUV.top.maxU, boxUV.top.minV,
                    0f, -1f, 0f)
                add(0.5f, 0.5f, 0.5f,
                    boxUV.top.maxU, boxUV.top.maxV,
                    0f, -1f, 0f)
                add(-0.5f, 0.5f, 0.5f,
                    boxUV.top.minU, boxUV.top.maxV,
                    0f, -1f, 0f)
            }

            // Right
            face {
                add(0.5f, -0.5f, 0.5f,
                    boxUV.right.minU, boxUV.right.maxV,
                    -1f, 0f, 0f)
                add(0.5f, 0.5f, 0.5f,
                    boxUV.right.minU, boxUV.right.minV,
                    -1f, 0f, 0f)
                add(0.5f, 0.5f, -0.5f,
                    boxUV.right.maxU, boxUV.right.minV,
                    -1f, 0f, 0f)
                add(0.5f, -0.5f, -0.5f,
                    boxUV.right.maxU, boxUV.right.maxV,
                    -1f, 0f, 0f)
            }

            // Back
            face {
                add(-0.5f, -0.5f, -0.5f,
                    boxUV.back.maxU, boxUV.back.maxV,
                    0f, 0f, 1f)
                add(0.5f, -0.5f, -0.5f,
                    boxUV.back.minU, boxUV.back.maxV,
                    0f, 0f, 1f)
                add(0.5f, 0.5f, -0.5f,
                    boxUV.back.minU, boxUV.back.minV,
                    0f, 0f, 1f)
                add(-0.5f, 0.5f, -0.5f,
                    boxUV.back.maxU, boxUV.back.minV,
                    0f, 0f, 1f)
            }

            // Bottom
            face {
                add(-0.5f, -0.5f, 0.5f,
                    boxUV.bottom.minU, boxUV.bottom.minV,
                    0f, 1f, 0f)
                add(0.5f, -0.5f, 0.5f,
                    boxUV.bottom.maxU, boxUV.bottom.minV,
                    0f, 1f, 0f)
                add(0.5f, -0.5f, -0.5f,
                    boxUV.bottom.maxU, boxUV.bottom.maxV,
                    0f, 1f, 0f)
                add(-0.5f, -0.5f, -0.5f,
                    boxUV.bottom.minU, boxUV.bottom.maxV,
                    0f, 1f, 0f)
            }

            // Left
            face {
                add(-0.5f, -0.5f, -0.5f,
                    boxUV.left.minU, boxUV.left.maxV,
                    1f, 0f, 0f)
                add(-0.5f, 0.5f, -0.5f,
                    boxUV.left.minU, boxUV.left.minV,
                    1f, 0f, 0f)
                add(-0.5f, 0.5f, 0.5f,
                    boxUV.left.maxU, boxUV.left.minV,
                    1f, 0f, 0f)
                add(-0.5f, -0.5f, 0.5f,
                    boxUV.left.maxU, boxUV.left.maxV,
                    1f, 0f, 0f)
            }
        }
    }
}
