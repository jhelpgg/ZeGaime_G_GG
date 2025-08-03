package fr.khelp.zegaime.engine3d.scene

import fr.khelp.zegaime.engine3d.geometry.Point3D
import fr.khelp.zegaime.engine3d.geometry.Rotf
import fr.khelp.zegaime.engine3d.geometry.Vec3f
import fr.khelp.zegaime.engine3d.geometry.VirtualBox
import fr.khelp.zegaime.engine3d.render.Color4f
import fr.khelp.zegaime.engine3d.render.Material
import fr.khelp.zegaime.engine3d.utils.NodePosition
import fr.khelp.zegaime.engine3d.utils.PICKING_PRECISION
import fr.khelp.zegaime.engine3d.utils.pickSame
import fr.khelp.zegaime.engine3d.utils.position
import fr.khelp.zegaime.utils.collections.queue.Queue
import fr.khelp.zegaime.utils.extensions.blue
import fr.khelp.zegaime.utils.extensions.degreeToRadian
import fr.khelp.zegaime.utils.extensions.green
import fr.khelp.zegaime.utils.extensions.red
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource
import java.util.Stack
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max
import kotlin.math.min
import org.lwjgl.opengl.GL11

/**
 * Represents a node in the scene graph.
 *
 * A node can have a position, a rotation, a scale, and a list of children nodes.
 *
 * **Creation example:**
 * ```kotlin
 * val node = Node("myNode")
 * node.x = 1f
 * node.angleY = 45f
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * val scene = Scene()
 * scene.root.addChild(node)
 * ```
 *
 * @property id The ID of the node.
 * @property x The X position of the node.
 * @property y The Y position of the node.
 * @property z The Z position of the node.
 * @property angleX The rotation angle around the X axis.
 * @property angleY The rotation angle around the Y axis.
 * @property angleZ The rotation angle around the Z axis.
 * @property scaleX The scale factor on the X axis.
 * @property scaleY The scale factor on the Y axis.
 * @property scaleZ The scale factor on the Z axis.
 * @property nodePositionObservable An observable that emits the node position changes.
 * @property parent The parent node.
 * @property visible Indicates if the node is visible.
 * @property canBePick Indicates if the node can be picked.
 * @property center The center of the node.
 * @property virtualBox The virtual box of the node.
 * @property root The root node of the scene graph.
 * @property colorPickingId The ID used for color picking.
 * @property numberOfChild The number of children of the node.
 * @constructor Creates a new node.
 */
open class Node(val id : String) : Iterable<Node>
{
    companion object
    {
        private const val MIN_SCALE = 0.001f

        /**
         * Next color picking ID
         */
        private var ID_PICKING = AtomicInteger(0)
    }

    var limitMinX = Float.NEGATIVE_INFINITY
        private set

    var limitMaxX = Float.POSITIVE_INFINITY
        private set

    var limitMinY = Float.NEGATIVE_INFINITY
        private set

    var limitMaxY = Float.POSITIVE_INFINITY
        private set

    var limitMinZ = Float.NEGATIVE_INFINITY
        private set

    var limitMaxZ = Float.POSITIVE_INFINITY
        private set

    //

    var limitMinAngleX = Float.NEGATIVE_INFINITY
        private set

    var limitMaxAngleX = Float.POSITIVE_INFINITY
        private set

    var limitMinAngleY = Float.NEGATIVE_INFINITY
        private set

    var limitMaxAngleY = Float.POSITIVE_INFINITY
        private set

    var limitMinAngleZ = Float.NEGATIVE_INFINITY
        private set

    var limitMaxAngleZ = Float.POSITIVE_INFINITY
        private set

    //

    var limitMinScaleX = Node.MIN_SCALE
        private set

    var limitMaxScaleX = Float.POSITIVE_INFINITY
        private set

    var limitMinScaleY = Node.MIN_SCALE
        private set

    var limitMaxScaleY = Float.POSITIVE_INFINITY
        private set

    var limitMinScaleZ = Node.MIN_SCALE
        private set

    var limitMaxScaleZ = Float.POSITIVE_INFINITY
        private set

