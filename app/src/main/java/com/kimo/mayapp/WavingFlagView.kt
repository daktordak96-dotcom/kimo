package com.kimo.mayapp

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.text.TextPaint
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * پرچمی که مثل پارچه در باد موج می‌خورد.
 * showPole = true  ← پرچم روی میله نصب می‌شود
 */
class WavingFlagView(context: Context, private val showPole: Boolean) : View(context) {

    private val MW = 24
    private val MH = 12
    private val verts = FloatArray((MW + 1) * (MH + 1) * 2)
    private val colors = IntArray((MW + 1) * (MH + 1))

    private var flag: Bitmap? = null
    private var currentEmoji = ""
    private var phase = 0f
    private var animator: ValueAnimator? = null

    private val bmpPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    private val polePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val knobPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#E0B43A") }
    private val rect = RectF()

    fun setEmoji(emoji: String) {
        if (emoji == currentEmoji && flag != null) return
        currentEmoji = emoji
        flag = renderEmoji(emoji)
        invalidate()
    }

    // ایموجی پرچم را به تصویر تبدیل می‌کند و حاشیهٔ خالی را می‌برد
    private fun renderEmoji(emoji: String): Bitmap? {
        val p = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 200f
            color = Color.BLACK
        }
        val w = Math.ceil(p.measureText(emoji).toDouble()).toInt()
        val fm = p.fontMetricsInt
        val h = fm.bottom - fm.top
        if (w <= 0 || h <= 0) return null

        val full = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        Canvas(full).drawText(emoji, 0f, -fm.top.toFloat(), p)

        val px = IntArray(w * h)
        full.getPixels(px, 0, w, 0, 0, w, h)
        var minX = w
        var minY = h
        var maxX = -1
        var maxY = -1
        for (y in 0 until h) {
            for (x in 0 until w) {
                if ((px[y * w + x] ushr 24) > 10) {
                    if (x < minX) minX = x
                    if (x > maxX) maxX = x
                    if (y < minY) minY = y
                    if (y > maxY) maxY = y
                }
            }
        }
        if (maxX < minX || maxY < minY) return full
        return Bitmap.createBitmap(full, minX, minY, maxX - minX + 1, maxY - minY + 1)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator = ValueAnimator.ofFloat(0f, (Math.PI * 2).toFloat()).apply {
            duration = 1800
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                phase = it.animatedValue as Float
                if (visibility == View.VISIBLE) invalidate()
            }
            start()
        }
    }

    override fun onDetachedFromWindow() {
        animator?.cancel()
        animator = null
        super.onDetachedFromWindow()
    }

    override fun onDraw(canvas: Canvas) {
        val bmp = flag ?: return
        val d = resources.displayMetrics.density
        val vw = width.toFloat()
        val vh = height.toFloat()
        if (vw <= 0f || vh <= 0f) return

        val poleW = if (showPole) 5f * d else 0f
        val poleGap = if (showPole) 1.5f * d else 0f
        val knobSpace = if (showPole) 10f * d else 0f
        val amp = vh * 0.07f

        val availW = vw - poleW - poleGap
        val availH = vh - amp * 2f - knobSpace
        val ratio = bmp.width.toFloat() / bmp.height
        var dw = availW
        var dh = dw / ratio
        if (dh > availH) {
            dh = availH
            dw = dh * ratio
        }

        val groupW = poleW + poleGap + dw
        val startX = (vw - groupW) / 2f
        val flagLeft = startX + poleW + poleGap
        val top = knobSpace + (vh - knobSpace - dh) / 2f

        // میله و سرِ طلایی
        if (showPole) {
            polePaint.shader = LinearGradient(
                startX, 0f, startX + poleW, 0f,
                intArrayOf(Color.parseColor("#8A94A3"), Color.parseColor("#EEF1F5"), Color.parseColor("#6B7280")),
                null, Shader.TileMode.CLAMP
            )
            rect.set(startX, top - 4f * d, startX + poleW, vh)
            canvas.drawRoundRect(rect, poleW / 2f, poleW / 2f, polePaint)
            canvas.drawCircle(startX + poleW / 2f, top - 6f * d, 4.5f * d, knobPaint)
        }

        // شبکهٔ موج
        var i = 0
        for (r in 0..MH) {
            val fy = r.toFloat() / MH
            for (cIdx in 0..MW) {
                val t = cIdx.toFloat() / MW
                val ang = (t * 2.4f * Math.PI).toFloat() - phase
                val s = Math.sin(ang.toDouble()).toFloat()
                val co = Math.cos(ang.toDouble()).toFloat()

                verts[i * 2] = flagLeft + t * dw - (1f - co) * amp * 0.12f * t
                verts[i * 2 + 1] = top + fy * dh + s * amp * t

                var shade = 1f - t * (0.10f + 0.10f * co)
                if (shade > 1f) shade = 1f
                if (shade < 0.75f) shade = 0.75f
                val v = (255 * shade).toInt()
                colors[i] = Color.rgb(v, v, v)
                i++
            }
        }
        canvas.drawBitmapMesh(bmp, MW, MH, verts, 0, colors, 0, bmpPaint)
    }
}