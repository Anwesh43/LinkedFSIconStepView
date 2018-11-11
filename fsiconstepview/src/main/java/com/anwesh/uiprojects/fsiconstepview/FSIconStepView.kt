package com.anwesh.uiprojects.fsiconstepview

/**
 * Created by anweshmishra on 11/11/18.
 */

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.content.Context
import android.app.Activity

val nodes : Int = 5

val lines : Int = 4

val parts : Int = 2

val scGap : Float = 0.05f

val FRONT_COLOR : Int = Color.WHITE

val BACK_GROUND : Int = Color.parseColor("#311B92")

val STROKE_FACTOR : Int = 60

val SIZE_FACTOR : Int = 3

val FS_SIZE_FACTOR : Int = 5

fun Int.getInverse() : Float = 1f / this

fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.getInverse(), Math.max(0f, this - n.getInverse() * i)) * n

fun Float.getScaleFactor() : Float = Math.floor(this/0.5).toFloat()

fun Float.updateScale(dir : Float, a : Int, b : Int) : Float {
    val sf : Float = getScaleFactor()
    return scGap * dir * ((1 - sf) * a.getInverse() + sf * b.getInverse())
}


fun Canvas.drawFSISNode(i : Int, scale : Float, paint : Paint) {
    val w  : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = w / (nodes + 1)
    val size : Float = gap / SIZE_FACTOR
    val lSize : Float = size / FS_SIZE_FACTOR
    val deg : Float = 360f / lines
    paint.color = FRONT_COLOR
    paint.strokeWidth = Math.min(w, h) / STROKE_FACTOR
    paint.strokeCap = Paint.Cap.ROUND
    val sc1 : Float = scale.divideScale(0, 2)
    val sc2 : Float = scale.divideScale(1, 2)
    save()
    translate(gap * (i + 1), h/2)
    rotate(90f * sc2)
    for (j in 0..(lines - 1)) {
        val sc : Float = sc1.divideScale(j, lines)
        save()
        rotate(deg * j)
        translate(size, size)
        for (k in 0..parts - 1) {
            val sck : Float = sc.divideScale(k, parts)
            save()
            rotate(90f * k)
            drawLine(0f, 0f, -lSize * sc, 0f, paint)
            restore()
        }
        restore()
    }
    restore()
}