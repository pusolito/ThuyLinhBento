package com.thuylinh.bento

import android.app.Dialog
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.google.android.gms.ads.MobileAds
import com.thuylinh.bento.network.RestApis
import com.thuylinh.bento.utils.Constants
import com.thuylinh.bento.utils.SharedPrefUtils
import com.thuylinh.bento.utils.extensions.getSharedPrefInstance
import com.onesignal.OneSignal
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import okhttp3.OkHttpClient


class WooBoxApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        getSharedPrefInstance().apply {
            appTheme = getIntValue(Constants.SharedPref.THEME, Constants.THEME.LIGHT)
        }

        // Set Custom Font

        ViewPump.init(ViewPump.builder().addInterceptor(CalligraphyInterceptor(CalligraphyConfig.Builder().setDefaultFontPath(getString(R.string.font_regular)).setFontAttrId(R.attr.fontPath).build())).build())
        MobileAds.initialize(this) {}

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init()
        OneSignal.clearOneSignalNotifications()
    }


    fun enableNotification(isEnabled: Boolean) {
        OneSignal.setSubscription(isEnabled)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        private lateinit var appInstance: WooBoxApp
        var restApis: RestApis? = null
        var okHttpClient: OkHttpClient? = null
        var sharedPrefUtils: SharedPrefUtils? = null
        var noInternetDialog: Dialog? = null
        var appTheme: Int = 0

        fun getAppInstance(): WooBoxApp {
            return appInstance
        }

        fun changeAppTheme(isDark: Boolean) {
            getSharedPrefInstance().apply {
                when {
                    isDark -> setValue(Constants.SharedPref.THEME, Constants.THEME.DARK)
                    else -> setValue(Constants.SharedPref.THEME, Constants.THEME.LIGHT)
                }
                appTheme = getIntValue(Constants.SharedPref.THEME, Constants.THEME.LIGHT)
            }

        }

    }
}
