package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.formatk3d.data.RobotPositionData
import fr.khelp.zegaime.utils.extensions.modulo

/**
 * Robot position creator
 */
class RobotPositionCreator
{
    /** Neck X angle */
    var neckAngleX : Float = 0f
        set(value)
        {
            field = value.coerceIn(-45f, 45f)
        }
    /** Neck Y angle */
    var neckAngleY : Float = 0f
        set(value)
        {
            field = value.coerceIn(-90f, 90f)
        }
    /** Neck Z angle */
    var neckAngleZ : Float = 0f
        set(value)
        {
            field = value.coerceIn(-22f, 22f)
        }

    //

    /** Right shoulder X angle */
    var rightShoulderAngleX : Float = 180f
        set(value)
        {
            field = value.modulo(0f, 360f)
        }
    /** Right shoulder Z angle */
    var rightShoulderAngleZ : Float = 0f
        set(value)
        {
            field = value.coerceIn(0f, 180f)
        }

    //

    /** Right elbow X angle */
    var rightElbowAngleX : Float = 0f
        set(value)
        {
            field = value.coerceIn(-150f, 0f)
        }

    //

    /** Left shoulder X angle */
    var leftShoulderAngleX : Float = 180f
        set(value)
        {
            field = value.modulo(0f, 360f)
        }
    /** Left shoulder Z angle */
    var leftShoulderAngleZ : Float = 0f
        set(value)
        {
            field = value.coerceIn(-180f, 0f)
        }

    //

    /** Left elbow X angle */
    var leftElbowAngleX : Float = 0f
        set(value)
        {
            field = value.coerceIn(-150f, 0f)
        }

    //

    /** Right ass X angle */
    var rightAssAngleX : Float = 180f
        set(value)
        {
            field = value.coerceIn(90f, 270f)
        }
    /** Right ass Z angle */
    var rightAssAngleZ : Float = 0f
        set(value)
        {
            field = value.coerceIn(-30f, 90f)
        }

    //

    /** Right knee X angle */
    var rightKneeAngleX : Float = 0f
        set(value)
        {
            field = value.coerceIn(0f, 150f)
        }

    //

    /** Left ass X angle */
    var leftAssAngleX : Float = 180f
        set(value)
        {
            field = value.coerceIn(90f, 270f)
        }
    /** Left ass Z angle */
    var leftAssAngleZ : Float = 0f
        set(value)
        {
            field = value.coerceIn(-90f, 30f)
        }

    //

    /** Left knee X angle */
    var leftKneeAngleX : Float = 0f
        set(value)
        {
            field = value.coerceIn(0f, 150f)
        }

    /** Robot position data for save */
    val robotPositionData : RobotPositionData
        get() =
            RobotPositionData(neckAngleX = this.neckAngleX,
                              neckAngleY = this.neckAngleY,
                              neckAngleZ = this.neckAngleZ,
                //
                              rightShoulderAngleX = this.rightShoulderAngleX,
                              rightShoulderAngleZ = this.rightShoulderAngleZ,
                //
                              rightElbowAngleX = this.rightElbowAngleX,
                //
                              leftShoulderAngleX = this.leftShoulderAngleX,
                              leftShoulderAngleZ = this.leftShoulderAngleZ,
                //
                              leftElbowAngleX = this.leftElbowAngleX,
                //
                              rightAssAngleX = this.rightAssAngleX,
                              rightAssAngleZ = this.rightAssAngleZ,
                //
                              rightKneeAngleX = this.rightKneeAngleX,
                //
                              leftAssAngleX = this.leftAssAngleX,
                              leftAssAngleZ = this.leftAssAngleZ,
                //
                              leftKneeAngleX = this.leftKneeAngleX)
}