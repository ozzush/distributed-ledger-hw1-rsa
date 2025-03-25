import org.jetbrains.kotlin.bigint.modInverse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigInteger

class ModularInverseTest {
    @Test
    fun `test modular inverse correctness`() {
        val e = BigInteger("65537") // Common RSA exponent
        val m = BigInteger("12345678901234567890") // Example modulus (should be co-prime with e)

        val d = modInverse(e, m)

        // Check that (d * e) % m == 1
        assertEquals(BigInteger.ONE, (d * e) % m, "d should be the modular inverse of e mod m")
    }

    @Test
    fun `test modular inverse of 1 is 1`() {
        val e = BigInteger.ONE
        val m = BigInteger("99999")

        val d = modInverse(e, m)

        assertEquals(BigInteger.ONE, d, "The modular inverse of 1 mod any number should be 1")
    }

    @Test
    fun `test modular inverse of coprime numbers`() {
        val e = BigInteger("3")
        val m = BigInteger("7")

        val d = modInverse(e, m)

        assertEquals(BigInteger("5"), d, "The modular inverse of 3 mod 7 should be 5 because (3 * 5) % 7 == 1")
    }
}