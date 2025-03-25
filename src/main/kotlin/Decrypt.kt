package org.jetbrains.kotlin

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import org.jetbrains.kotlin.rsa.RSADecryptor
import org.jetbrains.kotlin.rsa.RSAKey

class Decrypt : CliktCommand("decrypt") {
    val keyPath by option().file(mustExist = true).required()
    val inputFile by option().file(mustExist = true).required()
    val outputFile by option(help = "If not specified, decrypted data is printed to stdout").file()
    val charset by option().choice(
        "UTF8" to Charsets.UTF_8,
        "UTF16" to Charsets.UTF_16,
        "UTF32" to Charsets.UTF_32,
    ).default(Charsets.UTF_8)

    @OptIn(ExperimentalSerializationApi::class)
    override fun run() {
        outputFile?.createNewFile()
        val decryptionKey = Cbor.decodeFromByteArray<RSAKey>(keyPath.readBytes())
        val decryptor = RSADecryptor(decryptionKey, charset)
        val inputBlocks = Cbor.decodeFromByteArray<List<ByteArray>>(inputFile.readBytes())
        val decrypted = decryptor.decryptToString(inputBlocks)
        if (outputFile != null) {
            outputFile!!.writeText(decrypted)
            println("Decrypted message saved to file ${outputFile!!.absolutePath}")
        } else {
            println(decrypted)
        }
    }
}