package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.scene.prebuilt.FaceUV
import kotlinx.serialization.Serializable

/**
 * Face UV data for save
 * @param minU Minimum U
 * @param maxU Maximum U
 * @param minV Minimum V
 * @param maxV Maximum V
 */
@Serializable
data class FaceUVData(val minU : Float, val maxU : Float,
                      val minV : Float, val maxV : Float)
{
    /**
     * Create a new instance of FaceUVData from a FaceUV
     * @param faceUV FaceUV to copy
     */
    constructor(faceUV : FaceUV) : this(minU = faceUV.minU, maxU = faceUV.maxU,
                                        minV = faceUV.minV, maxV = faceUV.maxV)

    /**
     * The FaceUV
     */
    val faceUV : FaceUV by lazy {
        FaceUV(minU = this.minU, maxU = this.maxU,
               minV = this.minV, maxV = this.maxV)
    }
}