    /**
     * The X position of the node.
     */
    var x : Float = 0f
        set(value)
        {
            val oldValue = field
            field = value.coerceIn(this.limitMinX, this.limitMaxX)

            if (!fr.khelp.zegaime.utils.math.equals(oldValue, field))
            {
                this.nodePositionObservableSource.value = this.position
            }
        }

    /**
     * The Y position of the node.
     */
    var y : Float = 0f
        set(value)
        {
            val oldValue = field
            field = value.coerceIn(this.limitMinY, this.limitMaxY)

            if (!fr.khelp.zegaime.utils.math.equals(oldValue, field))
            {
                this.nodePositionObservableSource.value = this.position
            }
        }

    /**
     * The Z position of the node.
     */
    var z : Float = 0f
        set(value)
        {
            val oldValue = field
            field = value.coerceIn(this.limitMinZ, this.limitMaxZ)

            if (!fr.khelp.zegaime.utils.math.equals(oldValue, field))
            {
                this.nodePositionObservableSource.value = this.position
            }
        }

    /**
     * The rotation angle around the X axis.
     */
    var angleX : Float = 0f
        set(value)
        {
            val oldValue = field
            field = value.coerceIn(this.limitMinAngleX, this.limitMaxAngleX)

            if (!fr.khelp.zegaime.utils.math.equals(oldValue, field))
            {
                this.nodePositionObservableSource.value = this.position
            }
        }

    /**
     * The rotation angle around the Y axis.
     */
    var angleY : Float = 0f
        set(value)
        {
            val oldValue = field
            field = value.coerceIn(this.limitMinAngleY, this.limitMaxAngleY)

            if (!fr.khelp.zegaime.utils.math.equals(oldValue, field))
            {
                this.nodePositionObservableSource.value = this.position
            }
        }

    /**
     * The rotation angle around the Z axis.
     */
    var angleZ : Float = 0f
        set(value)
        {
            val oldValue = field
            field = value.coerceIn(this.limitMinAngleZ, this.limitMaxAngleZ)

            if (!fr.khelp.zegaime.utils.math.equals(oldValue, field))
            {
                this.nodePositionObservableSource.value = this.position
            }
        }

    /**
     * The scale factor on the X axis.
     */
    var scaleX : Float = 1f
        set(value)
        {
            val oldValue = field
            field = value.coerceIn(this.limitMinScaleX, this.limitMaxScaleX)

            if (!fr.khelp.zegaime.utils.math.equals(oldValue, field))
            {
                this.nodePositionObservableSource.value = this.position
            }
        }

    /**
     * The scale factor on the Y axis.
     */
    var scaleY : Float = 1f
        set(value)
        {
            val oldValue = field
            field = value.coerceIn(this.limitMinScaleY, this.limitMaxScaleY)

            if (!fr.khelp.zegaime.utils.math.equals(oldValue, field))
            {
                this.nodePositionObservableSource.value = this.position
            }
        }

    /**
     * The scale factor on the Z axis.
     */
    var scaleZ : Float = 1f
        set(value)
        {
            val oldValue = field
            field = value.coerceIn(this.limitMinScaleZ, this.limitMaxScaleZ)

            if (!fr.khelp.zegaime.utils.math.equals(oldValue, field))
            {
                this.nodePositionObservableSource.value = this.position
            }
        }

    private val nodePositionObservableSource = ObservableSource<NodePosition>(NodePosition())

    /**
     * An observable that emits the node position changes.
     */
    val nodePositionObservable : Observable<NodePosition> = this.nodePositionObservableSource.observable

    /**Parent node*/
    var parent : Node? = null
        protected set

    /**
     * Indicates if the node is visible.
     */
    var visible : Boolean = true

    /**
     * Indicates if the node can be picked.
     */
    var canBePick : Boolean = true

    /**
     * The center of the node.
     */
    open val center : Point3D get() = Point3D(this.x, this.y, this.z)

    /**
     * The virtual box of the node.
     */
    open val virtualBox : VirtualBox get() = VirtualBox()
    private val children = ArrayList<Node>(8)

