package com.thuylinh.bento.activity

import android.os.Bundle
import com.thuylinh.bento.AppBaseActivity
import com.thuylinh.bento.R
import com.thuylinh.bento.fragments.SearchFragment
import com.thuylinh.bento.utils.extensions.addFragment

class SearchActivity : AppBaseActivity() {

    private val mSearchFragment = SearchFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search2)
        addFragment(mSearchFragment, R.id.fragmentContainer)
    }

}