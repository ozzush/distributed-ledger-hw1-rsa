package org.jetbrains.kotlin.api

import java.nio.charset.Charset

interface Encryptor {
    val charset: Charset

    fun encrypt(msg: Collection<Byte>): List<ByteArray>

    fun encrypt(msg: ByteArray): List<ByteArray> {
        return encrypt(msg.toList())
    }

    fun encrypt(msg: String): List<ByteArray> {
        return encrypt(msg.toByteArray(charset))
    }
}