package com.thuylinh.bento.activity

import android.os.Bundle
import com.thuylinh.bento.AppBaseActivity
import com.thuylinh.bento.R
import com.thuylinh.bento.fragments.MyCartFragment
import com.thuylinh.bento.utils.Constants.AppBroadcasts.CART_COUNT_CHANGE
import com.thuylinh.bento.utils.extensions.BroadcastReceiverExt
import com.thuylinh.bento.utils.extensions.addFragment
import com.thuylinh.bento.utils.extensions.getCartListFromPref
import com.thuylinh.bento.utils.extensions.launchActivityWithNewTask
import kotlinx.android.synthetic.main.toolbar.*

class MyCartActivity : AppBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cart)
        setToolbar(toolbar)
        title = getString(R.string.menu_my_cart)

        val fr = MyCartFragment()
        addFragment(fr, R.id.container)
        BroadcastReceiverExt(this) {
            onAction(CART_COUNT_CHANGE) {
                if (fr.isAdded) {
                    fr.invalidateCartLayout(getCartListFromPref())
                }
            }
        }
    }

    fun shopNow() {
        launchActivityWithNewTask<DashBoardActivity>()
        finish()
    }

}