    /**
     * The root node of the scene graph.
     */
    val root : Node by lazy {
        var root = this

        while (root.parent != null)
        {
            root = root.parent!!
        }

        root
    }

    /**Color picking ID*/
    val colorPickingId : Int

    /**
     * The number of children of the node.
     */
    val numberOfChild : Int get() = synchronized(this.children) { this.children.size }

    /**Node Z order*/
    internal var zOrder = 0f
    internal var countInRender = false

    /**Red part of picking color*/
    private val redPicking : Float

    /**Green part of picking color*/
    private val greenPicking : Float

    /**Blue part of picking color*/
    private val bluePicking : Float

    init
    {
        this.colorPickingId = Node.ID_PICKING.getAndAccumulate(PICKING_PRECISION) { i1, i2 -> i1 + i2 }
        this.redPicking = this.colorPickingId.red / 255f
        this.greenPicking = this.colorPickingId.green / 255f
        this.bluePicking = this.colorPickingId.blue / 255f
    }

    /**
     * Adds a child node to this node.
     *
     * @param node The child node to add.
     */
    fun addChild(node : Node)
    {
        node.parent?.removeChild(node)
        node.parent = this

        synchronized(this.children)
        {
            this.children.add(node)
        }
    }

    /**
     * Removes a child node from this node.
     *
     * @param node The child node to remove.
     */
    fun removeChild(node : Node)
    {
        synchronized(this.children)
        {
            if (this.children.remove(node))
            {
                node.parent = null
            }
        }
    }

    /**
     * Removes all the children of this node.
     */
    fun removeAllChildren()
    {
        synchronized(this.children)
        {
            for (child in this.children)
            {
                child.parent = null
            }

            this.children.clear()
        }
    }

    /**
     * Returns the child node at the specified index.
     *
     * @param index The index of the child node.
     * @return The child node at the specified index.
     */
    fun child(index : Int) : Node =
        synchronized(this.children)
        {
            this.children[index]
        }

    /**
     * Returns the total virtual box of the node and its children.
     *
     * @return The total virtual box.
     */
    fun totalBox() : VirtualBox
    {
        val virtualBox = VirtualBox()
        val stack = Stack<Node>()
        stack.push(this)

        while (stack.isNotEmpty())
        {
            val node = stack.pop()
            virtualBox.add(node.projectedBox())

            synchronized(node.children)
            {
                for (child in node.children)
                {
                    stack.push(child)
                }
            }
        }

        return virtualBox
    }

    /**
     * Projects a virtual box from the node's space to the world space.
     *
     * @param box The virtual box to project.
     * @return The projected virtual box.
     */
    fun projectionPure(box : VirtualBox) : VirtualBox
    {
        val virtualBox = VirtualBox()
        virtualBox.add(this.projectionPure(Point3D(box.minX, box.minY, box.minZ)))
        virtualBox.add(this.projectionPure(Point3D(box.minX, box.minY, box.maxZ)))
        virtualBox.add(this.projectionPure(Point3D(box.minX, box.maxY, box.minZ)))
        virtualBox.add(this.projectionPure(Point3D(box.minX, box.maxY, box.maxZ)))
        virtualBox.add(this.projectionPure(Point3D(box.maxX, box.minY, box.minZ)))
        virtualBox.add(this.projectionPure(Point3D(box.maxX, box.minY, box.maxZ)))
        virtualBox.add(this.projectionPure(Point3D(box.maxX, box.maxY, box.minZ)))
        virtualBox.add(this.projectionPure(Point3D(box.maxX, box.maxY, box.maxZ)))
        return virtualBox
    }

    /**
     * Finds a node by its ID in the hierarchy of this node.
     *
     * @param id The ID of the node to find.
     * @return The node with the given ID, or `null` if it is not found.
     */
    fun <N : Node> findById(id : String) : N?
    {
        val queue = Queue<Node>()
        queue.inQueue(this)

        while (queue.isNotEmpty())
        {
            val node = queue.outQueue()

            if (id == node.id)
            {
                @Suppress("UNCHECKED_CAST")
                return node as N
            }


            synchronized(node.children)
            {
                for (child in node.children)
                {
                    queue.inQueue(child)
                }
            }
        }

        return null
    }

