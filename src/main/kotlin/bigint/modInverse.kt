package org.jetbrains.kotlin.bigint

import java.math.BigInteger


fun modInverse(base: BigInteger, modulus: BigInteger): BigInteger {
    var currentNum = base
    var modValue = modulus
    var prevCoefficient = BigInteger.ZERO
    var coefficient = BigInteger.ONE

    while (currentNum > BigInteger.ONE) {
        val quotient = currentNum / modValue
        val oldNum = modValue

        modValue = currentNum % modValue
        currentNum = oldNum

        val oldCoefficient = prevCoefficient
        prevCoefficient = coefficient - quotient * prevCoefficient
        coefficient = oldCoefficient
    }

    return if (coefficient < BigInteger.ZERO) coefficient + modulus else coefficient
}