package com.kimo.mayapp

import android.util.Log

object AppLogger {
    private val logBuffer = StringBuilder()
    var onLogListener: ((String) -> Unit)? = null

    fun log(tag: String, message: String) {
        Log.d(tag, message)
        logBuffer.insert(0, "[$tag] $message\n")
        onLogListener?.invoke(logBuffer.toString())
    }

    fun getLogs(): String = logBuffer.toString()
}
