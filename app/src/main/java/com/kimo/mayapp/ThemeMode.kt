package com.kimo.mayapp

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color

object ThemeMode {

    const val AUTO = 0
    const val LIGHT = 1
    const val DARK = 2

    private const val PREFS = "AppSettings"
    private const val KEY = "theme_mode"

    fun get(ctx: Context): Int =
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(KEY, DARK)

    fun set(ctx: Context, mode: Int) {
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putInt(KEY, mode).apply()
    }

    fun isDark(ctx: Context): Boolean = when (get(ctx)) {
        LIGHT -> false
        DARK -> true
        else -> (ctx.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
            Configuration.UI_MODE_NIGHT_YES
    }

    fun palette(ctx: Context) = Palette(isDark(ctx))
}

class Palette(val dark: Boolean) {
    private fun c(h: String) = Color.parseColor(h)

    val bg = if (dark) c("#0A1020") else c("#F4F7FB")
    val sysNav = if (dark) c("#0C1324") else Color.WHITE
    val navTop = if (dark) c("#0E1628") else Color.WHITE
    val navBottom = if (dark) c("#0A1020") else c("#F8FAFC")
    val card = if (dark) c("#0F1626") else Color.WHITE
    val card2 = if (dark) c("#0B111F") else c("#F8FAFC")
    val text = if (dark) Color.WHITE else c("#1F2937")
    val sub = if (dark) c("#C9D3EA") else c("#475569")
    val muted = if (dark) c("#7F8BA6") else c("#64748B")
    val searchIcon = if (dark) c("#9FB0D0") else c("#64748B")
    val searchBg = if (dark) c("#111A2E") else Color.WHITE
    val stroke = if (dark) Color.argb(0x1F, 255, 255, 255) else c("#E2E8F0")
    val chip = if (dark) Color.argb(0x12, 255, 255, 255) else c("#F1F5F9")
    val icon = if (dark) Color.WHITE else c("#1F2937")

    // منو
    val panel = if (dark) c("#111827") else Color.WHITE
    val divider = if (dark) c("#1F2937") else c("#E5E7EB")
    val segBg = if (dark) c("#0B1220") else c("#EEF2F7")
    val themeCard = if (dark) c("#1A2333") else c("#F8FAFC")
}