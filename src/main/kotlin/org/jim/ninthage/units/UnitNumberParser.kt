package org.jim.ninthage.units

import org.jim.ninthage.ParseException
import org.jim.ninthage.models.ArmyBookEntry
import java.util.regex.Pattern
import kotlin.streams.toList

class UnitNumberParser {
    val entryCountAndModelCount = Pattern.compile("(\\d+)\\s*[xX]\\s*(\\d+)")
    val numberPattern = Pattern.compile("(\\d+)([xX]?)")

    data class UnitNumber(
        val points: Int?,
        val modelCount: Int,
        val entryCount: Int
    )

    fun parse(rawValue: String, armyBookEntry: ArmyBookEntry): UnitNumber {
        val eAndM = getEntryCountAndModelCount(rawValue, armyBookEntry)
        val numbers = parseAllNumbers(rawValue)
        numbers.forEach {
            updateIsPoints(armyBookEntry, it)
            updateIsModels(armyBookEntry, it)
            updateIsEntry(armyBookEntry, it)
        }
        val points = findPoints(rawValue, numbers)
        if (eAndM != null) {
            return UnitNumber(
                points?.number,
                eAndM.second,
                eAndM.first
            )
        }
        val models = findModels(rawValue, armyBookEntry, numbers)
        val entries = findEntries(rawValue, numbers.toMutableList().apply { remove(models) })
        return UnitNumber(points?.number, models.number, entries)
    }

    private fun findEntries(rawValue: String, numbers: List<NumberCould>): Int {
        return numbers.find { it.couldBeEntry }?.number ?: 1
    }

    private fun findModels(
        rawValue: String,
        armyBookEntry: ArmyBookEntry,
        numbers: List<NumberCould>
    ): NumberCould {
        val possibleModels =
            numbers.stream()
                .filter { !it.couldBePoints }
                .filter { it.couldBeModels }
                .toList()
        return if (possibleModels.size == 0 && armyBookEntry.minCount == 1) {
            NumberCould(1, -1, false)
        } else if (possibleModels.size == 1) {
            possibleModels[0]
        } else {
            throw ParseException(rawValue + "\n" + armyBookEntry + "\n" + numbers)
        }

    }

    private fun findPoints(rawValue: String, numbers: List<NumberCould>): NumberCould? {
        return numbers.stream().filter { it.couldBePoints }.max { a, b -> Integer.compare(a.number, b.number) }
            .let {
                if (it.isPresent) {
                    it.orElseThrow()
                } else {
                    null
                }
            }
    }

    fun updateIsPoints(armyBookEntry: ArmyBookEntry, numberCould: NumberCould) {
        val minPoints = armyBookEntry.points + armyBookEntry.pointsPerModel * armyBookEntry.minCount
        numberCould.couldBePoints = numberCould.number >= minPoints
    }

    fun updateIsModels(armyBookEntry: ArmyBookEntry, numberCould: NumberCould) {
        numberCould.couldBeModels =
            numberCould.number in armyBookEntry.minCount.rangeTo(armyBookEntry.maxCount)
    }

    fun updateIsEntry(armyBookEntry: ArmyBookEntry, numberCould: NumberCould) {
        numberCould.couldBeEntry = numberCould.hasX
    }

    fun parseAllNumbers(rawValue: String): List<NumberCould> {
        val numberMatcher =
            numberPattern.matcher(rawValue)
        return sequence {
            while (numberMatcher.find()) {
                val intValue =
                    numberMatcher.group(1).toInt()
                yield(
                    NumberCould(
                        intValue,
                        numberMatcher.start(1),
                        !numberMatcher.group(2).isNullOrEmpty()
                    )
                )

            }
        }.toList()
    }

    fun getEntryCountAndModelCount(value: String, armyBookEntry: ArmyBookEntry): Pair<Int, Int>? {
        val m = entryCountAndModelCount.matcher(value)
        if (m.find()) {
            do {

                val v1 = m.group(1).toInt()
                val v2 = m.group(2).toInt()
                if (v2 in armyBookEntry.run { minCount.rangeTo(maxCount) }) {
                    return Pair(v1, v2)
                }
            } while (m.find())
            throw ParseException(value)
        }
        return null
    }

}

data class NumberCould(
    val number: Int,
    val position: Int,
    val hasX: Boolean,
    var couldBePoints: Boolean = false,
    var couldBeEntry: Boolean = false,
    var couldBeModels: Boolean = false
)