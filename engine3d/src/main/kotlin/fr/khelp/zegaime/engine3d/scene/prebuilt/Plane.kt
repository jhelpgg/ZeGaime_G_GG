package fr.khelp.zegaime.engine3d.scene.prebuilt

import fr.khelp.zegaime.engine3d.geometry.Face
import fr.khelp.zegaime.engine3d.render.TwoSidedRule
import fr.khelp.zegaime.engine3d.scene.Object3D

class Plane(id : String, faceUV : FaceUV = FaceUV()) : Object3D(id)
{
    init
    {
        this.twoSidedRule = TwoSidedRule.FORCE_TWO_SIDE

        val face = Face()
        face.add(-0.5f, 0.5f, 0f,
                 faceUV.minU, faceUV.minV,
                 0f, 0f, -1f)
        face.add(0.5f, 0.5f, 0f,
                 faceUV.maxU, faceUV.minV,
                 0f, 0f, -1f)
        face.add(0.5f, -0.5f, 0f,
                 faceUV.maxU, faceUV.maxV,
                 0f, 0f, -1f)
        face.add(-0.5f, -0.5f, 0f,
                 faceUV.minU, faceUV.maxV,
                 0f, 0f, -1f)
        this.mesh.addFace(face)
    }
}
