package com.kimo.mayapp

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MonthlyPackagesActivity : AppCompatActivity() {

    private var currentOperator: String = "اتصالات"

    override fun attachBaseContext(newBase: Context?) {
        val preferences = newBase?.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val lang = preferences?.getString(SELECTED_LANGUAGE, "fa") ?: "fa"
        super.attachBaseContext(updateBaseContextLocale(newBase!!, lang))
    }

    private fun updateBaseContextLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentOperator = intent.getStringExtra("OPERATOR_NAME") ?: "اتصالات"

        if (currentOperator != "اتصالات" && currentOperator != "Etisalat") {
            Toast.makeText(this, "بخش بسته‌های اینترنتی این اپراتور به زودی فعال خواهد شد", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setContentView(R.layout.activity_monthly_packages)

        val txtHeaderTitle = findViewById<TextView>(R.id.txtHeaderTitle)
        txtHeaderTitle?.text = "بسته‌های اینترنتی $currentOperator"

        setupTabs()
        loadOperatorPackages()
    }

    private fun loadOperatorPackages() {
        // بسته 1: فعال‌سازی M95 و غیرفعال‌سازی PU M95 به 3378
        setupPackageAction(
            R.id.btnActivate1, R.id.btnCheck1, R.id.btnDeactivate1,
            "3378", "M95",          // فعال‌سازی
            "606",                  // بررسی موجودی
            "3378", "PU M95"        // غیرفعال‌سازی
        )

        // بسته 2: فعال‌سازی 110 و غیرفعال‌سازی PU 110 به 3378
        setupPackageAction(
            R.id.btnActivate2, R.id.btnCheck2, R.id.btnDeactivate2,
            "3378", "110",          // فعال‌سازی
            "606",                  // بررسی موجودی
            "3378", "PU 110"        // غیرفعال‌سازی
        )
    }

    private fun setupPackageAction(
        btnActivateId: Int,
        btnCheckId: Int,
        btnDeactivateId: Int,
        activatePrefix: String,
        activateKeyword: String,
        checkCode: String,
        deactivatePrefix: String,
        deactivateKeyword: String
    ) {
        val btnActivate = findViewById<Button>(btnActivateId)
        val btnCheck = findViewById<Button>(btnCheckId)
        val btnDeactivate = findViewById<Button>(btnDeactivateId)

        // دکمه فعال‌‌سازی
        btnActivate?.setOnClickListener {
            executeUssdCode("$activatePrefix*$activateKeyword")
        }

        // دکمه چک بیلانس
        btnCheck?.setOnClickListener {
            executeUssdCode(checkCode)
        }

        // دکمه غیرفعال‌سازی (کاملاً تفکیک‌شده و مجزا)
        btnDeactivate?.setOnClickListener {
            executeUssdCode("$deactivatePrefix*$deactivateKeyword")
        }
    }

    private fun executeUssdCode(code: String) {
        try {
            val encodedCode = Uri.encode(code)
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$encodedCode"))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "خطا در اجرای کد دستوری", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupTabs() {
        val tabHourly = findViewById<TextView>(R.id.tabHourly)
        val tabDaily = findViewById<TextView>(R.id.tabDaily)
        val tabWeekly = findViewById<TextView>(R.id.tabWeekly)
        val tabMonthly = findViewById<TextView>(R.id.tabMonthly)

        tabMonthly?.setTextColor(android.graphics.Color.parseColor("#FF9800"))
        tabMonthly?.setTypeface(null, android.graphics.Typeface.BOLD)

        tabHourly?.setOnClickListener {
            val intent = Intent().setClassName(this, "com.kimo.mayapp.HourlyPackagesActivity").apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra("OPERATOR_NAME", currentOperator)
            }
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        tabDaily?.setOnClickListener {
            val intent = Intent().setClassName(this, "com.kimo.mayapp.DailyPackagesActivity").apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra("OPERATOR_NAME", currentOperator)
            }
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        tabWeekly?.setOnClickListener {
            val intent = Intent().setClassName(this, "com.kimo.mayapp.WeeklyPackagesActivity").apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra("OPERATOR_NAME", currentOperator)
            }
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }

    companion object {
        private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    }
}
