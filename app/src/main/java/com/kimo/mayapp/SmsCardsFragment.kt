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
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class SmsCardsFragment : Fragment() {

    private data class SmsCard(
        val title: String,
        val price: String,
        val desc: String,
        val activateText: String,
        val deactivateText: String,
        val hasCheck: Boolean
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val ctx = requireContext()
        val cards = cardsForTab(arguments?.getInt(ARG_TAB) ?: 0)

        // تب خالی (متفرقه)
        if (cards.isEmpty()) {
            return FrameLayout(ctx).apply {
                setBackgroundColor(Color.parseColor("#F1F1F1"))
                addView(
                    TextView(ctx).apply {
                        text = Strings.get(ctx, "soon_generic")
                        textSize = 18f
                        setTextColor(Color.parseColor("#6B7280"))
                        gravity = Gravity.CENTER
                    },
                    FrameLayout.LayoutParams(MATCH, MATCH)
                )
            }
        }

        val list = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), dp(8), dp(12), dp(16))
        }
        cards.forEach { list.addView(buildCard(it)) }

        return ScrollView(ctx).apply {
            setBackgroundColor(Color.parseColor("#F1F1F1"))
            addView(list)
        }
    }

    private fun cardsForTab(tab: Int): List<SmsCard> {
        val ctx = requireContext()
        fun s(key: String) = SmsStrings.get(ctx, key)
        return when (tab) {
            0 -> listOf(
                SmsCard(s("sms_daily_title"), s("sms_daily_price"), s("sms_daily_desc"), "BDS", "PU BDS", false)
            )
            1 -> listOf(
                SmsCard(s("sms_weekly_title"), s("sms_weekly_price"), s("sms_weekly_desc"), "BWS", "PU BWS", false)
            )
            2 -> listOf(
                SmsCard(s("sms_monthly_title"), s("sms_monthly_price"), s("sms_monthly_desc"), "BMS", "PU BMS", false)
            )
            3 -> listOf(
                SmsCard(s("sms_combo1_title"), s("sms_combo1_price"), s("sms_combo1_desc"), "M200", "PU M200", true),
                SmsCard(s("sms_combo2_title"), s("sms_combo2_price"), s("sms_combo2_desc"), "700", "PU 700", true)
            )
            else -> emptyList()
        }
    }

    private fun buildCard(card: SmsCard): CardView {
        val ctx = requireContext()
        val inner = LinearLayout(ctx).apply { orientation = LinearLayout.VERTICAL }

        // عنوان نارنجی
        inner.addView(TextView(ctx).apply {
            text = card.title
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.parseColor("#1A1A1A"))
            setBackgroundColor(Color.parseColor("#F97316"))
            textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            setPadding(dp(16), dp(16), dp(16), dp(16))
        })

        // قیمت
        inner.addView(TextView(ctx).apply {
            text = card.price
            textSize = 19f
            setTextColor(Color.parseColor("#1F2937"))
            setBackgroundColor(Color.parseColor("#DBEAFE"))
            setPadding(dp(14), dp(6), dp(14), dp(6))
            layoutParams = LinearLayout.LayoutParams(WRAP, WRAP).apply {
                gravity = Gravity.CENTER_HORIZONTAL
                topMargin = dp(14)
            }
        })

        // توضیحات
        inner.addView(TextView(ctx).apply {
            text = card.desc
            textSize = 16f
            setLineSpacing(dp(4).toFloat(), 1f)
            setTextColor(Color.parseColor("#1F2937"))
            textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply {
                setMargins(dp(16), dp(16), dp(16), dp(16))
            }
        })

        // خط جداکننده
        inner.addView(View(ctx).apply {
            setBackgroundColor(Color.parseColor("#E0E0E0"))
            layoutParams = LinearLayout.LayoutParams(MATCH, dp(1))
        })

        // دکمه‌های کپسولی
        val row = LinearLayout(ctx).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(dp(12), dp(12), dp(12), dp(12))
        }
        row.addView(actionButton(Strings.get(ctx, "btn_deactivate"), "#E11D48") {
            openSmsApp("3378", card.deactivateText)
        })
        if (card.hasCheck) {
            row.addView(actionButton(Strings.get(ctx, "btn_check"), "#0891B2") {
                openSmsApp("606", "505")
            })
        }
        row.addView(actionButton(Strings.get(ctx, "btn_activate"), "#16A34A") {
            openSmsApp("3378", card.activateText)
        })
        inner.addView(row)

        return CardView(ctx).apply {
            radius = dp(14).toFloat()
            cardElevation = dp(3).toFloat()
            setCardBackgroundColor(Color.WHITE)
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply {
                setMargins(0, dp(8), 0, dp(8))
            }
            addView(inner)
        }
    }

    private fun actionButton(label: String, colorHex: String, onClick: () -> Unit): Button {
        return Button(requireContext()).apply {
            text = label
            textSize = 13f
            isAllCaps = false
            setTextColor(Color.WHITE)
            minWidth = 0
            minimumWidth = 0
            setPadding(0, 0, 0, 0)
            background = GradientDrawable().apply {
                setColor(Color.parseColor(colorHex))
                cornerRadius = 200f
            }
            layoutParams = LinearLayout.LayoutParams(0, dp(48), 1f).apply {
                setMargins(dp(4), 0, dp(4), 0)
            }
            setOnClickListener { onClick() }
        }
    }

    private fun openSmsApp(number: String, body: String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$number")
                putExtra("sms_body", body)
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                Strings.get(requireContext(), "sms_app_not_found"),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    companion object {
        private const val ARG_TAB = "tab_index"
        private const val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
        private const val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT

        fun newInstance(tabIndex: Int) = SmsCardsFragment().apply {
            arguments = Bundle().apply { putInt(ARG_TAB, tabIndex) }
        }
    }
}