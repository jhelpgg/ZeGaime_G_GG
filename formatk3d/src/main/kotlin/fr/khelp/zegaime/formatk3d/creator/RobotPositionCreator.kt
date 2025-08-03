package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.formatk3d.data.RobotPositionData
import fr.khelp.zegaime.utils.extensions.modulo

class RobotPositionCreator
{
    var neckAngleX : Float = 0f
        set(value)
        {
            field = value.coerceIn(-45f, 45f)
        }
    var neckAngleY : Float = 0f
        set(value)
        {
            field = value.coerceIn(-90f, 90f)
        }
    var neckAngleZ : Float = 0f
        set(value)
        {
            field = value.coerceIn(-22f, 22f)
        }

    //

    var rightShoulderAngleX : Float = 180f
        set(value)
        {
            field = value.modulo(0f, 360f)
        }
    var rightShoulderAngleZ : Float = 0f
        set(value)
        {
            field = value.coerceIn(0f, 180f)
        }

    //

    var rightElbowAngleX : Float = 0f
        set(value)
        {
            field = value.coerceIn(-150f, 0f)
        }

    //

    var leftShoulderAngleX : Float = 180f
        set(value)
        {
            field = value.modulo(0f, 360f)
        }
    var leftShoulderAngleZ : Float = 0f
        set(value)
        {
            field = value.coerceIn(-180f, 0f)
        }

    //

    var leftElbowAngleX : Float = 0f
        set(value)
        {
            field = value.coerceIn(-150f, 0f)
        }

    //

    var rightAssAngleX : Float = 180f
        set(value)
        {
            field = value.coerceIn(90f, 270f)
        }
    var rightAssAngleZ : Float = 0f
        set(value)
        {
            field = value.coerceIn(-30f, 90f)
        }

    //

    var rightKneeAngleX : Float = 0f
        set(value)
        {
            field = value.coerceIn(0f, 150f)
        }

    //

    var leftAssAngleX : Float = 180f
        set(value)
        {
            field = value.coerceIn(90f, 270f)
        }
    var leftAssAngleZ : Float = 0f
        set(value)
        {
            field = value.coerceIn(-90f, 30f)
        }

    //

    var leftKneeAngleX : Float = 0f
        set(value)
        {
            field = value.coerceIn(0f, 150f)
        }

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