    /**
     * Clones the hierarchy of this node.
     *
     * @return The cloned node.
     */
    fun cloneHierarchy() : Node
    {
        val clone =
            when (this)
            {
                is Object3D    -> ObjectClone("clone_${this.id}", this)
                is ObjectClone -> ObjectClone("clone_${this.id}", this.reference)
                else           -> Node("clone_${this.id}")
            }

        clone.x = this.x
        clone.y = this.y
        clone.z = this.z
        clone.angleX = this.angleX
        clone.angleY = this.angleY
        clone.angleZ = this.angleZ
        clone.scaleX = this.scaleX
        clone.scaleY = this.scaleY
        clone.scaleZ = this.scaleZ

        if (this is NodeWithMaterial && clone is NodeWithMaterial)
        {
            clone.material = this.material
            clone.materialForSelection = this.materialForSelection
            clone.twoSidedRule = this.twoSidedRule
        }

        synchronized(this.children)
        {
            for (child in this.children)
            {
                val childClone = child.cloneHierarchy()
                childClone.parent = clone
                clone.children.add(childClone)
            }
        }
        return clone
    }

    /**
     * Applies a material to this node and its children.
     *
     * @param material The material to apply.
     * @param materialForSelection The material to apply when the node is selected.
     */
    fun applyMaterialHierarchically(material : Material, materialForSelection : Material = material)
    {
        val stack = Stack<Node>()
        stack.push(this)

        while (stack.isNotEmpty())
        {
            val node = stack.pop()

            if (node is NodeWithMaterial)
            {
                node.material = material
                node.materialForSelection = materialForSelection
            }

            for (child in node.children)
            {
                stack.push(child)
            }
        }
    }

    /**
     * Limits the X position of the node.
     *
     * @param limit1 The first limit.
     * @param limit2 The second limit.
     */
    fun limitX(limit1 : Float, limit2 : Float)
    {
        this.limitMinX = min(limit1, limit2)
        this.limitMaxX = max(limit1, limit2)
        this.x = this.x.coerceIn(this.limitMinX, this.limitMaxX)
    }

    /**
     * Limits the Y position of the node.
     *
     * @param limit1 The first limit.
     * @param limit2 The second limit.
     */
    fun limitY(limit1 : Float, limit2 : Float)
    {
        this.limitMinY = min(limit1, limit2)
        this.limitMaxY = max(limit1, limit2)
        this.y = this.y.coerceIn(this.limitMinY, this.limitMaxY)
    }

    /**
     * Limits the Z position of the node.
     *
     * @param limit1 The first limit.
     * @param limit2 The second limit.
     */
    fun limitZ(limit1 : Float, limit2 : Float)
    {
        this.limitMinZ = min(limit1, limit2)
        this.limitMaxZ = max(limit1, limit2)
        this.z = this.z.coerceIn(this.limitMinZ, this.limitMaxZ)
    }

    /**
     * Limits the rotation angle around the X axis.
     *
     * @param limit1 The first limit.
     * @param limit2 The second limit.
     */
    fun limitAngleX(limit1 : Float, limit2 : Float)
    {
        this.limitMinAngleX = min(limit1, limit2)
        this.limitMaxAngleX = max(limit1, limit2)
        this.angleX = this.angleX.coerceIn(this.limitMinAngleX, this.limitMaxAngleX)
    }

    /**
     * Limits the rotation angle around the Y axis.
     *
     * @param limit1 The first limit.
     * @param limit2 The second limit.
     */
    fun limitAngleY(limit1 : Float, limit2 : Float)
    {
        this.limitMinAngleY = min(limit1, limit2)
        this.limitMaxAngleY = max(limit1, limit2)
        this.angleY = this.angleY.coerceIn(this.limitMinAngleY, this.limitMaxAngleY)
    }

