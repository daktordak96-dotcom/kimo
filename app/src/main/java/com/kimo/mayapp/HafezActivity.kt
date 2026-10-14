package com.kimo.mayapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import java.util.Locale
import kotlin.random.Random

class HafezActivity : AppCompatActivity() {

    // ---- کد تبلیغ تمام‌صفحه (فعلاً کد تستی گوگل؛ بعداً کد واقعی را اینجا بگذارید) ----
    private val INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712"

    private val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
    private val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT

    // رنگ‌های بنفش فال حافظ
    private val PAGE_TOP = Color.parseColor("#120A24")
    private val PAGE_BOTTOM = Color.parseColor("#0A0616")
    private val PURPLE = Color.parseColor("#B46CFF")
    private val PURPLE_DEEP = Color.parseColor("#4A1C9A")
    private val CARD = Color.parseColor("#1C1236")
    private val GOLD = Color.parseColor("#FFD54F")
    private val TEXT = Color.parseColor("#F3ECFF")
    private val SUB = Color.parseColor("#C9B8E8")

    private var interstitial: InterstitialAd? = null
    private var lastIndex = -1
    private var waitingForAd = false

    private lateinit var content: LinearLayout
    private lateinit var scroll: ScrollView

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

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.statusBarColor = PAGE_TOP
        window.navigationBarColor = PAGE_BOTTOM

