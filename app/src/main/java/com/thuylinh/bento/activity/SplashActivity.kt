package com.thuylinh.bento.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thuylinh.bento.R
import com.thuylinh.bento.WooBoxApp
import com.thuylinh.bento.utils.Constants
import com.thuylinh.bento.utils.extensions.getSharedPrefInstance
import com.thuylinh.bento.utils.extensions.launchActivity
import com.thuylinh.bento.utils.extensions.runDelayed
import com.thuylinh.bento.utils.extensions.switchToDarkMode

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