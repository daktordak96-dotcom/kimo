package com.kimo.mayapp

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

private const val CI_GLOBE = 0
private const val CI_SEARCH = 1
private const val CI_CHEV_L = 2
private const val CI_CHEV_R = 3

class CountryCodesActivity : AppCompatActivity() {

    private val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
    private val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT

    private val GREEN = "#43A047"
    private val GREEN_DARK = "#2E7D32"
    private val CARD_BG = "#F1F8F1"
    private val CARD_STROKE = "#D6EBD7"
    private val TEXT_DARK = "#1F2937"
    private val TEXT_GREY = "#6B7280"

    private var lang = "fa"
    private var isRtl = true
    private var allCountries: List<Country> = emptyList()
    private var shown: List<Country> = emptyList()
    private var regionFilter: String? = null
    private var query = ""

    private val chipViews = ArrayList<Pair<String?, TextView>>()
    private lateinit var adapter: CountryAdapter
    private lateinit var emptyView: TextView

    private class Holder(
        val flag: TextView,
        val wave: WavingFlagView,
        val name: TextView,
        val sub: TextView,
        val region: TextView,
        val dial: TextView
    )

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
        supportActionBar?.hide()

        lang = Strings.currentLang(this)
        isRtl = lang != "en"
        window.statusBarColor = c(GREEN_DARK)

