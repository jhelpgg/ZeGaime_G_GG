package fr.khelp.zegaime.formatk3d.data

/**
 * Node type
 */
enum class NodeType
{
    /** Generic node */
    NODE,
    /** Object 3D */
    OBJECT,
    /** Clone of an object */
    CLONE,
    /** Plane */
    PLANE,
    /** Box */
    BOX,
    /** Sphere */
    SPHERE,
    /** Revolution */
    REVOLUTION,
    /** Dice */
    DICE,
    /** Sword */
    SWORD,
    /** Robot */
    ROBOT
}