package fr.khelp.zegaime.formatk3d.data

/**
 * Path element type
 */
enum class PathElementType
{
    /** Close the path */
    CLOSE,
    /** Move to a point */
    MOVE,
    /** Line to a point */
    LINE,
    /** Quadratic curve to a point */
    QUADRATIC,
    /** Cubic curve to a point */
    CUBIC
}