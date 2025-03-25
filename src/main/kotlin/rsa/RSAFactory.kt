package org.jetbrains.kotlin.rsa

import org.jetbrains.kotlin.bigint.BigPrimeGenerator
import org.jetbrains.kotlin.bigint.modInverse
import java.math.BigInteger

const val E_SUGGESTION = "65537"

class RSAFactory(seed: ByteArray? = null) {
    private val primeGenerator = BigPrimeGenerator(seed)

    /**
     * @return (short key, long key)
     */
    fun generateKeys(
        bitLength: Int = 512,
        eSuggestion: BigInteger = BigInteger(E_SUGGESTION),
        codeword: String
    ): Pair<RSAKey, RSAKey> {
        val p = primeGenerator.next(bitLength)
        val q = primeGenerator.next(bitLength)
        val n = p.multiply(q)
        val m = (p - BigInteger.ONE) * (q - BigInteger.ONE)

        var e = eSuggestion
        while (m.gcd(e) != BigInteger.ONE) {
            e = e.add(BigInteger.TWO)
        }

        val d = modInverse(e, m)
        return RSAKey(e, n, codeword) to RSAKey(d, n, codeword)
    }
}
