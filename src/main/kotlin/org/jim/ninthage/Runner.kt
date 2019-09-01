@file:JvmName("Runner")

package org.jim.ninthage

import java.nio.charset.Charset


fun main(args: Array<String>) {
    try {
        System.setProperty("line.separator", "\n")
        System.setProperty("file.encoding", "UTF-8")
        Parser().readAllList()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}
