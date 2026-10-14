package com.kimo.mayapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class AfghanServicesActivity : AppCompatActivity() {

    private val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
    private val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT

    // رنگ‌های افغان بیسیم
    private val NEON = "#29B6FF"
    private val DEEP = "#0D47A1"
    private val PAGE = "#0A1020"
    private val CARD = "#0F1626"
    private val GOLD = "#FFD54F"

    private var lang = "fa"
    private var isRtl = true

    override fun attachBaseContext(newBase: Context?) {
        val p = newBase?.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val l = p?.getString(SELECTED_LANGUAGE, "fa") ?: "fa"
        val locale = Locale(l)
        Locale.setDefault(locale)
        val config = Configuration(newBase!!.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        lang = Strings.currentLang(this)
        isRtl = lang != "en"
        window.statusBarColor = c(PAGE)
        window.navigationBarColor = c(PAGE)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(c(PAGE))
            layoutDirection = if (isRtl) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
        }

        // سربرگ آبی
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(8), dp(12), dp(16), dp(12))
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(c(DEEP), c("#1E88E5"), c(NEON))
            ).apply { cornerRadius = dp(20).toFloat(); setStroke(dp(1), c("#80D8FF")) }
            elevation = dp(8).toFloat()
            glow(this)
        }
        header.addView(TextView(this).apply {
            text = if (isRtl) "→" else "←"
            textSize = 22f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setOnClickListener { finish() }
        }, LinearLayout.LayoutParams(dp(44), dp(44)))
        header.addView(TextView(this).apply {
            text = pick("خدمات افغان بیسیم", "Afghan Wireless Services", "د افغان بېسیم خدمتونه")
            textSize = 20f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(Color.WHITE)
        }, LinearLayout.LayoutParams(0, WRAP, 1f).apply { marginStart = dp(6) })
        root.addView(header, LinearLayout.LayoutParams(MATCH, WRAP).apply { setMargins(dp(10), dp(10), dp(10), dp(6)) })

        val list = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), dp(6), dp(12), dp(24))
        }

        // ۱. دقیقه باقیمانده
        list.addView(card(
            pick("دقیقه باقیمانده", "Remaining Minutes", "پاتې دقیقې"),
            codeBox("*123#"),
            actionButton(pick("بررسی دقیقه باقیمانده", "Check Remaining", "پاتې دقیقې وګورئ")) { dial("*123#") }
        ))

        // ۲. ارسال کریدیت
        list.addView(card(
            pick("ارسال کریدیت", "Send Credit", "کریډیټ لېږل"),
            codeBox(pick("*444*1*070xxxxxxx*مبلغ#", "*444*1*070xxxxxxx*Amount#", "*444*1*070xxxxxxx*مقدار#")),
            note(pick(
                "شماره گیرنده و مبلغ را به‌جای x و «مبلغ» بنویسید.",
                "Replace x with the receiver's number and enter the amount.",
                "د ترلاسه کوونکي شمېره او مقدار ولیکئ."
            )),
            actionButton(pick("باز کردن شماره‌گیر", "Open Dialer", "ډایلر پرانیزئ")) { dial("*444*1*") }
        ))

        // ۳. تنظیمات APN
        list.addView(card(
            pick("تنظیمات APN", "APN Settings", "د APN تنظیمات"),
            kvRow("Name", "AWCC"),
            divider(),
            kvRow("APN", "Internet")
        ))

        // ۴. اضافه کردن کریدیت
        list.addView(card(
            pick("اضافه کردن کریدیت", "Add Credit", "کریډیټ اضافه کول"),
            codeBox("*124*xxxxxxxxxxxxxx#"),
            note(pick(
                "به‌جای x شمارهٔ کارت کریدیت را بنویسید.",
                "Replace x with your recharge card number.",
                "د x پر ځای د کارت شمېره ولیکئ."
            )),
            actionButton(pick("باز کردن شماره‌گیر", "Open Dialer", "ډایلر پرانیزئ")) { dial("*124*") }
        ))

        // ۵. فعال‌سازی 4G/3G
        list.addView(card(
            pick("فعال‌سازی 4G/3G", "4G/3G Activation", "4G/3G فعالول"),
            TextView(this).apply {
                text = "✓  " + pick("برای تمام سیم‌کارت‌ها فعال است", "Active on all SIM cards", "په ټولو سیم‌کارټونو فعال دی")
                textSize = 15f
                setTypeface(typeface, Typeface.BOLD)
                setTextColor(c("#69F0AE"))
                gravity = Gravity.CENTER
                setPadding(dp(12), dp(14), dp(12), dp(14))
                background = rounded(c("#0E2A22"), 14, c("#2E7D5B"))
            }
        ))

        val scroll = ScrollView(this).apply { isVerticalScrollBarEnabled = false; addView(list) }
        root.addView(scroll, LinearLayout.LayoutParams(MATCH, 0, 1f))
        setContentView(root)
    }

    // ---------------- ساخت کارت ----------------

    private fun card(title: String, vararg body: View): View {
        val outer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR, intArrayOf(alpha(NEON, 0x22), c(CARD), c("#0B111F"))
            ).apply { cornerRadius = dp(20).toFloat(); setStroke(dp(1), alpha(NEON, 0x88)) }
            elevation = dp(6).toFloat()
            glow(this)
            clipToOutline = true
            outlineProvider = android.view.ViewOutlineProvider.BACKGROUND
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { setMargins(0, dp(7), 0, dp(7)) }
        }
        outer.addView(TextView(this).apply {
            text = title
            textSize = 18f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(Color.WHITE)
            setPadding(dp(18), dp(14), dp(18), dp(14))
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(c(DEEP), c("#1E88E5"), c(NEON))
            )
        }, LinearLayout.LayoutParams(MATCH, WRAP))
        val inner = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(14), dp(16), dp(16))
        }
        body.forEach { inner.addView(it) }
        outer.addView(inner)
        return outer
    }

    private fun codeBox(code: String): View = TextView(this).apply {
        text = code
        textSize = 20f
        setTypeface(typeface, Typeface.BOLD)
        setTextColor(c(GOLD))
        textDirection = View.TEXT_DIRECTION_LTR
        gravity = Gravity.CENTER
        setPadding(dp(12), dp(10), dp(12), dp(10))
        background = rounded(c("#14203A"), 14, c(GOLD))
        setOnLongClickListener { copy(code); true }
    }

    private fun note(t: String): View = TextView(this).apply {
        text = t
        textSize = 13f
        setTextColor(c("#9FB0D0"))
        setPadding(dp(2), dp(10), dp(2), 0)
    }

    private fun actionButton(t: String, click: () -> Unit): View = TextView(this).apply {
        text = t
        textSize = 16f
        setTypeface(typeface, Typeface.BOLD)
        setTextColor(Color.WHITE)
        gravity = Gravity.CENTER
        setPadding(0, dp(13), 0, dp(13))
        background = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(c(DEEP), c(NEON))
        ).apply { cornerRadius = dp(16).toFloat() }
        elevation = dp(4).toFloat()
        glow(this)
        setOnClickListener { click() }
        layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(14) }
    }

    private fun kvRow(k: String, v: String): View {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutDirection = View.LAYOUT_DIRECTION_LTR
            setPadding(dp(2), dp(8), dp(2), dp(8))
        }
        row.addView(TextView(this).apply {
            text = "$k:"
            textSize = 16f
            setTextColor(c("#9FB0D0"))
        }, LinearLayout.LayoutParams(0, WRAP, 1f))
        row.addView(TextView(this).apply {
            text = v
            textSize = 17f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(c(GOLD))
        })
        row.addView(TextView(this).apply {
            text = "📋"
            textSize = 18f
            setTextColor(Color.BLACK)
            setPadding(dp(14), 0, 0, 0)
            setOnClickListener { copy(v) }
        })
        return row
    }

    private fun divider(): View = View(this).apply {
        setBackgroundColor(alpha(NEON, 0x40))
        layoutParams = LinearLayout.LayoutParams(MATCH, dp(1))
    }

    // ---------------- کارها ----------------

    private fun dial(code: String) {
        try {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(code))))
        } catch (e: Exception) {
            Toast.makeText(this, pick("شماره‌گیر پیدا نشد", "Dialer not found", "ډایلر ونه موندل شو"), Toast.LENGTH_SHORT).show()
        }
    }

    private fun copy(t: String) {
        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.setPrimaryClip(ClipData.newPlainText("code", t))
        Toast.makeText(this, pick("کپی شد: ", "Copied: ", "کاپي شو: ") + t, Toast.LENGTH_SHORT).show()
    }

    // ---------------- ابزارها ----------------

    private fun glow(v: View) {
        if (Build.VERSION.SDK_INT >= 28) {
            v.outlineAmbientShadowColor = c(NEON)
            v.outlineSpotShadowColor = c(NEON)
        }
    }

    private fun rounded(fill: Int, r: Int, stroke: Int?) = GradientDrawable().apply {
        cornerRadius = dp(r).toFloat(); setColor(fill); if (stroke != null) setStroke(dp(1), stroke)
    }

    private fun c(h: String) = Color.parseColor(h)
    private fun alpha(h: String, a: Int): Int {
        val x = c(h); return Color.argb(a, Color.red(x), Color.green(x), Color.blue(x))
    }
    private fun pick(fa: String, en: String, ps: String) = when (lang) { "en" -> en; "ps" -> ps; else -> fa }
    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    companion object {
        private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    }
}