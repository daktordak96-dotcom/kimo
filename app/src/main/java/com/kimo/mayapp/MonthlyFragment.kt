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

class MonthlyFragment : Fragment() {

    // ساختار داده‌ای برای نگهداری اطلاعات هر بسته
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

        // لیست کامل تمام بسته‌های ماهانه
        val packagesList = listOf(
            PackageModel(
                title = "1 جی بی ماهانه",
                price = "95 افغانی",
                description = "بسته 1 جی بی اینترنت برای 30 روز به قیمت 95 افغانی. تمدید خودکار فعال.\nبرای فعال‌سازی M95 و برای غیرفعال‌سازی PU M95 را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "M95",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU M95",
                deactivatePhone = "3378"
            ),
            PackageModel(
                title = "1.2 جی بی ماهانه",
                price = "110 افغانی",
                description = "بسته 1.2 جی بی اینترنت برای 30 روز به قیمت 110 افغانی. تمدید خودکار فعال.\nبرای فعال‌‌سازی 110 و برای غیرفعال‌سازی PU 110 را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "110",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU 110",
                deactivatePhone = "3378"
            ),
            PackageModel(
                title = "1.65 جی بی ماهانه",
                price = "150 افغانی",
                description = "بسته 1.65 جی بی اینترنت برای 30 روز به قیمت 150 افغانی. تمدید خودکار فعال.\nبرای فعال سازی M150 و برای غیرفعال سازی PU M150 را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "M150",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU M150",
                deactivatePhone = "3378"
            ),
            PackageModel(
                title = "2 جی بی ماهانه",
                price = "190 افغانی",
                description = "بسته 2 جی بی اینترنت برای 30 روز به قیمت 190 افغانی. تمدید خودکار فعال.\nبرای فعال سازی 190 و برای غیرفعال سازی PU 190 را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "190",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU 190",
                deactivatePhone = "3378"
            ),
            PackageModel(
                title = "3 جی بی ماهانه",
                price = "250 افغانی",
                description = "بسته 3 جی بی اینترنت برای 30 روز به قیمت 250 افغانی. تمدید خودکار فعال.\nبرای فعال سازی 2GB و برای غیرفعال سازی PU 2GB را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "2GB",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU 2GB",
                deactivatePhone = "3378"
            ),
            PackageModel(
                title = "4 جی بی ماهانه",
                price = "325 افغانی",
                description = "بسته 4 جی بی اینترنت برای 30 روز به قیمت 325 افغانی. تمدید خودکار فعال.\nبرای فعال سازی M325 و برای غیرفعال سازی PU M325 را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "M325",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU M325",
                deactivatePhone = "3378"
            ),
            PackageModel(
                title = "5 جی بی ماهانه",
                price = "325 افغانی",
                description = "بسته 5 جی بی اینترنت برای 30 روز به قیمت 325 افغانی. تمدید خودکار فعال.\nبرای فعال سازی N325 و برای غیرفعال سازی PU N325 را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "N325",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU N325",
                deactivatePhone = "3378"
            ),
            PackageModel(
                title = "5.5 جی بی ماهانه",
                price = "440 افغانی",
                description = "بسته 5.5 جی بی اینترنت برای 30 روز به قیمت 440 افغانی. تمدید خودکار فعال.\nبرای فعال سازی 440 و برای غیرفعال سازی PU 440 را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "440",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU 440",
                deactivatePhone = "3378"
            ),
            PackageModel(
                title = "7 جی بی ماهانه",
                price = "550 افغانی",
                description = "بسته 7 جی بی اینترنت برای 30 روز به قیمت 550 افغانی. تمدید خودکار فعال.\nبرای فعال سازی 550 و برای غیرفعال سازی PU 550 را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "550",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU 550",
                deactivatePhone = "3378"
            ),
            PackageModel(
                title = "14 جی بی ماهانه",
                price = "699 افغانی",
                description = "بسته 14 جی بی اینترنت برای 30 روز به قیمت 699 افغانی. تمدید خودکار فعال.\nبرای فعال سازی N699 و برای غیرفعال سازی PU N699 را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "N699",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU N699",
                deactivatePhone = "3378"
            ),
            PackageModel(
                title = "13.5 جی بی ماهانه",
                price = "799 افغانی",
                description = "بسته 13.5 جی بی اینترنت برای 30 روز به قیمت 799 افغانی. تمدید خودکار فعال.\nبرای فعال سازی 7GB و برای غیرفعال سازی PU 7GB را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "7GB",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU 7GB",
                deactivatePhone = "3378"
            ),
            PackageModel(
                title = "15 جی بی ماهانه (5 جی بی شبانه)",
                price = "699 افغانی",
                description = "بسته 15 جی بی اینترنت برای 30 روز به قیمت 699 افغانی. 5 جی بی قابل استفاده از 11 شب تا 7 صبح. تمدید خودکار فعال.\nبرای فعال سازی M15 و برای غیرفعال سازی PU M15 را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "M15",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU M15",
                deactivatePhone = "3378"
            ),
            PackageModel(
                title = "25 جی بی ماهانه",
                price = "999 افغانی",
                description = "بسته 25 جی بی اینترنت برای 30 روز به قیمت 999 افغانی. تمدید خودکار فعال.\nبرای فعال سازی 10GB و برای غیرفعال سازی PU 10GB را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "10GB",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU 10GB",
                deactivatePhone = "3378"
            ),
            PackageModel(
                title = "30 جی بی ماهانه (استاندارد)",
                price = "1199 افغانی",
                description = "بسته 30 جی بی اینترنت برای 30 روز به قیمت 1199 افغانی. تمدید خودکار فعال.\nبرای فعال سازی 1199 و برای غیرفعال سازی PU 1199 را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "1199",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU 1199",
                deactivatePhone = "3378"
            ),
            PackageModel(
                title = "30 جی بی ماهانه (پیشرفته)",
                price = "1399 افغانی",
                description = "بسته 30 جی بی اینترنت برای 30 روز به قیمت 1399 افغانی. تمدید خودکار فعال.\nبرای فعال سازی 1399 و برای غیرفعال سازی PU 1399 را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "1399",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU 1399",
                deactivatePhone = "3378"
            ),
            PackageModel(
                title = "40 جی بی ماهانه",
                price = "1699 افغانی",
                description = "بسته 40 جی بی اینترنت برای 30 روز به قیمت 1699 افغانی. تمدید خودکار فعال.\nبرای فعال سازی 1699 و برای غیرفعال سازی PU 1699 را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "1699",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU 1699",
                deactivatePhone = "3378"
            ),
            PackageModel(
                title = "50 جی بی ماهانه",
                price = "2099 افغانی",
                description = "بسته 50 جی بی اینترنت برای 30 روز به قیمت 2099 افغانی. تمدید خودکار فعال.\nبرای فعال سازی 2099 و برای غیرفعال سازی PU 2099 را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
                activateSmsCode = "2099",
                activatePhone = "3378",
                checkSmsCode = "505",
                checkPhone = "606",
                deactivateSmsCode = "PU 2099",
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
                    cornerRadius = 50f // شعاع گردی لبه‌ها
                }
                background = drawable
                
                setOnClickListener { onClick() }
            }
        }

        val btnActivate = createRoundedButton("فعال‌سازی", "#4CAF50") {
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
        @JvmStatic fun newInstance(operatorName: String) = MonthlyFragment().apply {
            arguments = Bundle().apply { putString("OPERATOR_NAME", operatorName) }
        }
    }
}
