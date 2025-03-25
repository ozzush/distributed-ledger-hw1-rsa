package org.jetbrains.kotlin.rsa

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigInteger

@Serializable
data class RSAKey(
    @Serializable(with = BigIntegerSerializer::class)
    val exponent: BigInteger,
    @Serializable(with = BigIntegerSerializer::class)
    val modulus: BigInteger,
    val codeword: String
)

object BigIntegerSerializer : KSerializer<BigInteger> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("java.math.BigInteger", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: BigInteger) {
        val string = value.toString()
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): BigInteger {
        val string = decoder.decodeString()
        return BigInteger(string)
    }
}