    /**
     * Limits the rotation angle around the Z axis.
     *
     * @param limit1 The first limit.
     * @param limit2 The second limit.
     */
    fun limitAngleZ(limit1 : Float, limit2 : Float)
    {
        this.limitMinAngleZ = min(limit1, limit2)
        this.limitMaxAngleZ = max(limit1, limit2)
        this.angleZ = this.angleZ.coerceIn(this.limitMinAngleZ, this.limitMaxAngleZ)
    }

    /**
     * Limits the scale factor on the X axis.
     *
     * @param limit1 The first limit.
     * @param limit2 The second limit.
     */
    fun limitScaleX(limit1 : Float, limit2 : Float)
    {
        this.limitMinScaleX = max(Node.MIN_SCALE, min(limit1, limit2))
        this.limitMaxScaleX = max(limit1, limit2)
        this.scaleX = this.scaleX.coerceIn(this.limitMinScaleX, this.limitMaxScaleX)
    }

    /**
     * Limits the scale factor on the Y axis.
     *
     * @param limit1 The first limit.
     * @param limit2 The second limit.
     */
    fun limitScaleY(limit1 : Float, limit2 : Float)
    {
        this.limitMinScaleY = max(Node.MIN_SCALE, min(limit1, limit2))
        this.limitMaxScaleY = max(limit1, limit2)
        this.scaleY = this.scaleY.coerceIn(this.limitMinScaleY, this.limitMaxScaleY)
    }

    /**
     * Limits the scale factor on the Z axis.
     *
     * @param limit1 The first limit.
     * @param limit2 The second limit.
     */
    fun limitScaleZ(limit1 : Float, limit2 : Float)
    {
        this.limitMinScaleZ = max(Node.MIN_SCALE, min(limit1, limit2))
        this.limitMaxScaleZ = max(limit1, limit2)
        this.scaleZ = this.scaleZ.coerceIn(this.limitMinScaleZ, this.limitMaxScaleZ)
    }

    /**
     * Removes the limits on the X position.
     */
    fun freeX()
    {
        this.limitMinX = Float.NEGATIVE_INFINITY
        this.limitMaxX = Float.POSITIVE_INFINITY
    }

    /**
     * Removes the limits on the Y position.
     */
    fun freeY()
    {
        this.limitMinY = Float.NEGATIVE_INFINITY
        this.limitMaxY = Float.POSITIVE_INFINITY
    }

    /**
     * Removes the limits on the Z position.
     */
    fun freeZ()
    {
        this.limitMinZ = Float.NEGATIVE_INFINITY
        this.limitMaxZ = Float.POSITIVE_INFINITY
    }

    /**
     * Removes the limits on the rotation angle around the X axis.
     */
    fun freeAngleX()
    {
        this.limitMinAngleX = Float.NEGATIVE_INFINITY
        this.limitMaxAngleX = Float.POSITIVE_INFINITY
    }

    /**
     * Removes the limits on the rotation angle around the Y axis.
     */
    fun freeAngleY()
    {
        this.limitMinAngleY = Float.NEGATIVE_INFINITY
        this.limitMaxAngleY = Float.POSITIVE_INFINITY
    }

    /**
     * Removes the limits on the rotation angle around the Z axis.
     */
    fun freeAngleZ()
    {
        this.limitMinAngleZ = Float.NEGATIVE_INFINITY
        this.limitMaxAngleZ = Float.POSITIVE_INFINITY
    }

    /**
     * Removes the limits on the scale factor on the X axis.
     */
    fun freeScaleX()
    {
        this.limitMinScaleX = Node.MIN_SCALE
        this.limitMaxScaleX = Float.POSITIVE_INFINITY
    }

    /**
     * Removes the limits on the scale factor on the Y axis.
     */
    fun freeScaleY()
    {
        this.limitMinScaleY = Node.MIN_SCALE
        this.limitMaxScaleY = Float.POSITIVE_INFINITY
    }

    /**
     * Removes the limits on the scale factor on the Z axis.
     */
    fun freeScaleZ()
    {
        this.limitMinScaleZ = Node.MIN_SCALE
        this.limitMaxScaleZ = Float.POSITIVE_INFINITY
    }

