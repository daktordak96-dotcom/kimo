package com.kimo.mayapp

import android.app.Activity
import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// در اندروید ۱۵ به بعد، محتوای همه صفحه‌ها را از زیر نوار وضعیت و نوار پایین بیرون می‌آورد
class EdgeToEdgeInit : ContentProvider() {

    override fun onCreate(): Boolean {
        if (Build.VERSION.SDK_INT >= 35) {
            val app = context?.applicationContext as? Application
            app?.registerActivityLifecycleCallbacks(Callbacks)
        }
        return true
    }

    override fun query(
        uri: Uri, projection: Array<out String>?, selection: String?,
        selectionArgs: Array<out String>?, sortOrder: String?
    ): Cursor? = null

    override fun getType(uri: Uri): String? = null
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0

    private object Callbacks : Application.ActivityLifecycleCallbacks {
        override fun onActivityStarted(activity: Activity) {
            val decor = activity.window?.decorView as? ViewGroup ?: return
            val root = decor.getChildAt(0) ?: return
            ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
                val bars = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
                )
                val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
                v.setPadding(bars.left, bars.top, bars.right, maxOf(bars.bottom, ime.bottom))
                WindowInsetsCompat.CONSUMED
            }
            ViewCompat.requestApplyInsets(root)
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
        override fun onActivityResumed(activity: Activity) {}
        override fun onActivityPaused(activity: Activity) {}
        override fun onActivityStopped(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        override fun onActivityDestroyed(activity: Activity) {}
    }
}
