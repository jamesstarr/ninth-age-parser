package org.jim.ninthage.utils

import java.io.InputStreamReader

object ResourceUtils {
    fun readResourceAsUtf8String(resourceName:String):String {
        javaClass.getResourceAsStream(resourceName).use {
            if(it == null) {
                throw RuntimeException(resourceName)
            }
            return InputStreamReader(it, Charsets.UTF_8).readText()
        }
    }
}