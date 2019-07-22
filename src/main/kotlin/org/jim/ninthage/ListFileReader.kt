package org.jim.ninthage

import com.google.common.base.Joiner
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths


class ListFileReader {

    public fun readFile(fileName:String) :String {
        com.google.common.base.Charsets.UTF_8.toString()
        return Joiner.on("\n")
            .join(Files.readAllLines(Paths.get(fileName), Charsets.UTF_8))

    }
}