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
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EtisalatServicesActivity : AppCompatActivity() {

    private val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
    private val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT

    private val SMS_NUMBER = "3378"
    private val GREEN_DARK = "#0B5A1F"
    private val GREEN_MAIN = "#1FA02F"
    private val GREEN_LIGHT = "#4CD137"
    private val GOLD = "#FFD54F"
    private val GOLD_DEEP = "#FFB300"
    private val BTN_GREEN = "#2E9E3E"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.parseColor("#031208")
        window.navigationBarColor = Color.parseColor("#06260F")

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(Color.parseColor("#031208"), Color.parseColor("#041A0B"), Color.parseColor("#06260F"))
            )
        }

        root.addView(header(), LinearLayout.LayoutParams(MATCH, WRAP).apply {
            setMargins(dp(10), dp(10), dp(10), dp(4))
        })

        val list = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(10), dp(6), dp(10), dp(24))
        }

        list.addView(card(Tr("دقیقه باقیمانده", "Remaining minutes", "پاتې دقیقې").get(this)) { body ->
            codeText(body, "*123#")
            button(body, Tr("دقیقه باقیمانده", "Remaining minutes", "پاتې دقیقې").get(this)) {
                dial("*123#")
            }
        })

        list.addView(card(Tr("قرضه 20 افغانی", "20 AFN loan", "د 20 افغانیو پور").get(this)) { body ->
            infoText(body, Tr("حروف 'SIM' را به 3378 بفرستید", "Send the letters 'SIM' to 3378", "'SIM' توري 3378 ته ولیږئ").get(this))
            button(body, Tr("قرضه", "Loan", "پور").get(this)) {
                sendSms(SMS_NUMBER, "SIM")
            }
        })

        list.addView(card(Tr("ارسال کردیت", "Send credit", "کریډیټ لیږل").get(this)) { body ->
            codeText(body, "*125*078xxxxxxx*مبلغ#")
        })

        list.addView(card(Tr("تنظیمات APN", "APN settings", "د APN تنظیمات").get(this)) { body ->
            row(body, "Name:", "Etisalat")
            row(body, "APN:", "etisalat.af.web")
        })

        list.addView(card(Tr("اضافه کردن کردیت", "Add credit", "کریډیټ اضافه کول").get(this)) { body ->
            codeText(body, "*404*xxxxxxxxxxxxxxx#")
        })

        list.addView(card(Tr("فعال سازی 3G", "Activate 3G", "3G فعالول").get(this)) { body ->
            infoText(body, Tr("حروف '3G' را به 3378 بفرستید", "Send the letters '3G' to 3378", "'3G' توري 3378 ته ولیږئ").get(this))
            button(body, Tr("فعال سازی", "Activate", "فعالول").get(this)) {
                sendSms(SMS_NUMBER, "3G")
            }
        })

        list.addView(card(Tr("فعال سازی 4G", "Activate 4G", "4G فعالول").get(this)) { body ->
            infoText(body, Tr("حروف '4G' را به 3378 بفرستید", "Send the letters '4G' to 3378", "'4G' توري 3378 ته ولیږئ").get(this))
            button(body, Tr("فعال سازی", "Activate", "فعالول").get(this)) {
                sendSms(SMS_NUMBER, "4G")
            }
        })

        val scroll = ScrollView(this).apply { addView(list) }
        root.addView(scroll, LinearLayout.LayoutParams(MATCH, 0, 1f))
        setContentView(root)
    }

    private fun header(): View {
        return TextView(this).apply {
            text = Tr("خدمات اتصالات", "Etisalat Services", "د اتصالاتو خدمتونه").get(this@EtisalatServicesActivity)
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(18), dp(14), dp(18), dp(14))
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.parseColor(GREEN_DARK), Color.parseColor(GREEN_MAIN), Color.parseColor(GREEN_LIGHT))
            ).apply {
                cornerRadius = dp(18).toFloat()
                setStroke(dp(1), Color.parseColor(GOLD))
            }
            setOnClickListener { finish() }
        }
    }

    private fun card(title: String, content: (LinearLayout) -> Unit): View {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = GradientDrawable().apply {
                cornerRadius = dp(18).toFloat()
                setColor(Color.parseColor("#06220F"))
                setStroke(dp(1), Color.parseColor(GREEN_LIGHT))
            }
            clipToOutline = true
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { setMargins(0, dp(6), 0, dp(6)) }
        }

        box.addView(TextView(this).apply {
            text = title
            textSize = 17f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            setPadding(dp(16), dp(14), dp(16), dp(14))
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.parseColor(GREEN_DARK), Color.parseColor(GREEN_MAIN), Color.parseColor(GREEN_LIGHT))
            )
        })

        val body = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(12), dp(16), dp(14))
        }
        content(body)
        box.addView(body)
        return box
    }

    private fun codeText(body: LinearLayout, code: String) {
        body.addView(TextView(this).apply {
            text = code
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.parseColor(GOLD))
            textDirection = View.TEXT_DIRECTION_LTR
            textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        })
    }

    private fun infoText(body: LinearLayout, text: String) {
        body.addView(TextView(this).apply {
            this.text = text
            textSize = 15f
            setTextColor(Color.parseColor("#D8F5DC"))
            setLineSpacing(dp(4).toFloat(), 1f)
        })
    }

    private fun row(body: LinearLayout, key: String, value: String) {
        val r = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutDirection = View.LAYOUT_DIRECTION_LTR
            setPadding(0, dp(8), 0, dp(8))
        }
        r.addView(TextView(this).apply {
            text = key
            textSize = 15f
            setTextColor(Color.parseColor("#9FE8DC"))
            layoutParams = LinearLayout.LayoutParams(0, WRAP, 1f)
        })
        r.addView(TextView(this).apply {
            text = value
            textSize = 16f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.parseColor(GOLD))
            gravity = Gravity.END
        })
        body.addView(r)
    }

    private fun button(body: LinearLayout, label: String, click: () -> Unit) {
        body.addView(Button(this).apply {
            text = label
            textSize = 14f
            isAllCaps = false
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            stateListAnimator = null
            background = GradientDrawable().apply {
                cornerRadius = dp(22).toFloat()
                setColor(Color.parseColor(BTN_GREEN))
            }
            setPadding(dp(22), 0, dp(22), 0)
            setOnClickListener { click() }
            layoutParams = LinearLayout.LayoutParams(WRAP, dp(44)).apply {
                gravity = Gravity.END
                topMargin = dp(12)
            }
        })
    }

    private fun dial(code: String) {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(code))))
    }

    private fun sendSms(number: String, body: String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:" + Uri.encode(number))
                putExtra("sms_body", body)
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "SMS error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
}