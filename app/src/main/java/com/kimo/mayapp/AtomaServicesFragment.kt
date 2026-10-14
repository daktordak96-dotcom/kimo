package com.kimo.mayapp

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment

class AtomaServicesFragment : Fragment() {

    private val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
    private val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT
    private val t = Gold

    // رنگ‌های زرد طلایی اتوما (ملایم)
    private object Gold {
        val pageGrad = intArrayOf(Color.parseColor("#0E0B04"), Color.parseColor("#16120A"), Color.parseColor("#0E0B04"))
        val barGrad = intArrayOf(Color.parseColor("#7A5E00"), Color.parseColor("#A07A00"), Color.parseColor("#C49A12"))
        val cardGrad = intArrayOf(Color.parseColor("#2A220E"), Color.parseColor("#1E180A"), Color.parseColor("#161208"))
        val titleText = Color.parseColor("#FFF8E1")
        val introText = Color.parseColor("#E8DDBF")
        val keyText = Color.parseColor("#B8A77A")
        val stroke = Color.parseColor("#8A7020")
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        val ctx = requireContext()

        val list = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            layoutDirection =
                if (Strings.currentLang(ctx) == "en") View.LAYOUT_DIRECTION_LTR
                else View.LAYOUT_DIRECTION_RTL
            setPadding(dp(12), dp(12), dp(12), dp(20))
        }
        AtomaData.services.forEach { list.addView(card(it)) }

        return ScrollView(ctx).apply {
            background = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, t.pageGrad)
            addView(list)
        }
    }

    private fun card(s: AtomaData.AtomaService): View {
        val ctx = requireContext()
        val box = LinearLayout(ctx).apply { orientation = LinearLayout.VERTICAL }

        box.addView(label(s.title.get(ctx), 19f, t.titleText).apply {
            typeface = Typeface.DEFAULT_BOLD
            setPadding(dp(18), dp(16), dp(18), dp(16))
            background = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, t.barGrad)
        })

        val body = s.body.get(ctx)
        if (body.isNotEmpty()) {
            box.addView(label(body, 18f, t.introText).apply {
                layoutDirection = View.LAYOUT_DIRECTION_LTR
                layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply {
                    setMargins(dp(18), dp(16), dp(18), dp(16))
                }
            })
        }

        s.rows.forEach { (k, v) ->
            val row = LinearLayout(ctx).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutDirection = View.LAYOUT_DIRECTION_LTR
                setPadding(dp(18), dp(10), dp(18), dp(10))
            }
            row.addView(label("$k:", 16f, t.keyText).apply {
                layoutParams = LinearLayout.LayoutParams(0, WRAP, 1f)
            })
            row.addView(label(v, 16f, t.introText).apply {
                typeface = Typeface.DEFAULT_BOLD
            })
            box.addView(row)
        }

        val radius = dp(20).toFloat()
        return FrameLayout(ctx).apply {
            background = GradientDrawable(GradientDrawable.Orientation.TL_BR, t.cardGrad).apply {
                cornerRadius = radius
                setStroke(dp(1), t.stroke)
            }
            clipToOutline = true
            outlineProvider = ViewOutlineProvider.BACKGROUND
            elevation = dp(4).toFloat()
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply {
                setMargins(dp(4), dp(8), dp(4), dp(8))
            }
            addView(box)
        }
    }

    private fun label(txt: String, size: Float, color: Int) = TextView(requireContext()).apply {
        text = txt
        textSize = size
        setTextColor(color)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    companion object {
        fun newInstance() = AtomaServicesFragment()
    }
}