package org.jim.ninthage.reports

import com.google.common.collect.Maps
import java.io.Closeable
import java.io.Writer
import java.nio.file.Files
import java.nio.file.Path

class ArmyBookWriterPool(val dir: Path) : Closeable {
    companion object {
        fun build(dir: Path): ArmyBookWriterPool {
            Files.createDirectories(dir)
            return ArmyBookWriterPool(dir)
        }
    }

    val armyBookToWriters = Maps.newHashMap<String, Writer>()

    fun write(armyBook: String, payload: String) {
        val writer = getWriter(armyBook)
        writer.write(payload)
        writer.write("\n<ARMY_BOOK>\n")
    }

    private fun getWriter(armyBook: String): Writer {

        return armyBookToWriters.getOrPut(armyBook) {
            Files.newBufferedWriter(dir.resolve(armyBook + ".txt"))
        }
    }

    override fun close() {
        armyBookToWriters.values.forEach { it.close() }
    }

}