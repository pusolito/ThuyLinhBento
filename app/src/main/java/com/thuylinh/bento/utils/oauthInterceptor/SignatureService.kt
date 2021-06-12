package com.thuylinh.bento.utils.oauthInterceptor

interface SignatureService {

    val signatureMethod: String
    fun getSignature(baseString: String, apiSecret: String, tokenSecret: String): String
}