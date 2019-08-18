@file:JvmName("Runner")
package org.jim.ninthage

import java.lang.Exception


fun main(args: Array<String>) {
    try {

        Parser().readAllList()
    } catch (ex:Exception) {
        ex.printStackTrace()
    }
}
