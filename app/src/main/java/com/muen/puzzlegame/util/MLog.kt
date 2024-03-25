package com.muen.puzzlegame.util

import android.util.Log


object MLog {
    private const val PROCESS = "Puzzle"
    private const val DEBUG = true
    fun d(tag: String, log: String?) {
        if (DEBUG) {
            Log.d(PROCESS + "_" + tag, log!!)
        }
    }

    fun i(tag: String, log: String?) {
        if (DEBUG) {
            Log.i(PROCESS + "_" + tag, log!!)
        }
    }

    fun w(tag: String, log: String?) {
        if (DEBUG) {
            Log.w(PROCESS + "_" + tag, log!!)
        }
    }

    fun e(tag: String, log: String?) {
        if (DEBUG) {
            Log.e(PROCESS + "_" + tag, log!!)
        }
    }
}