package com.kimo.mayapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class WeeklyFragment : Fragment() {

    private var currentOperator: String = "اتصالات"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { currentOperator = it.getString(ARG_OPERATOR_NAME, "اتصالات") }
    }

    // ساختار داده‌ای برای نگهداری اطلاعات هر بسته هفتگی
    data class PackageModel(
        val title: String,
        val price: String,
        val description: String,
        val activateSmsCode: String,
        val activatePhone: String,
        val checkSmsCode: String,
        val checkPhone: String,
        val deactivateSmsCode: String,
        val deactivatePhone: String
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val scrollView = ScrollView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.parseColor("#F5F5F5"))
        }

        val containerLayout = LinearLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
        }

        // لیست کامل بسته‌های هفتگی
        val packagesList = listOf(
            PackageModel(
                title = "550 ام بی هفتگی",
                price = "50 افغانی",
                description = "بسته 550 ام بی اینترنت برای 7 روز به قیمت 50 افغانی. تمدید خودکار فعال.\nبرای فعال‌سازی 300 و برای غیرفعال‌سازی PU 300 را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "300",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU 300",
                deactivatePhone = "3378"
            ),
            PackageModel(
                title = "1.5 جی بی هفتگی",
                price = "99 افغانی",
                description = "بسته 1.5 جی بی اینترنت برای 7 روز به قیمت 99 افغانی. تمدید خودکار فعال.\nبرای فعال‌سازی W99 و برای غیرفعال‌سازی PU W99 را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "W99",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU W99",
                deactivatePhone = "3378"
            ),
            PackageModel(
                title = "1.8 جی بی هفتگی",
                price = "120 افغانی",
                description = "بسته 1.8 جی بی اینترنت برای 7 روز به قیمت 120 افغانی. تمدید خودکار فعال.\nبرای فعال‌سازی W120 و برای غیرفعال‌سازی PU W120 را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "W120",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU W120",
                deactivatePhone = "3378"
            ),
            PackageModel(
                title = "2.5 جی بی هفتگی",
                price = "160 افغانی",
                description = "بسته 2.5 جی بی اینترنت برای 7 روز به قیمت 160 افغانی. تمدید خودکار فعال.\nبرای فعال‌سازی W160 و برای غیرفعال‌سازی PU W160 را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "W160",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU W160",
                deactivatePhone = "3378"
            ),
            PackageModel(
                title = "4 جی بی هفتگی",
                price = "220 افغانی",
                description = "بسته 4 جی بی اینترنت برای 7 روز به قیمت 220 افغانی. تمدید خودکار فعال.\nبرای فعال‌سازی W220 و برای غیرفعال‌سازی PU W220 را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "W220",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU W220",
                deactivatePhone = "3378"
            ),
            PackageModel(
                title = "7 جی بی هفتگی",
                price = "350 افغانی",
                description = "بسته 7 جی بی اینترنت برای 7 روز به قیمت 350 افغانی. تمدید خودکار فعال.\nبرای فعال‌سازی W350 و برای غیرفعال‌سازی PU W350 را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "W350",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU W350",
                deactivatePhone = "3378"
            )
        )

        // ساخت خودکار کارت‌ها بر اساس لیست بسته‌ها
        for (pkg in packagesList) {
            containerLayout.addView(createPackageCard(
                title = pkg.title,
                price = pkg.price,
                description = pkg.description,
                activateSmsCode = pkg.activateSmsCode,
                activatePhone = pkg.activatePhone,
                checkSmsCode = pkg.checkSmsCode,
                checkPhone = pkg.checkPhone,
                deactivateSmsCode = pkg.deactivateSmsCode,
                deactivatePhone = pkg.deactivatePhone
            ))
        }

        scrollView.addView(containerLayout)
        return scrollView
    }

    private fun createPackageCard(
        title: String,
        price: String,
        description: String,
        activateSmsCode: String,
        activatePhone: String,
        checkSmsCode: String,
        checkPhone: String,
        deactivateSmsCode: String,
        deactivatePhone: String
    ): View {
        val cardView = CardView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 24)
            }
            radius = 16f
            cardElevation = 6f
        }

        val cardLayout = LinearLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
        }

        // هدر کارت (رنگ نارنجی)
        val headerLayout = LinearLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setBackgroundColor(Color.parseColor("#FF9800"))
            setPadding(24, 24, 24, 24)
        }
        val titleView = TextView(requireContext()).apply {
            text = title
            setTextColor(Color.WHITE)
            textSize = 18f
            setTypeface(null, Typeface.BOLD)
        }
        headerLayout.addView(titleView)
        cardLayout.addView(headerLayout)

        // بدنه کارت
        val bodyLayout = LinearLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
            setBackgroundColor(Color.WHITE)
        }

        // کادر قیمت
        val priceContainer = LinearLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 16)
            }
            setBackgroundColor(Color.parseColor("#E1F5FE"))
            setPadding(24, 12, 24, 12)
        }
        val priceView = TextView(requireContext()).apply {
            text = price
            setTextColor(Color.parseColor("#0288D1"))
            textSize = 16f
            setTypeface(null, Typeface.BOLD)
        }
        priceContainer.addView(priceView)
        bodyLayout.addView(priceContainer)

        // متن توضیحات
        val descView = TextView(requireContext()).apply {
            text = description
            setTextColor(Color.parseColor("#333333"))
            textSize = 14f
            setPadding(0, 0, 0, 24)
        }
        bodyLayout.addView(descView)

        // سه دکمه (فعال‌سازی، چک بیلانس، غیرفعال‌سازی)
        val buttonsLayout = LinearLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.HORIZONTAL
            weightSum = 3f
        }

        // تابع کمکی برای ساخت دکمه‌های تخم‌مرغی و کپسولی مدرن
        fun createRoundedButton(textStr: String, bgColor: String, onClick: () -> Unit): Button {
            return Button(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                    setMargins(6, 0, 6, 0)
                }
                text = textStr
                setTextColor(Color.WHITE)
                textSize = 12f
                isAllCaps = false
                
                // اعمال ظاهر بیضی و تخم‌مرغی (کپسولی) با گوشه‌های کاملاً گرد
                val drawable = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    setColor(Color.parseColor(bgColor))
                    cornerRadius = 50f
                }
                background = drawable
                
                setOnClickListener { onClick() }
            }
        }

        val btnActivate = createRoundedButton("فعال‌‌سازی", "#4CAF50") {
            sendSms(activatePhone, activateSmsCode)
        }

        val btnCheck = createRoundedButton("چک بیلانس", "#009688") {
            sendSms(checkPhone, checkSmsCode)
        }

        val btnDeactivate = createRoundedButton("غیرفعال‌سازی", "#E91E63") {
            sendSms(deactivatePhone, deactivateSmsCode)
        }

        buttonsLayout.addView(btnActivate)
        buttonsLayout.addView(btnCheck)
        buttonsLayout.addView(btnDeactivate)
        bodyLayout.addView(buttonsLayout)

        cardLayout.addView(bodyLayout)
        cardView.addView(cardLayout)
        return cardView
    }

    private fun sendSms(phoneNo: String, message: String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$phoneNo")
                putExtra("sms_body", message)
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "خطا در ارسال پیامک", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val ARG_OPERATOR_NAME = "OPERATOR_NAME"
        @JvmStatic fun newInstance(operatorName: String) = WeeklyFragment().apply {
            arguments = Bundle().apply { putString(ARG_OPERATOR_NAME, operatorName) }
        }
    }
}
