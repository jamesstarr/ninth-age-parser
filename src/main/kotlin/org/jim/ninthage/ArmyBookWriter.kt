package org.jim.ninthage

import com.google.common.collect.Maps
import java.io.Closeable
import java.io.Writer
import java.nio.file.Files
import java.nio.file.Path

class ArmyBookWriter(val dir: Path): Closeable {
    companion object {
        fun build(dir:Path): ArmyBookWriter {
            Files.createDirectories(dir)
            return ArmyBookWriter(dir)
        }
    }

    val armyBookToWriters = Maps.newHashMap<String, Writer>()

    fun write(armyBook: String, payload:String) {
        val writer = getWriter(armyBook)
        writer.write(payload)
        writer.write("\n")
    }

    private fun getWriter(armyBook:String):Writer {

        return armyBookToWriters.getOrPut(armyBook) {
            Files.newBufferedWriter(dir.resolve(armyBook + ".txt"))
        }
    }

    override fun close() {
        armyBookToWriters.values.forEach{it.close()}
    }

}