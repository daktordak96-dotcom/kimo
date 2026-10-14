package com.kimo.mayapp

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.CornerPathEffect
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable

/**
 * لوگوی اتوما: دو شورون (فلش) سه‌بعدی؛ فلش فیروزه‌ای نیمه‌شفاف جلوی فلش آبی تیره.
 * شفافیت کل لوگو را از بیرون با drawable.alpha (0 تا 255) تنظیم کنید.
 */
class AtomaLogoDrawable : Drawable() {

    private var a = 255
    private var cf: ColorFilter? = null

    private val rect = RectF()

    // مسیرهای نهایی (با گوشه‌ی گرد)
    private val backPath = Path()
    private val frontPath = Path()
    private val shadowNear = Path()
    private val shadowFar = Path()

    private val backPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val frontPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val edgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE }

    // یک شورون (فلش >) با ضخامت مشخص
    private fun chevron(x: Float, y: Float, w: Float, h: Float, thick: Float): Path {
        val p = Path()
        p.moveTo(x, y)
        p.lineTo(x + thick, y)
        p.lineTo(x + w, y + h / 2f)
        p.lineTo(x + thick, y + h)
        p.lineTo(x, y + h)
        p.lineTo(x + w - thick, y + h / 2f)
        p.close()
        return p
    }

    // گرد کردن گوشه‌ها و برگرداندن مسیر نهایی
    private fun rounded(src: Path, radius: Float): Path {
        val helper = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            pathEffect = CornerPathEffect(radius)
        }
        val out = Path()
        helper.getFillPath(src, out)
        return out
    }

    override fun onBoundsChange(b: Rect) {
        super.onBoundsChange(b)
        if (b.isEmpty) return

        val s = minOf(b.width(), b.height()).toFloat()
        val ox = b.left + (b.width() - s) / 2f
        val oy = b.top + (b.height() - s) / 2f
        val r = s * 0.07f

        rect.set(b)

        // فلش پشتی (آبی تیره، کوچک‌تر، سمت چپ)
        val back = chevron(ox, oy + s * 0.12f, s * 0.60f, s * 0.76f, s * 0.28f)
        // فلش جلویی (فیروزه‌ای، بزرگ‌تر، سمت راست)
        val front = chevron(ox + s * 0.30f, oy, s * 0.70f, s, s * 0.34f)

        backPath.set(rounded(back, r))
        frontPath.set(rounded(front, r))

        // سایه‌ی فلش جلویی روی فلش پشتی (دو لایه برای نرمی)
        shadowNear.set(frontPath)
        shadowNear.offset(-s * 0.018f, s * 0.022f)
        shadowFar.set(frontPath)
        shadowFar.offset(-s * 0.040f, s * 0.050f)

        // فلش پشتی: گرادیان آبی روشن (بالا) به آبی خیلی تیره (پایین)
        backPaint.shader = LinearGradient(
            0f, oy, 0f, oy + s,
            intArrayOf(0xFF3A4FFF.toInt(), 0xFF2236F0.toInt(), 0xFF1A1FB0.toInt()),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )

        // فلش جلویی: گرادیان فیروزه‌ای نیمه‌شفاف (چپ‌بالا روشن، راست‌پایین عمیق‌تر)
        frontPaint.shader = LinearGradient(
            ox, oy, ox + s, oy + s,
            intArrayOf(0xCC2BD8FF.toInt(), 0xBB00C8FF.toInt(), 0xB000A8EE.toInt()),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )

        shadowPaint.color = 0x55000A50
        edgePaint.strokeWidth = s * 0.014f
        edgePaint.shader = LinearGradient(
            ox, oy, ox + s, oy + s,
            intArrayOf(0x99FFFFFF.toInt(), 0x1AFFFFFF.toInt()),
            null,
            Shader.TileMode.CLAMP
        )
    }

    override fun draw(canvas: Canvas) {
        if (bounds.isEmpty) return

        // کل لوگو یک‌جا با شفافیت مشخص رسم می‌شود تا ناحیه‌ی هم‌پوشانی خراب نشود
        val save = canvas.saveLayerAlpha(rect, a)

        // ۱) فلش پشتی
        canvas.drawPath(backPath, backPaint)

        // ۲) سایه‌ی فلش جلویی فقط روی فلش پشتی
        canvas.save()
        canvas.clipPath(backPath)
        shadowPaint.alpha = 0x30
        canvas.drawPath(shadowFar, shadowPaint)
        shadowPaint.alpha = 0x55
        canvas.drawPath(shadowNear, shadowPaint)
        canvas.restore()

        // ۳) فلش جلویی (نیمه‌شفاف) + لبه‌ی روشن برای حس برجستگی
        canvas.drawPath(frontPath, frontPaint)
        canvas.drawPath(frontPath, edgePaint)

        canvas.restoreToCount(save)
    }

    override fun setAlpha(alpha: Int) {
        a = alpha
        invalidateSelf()
    }

    override fun getAlpha(): Int = a

    override fun setColorFilter(colorFilter: ColorFilter?) {
        cf = colorFilter
        backPaint.colorFilter = colorFilter
        frontPaint.colorFilter = colorFilter
        shadowPaint.colorFilter = colorFilter
        edgePaint.colorFilter = colorFilter
        invalidateSelf()
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
}