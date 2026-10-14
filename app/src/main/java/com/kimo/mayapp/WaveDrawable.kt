package com.kimo.mayapp

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable

/**
 * پس‌زمینهٔ موج‌دار متحرک برای دکمه‌ها (روشن، اتوما، سلام).
 * با تغییر offset (۰ تا ۱) رنگ‌ها نرم در طول دکمه حرکت می‌کنند.
 */
class WaveDrawable(
    private val colors: IntArray,
    private val radius: Float = 200f
) : Drawable() {

    var offset = 0f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val shine = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rect = RectF()
    private val m = Matrix()
    private var shader: LinearGradient? = null
    private var lastW = -1f

    override fun draw(canvas: Canvas) {
        val b = bounds
        val w = b.width().toFloat()
        val h = b.height().toFloat()
        if (w <= 0f || h <= 0f) return
        rect.set(b)

        // گرادیان دو برابر عرض دکمه که آینه‌ای تکرار می‌شود
        if (shader == null || lastW != w) {
            val cols = if (colors.size >= 2) colors else intArrayOf(colors.firstOrNull() ?: 0xFF00897B.toInt(), colors.firstOrNull() ?: 0xFF00897B.toInt())
            shader = LinearGradient(0f, 0f, w * 2f, 0f, cols, null, Shader.TileMode.MIRROR)
            lastW = w
        }
        m.setTranslate(b.left - offset * w * 4f, 0f)
        shader?.setLocalMatrix(m)
        paint.shader = shader

        val r = minOf(radius, h / 2f)
        canvas.drawRoundRect(rect, r, r, paint)

        // برق ملایم روی نیمهٔ بالایی
        shine.shader = LinearGradient(0f, b.top.toFloat(), 0f, b.top + h * 0.6f,
            0x30FFFFFF, 0x00FFFFFF, Shader.TileMode.CLAMP)
        canvas.drawRoundRect(rect, r, r, shine)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
        invalidateSelf()
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
        invalidateSelf()
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
}