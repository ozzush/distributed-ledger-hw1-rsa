package org.jetbrains.kotlin

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.int
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.encodeToByteArray
import org.jetbrains.kotlin.rsa.E_SUGGESTION
import org.jetbrains.kotlin.rsa.RSAFactory
import java.io.File
import java.math.BigInteger

enum class Purpose {
    ENCRYPTION,
    SIGNATURE
}

class Keygen : CliktCommand("keygen") {
    val bitLength by option().int().default(512)
    val purpose by option()
        .choice("encryption" to Purpose.ENCRYPTION, "signature" to Purpose.SIGNATURE)
        .default(Purpose.ENCRYPTION)
    val eSuggestion by option().default(E_SUGGESTION)
    val keysPath by option().default("rsa_key")
    val seed by option()
    val codeword by option().required()

    @OptIn(ExperimentalSerializationApi::class)
    override fun run() {
        val factory = RSAFactory(seed?.toByteArray())
        val (shortKey, longKey) = factory.generateKeys(bitLength, BigInteger(eSuggestion), codeword)
        val (encryptor, decryptor) = when (purpose) {
            Purpose.ENCRYPTION -> shortKey to longKey
            Purpose.SIGNATURE -> longKey to shortKey
        }
        val encryptionKeyFile = File("$keysPath.enc").also { it.createNewFile() }
        val decryptionKeyFile = File("$keysPath.dec").also { it.createNewFile() }
        encryptionKeyFile.writeBytes(Cbor.encodeToByteArray(encryptor))
        println("Encryption key is written to file ${encryptionKeyFile.absolutePath}")
        decryptionKeyFile.writeBytes(Cbor.encodeToByteArray(decryptor))
        println("Decryption key is written to file ${decryptionKeyFile.absolutePath}")
    }
}