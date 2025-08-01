package fr.khelp.zegaime.engine3d.utils

import fr.khelp.zegaime.engine3d.scene.Node
import fr.khelp.zegaime.utils.math.sign

/**
 * Comparator for sorting nodes by their Z-order.
 *
 * This class is for internal use of the engine.
 */
internal object NodeComparatorOrderZ : Comparator<Node>
{
    /**
     * Compare two nodes with there Z-order.
     *
     * @param node1 Node 1
     * @param node2 Node 2
     * @return Result
     */
    override fun compare(node1 : Node, node2 : Node) = sign(node2.zOrder - node1.zOrder)
}
