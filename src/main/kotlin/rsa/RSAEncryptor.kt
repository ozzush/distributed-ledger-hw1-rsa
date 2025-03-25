package org.jetbrains.kotlin.rsa

import org.jetbrains.kotlin.api.Encryptor
import java.math.BigInteger
import java.nio.charset.Charset

class RSAEncryptor(
    val key: RSAKey,
    val blockSize: Int = 0,
    override val charset: Charset = Charsets.UTF_8
) : Encryptor {
    override fun encrypt(msg: Collection<Byte>): List<ByteArray> {
        val msgWithCodeword = key.codeword.toByteArray().toList() + msg
        val byteBlocks =
            if (blockSize <= 0) listOf(msgWithCodeword.toByteArray())
            else msgWithCodeword.chunked(blockSize) { it.toByteArray() }
        return byteBlocks
            .fold(emptyList()) { acc, bytes ->
                acc + BigInteger(bytes).modPow(key.exponent, key.modulus).toByteArray()
            }
    }
}