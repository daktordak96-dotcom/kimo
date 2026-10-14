# قوانین R8 برای نسخهٔ release

# نگه داشتن اطلاعات لازم برای گزارش خطا
-keepattributes Signature, *Annotation*, SourceFile, LineNumberTable
-renamesourcefileattribute SourceFile

# OkHttp 3 / Okio
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-dontwarn okhttp3.internal.platform.**
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn javax.annotation.**
