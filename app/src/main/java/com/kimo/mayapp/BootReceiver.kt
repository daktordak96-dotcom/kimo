package com.kimo.mayapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != null) {
            if (intent.action == Intent.ACTION_BOOT_COMPLETED || 
                intent.action == "android.intent.action.QUICKBOOT_POWERON") {
                Log.d("BootReceiver", "گوشی روشن شد و سرویس پیامک آماده به کار است.")
            }
        }
    }
}
