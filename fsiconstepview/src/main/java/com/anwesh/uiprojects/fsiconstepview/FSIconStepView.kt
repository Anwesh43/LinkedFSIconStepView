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
import android.util.Log

val nodes : Int = 5

val lines : Int = 4

val parts : Int = 2

val scGap : Float = 0.05f

val FRONT_COLOR : Int = Color.WHITE

val BACK_GROUND : Int = Color.parseColor("#311B92")

val STROKE_FACTOR : Int = 100

val SIZE_FACTOR : Int = 3

val FS_SIZE_FACTOR : Int = 3

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
            drawLine(0f, 0f, -lSize * sck, 0f, paint)
            restore()
        }
        restore()
    }
    restore()
}

class FSIconStepView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val renderer : Renderer = Renderer(this)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            val k : Float = scale.updateScale(dir, lines * parts, 1)
            scale += k
            Log.d("update scale is", "$k")
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class FSISNode(var i : Int, val state : State = State()) {

        private var next : FSISNode? = null

        private var prev : FSISNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < nodes - 1) {
                next = FSISNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawFSISNode(i, state.scale, paint)
            prev?.draw(canvas, paint)
        }

        fun update(cb : (Int, Float) -> Unit) {
            state.update {
                cb(i, it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : FSISNode {
            var curr : FSISNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class FSIconStep(var i : Int) {
        private var curr : FSISNode = FSISNode(0)

        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            curr.draw(canvas, paint)
        }

        fun update(cb : (Int, Float) -> Unit) {
            curr.update {i, scl ->
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                cb(i, scl)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            curr.startUpdating(cb)
        }
    }

    data class Renderer(var view : FSIconStepView) {
        private val animator : Animator = Animator(view)

        private val fsis : FSIconStep = FSIconStep(0)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(BACK_GROUND)
            fsis.draw(canvas, paint)
            animator.animate {
                fsis.update {i, scl ->
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            fsis.startUpdating {
                animator.start()
            }
        }
    }

    companion object {
        fun create(activity : Activity) : FSIconStepView {
            val view : FSIconStepView = FSIconStepView(activity)
            activity.setContentView(view)
            return view
        }
    }
}