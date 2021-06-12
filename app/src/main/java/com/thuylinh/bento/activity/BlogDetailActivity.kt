package com.thuylinh.bento.activity

import android.os.Bundle
import com.thuylinh.bento.AppBaseActivity
import com.thuylinh.bento.R
import com.thuylinh.bento.models.Blog
import com.thuylinh.bento.utils.Constants.KeyIntent.DATA
import com.thuylinh.bento.utils.extensions.loadImageFromUrl
import kotlinx.android.synthetic.main.activity_blog_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class BlogDetailActivity : AppBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_detail)

        setToolbar(toolbar)

        val blog = intent.getSerializableExtra(DATA) as Blog
        title = blog.title

        ivProduct.loadImageFromUrl(blog.image!!)
        tvTitle.text = blog.title
        tvPublishDate.text = blog.publish_date
        tvDescription.text = blog.description
    }
}