    /**
     * Compute a point projection from node space to world space
     *
     * @param point3D Point to project
     * @return Projected point
     */
    fun projection(point3D : Point3D) : Point3D
    {
        var point = point3D
        val stack = Stack<Node>()
        var node = this
        var parent = this.parent

        while (parent != null)
        {
            stack.push(node)
            node = parent
            parent = parent.parent
        }

        while (stack.isNotEmpty())
        {
            point = stack.pop()
                .projectionPure(point)
        }

        return this.projectionPure(point)
    }

    /**
     * Compute a point projection from node space to its parent space.
     *
     * @param point3D Point to project.
     * @return Projected point.
     */
    fun projectionPure(point3D : Point3D) : Point3D
    {
        var point = point3D
        point = point.add(this.x, this.y, this.z)
        var vect = point.toVect3f()
        val rotX = Rotf(Vec3f(1f, 0f, 0f), this.angleX.degreeToRadian)
        vect = rotX.rotateVector(vect)
        val rotY = Rotf(Vec3f(0f, 1f, 0f), this.angleY.degreeToRadian)
        vect = rotY.rotateVector(vect)
        val rotZ = Rotf(Vec3f(0f, 0f, 1f), this.angleZ.degreeToRadian)
        vect = rotZ.rotateVector(vect)
        return Point3D(vect)
    }

    /**
     * Compute a point projection from world space to node space
     *
     * @param point3D Point to project
     * @return Projected point
     */
    fun reverseProjection(point3D : Point3D) : Point3D
    {
        var node = this
        var parent = this.parent
        var point = point3D

        while (parent != null)
        {
            point = node.reverseProjectionPure(point3D)
            node = parent
            parent = node.parent
        }

        return point
    }

    /**
     * Compute a point projection from world space to node space, including the root node.
     *
     * For internal use only.
     *
     * @param point3D Point to project.
     * @return Projected point.
     */
    internal fun reverseProjectionWithRoot(point3D : Point3D) : Point3D
    {
        var node : Node? = this
        var point = point3D

        while (node != null)
        {
            point = node.reverseProjectionPure(point3D)
            node = node.parent
        }

        return point
    }

    /**
     * Compute a virtual box projection from world space to node space.
     *
     * @param virtualBox Virtual box to project.
     * @return Projected virtual box.
     */
    fun reverseProjection(virtualBox : VirtualBox) : VirtualBox
    {
        val box = VirtualBox()
        box.add(this.reverseProjection(Point3D(virtualBox.minX, virtualBox.minY, virtualBox.minZ)))
        box.add(this.reverseProjection(Point3D(virtualBox.minX, virtualBox.minY, virtualBox.maxZ)))
        box.add(this.reverseProjection(Point3D(virtualBox.minX, virtualBox.maxY, virtualBox.minZ)))
        box.add(this.reverseProjection(Point3D(virtualBox.minX, virtualBox.maxY, virtualBox.maxZ)))
        box.add(this.reverseProjection(Point3D(virtualBox.maxX, virtualBox.minY, virtualBox.minZ)))
        box.add(this.reverseProjection(Point3D(virtualBox.maxX, virtualBox.minY, virtualBox.maxZ)))
        box.add(this.reverseProjection(Point3D(virtualBox.maxX, virtualBox.maxY, virtualBox.minZ)))
        box.add(this.reverseProjection(Point3D(virtualBox.maxX, virtualBox.maxY, virtualBox.maxZ)))
        return box
    }

    private fun projectedBox() : VirtualBox
    {
        val box = this.virtualBox

        if (box.empty)
        {
            return box
        }

        val virtualBox = VirtualBox()
        virtualBox.add(this.projection(Point3D(box.minX, box.minY, box.minZ)))
        virtualBox.add(this.projection(Point3D(box.minX, box.minY, box.maxZ)))
        virtualBox.add(this.projection(Point3D(box.minX, box.maxY, box.minZ)))
        virtualBox.add(this.projection(Point3D(box.minX, box.maxY, box.maxZ)))
        virtualBox.add(this.projection(Point3D(box.maxX, box.minY, box.minZ)))
        virtualBox.add(this.projection(Point3D(box.maxX, box.minY, box.maxZ)))
        virtualBox.add(this.projection(Point3D(box.maxX, box.maxY, box.minZ)))
        virtualBox.add(this.projection(Point3D(box.maxX, box.maxY, box.maxZ)))
        return virtualBox
    }

