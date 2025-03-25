package org.jetbrains.kotlin

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.groups.mutuallyExclusiveOptions
import com.github.ajalt.clikt.parameters.groups.required
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.int
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import org.jetbrains.kotlin.rsa.RSAKey
import org.jetbrains.kotlin.rsa.RSAEncryptor

class Encrypt : CliktCommand("encrypt") {
    val keyPath by option().file(mustExist = true).required()
    val input by mutuallyExclusiveOptions(
        option("--input-file").file(mustExist = true).convert { it.readText() },
        option("--input-text"),
    ).required()
    val outputFile by option().file().required()
    val blockSize by option(help = "0 means no splitting into blocks").int().default(0)
    val charset by option().choice(
        "UTF8" to Charsets.UTF_8,
        "UTF16" to Charsets.UTF_16,
        "UTF32" to Charsets.UTF_32,
    ).default(Charsets.UTF_8)

    @OptIn(ExperimentalSerializationApi::class)
    override fun run() {
        outputFile.createNewFile()
        val encryptionKey = Cbor.decodeFromByteArray<RSAKey>(keyPath.readBytes())
        val encryptor = RSAEncryptor(encryptionKey, blockSize, charset)
        val encrypted = encryptor.encrypt(input)
        outputFile.writeBytes(Cbor.encodeToByteArray(encrypted))
        println("Encrypted message saved to file ${outputFile.absolutePath}")
    }
}