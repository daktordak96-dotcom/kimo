package com.kimo.mayapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.RadialGradient
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import java.util.Locale

private const val I_MENU = 0
private const val I_MORE = 1
private const val I_BELL = 2
private const val I_SEARCH = 3
private const val I_GLOBE = 4
private const val I_PHONE = 5
private const val I_SIM = 6
private const val I_GRID = 7
private const val I_HOME = 8
private const val I_HEART = 9
private const val I_GEAR = 10
private const val I_CHEV_LEFT = 11
private const val I_CHEV_RIGHT = 12
private const val I_BOOK = 13
private const val I_SIGNAL = 14
private const val I_BARS = 15
private const val I_SUN = 16
private const val I_ATOM = 17
private const val I_CHAT = 18

class MainActivity : AppCompatActivity() {

    private var shownLang: String = ""
    private var shownDark: Boolean = true
    private var lang: String = "fa"
    private var isRtl: Boolean = true

    private lateinit var pal: Palette
    private lateinit var mainScroll: ScrollView
    private lateinit var opGrid: LinearLayout
    private lateinit var txtNoResult: TextView
    private lateinit var titleOperators: View
    private lateinit var appPrefs: android.content.SharedPreferences
    private var bellDotView: View? = null
    private var heroView: View? = null

    private val opCards = ArrayList<CardView>()
    private val opKeys = ArrayList<String>()

    private class Op(
        val prefKey: String?,
        val nameKey: String,
        val latin: String,
        val info: String,
        val icon: Int,
        val neon: String,
        val mid: String,
        val onClick: () -> Unit
    )

    private class NavItem(
        val icon: Int,
        val label: String,
        val active: Boolean,
        val action: () -> Unit
    )

    override fun attachBaseContext(newBase: Context?) {
        val preferences = newBase?.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val lang = preferences?.getString(SELECTED_LANGUAGE, "fa") ?: "fa"
        super.attachBaseContext(updateBaseContextLocale(newBase!!, lang))
    }

    private fun updateBaseContextLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installCrashCatcher()
        setContentView(R.layout.activity_main)
        showLastCrash()

        shownLang = Strings.currentLang(this)
        lang = shownLang
        isRtl = resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
        appPrefs = getSharedPreferences("AppSettings", MODE_PRIVATE)

        pal = ThemeMode.palette(this)
        shownDark = pal.dark
        applySystemBars()

        mainScroll = findViewById(R.id.mainScroll)
        opGrid = findViewById(R.id.opGrid)
        txtNoResult = findViewById(R.id.txtNoResult)
        titleOperators = findViewById(R.id.titleOperators)

        // پس‌زمینهٔ کل صفحه بر اساس پوسته
        window.decorView.setBackgroundColor(pal.bg)
        findViewById<ViewGroup>(android.R.id.content).getChildAt(0)?.setBackgroundColor(pal.bg)
        mainScroll.setBackgroundColor(pal.bg)
        txtNoResult.setTextColor(pal.muted)

