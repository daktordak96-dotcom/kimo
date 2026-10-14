package com.kimo.mayapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale

class FavoritesActivity : AppCompatActivity() {

    private var lang: String = "fa"
    private lateinit var listBox: LinearLayout
    private lateinit var emptyText: TextView

    override fun attachBaseContext(newBase: Context?) {
        val p = newBase?.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val l = p?.getString(SELECTED_LANGUAGE, "fa") ?: "fa"
        super.attachBaseContext(updateBaseContextLocale(newBase!!, l))
    }

    private fun updateBaseContextLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lang = Strings.currentLang(this)
        window.statusBarColor = c("#0A1020")
        window.navigationBarColor = c("#0A1020")

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(c("#0A1020"))

        root.addView(buildHeader())

        val scroll = ScrollView(this)
        scroll.isFillViewport = true
        scroll.isVerticalScrollBarEnabled = false

        listBox = LinearLayout(this)
        listBox.orientation = LinearLayout.VERTICAL
        listBox.setPadding(dpi(12f), dpi(10f), dpi(12f), dpi(24f))

        emptyText = TextView(this).apply {
            text = pick(
                "هنوز هیچ بسته‌ای به علاقه‌مندی‌ها اضافه نکرده‌اید.\n\nاز بخش جستجو، بسته‌ها را با ♥ اضافه کنید.",
                "No favorites yet.\n\nAdd packages with ♥ from the search page.",
                "تر اوسه مو هیڅ بسته نه ده ثبت کړې.\n\nد لټون له برخې ♥ سره یې اضافه کړئ."
            )
            setTextColor(c("#7F8BA6"))
            textSize = 14f
            gravity = Gravity.CENTER
            setPadding(dpi(16f), dpi(50f), dpi(16f), dpi(16f))
        }

        scroll.addView(listBox, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        root.addView(scroll, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))