        // راه‌اندازی تبلیغات و آماده‌کردن اولین تبلیغ
        try {
            MobileAds.initialize(this) {}
        } catch (e: Exception) { }
        loadAd()

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(PAGE_TOP, PAGE_BOTTOM)
            )
            layoutDirection = if (Strings.currentLang(this@HafezActivity) == "en")
                View.LAYOUT_DIRECTION_LTR else View.LAYOUT_DIRECTION_RTL
        }
        root.addView(toolbar())

        scroll = ScrollView(this)
        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(14), dp(8), dp(14), dp(24))
        }
        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(MATCH, 0, 1f))
        setContentView(root)

        showIntro()
    }

    // ---------------- نوار بالا ----------------
    private fun toolbar(): View {
        val bar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(14), dp(10), dp(14), dp(10))
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(PURPLE_DEEP, Color.parseColor("#7B3FD6"))
            ).apply { cornerRadius = dp(20).toFloat() }
            elevation = dp(3).toFloat()
        }
        val back = TextView(this).apply {
            text = if (Strings.currentLang(this@HafezActivity) == "en") "←" else "→"
            textSize = 24f
            setTextColor(Color.WHITE)
            setPadding(dp(4), 0, dp(12), 0)
            setOnClickListener { finish() }
        }
        val title = TextView(this).apply {
            text = HafezData.title.get(this@HafezActivity)
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
        }
        bar.addView(back)
        bar.addView(title)
        val wrap = FrameLayout(this)
        wrap.addView(bar, FrameLayout.LayoutParams(MATCH, WRAP).apply {
            setMargins(dp(10), dp(8), dp(10), dp(8))
        })
        return wrap
    }

    // ---------------- صفحهٔ نیت ----------------
    private fun showIntro() {
        content.removeAllViews()

        content.addView(TextView(this).apply {
            text = "📜"
            textSize = 64f
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
            setPadding(0, dp(16), 0, dp(8))
        }, LinearLayout.LayoutParams(MATCH, WRAP))

        content.addView(card().apply {
            addView(TextView(this@HafezActivity).apply {
                text = HafezData.niyat.get(this@HafezActivity)
                textSize = 17f
                setTextColor(TEXT)
                gravity = Gravity.CENTER
                setLineSpacing(dp(6).toFloat(), 1f)
            })
        })

        content.addView(mainButton(HafezData.btnGet.get(this)) { onGetFal() },
            LinearLayout.LayoutParams(MATCH, dp(58)).apply { topMargin = dp(22) })
    }

    // ---------------- گرفتن فال: اول تبلیغ، بعد فال ----------------
    private fun onGetFal() {
        if (waitingForAd) return
        if (showAdThenFal()) return

        // تبلیغ آماده نیست: کمی صبر و بعد فال بدون تبلیغ
        showLoading()
        Handler(Looper.getMainLooper()).postDelayed({
            if (isFinishing || isDestroyed) return@postDelayed
            if (!showAdThenFal()) {
                showFal()
                loadAd()
            }
        }, 1500)
    }

    // اگر تبلیغ آماده باشد نشانش می‌دهد و بعد از بستنش فال را نمایش می‌دهد
    private fun showAdThenFal(): Boolean {
        val ad = interstitial ?: return false
        interstitial = null
        waitingForAd = true
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                waitingForAd = false
                showFal()
                loadAd()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                waitingForAd = false
                showFal()
                loadAd()
            }
        }
        ad.show(this)
        return true
    }

    private fun loadAd() {
        try {
            InterstitialAd.load(
                this,
                INTERSTITIAL_ID,
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: InterstitialAd) {
                        interstitial = ad
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        interstitial = null
                    }
                }
            )
        } catch (e: Exception) {
            interstitial = null
        }
    }

    private fun showLoading() {
        content.removeAllViews()
        content.addView(ProgressBar(this).apply {
            indeterminateTintList = android.content.res.ColorStateList.valueOf(PURPLE)
        }, LinearLayout.LayoutParams(WRAP, WRAP).apply {
            gravity = Gravity.CENTER_HORIZONTAL
            topMargin = dp(80)
        })
        content.addView(TextView(this).apply {
            text = HafezData.loading.get(this@HafezActivity)
            textSize = 16f
            setTextColor(SUB)
            gravity = Gravity.CENTER
        }, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(16) })
    }

    // ---------------- نمایش فال ----------------
    private fun showFal() {
        val list = HafezData.ghazals
        var idx = Random.nextInt(list.size)
        if (list.size > 1) {
            while (idx == lastIndex) idx = Random.nextInt(list.size)
        }
        lastIndex = idx
        val g = list[idx]

        content.removeAllViews()

        // غزل
        val poemCard = card()
        poemCard.addView(TextView(this).apply {
            text = "﴾ " + HafezData.title.get(this@HafezActivity) + " ﴿"
            textSize = 15f
            setTextColor(GOLD)
            gravity = Gravity.CENTER
            typeface = Typeface.DEFAULT_BOLD
        }, LinearLayout.LayoutParams(MATCH, WRAP).apply { bottomMargin = dp(10) })

        g.beyts.forEachIndexed { i, (m1, m2) ->
            poemCard.addView(TextView(this).apply {
                text = m1
                textSize = 17f
                setTextColor(TEXT)
                gravity = Gravity.CENTER
                layoutDirection = View.LAYOUT_DIRECTION_RTL
                textDirection = View.TEXT_DIRECTION_RTL
            }, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(8) })
            poemCard.addView(TextView(this).apply {
                text = m2
                textSize = 17f
                setTextColor(TEXT)
                gravity = Gravity.CENTER
                layoutDirection = View.LAYOUT_DIRECTION_RTL
                textDirection = View.TEXT_DIRECTION_RTL
            }, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(2) })
            if (i < g.beyts.size - 1) {
                poemCard.addView(View(this).apply {
                    setBackgroundColor(Color.argb(0x40, 180, 108, 255))
                }, LinearLayout.LayoutParams(dp(60), dp(1)).apply {
                    gravity = Gravity.CENTER_HORIZONTAL
                    topMargin = dp(8)
                })
            }
        }
        content.addView(poemCard)

        // تفسیر
        val tafsirCard = card()
        tafsirCard.addView(TextView(this).apply {
            text = HafezData.tafsirTitle.get(this@HafezActivity)
            textSize = 18f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(GOLD)
        })
        tafsirCard.addView(TextView(this).apply {
            text = g.tafsir.get(this@HafezActivity)
            textSize = 16f
            setTextColor(TEXT)
            setLineSpacing(dp(5).toFloat(), 1f)
        }, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(8) })
        content.addView(tafsirCard, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(14) })

        // دکمه‌ها
        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        row.addView(outlineButton(HafezData.btnCopy.get(this)) { copyFal(g) },
            LinearLayout.LayoutParams(0, dp(54), 1f).apply { marginEnd = dp(6) })
        row.addView(mainButton(HafezData.btnAgain.get(this)) { onGetFal() },
            LinearLayout.LayoutParams(0, dp(54), 1.4f).apply { marginStart = dp(6) })
        content.addView(row, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(18) })

        scroll.post { scroll.scrollTo(0, 0) }
    }

    private fun copyFal(g: Ghazal) {
        val sb = StringBuilder()
        g.beyts.forEach { (a, b) -> sb.append(a).append("\n").append(b).append("\n\n") }
        sb.append(HafezData.tafsirTitle.get(this)).append(":\n").append(g.tafsir.get(this))
        sb.append("\n\n— ").append(getString(R.string.app_name))
        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.setPrimaryClip(ClipData.newPlainText("hafez", sb.toString()))
        Toast.makeText(this, HafezData.copied.get(this), Toast.LENGTH_SHORT).show()
    }

    // ---------------- اجزای ظاهری ----------------
    private fun card() = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(18), dp(18), dp(18), dp(18))
        background = GradientDrawable(
            GradientDrawable.Orientation.TL_BR, intArrayOf(Color.parseColor("#26184A"), CARD)
        ).apply {
            cornerRadius = dp(20).toFloat()
            setStroke(dp(1), Color.argb(0x66, 180, 108, 255))
        }
        layoutParams = LinearLayout.LayoutParams(MATCH, WRAP)
    }

    private fun mainButton(txt: String, click: () -> Unit) = Button(this).apply {
        text = txt
        textSize = 17f
        isAllCaps = false
        typeface = Typeface.DEFAULT_BOLD
        setTextColor(Color.WHITE)
        stateListAnimator = null
        background = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(Color.parseColor("#7B3FD6"), PURPLE)
        ).apply { cornerRadius = dp(30).toFloat() }
        setOnClickListener { click() }
    }

    private fun outlineButton(txt: String, click: () -> Unit) = Button(this).apply {
        text = txt
        textSize = 15f
        isAllCaps = false
        typeface = Typeface.DEFAULT_BOLD
        setTextColor(PURPLE)
        stateListAnimator = null
        background = GradientDrawable().apply {
            setColor(Color.TRANSPARENT)
            cornerRadius = dp(30).toFloat()
            setStroke(dp(1), PURPLE)
        }
        setOnClickListener { click() }
    }

    companion object {
        private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    }
}
