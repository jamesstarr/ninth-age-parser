package org.jim.ninthage.units

import org.jim.ninthage.models.ArmyBookEntry
import java.util.regex.Pattern

class UnitNumberParser {
    val numberPattern = Pattern.compile("(\\d+)[^xX]")
    val entryCount = Pattern.compile("(\\d+)[xX]")

    data class UnitNumber(
        val points: Int?,
        val modelCount: Int,
        val entryCount: Int
    )

    fun parse(rawValue: String, armyBookEntry: ArmyBookEntry): UnitNumber {
        val em = entryCount.matcher(rawValue)
        val entryCount =
            if (em.find()) {
                em.group(1).toInt()
            } else {
                1
            }
        val numberMatcher =
            numberPattern.matcher(rawValue + " ")
        val numbers =
            sequence {
                while (numberMatcher.find()) {
                    yield(numberMatcher.group(1).toInt())
                }
            }.toList().sorted()
        return if (numbers.isEmpty()) {
             UnitNumber(null, 1, entryCount)
        } else if (numbers.size == 1) {
            val value = numbers[0]
            val ei = entryIs(armyBookEntry, value)
            when (ei) {
                EntryIs.Points -> {
                    if (armyBookEntry.minCount != 1) {
                        println("WARN ${rawValue}")
                    }
                    UnitNumber(value, armyBookEntry.minCount, entryCount)
                }
                EntryIs.ModelCount ->
                    UnitNumber(null, value, entryCount)
                else -> throw RuntimeException(rawValue)
            }
        } else {
            UnitNumber(numbers.last(), numbers[numbers.size -2], entryCount)
        }
    }

    enum class EntryIs {
        Points,
        ModelCount,
        Unknown
    }

    fun entryIs(armyBookEntry: ArmyBookEntry, value: Int): EntryIs {
        val minPoints = armyBookEntry.points + armyBookEntry.pointsPerModel * armyBookEntry.minCount
        return if (armyBookEntry.minCount <= value && value <= armyBookEntry.maxCount) {
            EntryIs.ModelCount
        } else if (minPoints <= value) {
            EntryIs.Points
        } else {
            EntryIs.Unknown
        }
    }

}