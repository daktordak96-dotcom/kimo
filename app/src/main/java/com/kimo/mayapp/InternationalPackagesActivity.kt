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

class InternationalPackagesActivity : AppCompatActivity() {

    private val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
    private val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT

    private val SMS_NUMBER = "3378"
    private val CHECK_NUMBER = "606"
    private val CHECK_BODY = "505"

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

        root.addView(TextView(this).apply {
            text = InternationalPackagesData.screenTitle.get(this@InternationalPackagesActivity)
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(18), dp(14), dp(18), dp(14))
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.parseColor("#0B5A1F"), Color.parseColor("#1FA02F"), Color.parseColor("#4CD137"))
            ).apply {
                cornerRadius = dp(18).toFloat()
                setStroke(dp(1), Color.parseColor("#FFD54F"))
            }
            setOnClickListener { finish() }
        }, LinearLayout.LayoutParams(MATCH, WRAP).apply { setMargins(dp(10), dp(10), dp(10), dp(6)) })

        val list = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(10), dp(6), dp(10), dp(24))
        }

        InternationalPackagesData.all.forEach { list.addView(card(it)) }

        root.addView(ScrollView(this).apply { addView(list) }, LinearLayout.LayoutParams(MATCH, 0, 1f))
        setContentView(root)
    }

    private fun card(p: IntlPackage): View {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = GradientDrawable().apply {
                cornerRadius = dp(18).toFloat()
                setColor(Color.parseColor("#06220F"))
                setStroke(dp(1), Color.parseColor("#4CD137"))
            }
            clipToOutline = true
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { setMargins(0, dp(6), 0, dp(6)) }
        }

        box.addView(TextView(this).apply {
            text = p.title.get(this@InternationalPackagesActivity)
            textSize = 17f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            setPadding(dp(16), dp(14), dp(16), dp(14))
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.parseColor("#0B5A1F"), Color.parseColor("#1FA02F"), Color.parseColor("#4CD137"))
            )
        })

        box.addView(TextView(this).apply {
            text = p.price.get(this@InternationalPackagesActivity)
            textSize = 17f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.parseColor("#FFD54F"))
            gravity = Gravity.CENTER
            setPadding(dp(18), dp(6), dp(18), dp(6))
            background = GradientDrawable().apply {
                cornerRadius = dp(20).toFloat()
                setColor(Color.parseColor("#06220F"))
                setStroke(dp(2), Color.parseColor("#FFB300"))
            }
            layoutParams = LinearLayout.LayoutParams(WRAP, WRAP).apply {
                gravity = Gravity.CENTER_HORIZONTAL
                topMargin = dp(12)
            }
        })

        box.addView(TextView(this).apply {
            text = p.description(this@InternationalPackagesActivity)
            textSize = 14f
            setTextColor(Color.parseColor("#D8F5DC"))
            setLineSpacing(dp(4).toFloat(), 1f)
            setPadding(dp(16), dp(12), dp(16), dp(4))
        })

        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(dp(10), dp(12), dp(10), dp(4))
        }
        row.addView(actionButton(Tr("غیرفعال‌سازی", "Deactivate", "غیرفعالول").get(this), "#E91E63") {
            sendSms(SMS_NUMBER, p.off)
        })
        if (p.checkLine) {
            row.addView(actionButton(Tr("چک بیلانس", "Check Balance", "بیلانس کتل").get(this), "#0097A7") {
                sendSms(CHECK_NUMBER, CHECK_BODY)
            })
        }
        row.addView(actionButton(Tr("فعال‌سازی", "Activate", "فعالول").get(this), "#2E9E3E") {
            sendSms(SMS_NUMBER, p.on)
        })
        box.addView(row)

        return box
    }

    private fun actionButton(label: String, color: String, click: () -> Unit): Button {
        return Button(this).apply {
            text = label
            textSize = 13f
            isAllCaps = false
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            stateListAnimator = null
            background = GradientDrawable().apply {
                cornerRadius = dp(12).toFloat()
                setColor(Color.parseColor(color))
            }
            layoutParams = LinearLayout.LayoutParams(0, dp(48), 1f).apply { setMargins(dp(4), 0, dp(4), dp(8)) }
            setOnClickListener { click() }
        }
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