package org.jetbrains.kotlin.bigint

import java.math.BigInteger
import java.security.SecureRandom

class BigPrimeGenerator(seed: ByteArray? = null) {
    private val random = if (seed != null) SecureRandom(seed) else SecureRandom()

    fun next(bitLength: Int): BigInteger {
        require(bitLength >= 2) { "Bit length must be at least 2" }

        var candidate: BigInteger
        do {
            candidate = BigInteger(bitLength, random).setBit(bitLength - 1)
            // Make sure that the candidate is odd
            candidate = candidate.or(BigInteger.ONE)
        } while (!isPrime(candidate))

        return candidate
    }

    // Miller-Rabin primality test
    private fun isPrime(number: BigInteger, iterations: Int = 100): Boolean {
        if (number < BigInteger.TWO) return false
        if (number == BigInteger.TWO || number == BigInteger.valueOf(3)) return true
        if (number.mod(BigInteger.TWO) == BigInteger.ZERO) return false

        var oddFactor = number - BigInteger.ONE
        var powerOfTwoFactor = 0
        while (oddFactor.mod(BigInteger.TWO) == BigInteger.ZERO) {
            oddFactor = oddFactor.divide(BigInteger.TWO)
            powerOfTwoFactor++
        }

        repeat(iterations) {
            val randomBase = getRandomBigInteger(BigInteger.TWO, number - BigInteger.TWO)
            var modularExponentiation = randomBase.modPow(oddFactor, number)

            if (modularExponentiation == BigInteger.ONE || modularExponentiation == number - BigInteger.ONE) {
                return@repeat
            }

            for (i in 0 until powerOfTwoFactor - 1) {
                modularExponentiation = modularExponentiation.modPow(BigInteger.TWO, number)
                if (modularExponentiation == BigInteger.ONE) return false
                if (modularExponentiation == number - BigInteger.ONE) break
            }

            if (modularExponentiation != number - BigInteger.ONE) return false
        }

        return true
    }

    private fun getRandomBigInteger(min: BigInteger, max: BigInteger): BigInteger {
        var result: BigInteger
        do {
            result = BigInteger(max.bitLength(), random)
        } while (result < min || result > max)
        return result
    }
}