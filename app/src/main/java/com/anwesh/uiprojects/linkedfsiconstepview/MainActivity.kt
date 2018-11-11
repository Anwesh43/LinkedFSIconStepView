package com.anwesh.uiprojects.linkedfsiconstepview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.fsiconstepview.FSIconStepView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FSIconStepView.create(this)
    }
}
