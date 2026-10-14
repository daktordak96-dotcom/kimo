package com.kimo.mayapp

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import java.util.Locale

private const val MI_SETTINGS = 0
private const val MI_HEART = 1
private const val MI_SEARCH = 2
private const val MI_INFO = 3
private const val MI_SHARE = 4
private const val MI_STAR = 5
private const val MI_POWER = 6
private const val MI_FLAG = 7
private const val MI_MAIL = 8
private const val MI_MOON = 9
private const val MI_WHATSAPP = 10
private const val MI_LOCK = 11

class MenuActivity : Activity() {

    private val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
    private val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT

    private val CONTACT_EMAIL = "strimosafr@gmail.com"
    private val WHATSAPP_GROUP = "https://chat.whatsapp.com/KP27TAkZ5qfH9Ri3NGmiHq"

    private var lang = "fa"
    private var isRtl = true
    private var panelW = 0
    private var closing = false

    private lateinit var pal: Palette
    private lateinit var root: FrameLayout
    private lateinit var panel: LinearLayout
    private lateinit var dim: View

    override fun attachBaseContext(newBase: Context?) {
        val p = newBase?.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val l = p?.getString(SELECTED_LANGUAGE, "fa") ?: "fa"
        super.attachBaseContext(updateLocale(newBase!!, l))
    }

