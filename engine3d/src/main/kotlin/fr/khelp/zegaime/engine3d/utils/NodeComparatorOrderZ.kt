package fr.khelp.zegaime.engine3d.utils

import fr.khelp.zegaime.engine3d.scene.Node
import fr.khelp.zegaime.utils.math.sign

internal object NodeComparatorOrderZ : Comparator<Node>
{
    /**
     * Compare two nodes with there Z-order
     *
     * @param node1 Node 1
     * @param node2 Node 2
     * @return Result
     * @see Comparator.compare
     */
    override fun compare(node1 : Node, node2 : Node) = sign(node2.zOrder - node1.zOrder)
}