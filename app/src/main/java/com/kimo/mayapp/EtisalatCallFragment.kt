package com.kimo.mayapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class EtisalatCallFragment : Fragment() {

    private val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
    private val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val ctx = requireContext()
        val tab = arguments?.getInt(ARG_TAB) ?: 0
        val items = EtisalatCallData.forTab(tab)

        val list = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), dp(10), dp(12), dp(24))
        }

        if (items.isEmpty()) {
            list.addView(TextView(ctx).apply {
                text = Tr("به‌زودی", "Coming soon", "ژر راځي").get(ctx)
                textSize = 18f
                setTextColor(Color.parseColor("#B2FFF0"))
                gravity = Gravity.CENTER
                setPadding(0, dp(60), 0, 0)
            })
        } else {
            items.forEach { list.addView(card(it)) }
        }

        return ScrollView(ctx).apply {
            setBackgroundColor(Color.parseColor("#04120F"))
            addView(list)
        }
    }

    private fun card(p: EtiPackage): View {
        val ctx = requireContext()
        val box = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(Color.parseColor("#0A3A1E"), Color.parseColor("#06220F"), Color.parseColor("#041A0B"))
            ).apply {
                cornerRadius = dp(18).toFloat()
                setStroke(dp(1), Color.parseColor("#4CD137"))
            }
            clipToOutline = true
            outlineProvider = ViewOutlineProvider.BACKGROUND
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { setMargins(0, dp(6), 0, dp(6)) }
        }

        box.addView(label(p.titleText(ctx), 17f, "#FFFFFF", true).apply {
            setPadding(dp(16), dp(14), dp(16), dp(14))
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.parseColor("#0B5A1F"), Color.parseColor("#1FA02F"), Color.parseColor("#4CD137"))
            )
        })

        box.addView(label(p.priceText(ctx), 17f, "#FFD54F", true).apply {
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

        val body = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(10), dp(16), dp(4))
        }
        body.addView(label(p.validityText(ctx), 14f, "#D8F5DC", false))
        body.addView(label(p.activateText(ctx), 14f, "#D8F5DC", false).apply { setPadding(0, dp(8), 0, 0) })
        body.addView(label(p.checkText(ctx), 14f, "#D8F5DC", false).apply { setPadding(0, dp(8), 0, 0) })
        box.addView(body)

        val row = LinearLayout(ctx).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(dp(10), dp(12), dp(10), dp(4))
        }
        row.addView(actionButton(EtisalatData.btnOff.get(ctx), "#E91E63") {
            sendSms(EtisalatCallData.SMS_NUMBER, p.offCode)
        })
        row.addView(actionButton(EtisalatData.btnCheck.get(ctx), "#0097A7") {
            sendSms(EtisalatCallData.CHECK_NUMBER, EtisalatCallData.CHECK_BODY)
        })
        row.addView(actionButton(EtisalatData.btnOn.get(ctx), "#2E9E3E") {
            sendSms(EtisalatCallData.SMS_NUMBER, p.onCode)
        })
        box.addView(row)

        return box
    }

    private fun actionButton(label: String, color: String, click: () -> Unit): Button {
        val ctx = requireContext()
        return Button(ctx).apply {
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

    private fun label(t: String, size: Float, color: String, bold: Boolean) =
        TextView(requireContext()).apply {
            text = t
            textSize = size
            setTextColor(Color.parseColor(color))
            if (bold) typeface = Typeface.DEFAULT_BOLD
            textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        }

    private fun sendSms(number: String, body: String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:" + Uri.encode(number))
                putExtra("sms_body", body)
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "SMS error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    companion object {
        private const val ARG_TAB = "tab"

        fun newInstance(tab: Int) = EtisalatCallFragment().apply {
            arguments = Bundle().apply { putInt(ARG_TAB, tab) }
        }
    }
}