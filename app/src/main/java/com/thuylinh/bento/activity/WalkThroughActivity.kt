package com.thuylinh.bento.activity

import android.os.Bundle
import com.thuylinh.bento.AppBaseActivity
import com.thuylinh.bento.R
import com.thuylinh.bento.adapter.WalkAdapter
import com.thuylinh.bento.utils.CarouselEffectTransformer
import com.thuylinh.bento.utils.Constants.SharedPref.SHOW_SWIPE
import com.thuylinh.bento.utils.extensions.*
import kotlinx.android.synthetic.main.activity_walk_through.*

class WalkThroughActivity : AppBaseActivity() {
    private var mCount: Int? = null
    private var mHeading = arrayOf("Chào mừng bạn tới Thùy Linh Bento!", "Ứng dụng website và android đặt đồ ăn!", "Tiết kiệm thời gian dành cho bạn!")
    private val mSubHeading = arrayOf("Chi phí giao hàng tốt nhất,\n an toàn và bảo đảm.", "Thực phẩm vệ sinh và được đóng gói kỹ lưỡng.", "Mọi thứ đều được cập nhật dù bạn đặt ở bất kì đâu.")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walk_through)

        init()
        val adapter = WalkAdapter()

        ViewPager.adapter = adapter

        dots.attachViewPager(ViewPager)
        dots.setDotDrawable(R.drawable.bg_circle_primary, R.drawable.black_dot)
        mCount = adapter.count

        btnStatShopping.onClick {
            getSharedPrefInstance().setValue(SHOW_SWIPE, true)
            launchActivityWithNewTask<DashBoardActivity>()
        }
        llSignIn.onClick {
            launchActivity<SignInUpActivity>()
        }
    }

    private fun init() {
        ViewPager.apply {
            clipChildren = false
            pageMargin = resources.getDimensionPixelOffset(R.dimen.spacing_small)
            offscreenPageLimit = 3
            setPageTransformer(false, CarouselEffectTransformer(this@WalkThroughActivity))
            offscreenPageLimit = 0

            onPageSelected { position: Int ->
                val animFadeIn = android.view.animation.AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
                tvHeading.startAnimation(animFadeIn)
                tvSubHeading.startAnimation(animFadeIn)
                tvHeading.text = mHeading[position]
                tvSubHeading.text = mSubHeading[position]
            }
        }
    }
}