        setupHeader()
        setupHero()
        setupSearch()
        setupSectionTitles()
        setupOperators()
        setupBottomNav()
    }

    override fun onResume() {
        super.onResume()
        if (Strings.currentLang(this) != shownLang || ThemeMode.isDark(this) != shownDark) {
            recreate()
            return
        }
        rebuildOperators()
        refreshBellDot()
    }

    private fun applySystemBars() {
        window.statusBarColor = pal.bg
        window.navigationBarColor = pal.sysNav
        if (Build.VERSION.SDK_INT >= 23) {
            var flags = 0
            if (!pal.dark) {
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                if (Build.VERSION.SDK_INT >= 26) flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
            window.decorView.systemUiVisibility = flags
        }
    }

    private fun dpf(v: Float) = v * resources.displayMetrics.density
    private fun dpi(v: Float) = (v * resources.displayMetrics.density).toInt()
    private fun c(hex: String) = Color.parseColor(hex)
    private fun alpha(hex: String, a: Int): Int {
        val col = c(hex)
        return Color.argb(a, Color.red(col), Color.green(col), Color.blue(col))
    }
    private fun pick(fa: String, en: String, ps: String): String = when (lang) {
        "en" -> en; "ps" -> ps; else -> fa
    }
    private fun circle(fill: Int, stroke: Int? = null, strokeDp: Float = 1f): GradientDrawable =
        GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(fill)
            if (stroke != null) setStroke(dpi(strokeDp), stroke)
        }
    private fun rounded(fill: Int, radiusDp: Float, stroke: Int? = null): GradientDrawable =
        GradientDrawable().apply {
            cornerRadius = dpf(radiusDp)
            setColor(fill)
            if (stroke != null) setStroke(dpi(1f), stroke)
        }
    private fun applyGlow(view: View, neon: String) {
        if (Build.VERSION.SDK_INT >= 28) {
            view.outlineAmbientShadowColor = c(neon)
            view.outlineSpotShadowColor = c(neon)
        }
    }
    private fun chevForward() = if (isRtl) I_CHEV_LEFT else I_CHEV_RIGHT
    private fun soonText() = pick("به‌زودی…", "Coming soon…", "ډېر ژر…")
    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    private fun scrollToOperators() {
        mainScroll.post {
            // عنوان اپراتورها درست زیر بنر ثابت قرار بگیرد
            val hero = heroView
            val titleTop = topInContent(titleOperators)
            val target = if (hero != null)
                titleTop - topInContent(hero) - hero.height - dpi(8f)
            else titleTop - dpi(8f)
            mainScroll.smoothScrollTo(0, Math.max(0, target))
        }
    }

    // فاصلهٔ یک ویو از بالای محتوای اسکرول
    private fun topInContent(v: View): Int {
        var y = 0
        var cur: View? = v
        val root = mainScroll.getChildAt(0)
        while (cur != null && cur !== root) {
            y += cur.top
            cur = cur.parent as? View
        }
        return y
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addPressEffect(view: View) {
        view.setOnTouchListener { v, e ->
            when (e.action) {
                MotionEvent.ACTION_DOWN ->
                    v.animate().scaleX(0.96f).scaleY(0.96f).setDuration(90).start()
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                    v.animate().scaleX(1f).scaleY(1f).setDuration(140).start()
            }
            false
        }
    }

    private fun animateIn(view: View, index: Int) {
        view.alpha = 0f
        view.translationY = dpf(26f)
        view.animate()
            .alpha(1f).translationY(0f)
            .setStartDelay(80L * index).setDuration(420)
            .setInterpolator(DecelerateInterpolator()).start()
    }

    private fun setupHeader() {
        findViewById<TextView>(R.id.txtHeaderTitle).apply {
            text = Strings.get(this@MainActivity, "app_title")
            setTextColor(pal.text)
        }
        findViewById<TextView>(R.id.txtHeaderSub).apply {
            text = pick(
                "بسته‌های اینترنت، تماس و خدمات اپراتورها",
                "Internet, call & service packages",
                "د اپراتورانو انټرنټ، زنګ او خدمتونه"
            )
            setTextColor(pal.sub)
        }
        // دکمهٔ سه‌نقطه حذف شد؛ منوی سه‌خط جای آن (سمت راست) قرار می‌گیرد
        findViewById<ImageView>(R.id.btnMenu).visibility = View.GONE
        findViewById<ImageView>(R.id.btnMore).apply {
            setImageDrawable(IconDrawable(I_MENU, pal.icon))
            setOnClickListener {
                startActivity(Intent(this@MainActivity, MenuActivity::class.java))
            }
        }
        findViewById<ImageView>(R.id.btnBell).apply {
            setImageDrawable(IconDrawable(I_BELL, pal.icon))
            setOnClickListener { showNotifications() }
        }
        bellDotView = findViewById<View>(R.id.bellDot)
        bellDotView?.background = circle(c("#FF3B4F"))
        refreshBellDot()
    }

    private fun refreshBellDot() {
        val seen = appPrefs.getInt("notif_seen", 0)
        bellDotView?.visibility = if (seen >= NOTIF_VERSION) View.GONE else View.VISIBLE
    }

    private fun showNotifications() {
        appPrefs.edit().putInt("notif_seen", NOTIF_VERSION).apply()
        refreshBellDot()
        val title = pick("اعلان‌ها", "Notifications", "خبرتیاوې")
        val msg = pick(
            "• به برنامه خوش آمدید!\n\n• بسته‌های همه اپراتورها در دسترس است.\n\n• اطلاعات بسته‌ها ممکن است تغییر کند؛ برای اطمینان با اپراتور خود چک کنید.",
            "• Welcome to the app!\n\n• Packages for all operators are available.\n\n• Package details may change; please confirm with your operator.",
            "• ښه راغلاست!\n\n• د ټولو اپراتورانو بستې شته.\n\n• د بستو معلومات ښايي بدل شي؛ له خپل اپراتور سره یې وګورئ."
        )
        AlertDialog.Builder(this).setTitle(title).setMessage(msg)
            .setPositiveButton(pick("باشه", "OK", "سمه ده"), null).show()
    }

    private fun showAbout() {
        val msg = pick(
            "این برنامه به‌صورت مستقل ساخته شده و وابسته به هیچ‌یک از اپراتورهای مخابراتی نیست.\n\nاطلاعات بسته‌ها ممکن است تغییر کند؛ برای اطمینان، با اپراتور خود چک کنید.",
            "This app is independent and is not affiliated with any mobile operator.\n\nPackage details may change; please confirm with your operator.",
            "دا اپلیکیشن خپلواک دی او له هېڅ مخابراتي اپراتور سره تړاو نه لري.\n\nد بستو معلومات ښايي بدل شي؛ د ډاډ لپاره له خپل اپراتور سره یې وګورئ."
        )
        AlertDialog.Builder(this).setTitle(Strings.get(this, "app_title")).setMessage(msg)
            .setPositiveButton(pick("باشه", "OK", "سمه ده"), null).show()
    }

    private fun setupHero() {
        val hero = findViewById<View>(R.id.heroBanner)
        heroView = hero
        // پس‌زمینه: پرچم موج‌دار افغانستان
        hero.background = HeroFlagDrawable(resources, dpf(22f), dpf(1f), isRtl)
        hero.clipToOutline = true
        makeHeroSticky(hero)
        findViewById<ImageView>(R.id.heroSim).setImageDrawable(SimCardDrawable())
        findViewById<TextView>(R.id.txtHeroAccent).text = pick("همه خدمات", "All services", "ټول خدمتونه")
        findViewById<TextView>(R.id.txtHeroTitle).text = pick("در یک برنامه", "in one app", "په یوه اپ کې")
        findViewById<TextView>(R.id.txtHeroSub).text = pick("سریع، آسان، مطمئن", "Fast, easy, reliable", "چټک، اسانه، باوري")
        findViewById<TextView>(R.id.txtHeroBtn).text = pick("مشاهده همه اپراتورها", "View all operators", "ټول اپراتوران")
        val btn = findViewById<View>(R.id.btnHero)
        btn.background = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(c("#2F8CFF"), c("#1A5FD6"))
        ).apply { cornerRadius = dpf(22f) }
        btn.setOnClickListener { scrollToOperators() }
        addPressEffect(btn)
        findViewById<ImageView>(R.id.icHeroBtn).setImageDrawable(IconDrawable(chevForward(), Color.WHITE))
        val dots = findViewById<LinearLayout>(R.id.heroDots)
        for (i in 0 until 4) {
            val size = if (i == 0) dpi(8f) else dpi(6f)
            val dot = View(this)
            dot.background = circle(if (i == 0) c("#29B6FF") else alpha("#FFFFFF", 0x55))
            val lp = LinearLayout.LayoutParams(size, size)
            lp.marginStart = dpi(5f)
            dots.addView(dot, lp)
        }
    }

    // بنر هنگام اسکرول سر جایش ثابت می‌ماند و بقیهٔ صفحه زیر آن رد می‌شود
    private fun makeHeroSticky(hero: View) {
        val content = mainScroll.getChildAt(0) ?: return
        val pinZ = dpf(16f)
        val chain = ArrayList<View>()
        var cur: View? = hero
        while (cur != null && cur !== content) {
            cur.translationZ = pinZ
            chain.add(cur)
            cur = cur.parent as? View
        }
        if (chain.isEmpty()) return
        val pinned = chain.last()
        mainScroll.viewTreeObserver.addOnScrollChangedListener {
            val y = mainScroll.scrollY
            pinned.translationY = y.toFloat()
            // محتوایی که بالای بنر رد می‌شود پنهان شود
            if (Build.VERSION.SDK_INT >= 18) {
                if (y > 0) {
                    val top = topInContent(hero) + y
                    content.clipBounds = Rect(0, top, content.width, content.height + y + mainScroll.height)
                } else {
                    content.clipBounds = null
                }
            }
        }
    }

    private fun setupSearch() {
        findViewById<View>(R.id.searchBar).background = rounded(pal.searchBg, 25f, pal.stroke)
        findViewById<ImageView>(R.id.icSearch).setImageDrawable(IconDrawable(I_SEARCH, pal.searchIcon))
        val edt = findViewById<EditText>(R.id.edtSearch)
        edt.hint = pick(
            "بسته دلخواه خود را جستجو کنید…",
            "Search for your package…",
            "خپله خوښه بسته ولټوئ…"
        )
        edt.setTextColor(pal.text)
        edt.setHintTextColor(pal.muted)
        edt.isFocusable = false
        edt.isFocusableInTouchMode = false
        edt.isClickable = true
        edt.setOnClickListener { startActivity(Intent(this, SearchActivity::class.java)) }
    }

    private fun setupSectionTitles() {
        findViewById<View>(R.id.barOperators).background = rounded(c("#2F8CFF"), 2f)
        findViewById<TextView>(R.id.txtSectionTitle).apply {
            text = pick("اپراتورها", "Operators", "اپراتوران")
            setTextColor(pal.text)
        }
    }

    private fun allOperators(): List<Op> {
        val info = pick("بسته‌ها و خدمات", "Packages & services", "بستې او خدمتونه")
        val hafezInfo = pick("فال روزانه", "Daily fortune", "ورځنی فال")
        return listOf(
            Op("op_etisalat", "etisalat", "ETISALAT", info, I_BARS, "#9CFF3A", "#2E7D12") {
                openOperator("etisalat", "اتصالات")
            },
            Op("op_afghan", "afghan_wireless", "AFGHAN WIRELESS", info, I_SIGNAL, "#29B6FF", "#0D47A1") {
                openOperator("afghan", "افغان بیسیم")
            },
            Op("op_roshan", "roshan", "ROSHAN", info, I_SUN, "#FF3B4F", "#A0001C") {
                openOperator("roshan", "روشن")
            },
            Op("op_atoma", "atoma", "ATOMA", info, I_ATOM, "#FFC928", "#A86B00") {
                openOperator("atoma", "اتوما")
            },
            Op(null, "fal_hafez", "", hafezInfo, I_BOOK, "#B46CFF", "#4A1C9A") {
                startActivity(Intent(this, HafezActivity::class.java))
            },
            Op("op_salaam", "salaam", "SALAAM", info, I_CHAT, "#1DE9D0", "#00796B") {
                openOperator("salaam", "سلام")
            }
        )
    }

    private fun setupOperators() {
        rebuildOperators()
        opCards.forEachIndexed { i, card -> animateIn(card, i) }
    }

    private fun rebuildOperators() {
        opCards.clear()
        opKeys.clear()
        for (op in allOperators()) {
            if (op.prefKey != null && !appPrefs.getBoolean(op.prefKey, true)) continue
            val name = Strings.get(this, op.nameKey)
            opCards.add(buildOperatorCard(op, name))
            opKeys.add((name + " " + op.latin + " " + op.nameKey).toLowerCase(Locale.ROOT))
        }
        layoutOperators()
    }

    private fun layoutOperators() {
        opGrid.removeAllViews()
        var i = 0
        while (i < opCards.size) {
            val row = LinearLayout(this)
            row.orientation = LinearLayout.HORIZONTAL
            row.isBaselineAligned = false
            for (j in 0..1) {
                val lp = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
                lp.setMargins(dpi(5f), dpi(5f), dpi(5f), dpi(5f))
                if (i + j < opCards.size) {
                    (opCards[i + j].parent as? ViewGroup)?.removeView(opCards[i + j])
                    row.addView(opCards[i + j], lp)
                } else {
                    row.addView(View(this), lp)
                }
            }
            opGrid.addView(row, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            i += 2
        }
        txtNoResult.visibility = View.GONE
    }

    private fun buildOperatorCard(op: Op, name: String): CardView {
        val match = ViewGroup.LayoutParams.MATCH_PARENT
        val wrap = ViewGroup.LayoutParams.WRAP_CONTENT
        val card = CardView(this)
        card.radius = dpf(18f)
        card.cardElevation = dpf(6f)
        card.preventCornerOverlap = false
        card.setCardBackgroundColor(pal.card)
        card.isClickable = true
        card.isFocusable = true
        applyGlow(card, op.neon)
        val frame = FrameLayout(this)
        frame.background = GradientDrawable(
            if (isRtl) GradientDrawable.Orientation.RIGHT_LEFT else GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(alpha(op.neon, if (pal.dark) 0x30 else 0x22), pal.card, pal.card2)
        ).apply {
            cornerRadius = dpf(18f)
            setStroke(dpi(1f), alpha(op.neon, 0x55))
        }
        card.addView(frame, FrameLayout.LayoutParams(match, match))
        val stripe = View(this)
        stripe.setBackgroundColor(c(op.neon))
        frame.addView(stripe, FrameLayout.LayoutParams(dpi(4f), match, Gravity.START))
        val content = LinearLayout(this)
        content.orientation = LinearLayout.VERTICAL
        content.setPaddingRelative(dpi(16f), dpi(12f), dpi(12f), dpi(12f))
        frame.addView(content, FrameLayout.LayoutParams(match, match))
        val top = LinearLayout(this)
        top.orientation = LinearLayout.HORIZONTAL
        top.gravity = Gravity.CENTER_VERTICAL
        content.addView(top, LinearLayout.LayoutParams(match, wrap))
        val emblem = FrameLayout(this)
        emblem.background = GradientDrawable(
            GradientDrawable.Orientation.TL_BR, intArrayOf(c(op.neon), c(op.mid))
        ).apply {
            shape = GradientDrawable.OVAL
            setStroke(dpi(2f), alpha("#FFFFFF", 0x40))
        }
        emblem.elevation = dpf(5f)
        applyGlow(emblem, op.neon)
        val symbol = ImageView(this)
        symbol.setImageDrawable(IconDrawable(op.icon, Color.WHITE))
        val sp = dpi(12f)
        symbol.setPadding(sp, sp, sp, sp)
        emblem.addView(symbol, FrameLayout.LayoutParams(match, match))
        top.addView(emblem, LinearLayout.LayoutParams(dpi(52f), dpi(52f)))
        top.addView(View(this), LinearLayout.LayoutParams(0, 1, 1f))
        val chev = ImageView(this)
        chev.background = circle(alpha(op.neon, 0x33))
        chev.setImageDrawable(IconDrawable(chevForward(), c(op.neon)))
        val cp = dpi(7f)
        chev.setPadding(cp, cp, cp, cp)
        top.addView(chev, LinearLayout.LayoutParams(dpi(30f), dpi(30f)))
        val title = TextView(this).apply {
            text = name
            setTextColor(pal.text)
            textSize = 17f
            setTypeface(typeface, Typeface.BOLD)
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.END
        }
        val titleLp = LinearLayout.LayoutParams(match, wrap)
        titleLp.topMargin = dpi(10f)
        content.addView(title, titleLp)
        if (op.latin.isNotEmpty() && lang != "en") {
            val latin = TextView(this).apply {
                text = op.latin
                setTextColor(if (pal.dark) alpha("#FFFFFF", 0xB3) else pal.muted)
                textSize = 10f
                letterSpacing = 0.06f
                setTypeface(typeface, Typeface.BOLD)
                maxLines = 1
                ellipsize = TextUtils.TruncateAt.END
            }
            content.addView(latin, LinearLayout.LayoutParams(match, wrap))
        }
        content.addView(View(this), LinearLayout.LayoutParams(1, 0, 1f))
        val strip = LinearLayout(this)
        strip.orientation = LinearLayout.HORIZONTAL
        strip.gravity = Gravity.CENTER_VERTICAL
        strip.background = rounded(pal.chip, 10f)
        strip.setPaddingRelative(dpi(8f), 0, dpi(8f), 0)
        val stripLp = LinearLayout.LayoutParams(match, dpi(28f))
        stripLp.topMargin = dpi(10f)
        content.addView(strip, stripLp)
        val globe = ImageView(this)
        globe.setImageDrawable(IconDrawable(I_GLOBE, pal.searchIcon))
        strip.addView(globe, LinearLayout.LayoutParams(dpi(14f), dpi(14f)))
        val infoText = TextView(this).apply {
            text = op.info
            setTextColor(pal.sub)
            textSize = 11f
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.END
        }
        val infoLp = LinearLayout.LayoutParams(0, wrap, 1f)
        infoLp.marginStart = dpi(6f)
        strip.addView(infoText, infoLp)
        card.setOnClickListener { op.onClick() }
        addPressEffect(card)
        return card
    }

    private fun setupBottomNav() {
        val nav = findViewById<LinearLayout>(R.id.navBar)
        nav.background = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(pal.navTop, pal.navBottom)
        )
        nav.elevation = dpf(12f)
        val activeColor = c("#2F8CFF")
        val idleColor = pal.muted
        val items = listOf(
            NavItem(I_HOME, pick("خانه", "Home", "کور"), true) {
                mainScroll.smoothScrollTo(0, 0)
            },
            NavItem(I_GRID, pick("اپراتورها", "Operators", "اپراتوران"), false) {
                scrollToOperators()
            },
            NavItem(I_HEART, pick("علاقه‌مندی‌ها", "Favorites", "خوښې"), false) {
                startActivity(Intent(this, FavoritesActivity::class.java))
            },
            NavItem(I_GEAR, pick("تنظیمات", "Settings", "تنظیمات"), false) {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        )
        for (item in items) {
            val col = if (item.active) activeColor else idleColor
            val box = LinearLayout(this)
            box.orientation = LinearLayout.VERTICAL
            box.gravity = Gravity.CENTER
            box.isClickable = true
            box.isFocusable = true
            val icon = ImageView(this)
            icon.setImageDrawable(IconDrawable(item.icon, col))
            box.addView(icon, LinearLayout.LayoutParams(dpi(24f), dpi(24f)))
            val label = TextView(this).apply {
                text = item.label
                setTextColor(col)
                textSize = 11f
                maxLines = 1
                if (item.active) setTypeface(typeface, Typeface.BOLD)
            }
            val llp = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            llp.topMargin = dpi(3f)
            box.addView(label, llp)
            val indicator = View(this)
            indicator.background = rounded(activeColor, 2f)
            indicator.visibility = if (item.active) View.VISIBLE else View.INVISIBLE
            val ilp = LinearLayout.LayoutParams(dpi(34f), dpi(3f))
            ilp.topMargin = dpi(4f)
            box.addView(indicator, ilp)
            box.setOnClickListener { item.action() }
            nav.addView(box, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f))
        }
    }

    private fun openOperator(key: String, name: String) {
        startActivity(Intent(this, OperatorMenuActivity::class.java).apply {
            putExtra("OPERATOR_NAME", name)
            putExtra("OPERATOR_KEY", key)
            putExtra("OPERATOR", key.toUpperCase(Locale.ROOT))
        })
    }

    private fun installCrashCatcher() {
        val prefs = getSharedPreferences("crash", MODE_PRIVATE)
        val oldHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            prefs.edit().putString("last", Log.getStackTraceString(e)).commit()
            oldHandler?.uncaughtException(t, e)
        }
    }

    private fun showLastCrash() {
        val prefs = getSharedPreferences("crash", MODE_PRIVATE)
        val last = prefs.getString("last", null) ?: return
        AlertDialog.Builder(this).setTitle("آخرین خطا").setMessage(last.take(1500))
            .setPositiveButton("باشه", null).show()
        prefs.edit().remove("last").apply()
    }

    companion object {
        private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
        private const val NOTIF_VERSION = 1
    }

    private class IconDrawable(private val type: Int, color: Int) : Drawable() {
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
        private val rect = RectF()
        override fun draw(canvas: Canvas) {
            val b = bounds
            val s = Math.min(b.width(), b.height()) / 24f
            canvas.save()
            canvas.translate(b.exactCenterX() - 12f * s, b.exactCenterY() - 12f * s)
            canvas.scale(s, s)
            stroke.strokeWidth = 2f
            when (type) {
                I_MENU -> {
                    canvas.drawLine(4f, 7f, 20f, 7f, stroke)
                    canvas.drawLine(4f, 12f, 20f, 12f, stroke)
                    canvas.drawLine(4f, 17f, 20f, 17f, stroke)
                }
                I_MORE -> {
                    canvas.drawCircle(12f, 5f, 1.9f, fill)
                    canvas.drawCircle(12f, 12f, 1.9f, fill)
                    canvas.drawCircle(12f, 19f, 1.9f, fill)
                }
                I_BELL -> {
                    stroke.strokeWidth = 1.8f
                    path.reset()
                    path.moveTo(6f, 17f); path.lineTo(6f, 11f)
                    rect.set(6f, 5f, 18f, 17f)
                    path.arcTo(rect, 180f, 180f)
                    path.lineTo(18f, 17f)
                    canvas.drawPath(path, stroke)
                    canvas.drawLine(4f, 17f, 20f, 17f, stroke)
                    canvas.drawLine(12f, 3f, 12f, 5f, stroke)
                    canvas.drawCircle(12f, 20f, 1.6f, fill)
                }
                I_SEARCH -> {
                    canvas.drawCircle(10.5f, 10.5f, 6.5f, stroke)
                    canvas.drawLine(15.5f, 15.5f, 20.5f, 20.5f, stroke)
                }
                I_GLOBE -> {
                    stroke.strokeWidth = 1.6f
                    canvas.drawCircle(12f, 12f, 9f, stroke)
                    rect.set(8f, 3f, 16f, 21f)
                    canvas.drawOval(rect, stroke)
                    canvas.drawLine(3f, 12f, 21f, 12f, stroke)
                    canvas.drawLine(4.3f, 7.5f, 19.7f, 7.5f, stroke)
                    canvas.drawLine(4.3f, 16.5f, 19.7f, 16.5f, stroke)
                }
                I_PHONE -> {
                    path.reset()
                    path.moveTo(6.62f, 10.79f)
                    path.cubicTo(8.06f, 13.62f, 10.38f, 15.93f, 13.21f, 17.38f)
                    path.lineTo(15.41f, 15.18f)
                    path.cubicTo(15.68f, 14.91f, 16.08f, 14.82f, 16.43f, 14.94f)
                    path.cubicTo(17.55f, 15.31f, 18.76f, 15.51f, 20f, 15.51f)
                    path.cubicTo(20.55f, 15.51f, 21f, 15.96f, 21f, 16.51f)
                    path.lineTo(21f, 20f)
                    path.cubicTo(21f, 20.55f, 20.55f, 21f, 20f, 21f)
                    path.cubicTo(10.61f, 21f, 3f, 13.39f, 3f, 4f)
                    path.cubicTo(3f, 3.45f, 3.45f, 3f, 4f, 3f)
                    path.lineTo(7.5f, 3f)
                    path.cubicTo(8.05f, 3f, 8.5f, 3.45f, 8.5f, 4f)
                    path.cubicTo(8.5f, 5.25f, 8.7f, 6.45f, 9.07f, 7.57f)
                    path.cubicTo(9.18f, 7.92f, 9.1f, 8.31f, 8.82f, 8.59f)
                    path.close()
                    canvas.drawPath(path, fill)
                }
                I_SIM -> {
                    stroke.strokeWidth = 1.8f
                    path.reset()
                    path.moveTo(6f, 3f); path.lineTo(14.5f, 3f); path.lineTo(19f, 7.5f)
                    path.lineTo(19f, 21f); path.lineTo(6f, 21f); path.close()
                    canvas.drawPath(path, stroke)
                    rect.set(9f, 11f, 16f, 18f)
                    canvas.drawRoundRect(rect, 1.2f, 1.2f, stroke)
                    canvas.drawLine(12.5f, 11f, 12.5f, 18f, stroke)
                    canvas.drawLine(9f, 14.5f, 16f, 14.5f, stroke)
                }
                I_GRID -> {
                    stroke.strokeWidth = 1.8f
                    rect.set(4f, 4f, 10.5f, 10.5f); canvas.drawRoundRect(rect, 1.8f, 1.8f, stroke)
                    rect.set(13.5f, 4f, 20f, 10.5f); canvas.drawRoundRect(rect, 1.8f, 1.8f, stroke)
                    rect.set(4f, 13.5f, 10.5f, 20f); canvas.drawRoundRect(rect, 1.8f, 1.8f, stroke)
                    rect.set(13.5f, 13.5f, 20f, 20f); canvas.drawRoundRect(rect, 1.8f, 1.8f, stroke)
                }
                I_HOME -> {
                    path.reset()
                    path.moveTo(12f, 3f); path.lineTo(21f, 11f); path.lineTo(18.5f, 11f)
                    path.lineTo(18.5f, 20f); path.lineTo(14f, 20f); path.lineTo(14f, 15f)
                    path.lineTo(10f, 15f); path.lineTo(10f, 20f); path.lineTo(5.5f, 20f)
                    path.lineTo(5.5f, 11f); path.lineTo(3f, 11f); path.close()
                    canvas.drawPath(path, fill)
                }
                I_HEART -> {
                    stroke.strokeWidth = 1.8f
                    path.reset()
                    path.moveTo(12f, 20f)
                    path.cubicTo(5f, 15f, 2.5f, 11.5f, 2.5f, 8.5f)
                    path.cubicTo(2.5f, 5.9f, 4.5f, 4f, 7f, 4f)
                    path.cubicTo(9f, 4f, 10.8f, 5.2f, 12f, 7f)
                    path.cubicTo(13.2f, 5.2f, 15f, 4f, 17f, 4f)
                    path.cubicTo(19.5f, 4f, 21.5f, 5.9f, 21.5f, 8.5f)
                    path.cubicTo(21.5f, 11.5f, 19f, 15f, 12f, 20f)
                    path.close()
                    canvas.drawPath(path, stroke)
                }
                I_GEAR -> {
                    stroke.strokeWidth = 1.8f
                    canvas.drawCircle(12f, 12f, 3f, stroke)
                    canvas.drawCircle(12f, 12f, 6.5f, stroke)
                    stroke.strokeWidth = 2.6f
                    for (idx in 0 until 8) {
                        val a = Math.toRadians(idx * 45.0)
                        val cos = Math.cos(a).toFloat()
                        val sin = Math.sin(a).toFloat()
                        canvas.drawLine(12f + cos * 7f, 12f + sin * 7f, 12f + cos * 9.5f, 12f + sin * 9.5f, stroke)
                    }
                }
                I_CHEV_LEFT -> {
                    stroke.strokeWidth = 2.6f
                    canvas.drawLine(15f, 5f, 8f, 12f, stroke)
                    canvas.drawLine(8f, 12f, 15f, 19f, stroke)
                }
                I_CHEV_RIGHT -> {
                    stroke.strokeWidth = 2.6f
                    canvas.drawLine(9f, 5f, 16f, 12f, stroke)
                    canvas.drawLine(16f, 12f, 9f, 19f, stroke)
                }
                I_BOOK -> {
                    stroke.strokeWidth = 1.6f
                    path.reset()
                    path.moveTo(12f, 6f)
                    path.cubicTo(10f, 4.5f, 6.5f, 4.2f, 3.5f, 5f)
                    path.lineTo(3.5f, 18.5f)
                    path.cubicTo(6.5f, 17.7f, 10f, 18f, 12f, 19.5f)
                    path.moveTo(12f, 6f)
                    path.cubicTo(14f, 4.5f, 17.5f, 4.2f, 20.5f, 5f)
                    path.lineTo(20.5f, 18.5f)
                    path.cubicTo(17.5f, 17.7f, 14f, 18f, 12f, 19.5f)
                    canvas.drawPath(path, stroke)
                    canvas.drawLine(12f, 6f, 12f, 19.5f, stroke)
                    canvas.drawLine(5.8f, 8.6f, 10f, 9.3f, stroke)
                    canvas.drawLine(5.8f, 11.6f, 10f, 12.3f, stroke)
                    canvas.drawLine(5.8f, 14.6f, 10f, 15.3f, stroke)
                    canvas.drawLine(14f, 9.3f, 18.2f, 8.6f, stroke)
                    canvas.drawLine(14f, 12.3f, 18.2f, 11.6f, stroke)
                    canvas.drawLine(14f, 15.3f, 18.2f, 14.6f, stroke)
                }
                I_SIGNAL -> {
                    stroke.strokeWidth = 2.2f
                    canvas.drawCircle(12f, 18f, 1.9f, fill)
                    val radii = floatArrayOf(5f, 9f, 13f)
                    for (r in radii) {
                        rect.set(12f - r, 18f - r, 12f + r, 18f + r)
                        canvas.drawArc(rect, 225f, 90f, false, stroke)
                    }
                }
                I_BARS -> {
                    rect.set(3f, 15f, 6f, 21f); canvas.drawRoundRect(rect, 1.3f, 1.3f, fill)
                    rect.set(8f, 11f, 11f, 21f); canvas.drawRoundRect(rect, 1.3f, 1.3f, fill)
                    rect.set(13f, 7f, 16f, 21f); canvas.drawRoundRect(rect, 1.3f, 1.3f, fill)
                    rect.set(18f, 3f, 21f, 21f); canvas.drawRoundRect(rect, 1.3f, 1.3f, fill)
                }
                I_SUN -> {
                    canvas.drawCircle(12f, 12f, 4.5f, fill)
                    stroke.strokeWidth = 2f
                    for (idx in 0 until 8) {
                        val a = Math.toRadians(idx * 45.0)
                        val cos = Math.cos(a).toFloat()
                        val sin = Math.sin(a).toFloat()
                        canvas.drawLine(12f + cos * 7f, 12f + sin * 7f, 12f + cos * 10f, 12f + sin * 10f, stroke)
                    }
                }
                I_ATOM -> {
                    canvas.drawCircle(12f, 12f, 2.2f, fill)
                    stroke.strokeWidth = 1.5f
                    val angles = floatArrayOf(0f, 60f, 120f)
                    for (ang in angles) {
                        canvas.save()
                        canvas.rotate(ang, 12f, 12f)
                        rect.set(2f, 8.5f, 22f, 15.5f)
                        canvas.drawOval(rect, stroke)
                        canvas.restore()
                    }
                }
                I_CHAT -> {
                    stroke.strokeWidth = 1.8f
                    rect.set(3f, 4f, 21f, 16f)
                    canvas.drawRoundRect(rect, 4f, 4f, stroke)
                    path.reset()
                    path.moveTo(8f, 16f); path.lineTo(7f, 20.5f); path.lineTo(12.5f, 16f)
                    canvas.drawPath(path, stroke)
                    canvas.drawCircle(8f, 10f, 1.3f, fill)
                    canvas.drawCircle(12f, 10f, 1.3f, fill)
                    canvas.drawCircle(16f, 10f, 1.3f, fill)
                }
            }
            canvas.restore()
        }
        override fun setAlpha(alpha: Int) { stroke.alpha = alpha; fill.alpha = alpha }
        override fun setColorFilter(colorFilter: ColorFilter?) {
            stroke.colorFilter = colorFilter; fill.colorFilter = colorFilter
        }
        override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
    }

    private class SimCardDrawable : Drawable() {
        private val p = Paint(Paint.ANTI_ALIAS_FLAG)
        private val path = Path()
        private val rect = RectF()
        override fun draw(canvas: Canvas) {
            val b = bounds
            val size = Math.min(b.width(), b.height()).toFloat()
            val cx = b.exactCenterX(); val cy = b.exactCenterY()
            p.style = Paint.Style.FILL
            p.shader = RadialGradient(cx, cy, size * 0.5f,
                Color.argb(0x66, 0, 229, 255), Color.argb(0, 0, 229, 255), Shader.TileMode.CLAMP)
            canvas.drawCircle(cx, cy, size * 0.5f, p)
            p.shader = null
            canvas.save()
            canvas.rotate(-18f, cx, cy)
            rect.set(cx - size * 0.48f, cy - size * 0.17f, cx + size * 0.48f, cy + size * 0.17f)
            p.style = Paint.Style.STROKE
            p.color = Color.argb(0x40, 0, 229, 255); p.strokeWidth = size * 0.05f
            canvas.drawOval(rect, p)
            p.color = Color.argb(0xCC, 0, 229, 255); p.strokeWidth = size * 0.012f
            canvas.drawOval(rect, p)
            canvas.restore()
            val w = size * 0.46f; val h = size * 0.62f
            canvas.save()
            canvas.rotate(-24f, cx, cy)
            val l = cx - w / 2f; val t = cy - h / 2f; val r = l + w; val bt = t + h
            val cut = w * 0.28f; val rad = w * 0.08f
            path.reset()
            path.moveTo(l + rad, t); path.lineTo(r - cut, t); path.lineTo(r, t + cut)
            path.lineTo(r, bt - rad); path.quadTo(r, bt, r - rad, bt); path.lineTo(l + rad, bt)
            path.quadTo(l, bt, l, bt - rad); path.lineTo(l, t + rad); path.quadTo(l, t, l + rad, t)
            path.close()
            p.style = Paint.Style.FILL
            p.shader = LinearGradient(l, t, r, bt,
                intArrayOf(Color.parseColor("#7FDBFF"), Color.parseColor("#2E7BEA"), Color.parseColor("#123FA8")),
                null, Shader.TileMode.CLAMP)
            canvas.drawPath(path, p)
            p.shader = null
            p.style = Paint.Style.STROKE; p.strokeWidth = size * 0.008f
            p.color = Color.argb(0x99, 255, 255, 255)
            canvas.drawPath(path, p)
            val chW = w * 0.48f; val chH = h * 0.27f
            val chL = cx - chW / 2f; val chT = cy - chH / 2f + h * 0.06f
            rect.set(chL, chT, chL + chW, chT + chH)
            p.style = Paint.Style.FILL
            p.shader = LinearGradient(chL, chT, chL + chW, chT + chH,
                Color.parseColor("#FFEDB0"), Color.parseColor("#D9A23A"), Shader.TileMode.CLAMP)
            canvas.drawRoundRect(rect, chW * 0.14f, chW * 0.14f, p)
            p.shader = null
            p.style = Paint.Style.STROKE; p.color = Color.parseColor("#A8771F"); p.strokeWidth = size * 0.007f
            canvas.drawRoundRect(rect, chW * 0.14f, chW * 0.14f, p)
            canvas.drawLine(rect.centerX(), rect.top, rect.centerX(), rect.bottom, p)
            canvas.drawLine(rect.left, rect.top + chH * 0.33f, rect.right, rect.top + chH * 0.33f, p)
            canvas.drawLine(rect.left, rect.top + chH * 0.66f, rect.right, rect.top + chH * 0.66f, p)
            canvas.restore()
            canvas.save()
            canvas.rotate(-18f, cx, cy)
            rect.set(cx - size * 0.48f, cy - size * 0.17f, cx + size * 0.48f, cy + size * 0.17f)
            p.style = Paint.Style.STROKE; p.color = Color.argb(0xEE, 0, 229, 255); p.strokeWidth = size * 0.014f
            canvas.drawArc(rect, 20f, 140f, false, p)
            canvas.restore()
            p.style = Paint.Style.FILL; p.color = Color.argb(0xDD, 255, 255, 255)
            canvas.drawCircle(cx + size * 0.40f, cy - size * 0.30f, size * 0.013f, p)
            canvas.drawCircle(cx - size * 0.42f, cy + size * 0.22f, size * 0.010f, p)
            canvas.drawCircle(cx + size * 0.30f, cy + size * 0.36f, size * 0.008f, p)
        }
        override fun setAlpha(alpha: Int) { p.alpha = alpha }
        override fun setColorFilter(colorFilter: ColorFilter?) { p.colorFilter = colorFilter }
        override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
    }

    // پرچم افغانستان از عکس afghan_flag، هم‌اندازهٔ کادر، با موج ملایم و چروک‌های ریز
    private class HeroFlagDrawable(
        private val res: Resources,
        private val radius: Float, private val strokeW: Float, private val simOnLeft: Boolean
    ) : Drawable() {
        private val p = Paint(Paint.ANTI_ALIAS_FLAG)
        private val meshPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        private val clip = Path()
        private val rect = RectF()
        private var flag: Bitmap? = null

        private val cols = 44
        private val rows = 22
        private val verts = FloatArray((cols + 1) * (rows + 1) * 2)
        private val colors = IntArray((cols + 1) * (rows + 1))

        private val tick = Runnable { invalidateSelf() }

        private fun loadFlag(): Bitmap {
            return try {
                val opts = BitmapFactory.Options().apply { inScaled = false }
                BitmapFactory.decodeResource(res, R.drawable.afghan_flag, opts) ?: fallbackFlag()
            } catch (e: Exception) {
                fallbackFlag()
            }
        }

        private fun fallbackFlag(): Bitmap {
            val bmp = Bitmap.createBitmap(300, 200, Bitmap.Config.ARGB_8888)
            val cv = Canvas(bmp)
            val fp = Paint()
            fp.color = Color.parseColor("#111111"); cv.drawRect(0f, 0f, 100f, 200f, fp)
            fp.color = Color.parseColor("#BE0000"); cv.drawRect(100f, 0f, 200f, 200f, fp)
            fp.color = Color.parseColor("#007A36"); cv.drawRect(200f, 0f, 300f, 200f, fp)
            return bmp
        }

        override fun draw(canvas: Canvas) {
            val b = bounds
            if (b.isEmpty) return
            val w = b.width().toFloat(); val h = b.height().toFloat()
            val x0 = b.left.toFloat(); val y0 = b.top.toFloat()
            val bmp = flag ?: loadFlag().also { flag = it }

            rect.set(b)
            clip.reset()
            clip.addRoundRect(rect, radius, radius, Path.Direction.CW)
            canvas.save()
            canvas.clipPath(clip)

            p.style = Paint.Style.FILL
            p.shader = null
            p.color = Color.parseColor("#0B1424")
            canvas.drawRect(rect, p)

            val time = (SystemClock.uptimeMillis() % 1000000L) / 1000f
            val phase = time * 1.2f
            val a1Amp = h * 0.018f
            val a2Amp = h * 0.008f
            val a3Amp = h * 0.004f
            var k = 0
            for (j in 0..rows) {
                for (i in 0..cols) {
                    val fx = i / cols.toFloat()
                    val fy = j / rows.toFloat()
                    val env = Math.sqrt(
                        Math.sin(Math.PI * fx) * Math.sin(Math.PI * fy)
                    ).toFloat()
                    val a1 = phase * 1.0f - fx * 5.5f - fy * 1.4f
                    val a2 = phase * 1.6f - fx * 12f + fy * 3.2f
                    val a3 = phase * 2.4f - fx * 22f - fy * 7f
                    val s1 = Math.sin(a1.toDouble()).toFloat()
                    val s2 = Math.sin(a2.toDouble()).toFloat()
                    val s3 = Math.sin(a3.toDouble()).toFloat()
                    val c1 = Math.cos(a1.toDouble()).toFloat()
                    val c2 = Math.cos(a2.toDouble()).toFloat()
                    val c3 = Math.cos(a3.toDouble()).toFloat()

                    val dy = (a1Amp * s1 + a2Amp * s2 + a3Amp * s3) * env
                    val dx = (a1Amp * 0.4f * c1 + a2Amp * 0.3f * c2) * env
                    verts[k * 2] = x0 + w * fx + dx
                    verts[k * 2 + 1] = y0 + h * fy + dy

                    val slope = 0.55f * c1 + 0.30f * c2 + 0.15f * c3
                    val v = (236f + 19f * slope).toInt().coerceIn(0, 255)
                    colors[k] = Color.rgb(v, v, v)
                    k++
                }
            }
            canvas.drawBitmapMesh(bmp, cols, rows, verts, 0, colors, 0, meshPaint)

            val clear = Color.argb(0x00, 6, 14, 34)
            val mid = Color.argb(0x30, 6, 14, 34)
            val dark = Color.argb(0x99, 6, 14, 34)
            p.shader = if (simOnLeft)
                LinearGradient(x0, y0, x0 + w, y0, intArrayOf(clear, mid, dark), floatArrayOf(0.35f, 0.6f, 1f), Shader.TileMode.CLAMP)
            else
                LinearGradient(x0, y0, x0 + w, y0, intArrayOf(dark, mid, clear), floatArrayOf(0f, 0.4f, 0.65f), Shader.TileMode.CLAMP)
            canvas.drawRect(rect, p)
            p.shader = null
            canvas.restore()

            p.style = Paint.Style.STROKE; p.strokeWidth = strokeW
            p.color = Color.argb(0x66, 255, 255, 255)
            rect.set(x0 + strokeW / 2f, y0 + strokeW / 2f, x0 + w - strokeW / 2f, y0 + h - strokeW / 2f)
            canvas.drawRoundRect(rect, radius, radius, p)

            unscheduleSelf(tick)
            scheduleSelf(tick, SystemClock.uptimeMillis() + 33)
        }

        override fun getOutline(outline: Outline) { outline.setRoundRect(bounds, radius) }
        override fun setAlpha(alpha: Int) { p.alpha = alpha; meshPaint.alpha = alpha }
        override fun setColorFilter(colorFilter: ColorFilter?) { p.colorFilter = colorFilter; meshPaint.colorFilter = colorFilter }
        override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
    }
}