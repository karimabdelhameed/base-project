package com.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*


object Utils {

    fun isConnectingToInternet(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        if (activeNetwork != null) {
            val nc: NetworkCapabilities? = connectivityManager.getNetworkCapabilities(activeNetwork)
            return (nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
        }
        return false
    }


    fun getHashKey(context: Activity) {

        val info: PackageInfo
        try {
            info = context.packageManager.getPackageInfo(
                "com.NatSolutions.TheLemonTree",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val something = String(Base64.encode(md.digest(), 0))
                Log.e("hash key", something)
            }
        } catch (e1: PackageManager.NameNotFoundException) {
            Log.e("name not found", e1.toString())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("no such an algorithm", e.toString())
        } catch (e: Exception) {
            Log.e("exception", e.toString())
        }

    }

    private fun isArabic(context: Context): Boolean {
        val locale: Locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.resources.configuration.locales.get(0)
        } else {
            locale = context.resources.configuration.locale
        }
        return locale.toString().contains("ar")
    }

    fun getLocaleLanguage(): String {
        var lang = ""
        runBlocking {
            val job = CoroutineScope(IO).launch {
                lang = preferencesGateway.load(Constants.LANGUAGE, "ar")
            }
            job.join()
        }
        return lang
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun share(context: Context, whatToShare: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, whatToShare)
        shareIntent.type = "text/plain"
        context.startActivity(Intent.createChooser(shareIntent, "Share with"))
    }


    fun navigateToLocation(context: Context, lat: String, lng: String) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?q=loc:$lat,$lng")
        )
        context.startActivity(intent)
    }

    fun logout(context: Context) {
        //        User.saveUser(null);
        //        Intent intent = new Intent(context, SplashActivity.class);
        //        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //        context.startActivity(intent);
    }


    fun openLink(context: Activity, url: String) {
        var mUrl = url
        if (!mUrl.startsWith("http://") && !mUrl.startsWith("https://")) {
            mUrl = "http://$mUrl"
        }
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mUrl))
        context.startActivity(browserIntent)
    }

    fun callNumber(context: Activity, number: String) {
        val callIntent = Intent(Intent.ACTION_VIEW)
        callIntent.data = Uri.parse("tel:$number")
        context.startActivity(callIntent)
    }

    fun formatIntToCommaSperatedString(number: Int): String {
        val symbols = DecimalFormatSymbols(Locale.ENGLISH)
        val formatter = DecimalFormat("#,###,###", symbols)
        return formatter.format(number.toLong())
    }


    fun dpToPx(dp: Int, context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }


    fun containsLowercaseLetter(text: String): Boolean {
        for (i in 0 until text.length) {
            val character = text[i]
            if (Character.isLowerCase(character))
                return true
        }
        return false
    }

    fun containsUppercaseLetter(text: String): Boolean {
        for (i in 0 until text.length) {
            val character = text[i]
            if (Character.isUpperCase(character))
                return true
        }
        return false
    }

    fun containsDigit(text: String): Boolean {
        for (i in 0 until text.length) {
            val character = text[i]
            if (Character.isDigit(character))
                return true
        }
        return false
    }

    @SuppressLint("HardwareIds")
    fun getDeviceID(): String {
        return Settings.Secure.getString(
            Domain.application.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    fun getMinSdkVersion(): Int {
        try {
            return Build.VERSION.SDK_INT

        } catch (e: Exception) {
        }

        return 0
    }

    fun openStore(context: Context) {
        val appPackageName = context.packageName // getPackageName() from Context or Activity object
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (anfe: android.content.ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }

}