    private fun reverseProjectionPure(point3D : Point3D) : Point3D
    {
        var vect = point3D.toVect3f()
        val rotZ = Rotf(Vec3f(0f, 0f, 1f), -this.angleZ.degreeToRadian)
        vect = rotZ.rotateVector(vect)
        val rotY = Rotf(Vec3f(0f, 1f, 0f), -this.angleY.degreeToRadian)
        vect = rotY.rotateVector(vect)
        val rotX = Rotf(Vec3f(1f, 0f, 0f), -this.angleX.degreeToRadian)
        vect = rotX.rotateVector(vect)
        return Point3D(vect.x - this.x, vect.y - this.y, vect.z - this.z)
    }

    /**
     * Returns an iterator over the children of this node.
     */
    override fun iterator() : Iterator<Node> =
        synchronized(this.children)
        {
            this.children.iterator()
        }

    /**
     * Locate the node in the scene.
     *
     * For internal use only.
     */
    internal fun matrix()
    {
        GL11.glTranslatef(this.x, this.y, this.z)
        GL11.glRotatef(this.angleX, 1f, 0f, 0f)
        GL11.glRotatef(this.angleY, 0f, 1f, 0f)
        GL11.glRotatef(this.angleZ, 0f, 0f, 1f)
        GL11.glScalef(this.scaleX, this.scaleY, this.scaleZ)
    }

    /**
     * Apply the matrix for to go root to this node.
     *
     * For internal use only.
     */
    internal fun matrixRootToMe()
    {
        val stack = Stack<Node>()
        var node : Node? = this

        while (node != null)
        {
            stack.push(node)
            node = node.parent
        }

        while (!stack.isEmpty())
        {
            stack.pop().matrix()
        }
    }

    /**
     * Render specific, used by sub-classes.
     *
     * For internal use only.
     */
    internal open fun renderSpecific()
    {
    }

    /**
     * Render the node for color picking.
     *
     * For internal use only.
     */
    @Synchronized
    internal fun renderTheNodePicking()
    {
        GL11.glPushMatrix()
        this.matrix()

        if (this.visible && this.canBePick)
        {
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glColor4f(this.redPicking, this.greenPicking, this.bluePicking, 1f)
            this.renderSpecificPicking()
        }

        synchronized(this.children)
        {
            for (child in this.children)
            {
                child.renderTheNodePicking()
            }
        }

        GL11.glPopMatrix()
    }

    /**
     * Render specific for color picking, used by sub-classes.
     *
     * For internal use only.
     */
    internal open fun renderSpecificPicking()
    {
    }

    /**
     * Looking for a child pick.
     *
     * For internal use only.
     *
     * @param color Picking color.
     * @return Node pick.
     */
    internal fun pickingNode(color : Color4f) : Node?
    {
        val red = color.red
        val green = color.green
        val blue = color.blue

        if (this.itIsMePick(red, green, blue))
        {
            return this
        }

        var node = this
        val stackNodes = Stack<Node>()
        stackNodes.push(node)

        while (!stackNodes.isEmpty())
        {
            node = stackNodes.pop()
            if (node.itIsMePick(red, green, blue))
            {
                return node
            }

            synchronized(node.children)
            {
                for (child in node.children)
                {
                    stackNodes.push(child)
                }
            }
        }

        return null
    }

    private fun itIsMePick(pickRed : Float, pickGreen : Float, pickBlue : Float) : Boolean =
        pickSame(this.redPicking, this.greenPicking, this.bluePicking, pickRed, pickGreen, pickBlue)

    /**
     * Returns a string representation of the node.
     *
     * @return A string representation of the node.
     */
    override fun toString() : String =
        "${this.javaClass.name} : ${this.id} : ${this.hashCode()}"
}