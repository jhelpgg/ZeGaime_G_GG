package fr.khelp.zegaime.engine3d.utils

/**
 * Multiply a number to a position in 3D
 */
operator fun Number.times(position3D : NodePosition) : NodePosition =
    position3D * this