package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.scene.prebuilt.BoxUV
import kotlinx.serialization.Serializable

/**
 * Box UV data for save
 * @param face Face UV
 * @param back Back UV
 * @param top Top UV
 * @param bottom Bottom UV
 * @param left Left UV
 * @param right Right UV
 */
@Serializable
data class BoxUVData(val face : FaceUVData, val back : FaceUVData,
                     val top : FaceUVData, val bottom : FaceUVData,
                     val left : FaceUVData, val right : FaceUVData)
{
    /**
     * Create a new instance of BoxUVData from a BoxUV
     * @param boxUV BoxUV to copy
     */
    constructor(boxUV : BoxUV) : this(face = FaceUVData(boxUV.face),
                                      back = FaceUVData(boxUV.back),
                                      top = FaceUVData(boxUV.top),
                                      bottom = FaceUVData(boxUV.bottom),
                                      left = FaceUVData(boxUV.left),
                                      right = FaceUVData(boxUV.right))

    /**
     * The BoxUV
     */
    val boxUV : BoxUV by lazy {
        BoxUV(face = this.face.faceUV,
              back = this.back.faceUV,
              top = this.top.faceUV,
              bottom = this.bottom.faceUV,
              left = this.left.faceUV,
              right = this.right.faceUV)
    }
}
