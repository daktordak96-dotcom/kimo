package com.kimo.mayapp

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class NightFragment : Fragment() {

    private var currentOperator: String = "اتصالات"

    // ====== اطلاعات روشن: اینجا را با مقادیر واقعی پر کنید ======
    private val roshanNumber = "0000"          // شمارهٔ ارسال پیامک روشن
    private val roshanActivate1 = "CODE1"      // کد فعال‌سازی بستهٔ ۱
    private val roshanDeactivate1 = "CODE1X"   // کد لغو بستهٔ ۱
    private val roshanActivate2 = "CODE2"      // کد فعال‌سازی بستهٔ ۲
    private val roshanDeactivate2 = "CODE2X"   // کد لغو بستهٔ ۲
    private val roshanCheckNumber = "0000"     // شمارهٔ استعلام
    private val roshanCheckText = "CHECK"      // متن استعلام

    private val roshanTitle1 = Tr("بستهٔ شبانهٔ ۱", "Night Package 1", "شپنی بسته ۱")
    private val roshanPrice1 = Tr("قیمت: ؟ افغانی", "Price: ? AFN", "بیه: ؟ افغانی")
    private val roshanDesc1 = Tr("توضیحات بسته", "Package details", "د بسته توضیحات")

    private val roshanTitle2 = Tr("بستهٔ شبانهٔ ۲", "Night Package 2", "شپنی بسته ۲")
    private val roshanPrice2 = Tr("قیمت: ؟ افغانی", "Price: ? AFN", "بیه: ؟ افغانی")
    private val roshanDesc2 = Tr("توضیحات بسته", "Package details", "د بسته توضیحات")
    // ============================================================

    private fun isRoshan(): Boolean =
        currentOperator == "روشن" ||
        currentOperator.equals("roshan", ignoreCase = true) ||
        currentOperator == "روښان"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentOperator = it.getString(ARG_OPERATOR_NAME, "اتصالات") ?: "اتصالات"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_night, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyTexts(view)

        if (isRoshan()) {
            setupPackageAction(view, R.id.btnActivate1, R.id.btnCheck1, R.id.btnDeactivate1,
                roshanNumber, roshanActivate1, roshanCheckNumber, roshanCheckText,
                roshanNumber, roshanDeactivate1, "#4CAF50", "#009688", "#E91E63")
            setupPackageAction(view, R.id.btnActivate2, R.id.btnCheck2, R.id.btnDeactivate2,
                roshanNumber, roshanActivate2, roshanCheckNumber, roshanCheckText,
                roshanNumber, roshanDeactivate2, "#4CAF50", "#009688", "#E91E63")
        } else {
            // بسته اول: ۱ گیگابایتی شبانه روزانه
            setupPackageAction(view, R.id.btnActivate1, R.id.btnCheck1, R.id.btnDeactivate1,
                "3378", "NBD", "606", "505", "3378", "PU NBD", "#4CAF50", "#009688", "#E91E63")
            // بسته دوم: ۳ گیگابایتی شبانه ماهانه
            setupPackageAction(view, R.id.btnActivate2, R.id.btnCheck2, R.id.btnDeactivate2,
                "3378", "NBS", "606", "505", "3378", "PU NBS", "#4CAF50", "#009688", "#E91E63")
        }
    }

    private fun applyTexts(view: View) {
        val ctx = requireContext()

        if (isRoshan()) {
            view.findViewById<TextView>(R.id.txtTitle1)?.text = roshanTitle1.get(ctx)
            view.findViewById<TextView>(R.id.txtPrice1)?.text = roshanPrice1.get(ctx)
            view.findViewById<TextView>(R.id.txtDesc1)?.text = roshanDesc1.get(ctx)

            view.findViewById<TextView>(R.id.txtTitle2)?.text = roshanTitle2.get(ctx)
            view.findViewById<TextView>(R.id.txtPrice2)?.text = roshanPrice2.get(ctx)
            view.findViewById<TextView>(R.id.txtDesc2)?.text = roshanDesc2.get(ctx)
        } else {
            view.findViewById<TextView>(R.id.txtTitle1)?.text = Strings.get(ctx, "night_1_title")
            view.findViewById<TextView>(R.id.txtPrice1)?.text = Strings.get(ctx, "night_1_price")
            view.findViewById<TextView>(R.id.txtDesc1)?.text = Strings.get(ctx, "night_1_desc")

            view.findViewById<TextView>(R.id.txtTitle2)?.text = Strings.get(ctx, "night_2_title")
            view.findViewById<TextView>(R.id.txtPrice2)?.text = Strings.get(ctx, "night_2_price")
            view.findViewById<TextView>(R.id.txtDesc2)?.text = Strings.get(ctx, "night_2_desc")
        }

        listOf(
            R.id.btnActivate1 to "btn_activate", R.id.btnActivate2 to "btn_activate",
            R.id.btnDeactivate1 to "btn_deactivate", R.id.btnDeactivate2 to "btn_deactivate",
            R.id.btnCheck1 to "btn_check", R.id.btnCheck2 to "btn_check"
        ).forEach { (id, key) ->
            view.findViewById<Button>(id)?.text = Strings.get(ctx, key)
        }
    }

    private fun setupPackageAction(
        view: View,
        activateBtnId: Int, checkBtnId: Int, deactivateBtnId: Int,
        activateNumber: String, activateText: String,
        checkNumber: String, checkText: String,
        deactivateNumber: String, deactivateText: String,
        actColor: String, chkColor: String, deactColor: String
    ) {
        fun styleButton(buttonId: Int, bgColor: String, onClickAction: () -> Unit) {
            view.findViewById<Button>(buttonId)?.let { btn ->
                btn.isAllCaps = false
                val drawable = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    setColor(Color.parseColor(bgColor))
                    cornerRadius = 50f
                }
                btn.background = drawable
                btn.setOnClickListener { onClickAction() }
            }
        }

        styleButton(activateBtnId, actColor) { sendSms(activateNumber, activateText) }
        styleButton(checkBtnId, chkColor) { sendSms(checkNumber, checkText) }
        styleButton(deactivateBtnId, deactColor) { sendSms(deactivateNumber, deactivateText) }
    }

    private fun sendSms(phoneNo: String, message: String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$phoneNo")
                putExtra("sms_body", message)
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), Strings.get(requireContext(), "sms_error"), Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val ARG_OPERATOR_NAME = "OPERATOR_NAME"

        @JvmStatic
        fun newInstance(operatorName: String) =
            NightFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_OPERATOR_NAME, operatorName)
                }
            }
    }
}