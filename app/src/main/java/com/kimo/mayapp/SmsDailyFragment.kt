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

class SmsDailyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sms_daily, container, false)

        val btnActivate = view.findViewById<Button>(R.id.btnActivate)
        val btnDeactivate = view.findViewById<Button>(R.id.btnDeactivate)

        // دکمه‌های کپسولی
        btnActivate.background = capsule("#4CAF50")
        btnDeactivate.background = capsule("#E91E63")
        btnActivate.setTextColor(Color.WHITE)
        btnDeactivate.setTextColor(Color.WHITE)

        // فعال‌سازی: پیام‌ها با شماره 3378 و متن BDS
        btnActivate.setOnClickListener { openSmsApp("3378", "BDS") }

        // غیرفعال‌سازی: پیام‌ها با شماره 3378 و متن PU BDS
        btnDeactivate.setOnClickListener { openSmsApp("3378", "PU BDS") }

        return view
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