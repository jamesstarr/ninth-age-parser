@file:JvmName("Runner")

package org.jim.ninthage


fun main(args: Array<String>) {
    try {

        Parser().readAllList()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}
