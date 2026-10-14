package com.kimo.mayapp

data class CallPackage(
    val title: String,
    val price: String,
    val intro: String,
    val activateText: String,
    val deactivateText: String,
    val autoRenew: Boolean = true
) {
    fun description(): String {
        val renew = if (autoRenew) "\nتمدید خودکار موجود است." else ""
        return intro + renew +
            "\nبرای فعال سازی $activateText و برای غیرفعال سازی $deactivateText را به 3378 ارسال نمائید." +
            "\nبرای بررسی موجودی 505 را به 606 ارسال نمائید یا به 888 زنگ بزنید."
    }
}

object CallPackagesData {

    // ترتیب تب‌ها: 0 روزانه، 1 هفته ای، 2 ماهانه، 3 ترکیبی، 4 شبانه، 5 متفرقه
    fun forTab(index: Int): List<CallPackage> = when (index) {
        0 -> listOf(
            CallPackage(
                title = "100 دقیقه روزانه",
                price = "25 افغانی",
                intro = "بسته 100 دقیقه تماس اتصالات برای یک روز به قیمت 25 افغانی.",
                activateText = "900",
                deactivateText = "PU 900"
            ),
            CallPackage(
                title = "50 دقیقه روزانه",
                price = "15 افغانی",
                intro = "بسته 50 دقیقه تماس اتصالات برای یک روز به قیمت 15 افغانی.",
                activateText = "801",
                deactivateText = "PU 801"
            )
        )
        1 -> listOf(
            CallPackage(
                title = "450 دقیقه هفتگی",
                price = "80 افغانی",
                intro = "بسته 450 دقیقه تماس اتصالات برای 7 روز به قیمت 80 افغانی.",
                activateText = "W75",
                deactivateText = "PU W75"
            ),
            CallPackage(
                title = "300 دقیقه هفتگی",
                price = "55 افغانی",
                intro = "بسته 300 دقیقه تماس اتصالات برای 7 روز به قیمت 55 افغانی.",
                activateText = "E55",
                deactivateText = "PU E55"
            )
        )
        2 -> listOf(
            CallPackage(
                title = "تماس نامحدود اتصالات",
                price = "300 افغانی",
                intro = "بسته تماس نامحدود اتصالات + 500 ام بی انترنت برای 30 روز به قیمت 300 افغانی.",
                activateText = "U300",
                deactivateText = "PU U300"
            ),
            CallPackage(
                title = "5000 دقیقه تقسیم شده",
                price = "599 افغانی",
                intro = "بسته 5000 دقیقه تماس اتصالات (4000 دقیقه از ساعت 10 شب تا 6 صبح + 1000 دقیقه از 6 صبح تا 10 شب) برای 30 روز به قیمت 599 افغانی.",
                activateText = "5000",
                deactivateText = "PU 5000"
            ),
            CallPackage(
                title = "1750 دقیقه ماهانه",
                price = "220 افغانی",
                intro = "بسته 1750 دقیقه تماس اتصالات برای 30 روز به قیمت 220 افغانی.",
                activateText = "M220",
                deactivateText = "PU M220"
            ),
            CallPackage(
                title = "1000 دقیقه استاندارد",
                price = "180 افغانی",
                intro = "بسته 1000 دقیقه تماس اتصالات برای 30 روز به قیمت 180 افغانی.",
                activateText = "M180",
                deactivateText = "PU M180"
            ),
            CallPackage(
                title = "650 دقیقه ماهانه",
                price = "135 افغانی",
                intro = "بسته 650 دقیقه تماس اتصالات برای 30 روز به قیمت 135 افغانی.",
                activateText = "M135",
                deactivateText = "PU M135"
            ),
            CallPackage(
                title = "نرخ 1 افغانی در دقیقه",
                price = "30 افغانی",
                intro = "بسته نرخ 1 افغانی در دقیقه برای تماس با اتصالات (صورتحساب 30 ثانیه‌ای) برای 30 روز به قیمت 30 افغانی.",
                activateText = "R30",
                deactivateText = "PU R30"
            )
        )
        3 -> listOf(
            CallPackage(
                title = "1000 دقیقه پریمیوم",
                price = "200 افغانی",
                intro = "بسته 1000 دقیقه تماس اتصالات + 200 ام بی انترنت برای 30 روز به قیمت 200 افغانی.",
                activateText = "M200",
                deactivateText = "PU M200"
            ),
            CallPackage(
                title = "125 دقیقه + 50 پیامک",
                price = "40 افغانی",
                intro = "بسته 125 دقیقه تماس اتصالات + 50 پیامک برای یک روز به قیمت 40 افغانی.",
                activateText = "700",
                deactivateText = "PU 700"
            )
        )
        4 -> listOf(
            CallPackage(
                title = "250 دقیقه شبانه",
                price = "10 افغانی",
                intro = "بسته 250 دقیقه تماس اتصالات (از ساعت 10 شب تا 5:59 صبح) برای یک روز به قیمت 10 افغانی.",
                activateText = "N10",
                deactivateText = "PU N10"
            ),
            CallPackage(
                title = "4000 دقیقه شبانه",
                price = "199 افغانی",
                intro = "بسته 4000 دقیقه تماس اتصالات (از ساعت 10 شب تا 5:59 صبح) برای 30 روز به قیمت 199 افغانی.",
                activateText = "199",
                deactivateText = "PU 199"
            ),
            CallPackage(
                title = "750 دقیقه شبانه",
                price = "35 افغانی",
                intro = "بسته 750 دقیقه از ساعت 10 شب تا 6 صبح برای 7 روز به قیمت 35 افغانی.",
                activateText = "N35",
                deactivateText = "PU N35"
            )
        )
        5 -> listOf(
            CallPackage(
                title = "60 دقیقه ساعتی",
                price = "10 افغانی",
                intro = "بسته 60 دقیقه تماس اتصالات ساعتی برای یک روز به قیمت 10 افغانی.",
                activateText = "H10",
                deactivateText = "PU H10"
            ),
            CallPackage(
                title = "400 دقیقه دو هفته‌ای",
                price = "80 افغانی",
                intro = "بسته 400 دقیقه تماس اتصالات برای 14 روز به قیمت 80 افغانی.",
                activateText = "80",
                deactivateText = "PU 80"
            ),
            CallPackage(
                title = "جمعه پریمیوم",
                price = "25 افغانی",
                intro = "بسته 100 دقیقه تماس اتصالات در روزهای جمعه + تماس رایگان از 12 شب تا 6 صبح برای روزهای جمعه به قیمت 25 افغانی.",
                activateText = "F20",
                deactivateText = "PU F20",
                autoRenew = false
            ),
            CallPackage(
                title = "جمعه استاندارد",
                price = "15 افغانی",
                intro = "بسته 90 دقیقه تماس اتصالات در روزهای جمعه برای روزهای جمعه به قیمت 15 افغانی.",
                activateText = "F15",
                deactivateText = "PU F15",
                autoRenew = false
            )
        )
        else -> emptyList()
    }
}