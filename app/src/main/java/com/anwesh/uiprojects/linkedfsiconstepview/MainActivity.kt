package com.anwesh.uiprojects.linkedfsiconstepview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.anwesh.uiprojects.fsiconstepview.FSIconStepView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view : FSIconStepView = FSIconStepView.create(this)
        fullScreen()
        view.addOnAnimationListener({
            createToast("animation number $it is complete")
        }, {
            createToast("animation number $it is reset")
        })
    }

    private fun createToast(msg : String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

fun MainActivity.fullScreen() {
    supportActionBar?.hide()
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}