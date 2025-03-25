package org.jetbrains.kotlin

import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.output.MordantHelpFormatter

class MainApp : NoOpCliktCommand("rsa") {
    init {
        context {
            helpFormatter = { MordantHelpFormatter(it, showDefaultValues = true, showRequiredTag = true) }
        }
    }
}

fun main(args: Array<String>) = MainApp().subcommands(Keygen(), Encrypt(), Decrypt()).main(args)