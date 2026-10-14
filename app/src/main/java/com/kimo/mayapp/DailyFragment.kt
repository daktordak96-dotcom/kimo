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
import android.widget.Toast
import androidx.fragment.app.Fragment

class DailyFragment : Fragment() {

    private var currentOperator: String = "اتصالات"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentOperator = it.getString(ARG_OPERATOR_NAME, "اتصالات")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_daily_packages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // تنظیم دکمه‌های بسته‌های روزانه همراه با تبدیل به شکل تخم‌مرغی و کپسولی
        setupPackageAction(view, R.id.btnActivate1, R.id.btnCheck1, R.id.btnDeactivate1, "3378", "D1", "606", "605", "3378", "PD1", "#4CAF50", "#009688", "#E91E63")
        setupPackageAction(view, R.id.btnActivate2, R.id.btnCheck2, R.id.btnDeactivate2, "3378", "D2", "606", "605", "3378", "PD2", "#4CAF50", "#009688", "#E91E63")
        setupPackageAction(view, R.id.btnActivate3, R.id.btnCheck3, R.id.btnDeactivate3, "3378", "D3", "606", "605", "3378", "PD3", "#4CAF50", "#009688", "#E91E63")
        setupPackageAction(view, R.id.btnActivate4, R.id.btnCheck4, R.id.btnDeactivate4, "3378", "D4", "606", "605", "3378", "PD4", "#4CAF50", "#009688", "#E91E63")
        setupPackageAction(view, R.id.btnActivate5, R.id.btnCheck5, R.id.btnDeactivate5, "3378", "D5", "606", "605", "3378", "PD5", "#4CAF50", "#009688", "#E91E63")
    }

    private fun setupPackageAction(
        view: View, 
        actBtn: Int, 
        chkBtn: Int, 
        deactBtn: Int, 
        actNo: String, 
        actTxt: String, 
        chkNo: String, 
        chkTxt: String, 
        deactNo: String, 
        deactTxt: String,
        actColor: String,
        chkColor: String,
        deactColor: String
    ) {
        // تابع کمکی برای استایل دادن و گرد کردن دکمه‌ها
        fun styleButton(buttonId: Int, bgColor: String, onClickAction: () -> Unit) {
            view.findViewById<Button>(buttonId)?.let { btn ->
                btn.isAllCaps = false
                val drawable = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    setColor(Color.parseColor(bgColor))
                    cornerRadius = 50f // گوشه‌های کاملاً گرد و تخم‌مرغی
                }
                btn.background = drawable
                btn.setOnClickListener { onClickAction() }
            }
        }

        styleButton(actBtn, actColor) { sendSms(actNo, actTxt) }
        styleButton(chkBtn, chkColor) { sendSms(chkNo, chkTxt) }
        styleButton(deactBtn, deactColor) { sendSms(deactNo, deactTxt) }
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
        @JvmStatic fun newInstance(operatorName: String) = DailyFragment().apply {
            arguments = Bundle().apply { putString(ARG_OPERATOR_NAME, operatorName) }
        }
    }
}
