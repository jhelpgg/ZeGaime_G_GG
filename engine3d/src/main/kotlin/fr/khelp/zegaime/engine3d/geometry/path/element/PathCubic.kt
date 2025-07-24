package fr.khelp.zegaime.engine3d.geometry.path.element

data class PathCubic(var control1X : Float, var control1Y : Float,
                     var control2X : Float, var control2Y : Float,
                     var x : Float, var y : Float) : PathElement()