package com.kimo.mayapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView

class ServicesActivity : BaseActivity() {

    private lateinit var container: LinearLayout

    private fun t(key: String): String = Strings.get(this, key)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = t("services_title") + " " + t("etisalat")
        toolbar.setNavigationOnClickListener { finish() }

        container = findViewById(R.id.servicesContainer)

        // ۱. دقیقه باقیمانده: شماره‌گیر با کد آماده باز می‌شود
        addCard(
            t("svc_remaining_title"), "*123#", 26f, true,
            t("svc_remaining_title")
        ) { openDialer("*123#") }

        // ۲. قرضه
        addCard(
            t("svc_loan_title"), t("svc_loan_body"), 18f, false,
            t("svc_loan_btn")
        ) { openSmsApp("3378", "SIM") }

        // ۳. ارسال کردیت (فقط متن)
        addCard(t("svc_credit_send_title"), t("svc_credit_send_body"), 22f, true)

        // ۴. تنظیمات APN
        addApnCard()

        // ۵. اضافه کردن کردیت (فقط متن)
        addCard(t("svc_credit_add_title"), "*404*xxxxxxxxxxxxxx#", 22f, true)

        // ۶. فعال سازی 3G
        addCard(
            t("svc_3g_title"), t("svc_3g_body"), 18f, false,
            t("btn_activate")
        ) { openSmsApp("3378", "3G") }

        // ۷. فعال سازی 4G
        addCard(
            t("svc_4g_title"), t("svc_4g_body"), 18f, false,
            t("btn_activate")
        ) { openSmsApp("3378", "4G") }
    }

    private fun addCard(
        title: String,
        bodyText: String,
        bodySize: Float = 18f,
        isCode: Boolean = false,
        buttonText: String? = null,
        onClick: (() -> Unit)? = null
    ) {
        val inner = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        inner.addView(headerView(title))

        inner.addView(TextView(this).apply {
            text = bodyText
            textSize = bodySize
            setTextColor(Color.parseColor("#1F2937"))
            textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            if (isCode) textDirection = View.TEXT_DIRECTION_LTR
            setPadding(dp(16), dp(20), dp(16), dp(16))
        })

        if (buttonText != null && onClick != null) {
            inner.addView(Button(this).apply {
                text = buttonText
                textSize = 14f
                isAllCaps = false
                setTextColor(Color.WHITE)
                background = capsule("#16A34A")
                minWidth = 0
                minimumWidth = 0
                minHeight = 0
                minimumHeight = 0
                setPadding(dp(24), 0, dp(24), 0)
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, dp(40)
                ).apply {
                    gravity = Gravity.START
                    setMargins(dp(16), 0, dp(16), dp(16))
                }
                setOnClickListener { onClick() }
            })
        }

        container.addView(wrapInCard(inner))
    }

    private fun addApnCard() {
        val inner = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        inner.addView(headerView(t("svc_apn_title")))
        inner.addView(apnRow("Name:", "Etisalat"))
        inner.addView(View(this).apply {
            setBackgroundColor(Color.parseColor("#E0E0E0"))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(1)
            ).apply { setMargins(dp(16), 0, dp(16), 0) }
        })
        inner.addView(apnRow("APN:", "etisalat.af.web"))
        container.addView(wrapInCard(inner))
    }

    private fun apnRow(label: String, value: String): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(dp(16), dp(14), dp(16), dp(14))
            addView(TextView(context).apply {
                text = label
                textSize = 18f
                setTextColor(Color.parseColor("#475569"))
                textDirection = View.TEXT_DIRECTION_LTR
                textAlignment = View.TEXT_ALIGNMENT_VIEW_START
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            })
            addView(TextView(context).apply {
                text = value
                textSize = 18f
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(Color.parseColor("#1F2937"))
                textDirection = View.TEXT_DIRECTION_LTR
                textAlignment = View.TEXT_ALIGNMENT_VIEW_END
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
        }
    }

    private fun headerView(title: String): TextView {
        return TextView(this).apply {
            text = title
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.parseColor("#F97316"))
            textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            setPadding(dp(16), dp(12), dp(16), dp(12))
        }
    }

    private fun wrapInCard(content: View): CardView {
        return CardView(this).apply {
            radius = dp(14).toFloat()
            cardElevation = dp(3).toFloat()
            setCardBackgroundColor(Color.WHITE)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(dp(12), dp(8), dp(12), dp(8)) }
            addView(content)
        }
    }

    private fun capsule(colorHex: String): GradientDrawable {
        return GradientDrawable().apply {
            setColor(Color.parseColor(colorHex))
            cornerRadius = 200f
        }
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    // باز کردن شماره‌گیر با کد آماده (بدون نیاز به مجوز تماس)
    private fun openDialer(code: String) {
        try {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(code))))
        } catch (e: Exception) {
            Toast.makeText(this, t("svc_dialer_not_found"), Toast.LENGTH_SHORT).show()
        }
    }

    // باز کردن برنامه‌ی پیام‌ها با شماره و متن آماده
    private fun openSmsApp(number: String, body: String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$number")
                putExtra("sms_body", body)
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, t("sms_app_not_found"), Toast.LENGTH_SHORT).show()
        }
    }
}