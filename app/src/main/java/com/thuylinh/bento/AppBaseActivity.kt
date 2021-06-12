package com.thuylinh.bento

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.ads.*
import com.thuylinh.bento.WooBoxApp.Companion.noInternetDialog
import com.thuylinh.bento.activity.DashBoardActivity
import com.thuylinh.bento.utils.Constants.THEME.DARK
import com.thuylinh.bento.utils.SLocaleHelper
import com.thuylinh.bento.utils.extensions.changeToolbarFont
import com.thuylinh.bento.utils.extensions.launchActivityWithNewTask
import com.thuylinh.bento.utils.extensions.switchToDarkMode
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.layout_abmob.*
import java.util.*

open class AppBaseActivity : AppCompatActivity() {
    private var progressDialog: Dialog? = null
    var language: Locale? = null
    private var themeApp: Int = 0
    var isAdShown = false

    fun setToolbarWithoutBackButton(mToolbar: Toolbar) {
        setSupportActionBar(mToolbar)
    }

    fun setToolbar(mToolbar: Toolbar) {
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace)
        mToolbar.changeToolbarFont()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        switchToDarkMode(WooBoxApp.appTheme == DARK)
        super.onCreate(savedInstanceState)
        noInternetDialog = null
        if (progressDialog == null) {
            progressDialog = Dialog(this)
            progressDialog?.window?.setBackgroundDrawable(ColorDrawable(0))
            progressDialog?.setContentView(R.layout.custom_dialog)
        }
        themeApp = WooBoxApp.appTheme
    }

    fun showBannerAds() {
        MobileAds.initialize(this, getString(R.string.ad_mob_app_id))
        val adMobBanner = AdView(this)
        adMobBanner.adSize = AdSize.BANNER
        adMobBanner.adUnitId = getString(R.string.ad_mob_banner_id)
        val adRequest: AdRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build()
        adMobBanner.loadAd(adRequest)
        adMobBanner.adListener = object : AdListener() {
            override fun onAdLoaded() {
                runOnUiThread {
                    adView.visibility = View.VISIBLE
                }
            }
        }
        adView.setHorizontalGravity(Gravity.CENTER_HORIZONTAL)
        adView.addView(adMobBanner)
    }

    fun showInterstitialAd() {
        isAdShown = true
        MobileAds.initialize(this, getString(R.string.ad_mob_app_id))
        val requestBuilder = AdRequest.Builder()
        val interstitial = InterstitialAd(this)
        interstitial.adUnitId = getString(R.string.ad_mob_interstitial_id)
        interstitial.loadAd(requestBuilder.build())
        interstitial.adListener = object : AdListener() {
            override fun onAdLoaded() {
                interstitial.show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    fun showProgress(show: Boolean) {
        when {
            show -> {
                if (!isFinishing && !progressDialog!!.isShowing) {
                    progressDialog?.setCanceledOnTouchOutside(false)
                    progressDialog?.show()
                }
            }
            else -> try {
                if (progressDialog?.isShowing!! && !isFinishing) {
                    progressDialog?.dismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(SLocaleHelper.onAttach(newBase!!)))
    }

    override fun onStart() {
        Log.d("onStart", "called")
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        val appTheme = WooBoxApp.appTheme

        /*val locale = Locale(WooBoxApp.language)
        if (language != null && locale != language) {
            recreate()
            return
        }*/

        if (themeApp != 0 && themeApp != appTheme) {
            launchActivityWithNewTask<DashBoardActivity>()
        }

    }
}