    private fun updateLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)

        lang = Strings.currentLang(this)
        isRtl = lang != "en"
        panelW = (resources.displayMetrics.widthPixels * 0.82f).toInt()

        root = FrameLayout(this)
        setContentView(root)
        build(true)
    }

    // ساخت کامل منو (با تغییر پوسته دوباره ساخته می‌شود)
    private fun build(animate: Boolean) {
        pal = ThemeMode.palette(this)
        closing = false
        root.removeAllViews()

        dim = View(this).apply {
            setBackgroundColor(Color.parseColor("#99000000"))
            alpha = if (animate) 0f else 1f
            setOnClickListener { close() }
        }
        root.addView(dim, FrameLayout.LayoutParams(MATCH, MATCH))

        panel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(pal.panel)
            layoutDirection = if (isRtl) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
            isClickable = true
            elevation = dp(16).toFloat()
            setPadding(0, 0, 0, dp(12))
        }
        root.addView(panel, FrameLayout.LayoutParams(panelW, MATCH, if (isRtl) Gravity.RIGHT else Gravity.LEFT))

        panel.addView(buildHeader(), LinearLayout.LayoutParams(MATCH, WRAP))

        val scroll = ScrollView(this).apply { isVerticalScrollBarEnabled = false }
        val list = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, dp(4), 0, dp(12))
        }

        list.addView(themeCard(), LinearLayout.LayoutParams(MATCH, WRAP).apply {
            setMargins(dp(16), dp(14), dp(16), dp(8))
        })

        list.addView(item(MI_HEART, pick("علاقه‌مندی‌ها", "Favorites", "خوښې"), "#FDECEF", "#E5486A") {
            open(FavoritesActivity::class.java)
        })
        list.addView(item(MI_SEARCH, pick("جستجوی بسته‌ها", "Search Packages", "د بستو لټون"), "#E8EEFC", "#4A6CF7") {
            open(SearchActivity::class.java)
        })
        list.addView(item(MI_FLAG, pick("کد کشورها", "Country Codes", "د هېوادونو کوډونه"), "#E6F5E9", "#3BAA57") {
            open(CountryCodesActivity::class.java)
        })
        list.addView(item(
            MI_LOCK,
            pick("گالری خصوصی", "Private Gallery", "شخصي ګالري"),
            "#EFE9FF", "#7C5CFF",
            subtitle = pick("عکس‌های شما با رمز", "Your photos, PIN-locked", "ستاسو انځورونه د رمز سره")
        ) {
            open(VaultActivity::class.java)
        })
        list.addView(item(MI_SETTINGS, pick("تنظیمات", "Settings", "تنظیمات"), "#FFF4E0", "#F5A623") {
            open(SettingsActivity::class.java)
        })

        list.addView(divider())

        list.addView(item(
            MI_WHATSAPP,
            pick("گروه واتساپ", "WhatsApp Group", "د واټساپ ګروپ"),
            "#E7F8EE", "#25D366",
            subtitle = pick("نظر، انتقاد و پیشنهاد", "Feedback & suggestions", "نظر، نیوکه او وړاندیز")
        ) {
            openWhatsAppGroup()
        })
        list.addView(item(MI_INFO, pick("درباره برنامه", "About", "د اپلیکیشن په اړه"), "#EEF2F7", "#64748B") {
            showAbout()
        })
        list.addView(item(MI_MAIL, pick("ارتباط با ما", "Contact Us", "اړیکه ونیسئ"), "#FDE7E7", "#E05252") {
            contactUs()
        })
        list.addView(item(MI_STAR, pick("امتیاز به برنامه", "Rate the App", "اپلیکیشن ته نمره"), "#FFF6DC", "#E0A800") {
            rateApp()
        })
        list.addView(item(MI_SHARE, pick("اشتراک‌گذاری", "Share", "شریکول"), "#E7F1FD", "#3B8BEB") {
            shareApp()
        })

        scroll.addView(list)
        panel.addView(scroll, LinearLayout.LayoutParams(MATCH, 0, 1f))

        panel.addView(divider())
        panel.addView(item(MI_POWER, pick("خروج", "Exit", "وتل"), "#FDE7E7", "#D84343", Color.parseColor("#D84343")) {
            finishAffinity()
        })

        if (animate) {
            panel.translationX = if (isRtl) panelW.toFloat() else -panelW.toFloat()
            panel.animate().translationX(0f).setDuration(260)
                .setInterpolator(DecelerateInterpolator()).start()
            dim.animate().alpha(1f).setDuration(260).start()
        }
    }

    override fun onBackPressed() {
        close()
    }

    private fun close() {
        if (closing) return
        closing = true
        val target = if (isRtl) panelW.toFloat() else -panelW.toFloat()
        dim.animate().alpha(0f).setDuration(200).start()
        panel.animate().translationX(target).setDuration(200).withEndAction {
            finish()
            overridePendingTransition(0, 0)
        }.start()
    }

    // ---------------- سربرگ سبز ----------------

    private fun buildHeader(): View {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(36), dp(22), dp(26))
            background = HeaderBg(dp(44).toFloat(), isRtl)
        }

        val logo = ImageView(this).apply {
            setImageResource(R.mipmap.ic_launcher)
            scaleType = ImageView.ScaleType.CENTER_CROP
            background = GradientDrawable().apply {
                cornerRadius = dp(18).toFloat()
                setColor(Color.WHITE)
            }
            outlineProvider = ViewOutlineProvider.BACKGROUND
            clipToOutline = true
            elevation = dp(4).toFloat()
        }
        box.addView(logo, LinearLayout.LayoutParams(dp(64), dp(64)))

        box.addView(TextView(this).apply {
            text = Strings.get(this@MenuActivity, "app_title")
            textSize = 20f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(Color.WHITE)
        }, LinearLayout.LayoutParams(WRAP, WRAP).apply { topMargin = dp(14) })

        box.addView(TextView(this).apply {
            text = pick("نسخه ", "Version ", "نسخه ") + versionName()
            textSize = 13f
            setTextColor(Color.argb(0xDD, 255, 255, 255))
        }, LinearLayout.LayoutParams(WRAP, WRAP).apply { topMargin = dp(4) })

        return box
    }

    // ---------------- کارت پوسته (روشن / خودکار / تیره) ----------------

    private fun themeCard(): View {
        val mode = ThemeMode.get(this)

        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(14), dp(12), dp(14), dp(14))
            background = GradientDrawable().apply {
                cornerRadius = dp(20).toFloat()
                setColor(pal.themeCard)
                setStroke(dp(1), pal.divider)
            }
        }

        val top = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        top.addView(iconBox(MI_MOON, "#E6F5E9", "#3BAA57"), LinearLayout.LayoutParams(dp(44), dp(44)))

        val texts = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        texts.addView(TextView(this).apply {
            text = pick("پوسته", "Theme", "بڼه")
            textSize = 17f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(pal.text)
        })
        texts.addView(TextView(this).apply {
            text = modeName(mode)
            textSize = 13f
            setTextColor(pal.muted)
        })
        top.addView(texts, LinearLayout.LayoutParams(0, WRAP, 1f).apply { marginStart = dp(14) })
        card.addView(top)

        val seg = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(dp(4), dp(4), dp(4), dp(4))
            background = GradientDrawable().apply {
                cornerRadius = dp(16).toFloat()
                setColor(pal.segBg)
            }
        }

        val options = listOf(
            ThemeMode.LIGHT to "☀️ " + pick("روشن", "Light", "روښانه"),
            ThemeMode.AUTO to "A  " + pick("خودکار", "Auto", "اتومات"),
            ThemeMode.DARK to "🌙 " + pick("تیره", "Dark", "تیاره")
        )
        for ((m, label) in options) {
            val selected = m == mode
            seg.addView(TextView(this).apply {
                text = label
                textSize = 13f
                gravity = Gravity.CENTER
                setTypeface(typeface, Typeface.BOLD)
                maxLines = 1
                setPadding(0, dp(9), 0, dp(9))
                setTextColor(if (selected) Color.WHITE else pal.text)
                background = if (selected) GradientDrawable().apply {
                    cornerRadius = dp(12).toFloat()
                    setColor(Color.parseColor("#4FA853"))
                } else null
                setOnClickListener {
                    if (m != ThemeMode.get(this@MenuActivity)) {
                        ThemeMode.set(this@MenuActivity, m)
                        build(false)
                    }
                }
            }, LinearLayout.LayoutParams(0, WRAP, 1f))
        }
        card.addView(seg, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(12) })

        return card
    }

    private fun modeName(mode: Int): String = when (mode) {
        ThemeMode.LIGHT -> pick("حالت روشن", "Light mode", "روښانه حالت")
        ThemeMode.DARK -> pick("حالت تیره", "Dark mode", "تیاره حالت")
        else -> pick("خودکار، مطابق تنظیم گوشی", "Auto, follows your phone", "اتومات، د تلیفون له مخې")
    }

    // ---------------- گزینه‌های منو ----------------

    private fun iconBox(icon: Int, bg: String, fg: String): View {
        val fgColor = Color.parseColor(fg)
        val box = FrameLayout(this).apply {
            background = GradientDrawable().apply {
                cornerRadius = dp(14).toFloat()
                setColor(
                    if (pal.dark) Color.argb(0x2E, Color.red(fgColor), Color.green(fgColor), Color.blue(fgColor))
                    else Color.parseColor(bg)
                )
            }
        }
        box.addView(ImageView(this).apply {
            setImageDrawable(MenuIcon(icon, fgColor))
        }, FrameLayout.LayoutParams(dp(26), dp(26), Gravity.CENTER))
        return box
    }

    private fun item(
        icon: Int,
        label: String,
        bg: String,
        fg: String,
        textColor: Int? = null,
        subtitle: String? = null,
        click: () -> Unit
    ): View {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(20), dp(10), dp(20), dp(10))
            isClickable = true
            isFocusable = true
            background = RippleDrawable(
                ColorStateList.valueOf(if (pal.dark) Color.parseColor("#26FFFFFF") else Color.parseColor("#1A000000")),
                null,
                ColorDrawable(Color.WHITE)
            )
            setOnClickListener { click() }
        }

        row.addView(iconBox(icon, bg, fg), LinearLayout.LayoutParams(dp(48), dp(48)))

        val texts = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        texts.addView(TextView(this).apply {
            text = label
            textSize = 17f
            setTextColor(textColor ?: pal.text)
            setTypeface(typeface, Typeface.BOLD)
        })
        if (subtitle != null) {
            texts.addView(TextView(this).apply {
                text = subtitle
                textSize = 12f
                setTextColor(pal.muted)
            }, LinearLayout.LayoutParams(WRAP, WRAP).apply { topMargin = dp(1) })
        }
        row.addView(texts, LinearLayout.LayoutParams(0, WRAP, 1f).apply { marginStart = dp(16) })

        return row
    }

    private fun divider(): View = View(this).apply {
        setBackgroundColor(pal.divider)
        layoutParams = LinearLayout.LayoutParams(MATCH, dp(1)).apply {
            setMargins(dp(20), dp(8), dp(20), dp(8))
        }
    }

    // ---------------- کارها ----------------

    private fun open(target: Class<*>) {
        startActivity(Intent(this, target))
        finish()
        overridePendingTransition(0, 0)
    }

    private fun openWhatsAppGroup() {
        val uri = Uri.parse(WHATSAPP_GROUP)
        try {
            startActivity(Intent(Intent.ACTION_VIEW, uri).setPackage("com.whatsapp"))
        } catch (e: ActivityNotFoundException) {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            } catch (e2: ActivityNotFoundException) {
                Toast.makeText(
                    this,
                    pick("واتساپ پیدا نشد", "WhatsApp not found", "واټساپ ونه موندل شو"),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showAbout() {
        val msg = pick(
            "این برنامه به‌صورت مستقل ساخته شده و وابسته به هیچ‌یک از اپراتورهای مخابراتی نیست.\n\nاطلاعات بسته‌ها ممکن است تغییر کند؛ برای اطمینان، با اپراتور خود چک کنید.\n\nارتباط با ما:\n$CONTACT_EMAIL",
            "This app is independent and is not affiliated with any mobile operator.\n\nPackage details may change; please confirm with your operator.\n\nContact us:\n$CONTACT_EMAIL",
            "دا اپلیکیشن خپلواک دی او له هېڅ مخابراتي اپراتور سره تړاو نه لري.\n\nد بستو معلومات ښايي بدل شي؛ د ډاډ لپاره له خپل اپراتور سره یې وګورئ.\n\nاړیکه:\n$CONTACT_EMAIL"
        )
        val style = if (pal.dark) android.R.style.Theme_DeviceDefault_Dialog_Alert
        else android.R.style.Theme_DeviceDefault_Light_Dialog_Alert
        AlertDialog.Builder(this, style)
            .setTitle(Strings.get(this, "app_title"))
            .setMessage(msg)
            .setPositiveButton(pick("باشه", "OK", "سمه ده"), null)
            .show()
    }

    private fun contactUs() {
        val mail = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$CONTACT_EMAIL")
            putExtra(Intent.EXTRA_SUBJECT, Strings.get(this@MenuActivity, "app_title"))
        }
        try {
            startActivity(mail)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, CONTACT_EMAIL, Toast.LENGTH_LONG).show()
        }
    }

    private fun rateApp() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
        }
    }

    private fun shareApp() {
        val link = "https://play.google.com/store/apps/details?id=$packageName"
        val send = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, Strings.get(this@MenuActivity, "app_title") + "\n" + link)
        }
        startActivity(Intent.createChooser(send, pick("اشتراک‌گذاری", "Share", "شریکول")))
    }

    // ---------------- ابزارها ----------------

    private fun versionName(): String = try {
        packageManager.getPackageInfo(packageName, 0).versionName ?: "1.0"
    } catch (e: Exception) {
        "1.0"
    }

    private fun pick(fa: String, en: String, ps: String) = when (lang) {
        "en" -> en
        "ps" -> ps
        else -> fa
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    companion object {
        private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    }

    // ---------------- پس‌زمینهٔ سبز سربرگ ----------------

    private class HeaderBg(private val radius: Float, private val rtl: Boolean) : Drawable() {
        private val p = Paint(Paint.ANTI_ALIAS_FLAG)
        private val path = Path()
        private val rect = RectF()

        override fun draw(canvas: Canvas) {
            val b = bounds
            val w = b.width().toFloat()
            val h = b.height().toFloat()
            rect.set(b)

            val radii = if (rtl)
                floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, radius, radius)
            else
                floatArrayOf(0f, 0f, 0f, 0f, radius, radius, 0f, 0f)

            path.reset()
            path.addRoundRect(rect, radii, Path.Direction.CW)
            canvas.save()
            canvas.clipPath(path)

            p.style = Paint.Style.FILL
            p.shader = LinearGradient(
                b.left.toFloat(), b.top.toFloat(), b.right.toFloat(), b.bottom.toFloat(),
                Color.parseColor("#86CF89"), Color.parseColor("#4FA853"), Shader.TileMode.CLAMP
            )
            canvas.drawRect(rect, p)
            p.shader = null

            p.color = Color.argb(0x26, 255, 255, 255)
            canvas.drawCircle(b.left + w * 0.90f, b.top + h * 0.10f, h * 0.38f, p)
            canvas.drawCircle(b.left + w * 0.75f, b.top + h * 0.98f, h * 0.45f, p)

            canvas.restore()
        }

        override fun setAlpha(alpha: Int) { p.alpha = alpha }
        override fun setColorFilter(colorFilter: ColorFilter?) { p.colorFilter = colorFilter }
        override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
    }

    // ---------------- آیکون‌های منو (روی شبکهٔ ۲۴×۲۴ کشیده می‌شوند) ----------------

    private class MenuIcon(private val type: Int, color: Int) : Drawable() {
        private val stroke = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
            this.color = color
        }
        private val fill = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            this.color = color
        }
        private val path = Path()
        private val r = RectF()

        override fun draw(canvas: Canvas) {
            val b = bounds
            val s = minOf(b.width(), b.height()) / 24f
            canvas.save()
            canvas.translate(b.left + (b.width() - 24 * s) / 2f, b.top + (b.height() - 24 * s) / 2f)
            canvas.scale(s, s)
            stroke.strokeWidth = 2f
            path.reset()

            when (type) {
                MI_SETTINGS -> {
                    canvas.drawCircle(12f, 12f, 3f, stroke)
                    for (i in 0 until 8) {
                        val a = Math.toRadians(i * 45.0)
                        val x1 = 12f + 6.5f * Math.cos(a).toFloat()
                        val y1 = 12f + 6.5f * Math.sin(a).toFloat()
                        val x2 = 12f + 9.5f * Math.cos(a).toFloat()
                        val y2 = 12f + 9.5f * Math.sin(a).toFloat()
                        canvas.drawLine(x1, y1, x2, y2, stroke)
                    }
                    canvas.drawCircle(12f, 12f, 6.5f, stroke)
                }
                MI_HEART -> {
                    path.moveTo(12f, 20f)
                    path.cubicTo(4f, 14.5f, 2.5f, 10f, 3.5f, 7f)
                    path.cubicTo(5f, 3.5f, 10f, 3.5f, 12f, 7.5f)
                    path.cubicTo(14f, 3.5f, 19f, 3.5f, 20.5f, 7f)
                    path.cubicTo(21.5f, 10f, 20f, 14.5f, 12f, 20f)
                    path.close()
                    canvas.drawPath(path, stroke)
                }
                MI_SEARCH -> {
                    canvas.drawCircle(10.5f, 10.5f, 6.5f, stroke)
                    canvas.drawLine(15.5f, 15.5f, 20.5f, 20.5f, stroke)
                }
                MI_INFO -> {
                    canvas.drawCircle(12f, 12f, 9f, stroke)
                    canvas.drawLine(12f, 11f, 12f, 16.5f, stroke)
                    canvas.drawCircle(12f, 7.6f, 1.2f, fill)
                }
                MI_SHARE -> {
                    canvas.drawCircle(18f, 5.5f, 2.5f, stroke)
                    canvas.drawCircle(6f, 12f, 2.5f, stroke)
                    canvas.drawCircle(18f, 18.5f, 2.5f, stroke)
                    canvas.drawLine(8.2f, 10.8f, 15.8f, 6.7f, stroke)
                    canvas.drawLine(8.2f, 13.2f, 15.8f, 17.3f, stroke)
                }
                MI_STAR -> {
                    for (i in 0 until 10) {
                        val rad = if (i % 2 == 0) 9.5f else 4f
                        val a = Math.toRadians(-90.0 + i * 36.0)
                        val x = 12f + rad * Math.cos(a).toFloat()
                        val y = 12.5f + rad * Math.sin(a).toFloat()
                        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    }
                    path.close()
                    canvas.drawPath(path, stroke)
                }
                MI_POWER -> {
                    r.set(4f, 4f, 20f, 20f)
                    canvas.drawArc(r, -60f, 300f, false, stroke)
                    canvas.drawLine(12f, 2.5f, 12f, 11f, stroke)
                }
                MI_FLAG -> {
                    canvas.drawLine(5f, 3f, 5f, 21f, stroke)
                    path.moveTo(5f, 4f)
                    path.lineTo(19f, 4f)
                    path.lineTo(16f, 8.5f)
                    path.lineTo(19f, 13f)
                    path.lineTo(5f, 13f)
                    canvas.drawPath(path, stroke)
                }
                MI_MAIL -> {
                    r.set(3f, 5.5f, 21f, 18.5f)
                    canvas.drawRoundRect(r, 2f, 2f, stroke)
                    path.moveTo(3.5f, 6.5f)
                    path.lineTo(12f, 12.5f)
                    path.lineTo(20.5f, 6.5f)
                    canvas.drawPath(path, stroke)
                }
                MI_MOON -> {
                    path.moveTo(15f, 3.5f)
                    path.cubicTo(8f, 4.5f, 6f, 13f, 10f, 17f)
                    path.cubicTo(13.5f, 20.5f, 18.5f, 19.5f, 20.5f, 15.5f)
                    path.cubicTo(14.5f, 16.5f, 10.5f, 10f, 15f, 3.5f)
                    path.close()
                    canvas.drawPath(path, stroke)
                }
                MI_WHATSAPP -> {
                    r.set(3.5f, 3.5f, 20.5f, 20.5f)
                    canvas.drawArc(r, 135f, 315f, false, stroke)
                    path.moveTo(6f, 18f)
                    path.lineTo(3f, 21f)
                    path.lineTo(7.5f, 19.8f)
                    canvas.drawPath(path, stroke)
                    path.reset()
                    path.moveTo(9f, 8.5f)
                    path.cubicTo(9f, 12f, 12f, 15f, 15.5f, 15f)
                    canvas.drawPath(path, stroke)
                }
                MI_LOCK -> {
                    r.set(4.5f, 10.5f, 19.5f, 20.5f)
                    canvas.drawRoundRect(r, 2.5f, 2.5f, stroke)
                    r.set(8f, 3.5f, 16f, 13.5f)
                    canvas.drawArc(r, 180f, 180f, false, stroke)
                    canvas.drawLine(8f, 8.5f, 8f, 10.5f, stroke)
                    canvas.drawLine(16f, 8.5f, 16f, 10.5f, stroke)
                    canvas.drawCircle(12f, 15f, 1.4f, fill)
                    canvas.drawLine(12f, 15.5f, 12f, 17.5f, stroke)
                }
            }
            canvas.restore()
        }

        override fun setAlpha(alpha: Int) {
            stroke.alpha = alpha
            fill.alpha = alpha
        }
        override fun setColorFilter(colorFilter: ColorFilter?) {
            stroke.colorFilter = colorFilter
            fill.colorFilter = colorFilter
        }
        override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
    }
}