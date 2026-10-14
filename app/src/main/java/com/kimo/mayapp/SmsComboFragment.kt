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

class SmsComboFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sms_combo, container, false)

        // بسته اول: M200 / PU M200
        setupCard(
            activate = view.findViewById(R.id.btnActivate1),
            check = view.findViewById(R.id.btnCheck1),
            deactivate = view.findViewById(R.id.btnDeactivate1),
            onText = "M200",
            offText = "PU M200"
        )

        // بسته دوم: 700 / PU 700
        setupCard(
            activate = view.findViewById(R.id.btnActivate2),
            check = view.findViewById(R.id.btnCheck2),
            deactivate = view.findViewById(R.id.btnDeactivate2),
            onText = "700",
            offText = "PU 700"
        )

        return view
    }

    private fun setupCard(
        activate: Button,
        check: Button,
        deactivate: Button,
        onText: String,
        offText: String
    ) {
        activate.background = capsule("#16A34A")
        check.background = capsule("#0891B2")
        deactivate.background = capsule("#E11D48")

        activate.setOnClickListener { openSmsApp("3378", onText) }
        deactivate.setOnClickListener { openSmsApp("3378", offText) }
        check.setOnClickListener { openSmsApp("606", "505") }
    }

    private fun capsule(colorHex: String): GradientDrawable {
        return GradientDrawable().apply {
            setColor(Color.parseColor(colorHex))
            cornerRadius = 200f
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
            Toast.makeText(requireContext(), "برنامه پیام‌ها پیدا نشد", Toast.LENGTH_SHORT).show()
        }
    }
}