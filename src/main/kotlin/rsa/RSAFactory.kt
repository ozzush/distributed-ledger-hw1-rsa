package org.jetbrains.kotlin.rsa

import java.math.BigInteger
import java.nio.charset.Charset
import java.security.SecureRandom

const val E_SUGGESTION = "65537"

class RSAFactory(seed: ByteArray? = null) {
    private val random = if (seed != null) SecureRandom(seed) else SecureRandom()

    /**
     * @return short key and long key
     */
    fun generateKeys(
        bitLength: Int = 512,
        eSuggestion: BigInteger = BigInteger(E_SUGGESTION),
        codeword: String
    ): Pair<RSAKey, RSAKey> {
        val p = BigInteger.probablePrime(bitLength, random)
        val q = BigInteger.probablePrime(bitLength, random)
        val n = p.multiply(q)
        val m = (p - BigInteger.ONE) * (q - BigInteger.ONE)

        var e = eSuggestion
        while (m.gcd(e) != BigInteger.ONE) {
            e = e.add(BigInteger.TWO)
        }

        val d = e.modInverse(m)
        return RSAKey(e, n, codeword) to RSAKey(d, n, codeword)
    }

    fun generate(
        keyBitLength: Int,
        eSuggestion: BigInteger = BigInteger(E_SUGGESTION),
        blockSize: Int = 0,
        codeword: String,
        charset: Charset = Charsets.UTF_8
    ): Pair<RSAEncryptor, RSADecryptor> {
        val (encryptionKey, decryptionKey) = generateKeys(keyBitLength, eSuggestion, codeword)
        val encryptor = RSAEncryptor(encryptionKey, blockSize, charset)
        val decryptor = RSADecryptor(decryptionKey, charset)
        return encryptor to decryptor
    }
}
