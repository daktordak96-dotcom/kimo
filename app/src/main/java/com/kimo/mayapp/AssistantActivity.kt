package com.kimo.mayapp

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import java.util.Locale

/** دستیار آفلاین برنامه */
class AssistantActivity : Activity() {

    private val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
    private val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT

    private val SHEET = Color.parseColor("#0E1328")
    private val SHEET2 = Color.parseColor("#171E3D")
    private val ACCENT = Color.parseColor("#7C5CFF")
    private val ACCENT2 = Color.parseColor("#2EC9E6")
    private val TEXT = Color.parseColor("#F2F4FF")
    private val SUB = Color.parseColor("#94A0C8")

    private lateinit var lang: String
    private lateinit var dim: View
    private lateinit var sheet: LinearLayout
    private lateinit var msgs: LinearLayout
    private lateinit var scroll: ScrollView
    private lateinit var input: EditText
    private var typingView: View? = null
    private var closing = false
    private val ui = Handler(Looper.getMainLooper())

    private fun t(fa: String, en: String, ps: String) = when (lang) { "en" -> en; "ps" -> ps; else -> fa }
    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
    private fun dpf(v: Float) = v * resources.displayMetrics.density

    private class Act(val label: String, val run: () -> Unit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        lang = Strings.currentLang(this)

        val root = FrameLayout(this).apply {
            layoutDirection = if (lang == "en") View.LAYOUT_DIRECTION_LTR else View.LAYOUT_DIRECTION_RTL
        }
        dim = View(this).apply {
            setBackgroundColor(Color.parseColor("#AA000000"))
            alpha = 0f
            setOnClickListener { close() }
        }
        root.addView(dim, FrameLayout.LayoutParams(MATCH, MATCH))

        sheet = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            isClickable = true
            background = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(SHEET2, SHEET)).apply {
                val r = dpf(28f)
                cornerRadii = floatArrayOf(r, r, r, r, 0f, 0f, 0f, 0f)
            }
            elevation = dpf(20f)
        }
        val topGap = (resources.displayMetrics.heightPixels * 0.18f).toInt()
        root.addView(sheet, FrameLayout.LayoutParams(MATCH, MATCH).apply { topMargin = topGap })

        buildSheet()
        setContentView(root)

        sheet.translationY = resources.displayMetrics.heightPixels.toFloat()
        sheet.animate().translationY(0f).setDuration(380).setInterpolator(DecelerateInterpolator()).start()
        dim.animate().alpha(1f).setDuration(300).start()

        ui.postDelayed({ welcome() }, 350)
    }

    override fun onBackPressed() { close() }

    private fun close() {
        if (closing) return
        closing = true
        hideKeyboard()
        dim.animate().alpha(0f).setDuration(220).start()
        sheet.animate().translationY(sheet.height.toFloat()).setDuration(240).withEndAction {
            finish()
            overridePendingTransition(0, 0)
        }.start()
    }

    // ---------------- ساختار صفحه ----------------
    private fun buildSheet() {
        sheet.addView(View(this).apply {
            background = GradientDrawable().apply { cornerRadius = dpf(3f); setColor(Color.argb(0x44, 255, 255, 255)) }
        }, LinearLayout.LayoutParams(dp(42), dp(5)).apply { gravity = Gravity.CENTER_HORIZONTAL; topMargin = dp(10) })

        val head = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(18), dp(12), dp(14), dp(12))
        }
        val avatar = TextView(this).apply {
            text = "🤖"
            textSize = 24f
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
            background = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(ACCENT, ACCENT2)).apply {
                shape = GradientDrawable.OVAL
            }
        }
        head.addView(avatar, LinearLayout.LayoutParams(dp(48), dp(48)))
        ValueAnimator.ofFloat(-1f, 1f).apply {
            duration = 1600
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener { avatar.rotation = (it.animatedValue as Float) * 6f }
            start()
        }

        val names = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        names.addView(TextView(this).apply {
            text = t("دستیار سیم کارت 27", "SIM Card 27 Assistant", "د سیم کارټ 27 مرستیال")
            textSize = 17f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(TEXT)
        })
        val status = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        status.addView(View(this).apply {
            background = GradientDrawable().apply { shape = GradientDrawable.OVAL; setColor(Color.parseColor("#3BE38B")) }
        }, LinearLayout.LayoutParams(dp(8), dp(8)).apply { marginEnd = dp(6) })
        status.addView(TextView(this).apply {
            text = t("آماده • بدون نیاز به اینترنت", "Ready • works offline", "چمتو • له انټرنټ پرته")
            textSize = 12f
            setTextColor(SUB)
        })
        names.addView(status)
        head.addView(names, LinearLayout.LayoutParams(0, WRAP, 1f).apply { marginStart = dp(12) })

        head.addView(TextView(this).apply {
            text = "✕"
            textSize = 16f
            setTextColor(TEXT)
            gravity = Gravity.CENTER
            background = GradientDrawable().apply { shape = GradientDrawable.OVAL; setColor(Color.argb(0x1F, 255, 255, 255)) }
            setOnClickListener { close() }
        }, LinearLayout.LayoutParams(dp(38), dp(38)))
        sheet.addView(head)

        sheet.addView(View(this).apply { setBackgroundColor(Color.argb(0x1A, 255, 255, 255)) },
            LinearLayout.LayoutParams(MATCH, dp(1)))

        msgs = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(14), dp(12), dp(14), dp(12))
        }
        scroll = ScrollView(this).apply {
            isVerticalScrollBarEnabled = false
            addView(msgs)
        }
        sheet.addView(scroll, LinearLayout.LayoutParams(MATCH, 0, 1f))

        val chips = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(dp(10), dp(4), dp(10), dp(4))
        }
        val sugg = listOf(
            t("📶 بستهٔ انترنت", "📶 Internet packages", "📶 د انټرنټ بستې"),
            t("💰 چک بیلانس", "💰 Check balance", "💰 بیلانس کتل"),
            t("📞 بستهٔ تماس", "📞 Call packages", "📞 د زنګ بستې"),
            t("🌍 کد کشورها", "🌍 Country codes", "🌍 د هېوادونو کوډونه"),
            t("📜 فال حافظ", "📜 Hafez fal", "📜 د حافظ فال"),
            t("🔒 گالری خصوصی", "🔒 Private gallery", "🔒 شخصي ګالري"),
            t("❓ چه کارهایی می‌کنی؟", "❓ What can you do?", "❓ ته څه کولای شې؟")
        )
        for (s in sugg) {
            chips.addView(TextView(this).apply {
                text = s
                textSize = 13f
                setTextColor(TEXT)
                setPadding(dp(14), dp(8), dp(14), dp(8))
                background = GradientDrawable().apply {
                    cornerRadius = dpf(18f)
                    setColor(Color.argb(0x1A, 255, 255, 255))
                    setStroke(dp(1), Color.argb(0x40, 124, 92, 255))
                }
                setOnClickListener { send(s) }
            }, LinearLayout.LayoutParams(WRAP, WRAP).apply { setMargins(dp(4), 0, dp(4), 0) })
        }
        sheet.addView(HorizontalScrollView(this).apply {
            isHorizontalScrollBarEnabled = false
            addView(chips)
        }, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(4) })

        val bar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(12), dp(8), dp(12), dp(14))
        }
        input = EditText(this).apply {
            hint = t("سؤالتان را بنویسید…", "Type your question…", "خپله پوښتنه ولیکئ…")
            setHintTextColor(SUB)
            setTextColor(TEXT)
            textSize = 15f
            setPadding(dp(16), dp(11), dp(16), dp(11))
            background = GradientDrawable().apply {
                cornerRadius = dpf(24f)
                setColor(Color.argb(0x1C, 255, 255, 255))
                setStroke(dp(1), Color.argb(0x30, 255, 255, 255))
            }
            imeOptions = EditorInfo.IME_ACTION_SEND
            setSingleLine(true)
            setOnEditorActionListener { _, id, ev ->
                if (id == EditorInfo.IME_ACTION_SEND || ev?.keyCode == KeyEvent.KEYCODE_ENTER) {
                    sendTyped(); true
                } else false
            }
        }
        bar.addView(input, LinearLayout.LayoutParams(0, WRAP, 1f))
        bar.addView(TextView(this).apply {
            text = "➤"
            rotation = if (lang == "en") 0f else 180f
            textSize = 18f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            background = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(ACCENT, ACCENT2)).apply {
                shape = GradientDrawable.OVAL
            }
            setOnClickListener { sendTyped() }
        }, LinearLayout.LayoutParams(dp(46), dp(46)).apply { marginStart = dp(8) })
        sheet.addView(bar)
    }

    // ---------------- پیام‌ها ----------------
    private fun sendTyped() {
        val s = input.text.toString().trim()
        if (s.isEmpty()) return
        input.setText("")
        send(s)
    }

    private fun send(text: String) {
        userMsg(text)
        showTyping()
        ui.postDelayed({
            removeTyping()
            answer(text)
        }, 700)
    }

    private fun userMsg(text: String) {
        val tv = TextView(this).apply {
            this.text = text
            textSize = 15f
            setTextColor(Color.WHITE)
            setPadding(dp(14), dp(10), dp(14), dp(10))
            background = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(ACCENT, Color.parseColor("#9A6CFF"))).apply {
                val r = dpf(18f)
                cornerRadii = floatArrayOf(r, r, r, r, dpf(4f), dpf(4f), r, r)
            }
            maxWidth = (resources.displayMetrics.widthPixels * 0.75f).toInt()
        }
        addMsg(tv, Gravity.END)
    }

    private fun botMsg(text: String, acts: List<Act> = emptyList()) {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(14), dp(11), dp(14), dp(12))
            background = GradientDrawable().apply {
                val r = dpf(18f)
                cornerRadii = floatArrayOf(r, r, r, r, r, r, dpf(4f), dpf(4f))
                setColor(Color.argb(0x1F, 255, 255, 255))
                setStroke(dp(1), Color.argb(0x22, 255, 255, 255))
            }
        }
        box.addView(TextView(this).apply {
            this.text = text
            textSize = 15f
            setTextColor(TEXT)
            setLineSpacing(dpf(3f), 1f)
            maxWidth = (resources.displayMetrics.widthPixels * 0.75f).toInt()
        })
        for (a in acts) {
            box.addView(TextView(this).apply {
                this.text = a.label
                textSize = 14f
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(Color.WHITE)
                gravity = Gravity.CENTER
                setPadding(dp(14), dp(9), dp(14), dp(9))
                background = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(ACCENT, ACCENT2)).apply {
                    cornerRadius = dpf(16f)
                }
                setOnClickListener { a.run() }
            }, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(8) })
        }
        addMsg(box, Gravity.START)
    }

    private fun addMsg(v: View, gravity: Int) {
        msgs.addView(v, LinearLayout.LayoutParams(WRAP, WRAP).apply {
            this.gravity = gravity
            topMargin = dp(6)
            bottomMargin = dp(4)
        })
        v.alpha = 0f
        v.translationY = dpf(16f)
        v.animate().alpha(1f).translationY(0f).setDuration(280).setInterpolator(DecelerateInterpolator()).start()
        scroll.post { scroll.fullScroll(View.FOCUS_DOWN) }
    }

    private fun showTyping() {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(dp(16), dp(14), dp(16), dp(14))
            background = GradientDrawable().apply {
                cornerRadius = dpf(18f)
                setColor(Color.argb(0x1F, 255, 255, 255))
            }
        }
        for (i in 0 until 3) {
            val dot = View(this).apply {
                background = GradientDrawable().apply { shape = GradientDrawable.OVAL; setColor(SUB) }
            }
            row.addView(dot, LinearLayout.LayoutParams(dp(8), dp(8)).apply { setMargins(dp(3), 0, dp(3), 0) })
            ValueAnimator.ofFloat(0f, 1f, 0f).apply {
                duration = 700
                startDelay = i * 140L
                repeatCount = ValueAnimator.INFINITE
                addUpdateListener { dot.translationY = -(it.animatedValue as Float) * dpf(5f) }
                start()
            }
        }
        typingView = row
        addMsg(row, Gravity.START)
    }

    private fun removeTyping() {
        typingView?.let { msgs.removeView(it) }
        typingView = null
    }

    // ---------------- مغز دستیار ----------------
    private fun welcome() {
        botMsg(t(
            "سلام! 👋 من دستیار «سیم کارت 27» هستم.\nمی‌توانم کمکتان کنم بسته‌ها و بخش‌های برنامه را سریع پیدا کنید. چه کمکی از دستم برمی‌آید؟",
            "Hi! 👋 I'm the SIM Card 27 assistant.\nI can help you quickly find packages and app features. How can I help?",
            "سلام! 👋 زه د «سیم کارټ 27» مرستیال یم.\nکولای شم چې بستې او د اپ برخې ژر پیدا کړئ. څنګه مرسته وکړم؟"
        ))
    }

    private fun norm(s: String): String = s.toLowerCase(Locale.ROOT)
        .replace('ي', 'ی').replace('ك', 'ک').replace('ى', 'ی')
        .replace("\u200c", "").replace("ـ", "")
        .replace("ۀ", "ه").replace("ة", "ه")

    private data class Op(val key: String, val fa: String, val en: String, val ps: String)

    private val ops = listOf(
        Op("afghan", "افغان بیسیم", "AWCC", "افغان بېسیم"),
        Op("etisalat", "اتصالات", "Etisalat", "اتصالات"),
        Op("roshan", "روشن", "Roshan", "روشن"),
        Op("salaam", "سلام", "Salaam", "سلام"),
        Op("atoma", "اتوما", "Atoma", "اتوما")
    )

    private fun opName(o: Op) = t(o.fa, o.en, o.ps)

    private fun detectOp(q: String, hasTopic: Boolean): Op? {
        fun has(vararg w: String) = w.any { q.contains(it) }
        return when {
            has("افغان", "بیسیم", "بی سیم", "بېسیم", "awcc", "afghan wireless", "wireless") -> ops[0]
            has("اتصالات", "etisalat", "اتصلات") -> ops[1]
            has("روشن", "roshan", "روښان") -> ops[2]
            has("اتوما", "atoma", "mtn", "ام تی ان") -> ops[4]
            has("salaam") -> ops[3]
            q.contains("سلام") && (hasTopic || has("شرکت", "سیم", "اپراتور", "تیلیکام", "تلکام")) -> ops[3]
            else -> null
        }
    }

    private fun openOp(o: Op) {
        startActivity(Intent(this, OperatorMenuActivity::class.java).apply {
            putExtra("OPERATOR_NAME", o.fa)
            putExtra("OPERATOR_KEY", o.key)
            putExtra("OPERATOR", o.key.toUpperCase(Locale.ROOT))
        })
        finish()
        overridePendingTransition(0, 0)
    }

    private fun go(c: Class<*>) {
        startActivity(Intent(this, c))
        finish()
        overridePendingTransition(0, 0)
    }

    private fun opButtons(): List<Act> = ops.map { o -> Act("📱 " + opName(o)) { openOp(o) } }

    private fun answer(raw: String) {
        val q = norm(raw)
        fun has(vararg w: String) = w.any { q.contains(it) }

        val internet = has("انترنت", "اینترنت", "انټرنټ", "internet", "data", "دیتا", "گیگ", "گیګ", "مگ", "mb", "gb", "نت ")
        val call = has("تماس", "زنگ", "دقیقه", "مکالمه", "call", "minute", "زنګ")
        val sms = has("پیام", "اس ام اس", "sms", "message", "پیغام", "مسیج")
        val balance = has("بیلانس", "شارژ", "کریدیت", "کریدت", "اعتبار", "balance", "credit", "charge", "پول")
        val topic = internet || call || sms || balance
        val op = detectOp(q, topic)

        when {
            has("حافظ", "فال", "hafez", "hafiz") ->
                botMsg(t("فال حافظ آماده است 📜 اول نیت کنید، بعد دکمه را بزنید.",
                    "Hafez fal is ready 📜 Make a wish, then tap the button.",
                    "د حافظ فال چمتو دی 📜 لومړی نیت وکړئ، بیا تڼۍ کېکاږئ."),
                    listOf(Act(t("باز کردن فال حافظ", "Open Hafez fal", "د حافظ فال پرانیستل")) { go(HafezActivity::class.java) }))

            has("گالری", "گالري", "ګالري", "عکس", "انځور", "gallery", "photo", "picture", "مخفی", "خصوصی") ->
                botMsg(t("در گالری خصوصی می‌توانید عکس‌هایتان را با رمز ۴ رقمی امن نگه دارید 🔒",
                    "In the private gallery you can keep photos safe with a 4-digit PIN 🔒",
                    "په شخصي ګالرۍ کې کولای شئ انځورونه د ۴ رقمي رمز سره خوندي وساتئ 🔒"),
                    listOf(Act(t("باز کردن گالری خصوصی", "Open private gallery", "شخصي ګالري پرانیستل")) { go(VaultActivity::class.java) }))

            has("کد کشور", "کشور", "کد بین", "country", "dial code", "هېواد", "هیواد") ->
                botMsg(t("کد تلفن همهٔ کشورها اینجاست 🌍", "All country dialing codes are here 🌍", "د ټولو هېوادونو کوډونه دلته دي 🌍"),
                    listOf(Act(t("باز کردن کد کشورها", "Open country codes", "د هېوادونو کوډونه پرانیستل")) { go(CountryCodesActivity::class.java) }))

            has("علاقه", "favorite", "favourite", "saved", "خوښ") ->
                botMsg(t("بسته‌هایی که ❤️ کرده‌اید در علاقه‌مندی‌ها هستند.", "Packages you ❤️ are in Favorites.", "هغه بستې چې ❤️ مو کړې په خوښو کې دي."),
                    listOf(Act(t("باز کردن علاقه‌مندی‌ها", "Open favorites", "خوښې پرانیستل")) { go(FavoritesActivity::class.java) }))

            has("جستجو", "جست و جو", "پیدا", "search", "find", "لټون", "پلټ") ->
                botMsg(t("در جستجو می‌توانید بین بسته‌های همهٔ اپراتورها بگردید 🔍", "Search across all operators' packages 🔍", "د ټولو اپراتورونو په بستو کې ولټوئ 🔍"),
                    listOf(Act(t("باز کردن جستجو", "Open search", "لټون پرانیستل")) { go(SearchActivity::class.java) }))

            has("زبان", "language", "ژبه", "ژبې", "انگلیسی", "english") ->
                botMsg(t("زبان برنامه را در تنظیمات عوض کنید 🌐", "Change the app language in Settings 🌐", "د اپ ژبه په تنظیماتو کې بدله کړئ 🌐"),
                    listOf(Act(t("باز کردن تنظیمات", "Open settings", "تنظیمات پرانیستل")) { go(SettingsActivity::class.java) }))

            has("تیره", "تاریک", "پوسته", "dark", "theme", "night", "تیاره") ->
                botMsg(t("برای عوض کردن پوسته (روشن، خودکار، تیره)، منوی ☰ بالای صفحهٔ اصلی را باز کنید؛ اولین گزینه «پوسته» است 🌙",
                    "To change the theme (Light, Auto, Dark), open the ☰ menu on the home screen; the first option is Theme 🌙",
                    "د بڼې بدلولو لپاره (روښانه، اتومات، تیاره)، د کور پاڼې ☰ مینو پرانیزئ؛ لومړی برخه «بڼه» ده 🌙"))

            has("تاریخ", "امروز", "تقویم", "date", "today", "calendar", "نېټه", "نیټه") ->
                botMsg(t("تاریخ امروز به سه تقویم (هجری شمسی، قمری و میلادی) روی پرچم بالای صفحهٔ اصلی نوشته شده است 📅",
                    "Today's date in all three calendars (Solar, Lunar, Gregorian) is shown on the flag at the top of the home screen 📅",
                    "د نن نېټه په درې کلیزو (لمریز، قمري او میلادي) د کور پاڼې په سر بیرغ کې لیکل شوې 📅"))

            has("تنظیمات", "setting") ->
                botMsg(t("تنظیمات برنامه اینجاست ⚙️", "Here are the app settings ⚙️", "د اپ تنظیمات دلته دي ⚙️"),
                    listOf(Act(t("باز کردن تنظیمات", "Open settings", "تنظیمات پرانیستل")) { go(SettingsActivity::class.java) }))

            op != null && topic -> {
                val what = when {
                    internet -> t("بسته‌های انترنت", "internet packages", "د انټرنټ بستې")
                    call -> t("بسته‌های تماس", "call packages", "د زنګ بستې")
                    sms -> t("بسته‌های پیام", "SMS packages", "د پیغام بستې")
                    else -> t("خدمات و چک بیلانس", "services and balance check", "خدمتونه او بیلانس کتل")
                }
                botMsg(t("$what ${opName(op)} را در بخش ${opName(op)} پیدا می‌کنید. همان‌جا با یک لمس فعالش کنید ✅",
                    "You'll find ${opName(op)} $what in the ${opName(op)} section. Activate with one tap ✅",
                    "د ${opName(op)} $what د ${opName(op)} په برخه کې پیدا کوئ. هلته یې په یوه ټک فعاله کړئ ✅"),
                    listOf(Act(t("باز کردن بخش ${opName(op)}", "Open ${opName(op)}", "د ${opName(op)} برخه پرانیستل")) { openOp(op) }))
            }

            op != null ->
                botMsg(t("بخش ${opName(op)} شامل بسته‌های انترنت، تماس، پیام و خدمات است.",
                    "The ${opName(op)} section has internet, call, SMS packages and services.",
                    "د ${opName(op)} برخه د انټرنټ، زنګ، پیغام بستې او خدمتونه لري."),
                    listOf(Act(t("باز کردن بخش ${opName(op)}", "Open ${opName(op)}", "د ${opName(op)} برخه پرانیستل")) { openOp(op) }))

            topic -> {
                val what = when {
                    internet -> t("بستهٔ انترنت", "internet packages", "د انټرنټ بستې")
                    call -> t("بستهٔ تماس", "call packages", "د زنګ بستې")
                    sms -> t("بستهٔ پیام", "SMS packages", "د پیغام بستې")
                    else -> t("چک بیلانس و خدمات", "balance check and services", "بیلانس کتل او خدمتونه")
                }
                botMsg(t("$what کدام اپراتور را می‌خواهید؟ 👇", "Which operator's $what? 👇", "د کوم اپراتور $what غواړئ؟ 👇"), opButtons())
            }

            has("سلام", "درود", "hello", "hi", "hey", "salam", "صبح بخیر", "روز بخیر") ->
                botMsg(t("سلام، خوش آمدید! 😊 دنبال چه می‌گردید؟ مثلاً بنویسید «بستهٔ انترنت روشن».",
                    "Hello, welcome! 😊 What are you looking for? Try “Roshan internet”.",
                    "سلام، ښه راغلاست! 😊 څه لټوئ؟ د بېلګې په توګه ولیکئ «د روشن انټرنټ»."))

            has("تشکر", "ممنون", "مرسی", "سپاس", "thank", "thx", "مننه") ->
                botMsg(t("خواهش می‌کنم! 🌷 هر وقت سؤالی داشتید، من اینجا هستم.",
                    "You're welcome! 🌷 I'm here whenever you need me.",
                    "هرکله! 🌷 هر وخت پوښتنه لرئ، زه دلته یم."))

            has("کمک", "راهنما", "چه کار", "چکار", "چی کار", "help", "what can", "مرسته", "لارښود", "څه کول") ->
                botMsg(t("این کارها را برایتان انجام می‌دهم:\n• پیدا کردن بسته‌های انترنت، تماس و پیام هر اپراتور\n• راهنمای چک بیلانس و خدمات\n• کد کشورها\n• فال حافظ\n• گالری خصوصی\n• تنظیمات، زبان و پوسته",
                    "I can help you with:\n• Internet, call and SMS packages of every operator\n• Balance check and services\n• Country codes\n• Hafez fal\n• Private gallery\n• Settings, language and theme",
                    "زه دا کارونه کولای شم:\n• د هر اپراتور د انټرنټ، زنګ او پیغام بستې\n• بیلانس کتل او خدمتونه\n• د هېوادونو کوډونه\n• د حافظ فال\n• شخصي ګالري\n• تنظیمات، ژبه او بڼه"))

            else ->
                botMsg(t("ببخشید، منظورتان را کامل متوجه نشدم 🤔\nمی‌توانید اسم اپراتور یا کاری که می‌خواهید را بنویسید، یا یکی از اپراتورها را انتخاب کنید:",
                    "Sorry, I didn't quite get that 🤔\nType an operator name or what you need, or pick an operator:",
                    "بښنه غواړم، مطلب مو پوره ونه پوهېدم 🤔\nد اپراتور نوم یا خپل کار ولیکئ، یا یو اپراتور وټاکئ:"),
                    opButtons())
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(input.windowToken, 0)
    }
}