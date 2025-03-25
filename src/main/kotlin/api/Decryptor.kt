package org.jetbrains.kotlin.api

import java.nio.charset.Charset

interface Decryptor {
    val charset: Charset

    fun decrypt(msg: Collection<ByteArray>): ByteArray

    fun decryptToString(msg: Collection<ByteArray>): String {
        return decrypt(msg).toString(charset)
    }
}