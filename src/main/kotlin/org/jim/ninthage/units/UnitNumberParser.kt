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
        val eAndM = getEntryCountAndModelCount(rawValue)
        val numbers = parseAllNumbers(rawValue)
        numbers.forEach {
            updateIsPoints(armyBookEntry, it)
            updateIsModels(armyBookEntry, it)
            updateIsEntry(armyBookEntry, it)
        }
        val points = findPoints(rawValue, numbers)
        if (eAndM != null) {
            return UnitNumber(
                points,
                eAndM.second,
                eAndM.first
            )
        }
        val models = findModels(rawValue, armyBookEntry, numbers)
        val entries = findEntries(rawValue, numbers)
        return UnitNumber(points, models, entries)
    }

    private fun findEntries(rawValue: String, numbers: List<NumberCould>): Int {
        return numbers.find { it.couldBeEntry }?.number ?: 1
    }

    private fun findModels(
        rawValue: String,
        armyBookEntry: ArmyBookEntry,
        numbers: List<NumberCould>
    ): Int {
        val possibleModels =
            numbers.stream()
                .filter { !it.couldBePoints }
                .filter { it.couldBeModels }
                .toList()
        return if (possibleModels.size == 0 && armyBookEntry.minCount == 1) {
            1
        } else if (possibleModels.size == 1) {
            possibleModels[0].number
        } else {
            throw ParseException(rawValue+"\n"+armyBookEntry+"\n"+numbers)
        }

    }

    private fun findPoints(rawValue: String, numbers: List<NumberCould>): Int? {
        return numbers.stream().filter { it.couldBePoints }.mapToInt { it.number }.max()
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

    fun getEntryCountAndModelCount(value: String): Pair<Int, Int>? {
        val m = entryCountAndModelCount.matcher(value)
        return if (m.find()) {
            return Pair(
                m.group(1).toInt(),
                m.group(2).toInt()
            )
        } else {
            null
        }
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