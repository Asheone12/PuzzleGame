package com.muen.puzzlegame

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    companion object{
        private var screenWidth = 0
        private var screenHeight = 0
        fun getScreenWidth(): Int {
            return screenWidth
        }

        fun getScreenHeight(): Int {
            return screenHeight
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(1)
        window.setFlags(1024, 1024)
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(metrics)
        screenWidth = metrics.widthPixels
        screenHeight = metrics.heightPixels
        setContentView(R.layout.activity_main)
    }



}