package com.thuylinh.bento.utils.oauthInterceptor

open class OAuthException(message: String, e: Exception) : RuntimeException() {

    companion object {
        private val serialVersionUID = 1L
    }
}