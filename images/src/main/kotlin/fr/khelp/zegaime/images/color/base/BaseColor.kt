package fr.khelp.zegaime.images.color.base

import fr.khelp.zegaime.images.color.ARGB

sealed interface BaseColor<BC : BaseColor<BC>>
{
    /** Color representation */
    val color : ARGB

    /** If their no lighter version, just return the same color */
    val lighter : BC

    /** If their no darker version, just return the same color */
    val darker : BC

    /** Lightest color */
    val lightest : BC

    /** Darkest color */
    val darkest : BC

    /** Representative color */
    val representative : BC
}
