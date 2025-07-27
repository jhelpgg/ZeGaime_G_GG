package fr.khelp.zegaime.engine3d.utils

/**
 * Multiply a number to a position in 3D.
 *
 * @param position3D The position to multiply.
 * @return The multiplied position.
 */
operator fun Number.times(position3D: NodePosition): NodePosition =
    position3D * this