        setContentView(root)
        renderList()
    }

    override fun onResume() {
        super.onResume()
        if (::listBox.isInitialized) renderList()
    }

    // ---------- ابزارها ----------

    private fun dpf(v: Float) = v * resources.displayMetrics.density
    private fun dpi(v: Float) = (v * resources.displayMetrics.density).toInt()
    private fun c(hex: String) = Color.parseColor(hex)
    private fun alpha(hex: String, a: Int): Int {
        val col = c(hex); return Color.argb(a, Color.red(col), Color.green(col), Color.blue(col))
    }
    private fun pick(fa: String, en: String, ps: String) = when (lang) {
        "en" -> en; "ps" -> ps; else -> fa
    }
    private fun rounded(fill: Int, radiusDp: Float, stroke: Int? = null): GradientDrawable =
        GradientDrawable().apply {
            cornerRadius = dpf(radiusDp); setColor(fill)
            if (stroke != null) setStroke(dpi(1f), stroke)
        }

    private fun kindLabel(kind: String) = when (kind) {
        "call" -> pick("تماس", "Call", "زنګ")
        else -> pick("اینترنت", "Internet", "انټرنټ")
    }

    // ---------- نوار بالا ----------

    private fun buildHeader(): View {
        val bar = LinearLayout(this)
        bar.orientation = LinearLayout.HORIZONTAL
        bar.gravity = Gravity.CENTER_VERTICAL
        bar.setBackgroundColor(c("#0E1628"))
        bar.setPadding(dpi(8f), dpi(12f), dpi(12f), dpi(12f))
        bar.elevation = dpf(6f)

        val back = TextView(this)
        back.text = "←"
        back.textSize = 22f
        back.setTextColor(Color.WHITE)
        back.gravity = Gravity.CENTER
        val bp = dpi(8f); back.setPadding(bp, bp, bp, bp)
        back.isClickable = true
        back.setOnClickListener { finish() }
        bar.addView(back, LinearLayout.LayoutParams(dpi(44f), dpi(44f)))

        val title = TextView(this)
        title.text = pick("علاقه‌مندی‌ها", "Favorites", "خوښې")
        title.textSize = 19f
        title.setTextColor(Color.WHITE)
        title.setTypeface(title.typeface, Typeface.BOLD)
        val tlp = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        tlp.marginStart = dpi(8f)
        bar.addView(title, tlp)

        return bar
    }

    // ---------- نمایش لیست ----------

    private fun renderList() {
        listBox.removeAllViews()
        val arr = Favorites.all(this)

        if (arr.length() == 0) {
            listBox.addView(emptyText)
            return
        }

        for (i in 0 until arr.length()) {
            val o = arr.optJSONObject(i) ?: continue
            listBox.addView(buildCard(o))
        }
    }

    private fun buildCard(o: JSONObject): View {
        val opName = o.optString("op")
        val opColor = o.optString("color", "#2F8CFF")
        val kind = o.optString("kind", "internet")
        val title = o.optString("title")
        val price = o.optString("price")
        val desc = o.optString("desc")

        val card = LinearLayout(this)
        card.orientation = LinearLayout.HORIZONTAL
        card.gravity = Gravity.CENTER_VERTICAL
        card.background = rounded(c("#111A2E"), 14f, alpha(opColor, 0x44))
        card.setPadding(dpi(12f), dpi(12f), dpi(12f), dpi(12f))
        card.isClickable = true
        val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.bottomMargin = dpi(8f)
        card.layoutParams = lp

        val stripe = View(this)
        stripe.background = rounded(c(opColor), 2f)
        card.addView(stripe, LinearLayout.LayoutParams(dpi(4f), dpi(44f)))

        val texts = LinearLayout(this)
        texts.orientation = LinearLayout.VERTICAL
        val tlp = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        tlp.marginStart = dpi(10f)
        card.addView(texts, tlp)

        val t = TextView(this).apply {
            text = title
            setTextColor(Color.WHITE)
            textSize = 14f
            setTypeface(typeface, Typeface.BOLD)
            maxLines = 2
            ellipsize = TextUtils.TruncateAt.END
        }
        texts.addView(t)

        val meta = LinearLayout(this)
        meta.orientation = LinearLayout.HORIZONTAL
        meta.gravity = Gravity.CENTER_VERTICAL
        val mlp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mlp.topMargin = dpi(5f)
        texts.addView(meta, mlp)

        val badge = TextView(this).apply {
            text = opName + " · " + kindLabel(kind)
            setTextColor(c(opColor))
            textSize = 11f
            background = rounded(alpha(opColor, 0x22), 8f)
            setPadding(dpi(7f), dpi(2f), dpi(7f), dpi(2f))
        }
        meta.addView(badge)

        val p = TextView(this).apply {
            text = price
            setTextColor(c("#C9D3EA"))
            textSize = 12f
            setTypeface(typeface, Typeface.BOLD)
            gravity = Gravity.END
        }
        val plp = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        plp.marginStart = dpi(8f)
        meta.addView(p, plp)

        // دکمهٔ قلب (حذف از علاقه‌مندی)
        val heart = TextView(this).apply {
            text = "♥"
            setTextColor(c("#FF3B4F"))
            textSize = 22f
            gravity = Gravity.CENTER
        }
        heart.setPadding(dpi(8f), dpi(4f), dpi(4f), dpi(4f))
        heart.setOnClickListener {
            Favorites.remove(this@FavoritesActivity, o.optString("key"))
            renderList()
        }
        card.addView(heart)

        card.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("$title  —  $price")
                .setMessage(opName + " · " + kindLabel(kind) + "\n\n" + desc)
                .setPositiveButton(pick("باشه", "OK", "سمه ده"), null)
                .show()
        }
        return card
    }

    companion object {
        private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    }
}

// ===================================================================
//  نگهدارندهٔ علاقه‌مندی‌ها — مشترک بین جستجو و این صفحه
// ===================================================================
object Favorites {
    private const val PREFS = "Favorites"
    private const val KEY = "list"

    fun all(ctx: Context): JSONArray {
        val raw = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY, "[]") ?: "[]"
        return try { JSONArray(raw) } catch (e: Exception) { JSONArray() }
    }

    fun contains(ctx: Context, key: String): Boolean {
        val arr = all(ctx)
        for (i in 0 until arr.length()) {
            if (arr.optJSONObject(i)?.optString("key") == key) return true
        }
        return false
    }

    fun add(ctx: Context, key: String, op: String, color: String,
            kind: String, title: String, price: String, desc: String) {
        if (contains(ctx, key)) return
        val arr = all(ctx)
        val o = JSONObject()
        o.put("key", key)
        o.put("op", op)
        o.put("color", color)
        o.put("kind", kind)
        o.put("title", title)
        o.put("price", price)
        o.put("desc", desc)
        arr.put(o)
        save(ctx, arr)
    }

    fun remove(ctx: Context, key: String) {
        val arr = all(ctx)
        val out = JSONArray()
        for (i in 0 until arr.length()) {
            val o = arr.optJSONObject(i) ?: continue
            if (o.optString("key") != key) out.put(o)
        }
        save(ctx, out)
    }

    private fun save(ctx: Context, arr: JSONArray) {
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putString(KEY, arr.toString()).apply()
    }
}