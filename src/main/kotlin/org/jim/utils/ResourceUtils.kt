package org.jim.utils

import java.io.InputStream
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

    fun resourceAsInputStream(resourceName:String):InputStream {
        return javaClass.getResourceAsStream(resourceName)
    }
}