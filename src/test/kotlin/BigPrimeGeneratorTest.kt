import org.jetbrains.kotlin.bigint.BigPrimeGenerator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class BigPrimeGeneratorTest {

    @ParameterizedTest
    @ValueSource(strings = ["first", "second", "third", "fourth", "fifth"])
    fun `test generated prime is actually prime`(seed: String) {
        val gen = BigPrimeGenerator(seed.toByteArray())
        val bitLength = 512
        val prime = gen.next(bitLength)

        assertTrue(prime.isProbablePrime(100), "Generated number should be prime")
    }

    @ParameterizedTest
    @ValueSource(strings = ["first", "second", "third", "fourth", "fifth"])
    fun `test prime has correct bit length`(seed: String) {
        val gen = BigPrimeGenerator(seed.toByteArray())
        val bitLength = 512
        val prime = gen.next(bitLength)

        assertEquals(bitLength, prime.bitLength(), "Generated prime should have correct bit length")
    }
}
