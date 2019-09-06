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
        writer.flush()
    }

    private fun getWriter(armyBook: String): Writer {

        return armyBookToWriters.getOrPut(armyBook) {
            val file = dir.resolve(armyBook + ".txt")
            Files.delete(file)
            Files.newBufferedWriter(file, Charsets.UTF_8)
        }
    }

    override fun close() {
        armyBookToWriters.values.forEach {
            it.flush()
            it.close()
        }
    }

}