        // افغانستان اول، بقیه به ترتیب الفبا
        val sorted = CountryData.all.sortedBy { mainName(it).toLowerCase(Locale.ROOT) }
        allCountries = sorted.filter { it.iso == "AF" } + sorted.filter { it.iso != "AF" }
        shown = allCountries

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(c("#F5F8F5"))
            layoutDirection = if (isRtl) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
        }

        root.addView(buildHeader(), LinearLayout.LayoutParams(MATCH, WRAP))
        root.addView(buildSearch(), LinearLayout.LayoutParams(MATCH, WRAP).apply {
            setMargins(dp(16), -dp(30), dp(16), dp(4))
        })
        root.addView(buildRegionChips(), LinearLayout.LayoutParams(MATCH, WRAP))

        emptyView = TextView(this).apply {
            text = pick("کشوری پیدا نشد", "No country found", "هېواد ونه موندل شو")
            textSize = 16f
            setTextColor(c(TEXT_GREY))
            gravity = Gravity.CENTER
            setPadding(0, dp(40), 0, dp(40))
            visibility = View.GONE
        }
        root.addView(emptyView, LinearLayout.LayoutParams(MATCH, WRAP))

        val listView = ListView(this).apply {
            divider = null
            dividerHeight = 0
            setPadding(dp(12), dp(4), dp(12), dp(20))
            clipToPadding = false
            setSelector(ColorDrawable(Color.TRANSPARENT))
        }
        adapter = CountryAdapter()
        listView.adapter = adapter
        listView.setOnItemClickListener { _, _, position, _ -> showDetails(shown[position]) }
        root.addView(listView, LinearLayout.LayoutParams(MATCH, 0, 1f))

        setContentView(root)
    }

    // ---------------- سربرگ موج‌دار ----------------

    private fun buildHeader(): View {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(8), dp(16), dp(58))
            background = WaveHeader(dp(26).toFloat())
        }

        box.addView(TextView(this).apply {
            text = if (isRtl) "→" else "←"
            textSize = 24f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setOnClickListener { finish() }
        }, LinearLayout.LayoutParams(dp(44), dp(44)))

        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val emblem = FrameLayout(this).apply {
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(Color.argb(0x33, 255, 255, 255))
                setStroke(dp(2), Color.argb(0x77, 255, 255, 255))
            }
        }
        emblem.addView(ImageView(this).apply {
            setImageDrawable(CIcon(CI_GLOBE, Color.WHITE))
        }, FrameLayout.LayoutParams(dp(34), dp(34), Gravity.CENTER))
        row.addView(emblem, LinearLayout.LayoutParams(dp(66), dp(66)))

        val texts = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        texts.addView(TextView(this).apply {
            text = pick("کد کشورها", "Country Codes", "د هېوادونو کوډونه")
            textSize = 24f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(Color.WHITE)
        })
        texts.addView(TextView(this).apply {
            text = pick("${allCountries.size} کشور", "${allCountries.size} countries", "${allCountries.size} هېوادونه")
            textSize = 14f
            setTextColor(Color.argb(0xDD, 255, 255, 255))
        }, LinearLayout.LayoutParams(WRAP, WRAP).apply { topMargin = dp(2) })
        row.addView(texts, LinearLayout.LayoutParams(0, WRAP, 1f).apply { marginStart = dp(14) })

        box.addView(row, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(4) })
        return box
    }

    // ---------------- جستجو ----------------

    private fun buildSearch(): View {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(16), dp(4), dp(10), dp(4))
            background = rounded(Color.WHITE, 26, c("#E3EFE3"))
            elevation = dp(6).toFloat()
        }
        box.addView(ImageView(this).apply {
            setImageDrawable(CIcon(CI_SEARCH, c(GREEN)))
        }, LinearLayout.LayoutParams(dp(22), dp(22)))

        val edit = EditText(this).apply {
            hint = pick("نام کشور، کد یا قاره…", "Country, code or region…", "هېواد، کوډ یا لویه وچه…")
            textSize = 16f
            setSingleLine(true)
            background = null
            setTextColor(c(TEXT_DARK))
            setHintTextColor(c("#9CA3AF"))
            setPadding(dp(12), dp(12), dp(8), dp(12))
        }
        box.addView(edit, LinearLayout.LayoutParams(0, WRAP, 1f))

        edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                query = (s?.toString() ?: "").trim().toLowerCase(Locale.ROOT).removePrefix("+")
                applyFilter()
            }
        })
        return box
    }

    // ---------------- دکمه‌های قاره ----------------

    private fun buildRegionChips(): View {
        val scroll = HorizontalScrollView(this).apply {
            isHorizontalScrollBarEnabled = false
        }
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(dp(14), dp(12), dp(14), dp(6))
        }

        val regions = listOf<String?>(null, "Asia", "Europe", "Africa", "Americas", "Oceania")
        for (r in regions) {
            val chip = TextView(this).apply {
                text = if (r == null) pick("همه", "All", "ټول") else regionName(r)
                textSize = 14f
                setTypeface(typeface, Typeface.BOLD)
                setPadding(dp(16), dp(8), dp(16), dp(8))
                setOnClickListener {
                    regionFilter = r
                    chipViews.forEach { (key, view) -> styleChip(view, key == regionFilter) }
                    applyFilter()
                }
            }
            styleChip(chip, r == null)
            chipViews.add(Pair(r, chip))
            row.addView(chip, LinearLayout.LayoutParams(WRAP, WRAP).apply { marginEnd = dp(8) })
        }

        scroll.addView(row)
        if (isRtl) scroll.post { scroll.fullScroll(View.FOCUS_RIGHT) }
        return scroll
    }

    private fun styleChip(tv: TextView, selected: Boolean) {
        tv.setTextColor(if (selected) Color.WHITE else c(TEXT_DARK))
        tv.background = if (selected) rounded(c(GREEN), 18, null)
        else rounded(Color.WHITE, 18, c(CARD_STROKE))
    }

    private fun applyFilter() {
        val q = query
        shown = allCountries.filter { country ->
            (regionFilter == null || country.region == regionFilter) &&
                (q.isEmpty() ||
                    country.fa.contains(q) ||
                    country.en.toLowerCase(Locale.ROOT).contains(q) ||
                    country.dial.startsWith(q) ||
                    regionName(country.region).toLowerCase(Locale.ROOT).contains(q))
        }
        adapter.notifyDataSetChanged()
        emptyView.visibility = if (shown.isEmpty()) View.VISIBLE else View.GONE
    }

    // ---------------- لیست ----------------

    private inner class CountryAdapter : BaseAdapter() {
        override fun getCount() = shown.size
        override fun getItem(position: Int): Any = shown[position]
        override fun getItemId(position: Int) = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val v = convertView ?: buildRow()
            val h = v.tag as Holder
            val country = shown[position]

            if (country.iso == "AF") {
                h.wave.setEmoji(country.flag)
                h.wave.visibility = View.VISIBLE
                h.flag.visibility = View.GONE
            } else {
                h.flag.text = country.flag
                h.flag.visibility = View.VISIBLE
                h.wave.visibility = View.GONE
            }

            h.name.text = mainName(country)
            h.sub.text = secondName(country)
            h.dial.text = country.dialText
            val (bg, fg) = regionColors(country.region)
            h.region.text = regionName(country.region)
            h.region.setTextColor(c(fg))
            h.region.background = rounded(c(bg), 12, null)
            return v
        }
    }

    private fun buildRow(): View {
        val outer = LinearLayout(this).apply { setPadding(dp(2), dp(6), dp(2), dp(6)) }

        val card = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(14), dp(14), dp(14), dp(14))
            background = rounded(Color.WHITE, 20, c("#E8F0E8"))
            elevation = dp(2).toFloat()
        }
        outer.addView(card, LinearLayout.LayoutParams(MATCH, WRAP))

        // دایرهٔ سفید برجسته برای پرچم
        val flagBox = FrameLayout(this).apply {
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(Color.WHITE)
                setStroke(dp(1), c(CARD_STROKE))
            }
            elevation = dp(3).toFloat()
        }
        val flag = TextView(this).apply {
            textSize = 30f
            gravity = Gravity.CENTER
            setTextColor(Color.BLACK)   // رنگ کاملاً پررنگ تا ایموجی کم‌رنگ نشود
            includeFontPadding = false
        }
        flagBox.addView(flag, FrameLayout.LayoutParams(MATCH, MATCH))

        val wave = WavingFlagView(this, false).apply { visibility = View.GONE }
        flagBox.addView(wave, FrameLayout.LayoutParams(MATCH, MATCH).apply {
            setMargins(dp(8), dp(8), dp(8), dp(8))
        })
        card.addView(flagBox, LinearLayout.LayoutParams(dp(60), dp(60)))

        val texts = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        val name = TextView(this).apply {
            textSize = 17f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(c(TEXT_DARK))
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.END
        }
        val sub = TextView(this).apply {
            textSize = 13f
            setTextColor(c(TEXT_GREY))
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.END
        }
        texts.addView(name)
        texts.addView(sub)

        val chips = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        val dial = TextView(this).apply {
            textSize = 13f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(c(GREEN_DARK))
            textDirection = View.TEXT_DIRECTION_LTR
            setPadding(dp(10), dp(4), dp(10), dp(4))
            background = rounded(c(CARD_BG), 12, c(CARD_STROKE))
        }
        val region = TextView(this).apply {
            textSize = 12f
            setTypeface(typeface, Typeface.BOLD)
            setPadding(dp(10), dp(4), dp(10), dp(4))
        }
        chips.addView(dial)
        chips.addView(region, LinearLayout.LayoutParams(WRAP, WRAP).apply { marginStart = dp(8) })
        texts.addView(chips, LinearLayout.LayoutParams(WRAP, WRAP).apply { topMargin = dp(8) })

        card.addView(texts, LinearLayout.LayoutParams(0, WRAP, 1f).apply { marginStart = dp(14) })

        card.addView(ImageView(this).apply {
            setImageDrawable(CIcon(if (isRtl) CI_CHEV_L else CI_CHEV_R, c("#A5D6A7")))
        }, LinearLayout.LayoutParams(dp(20), dp(20)))

        outer.tag = Holder(flag, wave, name, sub, region, dial)
        return outer
    }

    // ---------------- کارت جزئیات (از پایین) ----------------

    private fun showDetails(country: Country) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val r = dp(26).toFloat()
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            layoutDirection = if (isRtl) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
            setPadding(dp(20), dp(12), dp(20), dp(24))
            background = GradientDrawable().apply {
                setColor(Color.WHITE)
                cornerRadii = floatArrayOf(r, r, r, r, 0f, 0f, 0f, 0f)
            }
        }

        box.addView(View(this).apply {
            background = rounded(c("#D1D5DB"), 3, null)
        }, LinearLayout.LayoutParams(dp(44), dp(5)).apply { bottomMargin = dp(14) })

        // پرچم: افغانستان زنده و روی میله، بقیه ثابت و پررنگ
        if (country.iso == "AF") {
            val wave = WavingFlagView(this, true)
            wave.setEmoji(country.flag)
            box.addView(wave, LinearLayout.LayoutParams(dp(180), dp(130)))
        } else {
            box.addView(TextView(this).apply {
                text = country.flag
                textSize = 72f
                setTextColor(Color.BLACK)
                gravity = Gravity.CENTER
                includeFontPadding = false
                setShadowLayer(dp(6).toFloat(), 0f, dp(3).toFloat(), Color.argb(0x40, 0, 0, 0))
            }, LinearLayout.LayoutParams(WRAP, WRAP))
        }

        box.addView(TextView(this).apply {
            text = mainName(country)
            textSize = 24f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(c(TEXT_DARK))
        }, LinearLayout.LayoutParams(WRAP, WRAP).apply { topMargin = dp(8) })

        box.addView(TextView(this).apply {
            text = secondName(country)
            textSize = 17f
            setTextColor(c(TEXT_GREY))
        }, LinearLayout.LayoutParams(WRAP, WRAP).apply { topMargin = dp(2); bottomMargin = dp(10) })

        box.addView(infoCard("📞", pick("کد تلفن", "Dial Code", "د تلیفون کوډ"), country.dialText, true))
        box.addView(infoCard("🌐", pick("قاره", "Region", "لویه وچه"), regionName(country.region), false))
        box.addView(infoCard("🗺️", pick("منطقه", "Subregion", "سیمه"), country.subregion, false))
        box.addView(infoCard("🏙️", pick("پایتخت", "Capital", "پلازمینه"), country.capital, false))

        box.addView(TextView(this).apply {
            text = pick("کپی کد تلفن", "Copy Dial Code", "کوډ کاپي کړئ") + "  📋"
            textSize = 17f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, dp(14), 0, dp(14))
            background = rounded(c(GREEN), 16, null)
            setOnClickListener {
                copy(country.dialText)
                dialog.dismiss()
            }
        }, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(18) })

        val scroll = ScrollView(this).apply { addView(box) }
        dialog.setContentView(scroll)

        dialog.window?.let { w ->
            w.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            w.setGravity(Gravity.BOTTOM)
            w.attributes = w.attributes.apply { windowAnimations = android.R.style.Animation_InputMethod }
        }
        dialog.show()
        dialog.window?.setLayout(MATCH, WRAP)
    }

    private fun infoCard(emoji: String, label: String, value: String, ltr: Boolean): View {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(16), dp(12), dp(16), dp(12))
            background = rounded(c(CARD_BG), 16, c(CARD_STROKE))
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(10) }
        }
        card.addView(TextView(this).apply {
            text = emoji
            textSize = 22f
            setTextColor(Color.BLACK)
        })
        val texts = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        texts.addView(TextView(this).apply {
            text = label
            textSize = 13f
            setTextColor(c(TEXT_GREY))
        })
        texts.addView(TextView(this).apply {
            text = value
            textSize = 18f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(c(TEXT_DARK))
            if (ltr) {
                textDirection = View.TEXT_DIRECTION_LTR
                textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            }
        })
        card.addView(texts, LinearLayout.LayoutParams(0, WRAP, 1f).apply { marginStart = dp(14) })
        return card
    }

    // ---------------- ابزارها ----------------

    private fun regionName(r: String): String = when (r) {
        "Asia" -> pick("آسیا", "Asia", "آسیا")
        "Europe" -> pick("اروپا", "Europe", "اروپا")
        "Africa" -> pick("آفریقا", "Africa", "افریقا")
        "Americas" -> pick("امریکا", "Americas", "امریکا")
        "Oceania" -> pick("اقیانوسیه", "Oceania", "اوقیانوسیه")
        else -> r
    }

    private fun regionColors(r: String): Pair<String, String> = when (r) {
        "Asia" -> "#E8F5E9" to "#2E7D32"
        "Europe" -> "#E3F2FD" to "#1565C0"
        "Africa" -> "#FFF3E0" to "#E65100"
        "Americas" -> "#FCE4EC" to "#AD1457"
        "Oceania" -> "#E0F7FA" to "#00838F"
        else -> "#F3F4F6" to "#374151"
    }

    private fun copy(text: String) {
        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.setPrimaryClip(ClipData.newPlainText("dial", text))
        Toast.makeText(this, pick("کپی شد: ", "Copied: ", "کاپي شو: ") + text, Toast.LENGTH_SHORT).show()
    }

    private fun mainName(country: Country) = if (lang == "en") country.en else country.fa
    private fun secondName(country: Country) = if (lang == "en") country.fa else country.en

    private fun rounded(fill: Int, radiusDp: Int, stroke: Int?): GradientDrawable =
        GradientDrawable().apply {
            cornerRadius = dp(radiusDp).toFloat()
            setColor(fill)
            if (stroke != null) setStroke(dp(1), stroke)
        }

    private fun c(hex: String) = Color.parseColor(hex)

    private fun pick(fa: String, en: String, ps: String) = when (lang) {
        "en" -> en
        "ps" -> ps
        else -> fa
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    companion object {
        private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    }

    // ---------------- پس‌زمینهٔ موج‌دار سربرگ ----------------

    private class WaveHeader(private val wave: Float) : Drawable() {
        private val p = Paint(Paint.ANTI_ALIAS_FLAG)
        private val path = Path()

        override fun draw(canvas: Canvas) {
            val b = bounds
            val w = b.width().toFloat()
            val h = b.height().toFloat()
            val x0 = b.left.toFloat()
            val y0 = b.top.toFloat()

            path.reset()
            path.moveTo(x0, y0)
            path.lineTo(x0 + w, y0)
            path.lineTo(x0 + w, y0 + h - wave)
            path.cubicTo(
                x0 + w * 0.65f, y0 + h + wave * 0.3f,
                x0 + w * 0.35f, y0 + h - wave * 1.6f,
                x0, y0 + h - wave * 0.6f
            )
            path.close()

            canvas.save()
            canvas.clipPath(path)

            p.style = Paint.Style.FILL
            p.shader = LinearGradient(
                x0, y0, x0 + w, y0 + h,
                Color.parseColor("#2E7D32"), Color.parseColor("#66BB6A"), Shader.TileMode.CLAMP
            )
            canvas.drawRect(x0, y0, x0 + w, y0 + h, p)
            p.shader = null

            p.color = Color.argb(0x22, 255, 255, 255)
            canvas.drawCircle(x0 + w * 0.08f, y0 + h * 0.15f, h * 0.35f, p)
            canvas.drawCircle(x0 + w * 0.95f, y0 + h * 0.85f, h * 0.30f, p)
            p.color = Color.argb(0x14, 255, 255, 255)
            canvas.drawCircle(x0 + w * 0.60f, y0, h * 0.25f, p)

            canvas.restore()
        }

        override fun setAlpha(alpha: Int) { p.alpha = alpha }
        override fun setColorFilter(colorFilter: ColorFilter?) { p.colorFilter = colorFilter }
        override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
    }

    // ---------------- آیکون‌ها ----------------

    private class CIcon(private val type: Int, color: Int) : Drawable() {
        private val stroke = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
            this.color = color
        }
        private val rect = RectF()

        override fun draw(canvas: Canvas) {
            val b = bounds
            val s = Math.min(b.width(), b.height()) / 24f
            canvas.save()
            canvas.translate(b.exactCenterX() - 12f * s, b.exactCenterY() - 12f * s)
            canvas.scale(s, s)
            stroke.strokeWidth = 2f

            when (type) {
                CI_GLOBE -> {
                    stroke.strokeWidth = 1.7f
                    canvas.drawCircle(12f, 12f, 9f, stroke)
                    rect.set(7.5f, 3f, 16.5f, 21f)
                    canvas.drawOval(rect, stroke)
                    canvas.drawLine(3f, 12f, 21f, 12f, stroke)
                    canvas.drawLine(4.3f, 7.5f, 19.7f, 7.5f, stroke)
                    canvas.drawLine(4.3f, 16.5f, 19.7f, 16.5f, stroke)
                }
                CI_SEARCH -> {
                    stroke.strokeWidth = 2.4f
                    canvas.drawCircle(10.5f, 10.5f, 6.5f, stroke)
                    canvas.drawLine(15.5f, 15.5f, 20.5f, 20.5f, stroke)
                }
                CI_CHEV_L -> {
                    stroke.strokeWidth = 2.4f
                    canvas.drawLine(15f, 5f, 8f, 12f, stroke)
                    canvas.drawLine(8f, 12f, 15f, 19f, stroke)
                }
                CI_CHEV_R -> {
                    stroke.strokeWidth = 2.4f
                    canvas.drawLine(9f, 5f, 16f, 12f, stroke)
                    canvas.drawLine(16f, 12f, 9f, 19f, stroke)
                }
            }
            canvas.restore()
        }

        override fun setAlpha(alpha: Int) { stroke.alpha = alpha }
        override fun setColorFilter(colorFilter: ColorFilter?) { stroke.colorFilter = colorFilter }
        override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
    }
}