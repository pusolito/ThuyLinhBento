package com.thuylinh.bento.utils.oauthInterceptor

interface TimestampService {
    val timestampInSeconds: String
    val nonce: String
}