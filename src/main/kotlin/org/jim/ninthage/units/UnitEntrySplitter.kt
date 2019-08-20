package org.jim.ninthage.units

import com.google.common.base.Splitter
import org.jim.ninthage.models.ArmyList
import org.jim.ninthage.models.UnitEntry
import org.jim.opennlp.SimpleClassifier

class UnitEntrySplitter(
    val armyBookToClassifier: Map<String, SimpleClassifier> = UnitEntryClassifier.build()
) {


    fun parseArmyList(rawArmyList: String, armyBook: String): ArmyList {
        val rawUnits =
            Splitter.on("\n")
                .omitEmptyStrings()
                .split(rawArmyList)
                .filter { it.isNotBlank() }
        val units =
            if (armyBookToClassifier.containsKey(armyBook)) {
                val classifier = armyBookToClassifier[armyBook]
                rawUnits.map { UnitEntry(it, classifier!!.classify(it)) }
            } else {
                rawUnits.map { UnitEntry(it, "Unknown") }
            }

        return ArmyList(
            rawArmyList,
            armyBook,
            units
        )

    }
}
