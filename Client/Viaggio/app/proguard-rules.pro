# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepclasseswithmembers interface * {
    @retrofit2.* <methods>;
}
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keepattributes InnerClasses


-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeInvisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes RuntimeInvisibleParameterAnnotations

# Retain generic type information for use by reflection by converters and adapters.
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#error container
-dontwarn com.google.**
-dontwarn org.apache.**
-dontwarn com.fasterxml.**
-dontwarn org.slf4j.**

-keep class com.google.gson.** { *; }
-keep public class com.google.gson.** {public private protected *;}
-keep class com.google.inject.** { *; }
-keep class org.apache.http.** { *; }
-keep class org.apache.james.mime4j.** { *; }
-keep class javax.inject.** { *; }
-keep class javax.xml.stream.** { *; }
-keep class com.google.appengine.** { *; }
-keep class retrofit.** { *; }

-dontwarn afu.org.checkerframework.**
-dontwarn androidx.work.**

#root cause
-dontwarn java.beans.**
-dontwarn **.annotations.**
-dontwarn **.annotation.**
-dontwarn com.amazonaws.mobile.auth.**

-keep class com.bumptech.glide.integration.okhttp3.OkHttpGlideModule { *; }

-keep class com.bumptech.glide.integration.volley.VolleyGlideModule { *; }

-keep class com.google.common.** { *; }
-keep class com.google.auto.** { *; }

-keep class okio.** { *; }

-keep class android.net.http.* { *; }
-keep class org.apache.commons.codec.** { *; }
-keep class org.apache.http.** { *; }

-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

-keep public class com.google.** {*;}

-keep class net.ellerton.japng.** { *; }

-dontwarn com.crashlytics.android.answers.shim.**

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-verbose
-dontpreverify
-repackageclasses ''
-allowaccessmodification
-optimizations !code/simplification/arithmetic
# removed -keep public class * extends android.app.Activity for possible logic leak
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-keep public class * extends android.view.View {
      public <init>(android.content.Context);
      public <init>(android.content.Context, android.util.AttributeSet);
      public <init>(android.content.Context, android.util.AttributeSet, int);
      public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.content.Context {
    public void *(android.view.View);
    public void *(android.view.MenuItem);
}

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keepnames class * implements java.io.Serializable

-keepclassmembers enum * {
    *;
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

#Viaggio Customization
-keep enum com.kotlin.viaggio.**$** {*;}
-keep class com.kotlin.viaggio.data.obj.**$** {*;}
-keep class com.kotlin.viaggio.data.obj.Theme {*;}
-keep class com.kotlin.viaggio.data.obj.Country {*;}
-keep class com.kotlin.viaggio.data.obj.Travel {*;}
-keep class com.kotlin.viaggio.data.obj.TravelCard {*;}

-keep class com.kotlin.viaggio.data.obj.ViaggioApiTravelsData {*;}
-keep class com.kotlin.viaggio.data.obj.ViaggioApiTravelCardsData {*;}
-keep class com.kotlin.viaggio.data.obj.ViaggioApiAuth {*;}
-keep class com.kotlin.viaggio.data.obj.ViaggioApiAWSAuth {*;}
-keep class com.kotlin.viaggio.data.obj.ViaggioApiTravelResult {*;}
-keep class com.kotlin.viaggio.data.obj.ViaggioApiTravelsResult {*;}
-keep class com.kotlin.viaggio.data.obj.ViaggioApiTravelCardsResult {*;}
-keep class com.kotlin.viaggio.data.obj.ViaggioApiTravels {*;}
-keep class com.kotlin.viaggio.data.obj.ViaggioApiTravelCards {*;}
-keep class com.kotlin.viaggio.data.obj.ViaggioApiSync {*;}
-keep class com.kotlin.viaggio.data.obj.TravelBody {*;}
-keep class com.kotlin.viaggio.data.obj.TravelCardBody {*;}
-keep class com.kotlin.viaggio.data.obj.TravelBodyList {*;}
-keep class com.kotlin.viaggio.data.obj.TravelCardBodyList {*;}
-keep class com.kotlin.viaggio.data.obj.ViaggioApiCountry {*;}
-keep class com.kotlin.viaggio.data.obj.ViaggioApiTheme {*;}
-keep class com.kotlin.viaggio.data.obj.ViaggioApiDomestics {*;}
-keep class com.kotlin.viaggio.data.obj.GoogleSignInBody {*;}

-keep class com.kotlin.viaggio.data.obj.Error {*;}