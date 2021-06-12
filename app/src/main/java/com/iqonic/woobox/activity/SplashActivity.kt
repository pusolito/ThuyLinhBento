package com.iqonic.woobox.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.iqonic.woobox.R
import com.iqonic.woobox.WooBoxApp
import com.iqonic.woobox.utils.Constants
import com.iqonic.woobox.utils.extensions.getSharedPrefInstance
import com.iqonic.woobox.utils.extensions.launchActivity
import com.iqonic.woobox.utils.extensions.runDelayed
import com.iqonic.woobox.utils.extensions.switchToDarkMode

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        switchToDarkMode(WooBoxApp.appTheme == Constants.THEME.DARK)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        runDelayed(1000) {
            if (getSharedPrefInstance().getBooleanValue(Constants.SharedPref.SHOW_SWIPE)) {
                launchActivity<DashBoardActivity>()
            } else {
                launchActivity<WalkThroughActivity>()
            }
            finish()
        }
    }
}