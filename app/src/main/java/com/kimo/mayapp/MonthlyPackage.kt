package com.kimo.mayapp

// مدل داده‌ای بسته‌های اینترنت ماهانه
data class MonthlyPackage(
    val title: String,
    val priceAfghani: Int,
    val durationDays: Int,
    val activationCode: String,      // کدی که باید به 3378 ارسال شود
    val deactivationCode: String,    // کد غیرفعال‌سازی
    val checkBalanceCode: String,    // کدی که باید به 606 ارسال شود
    val description: String
)

object PackageDataSource {
    // لیست کامل و اصلاح‌شده بسته‌های ماهانه بر اساس تصاویر ارسال شده
    val monthlyPackagesList = listOf(
        MonthlyPackage(
            title = "13.5 جی بی ماهانه",
            priceAfghani = 799,
            durationDays = 30,
            activationCode = "PU 7GB",
            deactivationCode = "PU 7GB",
            checkBalanceCode = "505",
            description = "بسته 13.5 جی بی اینترنت برای 30 روز به قیمت 799 افغانی. تمدید خودکار فعال."
        ),
        MonthlyPackage(
            title = "15 جی بی ماهانه (5 جی بی شبانه)",
            priceAfghani = 699,
            durationDays = 30,
            activationCode = "PU M15",
            deactivationCode = "PU M15",
            checkBalanceCode = "505",
            description = "بسته 15 جی بی اینترنت برای 30 روز به قیمت 699 افغانی. 5 جی بی قابل استفاده از 11 شب تا 7 صبح. تمدید خودکار فعال."
        ),
        MonthlyPackage(
            title = "8 جی بی ماهانه",
            priceAfghani = 500,
            durationDays = 30,
            activationCode = "PU 8GB",
            deactivationCode = "PU 8GB",
            checkBalanceCode = "505",
            description = "بسته 8 جی بی اینترنت برای 30 روز به قیمت 500 افغانی. تمدید خودکار فعال."
        ),
        MonthlyPackage(
            title = "12 جی بی ماهانه",
            priceAfghani = 599,
            durationDays = 30,
            activationCode = "PU 599",
            deactivationCode = "PU 599",
            checkBalanceCode = "505",
            description = "بسته 12 جی بی اینترنت برای 30 روز به قیمت 599 افغانی. تمدید خودکار فعال."
        ),
        MonthlyPackage(
            title = "25 جی بی ماهانه",
            priceAfghani = 999,
            durationDays = 30,
            activationCode = "PU 10GB",
            deactivationCode = "PU 10GB",
            checkBalanceCode = "505",
            description = "بسته 25 جی بی اینترنت برای 30 روز به قیمت 999 افغانی. تمدید خودکار فعال."
        ),
        MonthlyPackage(
            title = "30 جی بی ماهانه (استاندارد)",
            priceAfghani = 1199,
            durationDays = 30,
            activationCode = "PU 1199",
            deactivationCode = "PU 1199",
            checkBalanceCode = "505",
            description = "بسته 30 جی بی اینترنت برای 30 روز به قیمت 1199 افغانی. تمدید خودکار فعال."
        ),
        MonthlyPackage(
            title = "30 جی بی ماهانه (پیشرفته)",
            priceAfghani = 1399,
            durationDays = 30,
            activationCode = "PU 1399",
            deactivationCode = "PU 1399",
            checkBalanceCode = "505",
            description = "بسته 30 جی بی اینترنت برای 30 روز به قیمت 1399 افغانی. تمدید خودکار فعال."
        ),
        MonthlyPackage(
            title = "40 جی بی ماهانه",
            priceAfghani = 1699,
            durationDays = 30,
            activationCode = "PU 1699",
            deactivationCode = "PU 1699",
            checkBalanceCode = "505",
            description = "بسته 40 جی بی اینترنت برای 30 روز به قیمت 1699 افغانی. تمدید خودکار فعال."
        ),
        MonthlyPackage(
            title = "50 جی بی ماهانه",
            priceAfghani = 2099,
            durationDays = 30,
            activationCode = "PU 2099",
            deactivationCode = "PU 2099",
            checkBalanceCode = "505",
            description = "بسته 50 جی بی اینترنت برای 30 روز به قیمت 2099 افغانی. تمدید خودکار فعال."
        )
    )
}
