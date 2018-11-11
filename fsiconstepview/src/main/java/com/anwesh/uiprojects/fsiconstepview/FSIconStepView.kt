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

fun Int.getInverse() : Float = 1f / this

fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.getInverse(), Math.max(0f, this - n.getInverse() * i)) * n

fun Float.getScaleFactor() : Float = Math.floor(this/0.5).toFloat()

fun Float.updateScale(dir : Float, a : Int, b : Int) : Float {
    val sf : Float = getScaleFactor()
    return scGap * dir * ((1 - sf) * a.getInverse() + sf * b.getInverse())
} 