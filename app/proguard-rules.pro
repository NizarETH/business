# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Euphor\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keep class com.stripe.** { *; }
-dontwarn com.squareup.okhttp.**
-dontwarn CompatHoneycomb -keep class android.support.v4. { *; }
-dontwarn **CompatCreatorHoneycombMR2
-dontwarn **ActivityCompatHoneycomb
-dontwarn **MenuCompatHoneycomb
-dontwarn android.support.v4.**
# Guava exclusions (http://code.google.com/p/guava-libraries/wiki/UsingProGuardWithGuava)
-dontwarn sun.misc.Unsafe
-dontwarn com.google.common.collect.MinMaxPriorityQueue
-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}

# Guava depends on the annotation and inject packages for its annotations, keep them both
-keep public class javax.annotation.**
-keep public class javax.inject.**
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepclassmembers

-dontskipnonpubliclibraryclasses
-dontobfuscate
-forceprocessing
-optimizationpasses 5

-keep class * extends android.app.Activity
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
