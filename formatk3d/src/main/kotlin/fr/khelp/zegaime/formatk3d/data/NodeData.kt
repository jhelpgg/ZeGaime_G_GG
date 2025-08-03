package fr.khelp.zegaime.formatk3d.data

import kotlinx.serialization.Serializable

@Serializable
open class NodeData(val name : String, val nodeType : NodeType,
                    val positionData : NodePositionData, val limitData : NodeLimitData,
                    val children : List<NodeData>,
                    val nodeWithMaterialData : NodeWithMaterialData? = null, // Available if type is OBJECT, CLONE, PLANE, BOX, SPHERE or REVOLUTION
                    val objectData : ObjectData? = null, // Available only if type is OBJECT
                    val objectCloneData : ObjectCloneData? = null, // Available only if type is CLONE
                    val planeData : PlaneData? = null, // Available only if type is PLANE
                    val boxData : BoxData? = null,  // Available only if type is BOX
                    val sphereData : SphereData? = null, // Available only if type is SPHERE
                    val revolutionData : RevolutionData? = null, // Available only if type is REVOLUTION
                    val diceData : DiceData? = null, // Available only if type is DICE
                    val swordData : SwordData? = null, // Available only if type is SWORD
                    val robotData : RobotData? = null // Available only if type is ROBOT
                   )
