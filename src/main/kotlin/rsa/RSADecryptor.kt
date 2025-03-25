package org.jetbrains.kotlin.rsa

import org.jetbrains.kotlin.api.Decryptor
import java.math.BigInteger
import java.nio.charset.Charset

class RSADecryptionException(msg: String) : RuntimeException(msg)

class RSADecryptor(
    val key: RSAKey,
    override val charset: Charset
) : Decryptor {
    override fun decrypt(msg: Collection<ByteArray>): ByteArray {
        var checkedCodeword = false
        val codewordBytes = key.codeword.toByteArray(charset)
        return msg
            .asSequence()
            .map { BigInteger(it).modPow(key.exponent, key.modulus).toByteArray() }
            .fold(emptyArray<Byte>()) { acc, bytes ->
                val next = acc + bytes.toTypedArray()
                if (!checkedCodeword && next.size >= codewordBytes.size) {
                    // check the codeword and strip it from next
                    if (next.take(codewordBytes.size) != codewordBytes.toList()) {
                        throw RSADecryptionException("Couldn't decrypt the message: codeword doesn't match")
                    }
                    checkedCodeword = true
                    next.drop(codewordBytes.size).toTypedArray()
                } else next
            }.toByteArray()
    }
}