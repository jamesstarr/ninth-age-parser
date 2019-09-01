package org.jim.utils

import java.nio.file.Files
import java.nio.file.Paths

fun main(vararg args:String) {
    val file = Paths.get(args[0])
    val fileString = Files.readString(
        file,
        Charsets.UTF_8
    )
    Files.writeString(
        file,
        fileString.replace("\r\n", "\n"),
        Charsets.UTF_8
    )
}