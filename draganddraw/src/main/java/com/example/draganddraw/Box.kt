package com.example.draganddraw

import android.graphics.PointF
import kotlin.math.min
import kotlin.math.max

data class Box(val start: PointF) {

    var end: PointF = start

    val left: Float
        get() = min(start.x, end.x)

    val right: Float
        get() = max(start.x, end.x)

    val top: Float
        get() = min(start.y, end.y)

    val bottom: Float
        get() = max(start.y, end.y)
}
