package org.jim.ninthage.roster

import com.google.common.base.Splitter
import org.jim.ninthage.armybook.ArmyBookClassifier
import org.jim.ninthage.data.ArmyBooks
import org.jim.ninthage.models.Roster
import org.jim.ninthage.models.RosterUnit
import org.jim.ninthage.units.UnitEntryParser
import org.jim.opennlp.classifier.SimpleClassifier

class RosterParser(
    val armyBookToUnitEntryParser: Map<String, UnitEntryParser>,
    val armyBookDetector: SimpleClassifier = ArmyBookClassifier.build()
) {
    companion object {
        fun build(): RosterParser {
            return RosterParser(
                ArmyBooks.All.map {
                    Pair(it.shortLabel, UnitEntryParser.build(armyBook = it))
                }.toMap()
            )
        }

    }

    fun parseRoster(rawArmyList: String): Roster {
        val armyBook = armyBookDetector.classify(rawArmyList)
        val rawUnits =
            Splitter.on("\n")
                .omitEmptyStrings()
                .split(rawArmyList)
                .filter { it.isNotBlank() }
        val units =
            if (armyBookToUnitEntryParser.containsKey(armyBook)) {
                val classifier = armyBookToUnitEntryParser.get(armyBook)!!
                rawUnits.map { classifier.parse(it) }
            } else {
                rawUnits.map { RosterUnit(it, "Unknown") }
            }

        return Roster(
            rawArmyList,
            armyBook,
            units
        )

    }
}

