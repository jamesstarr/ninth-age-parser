package org.jim.ninthage.units

import org.jim.ninthage.data.TrainingData
import org.jim.ninthage.models.ArmyBook
import org.jim.ninthage.models.RosterUnit
import org.jim.utils.ResourceUtils
import java.lang.StringBuilder


class UnitEntryParser(
    private val armyBook: ArmyBook,
    private val unitEntryClassifier: UnitEntryClassifier,
    private val unitDecorators: Map<String, OptionParser>
) {
    val unitNumberParser = UnitNumberParser()

    companion object {
        fun build(armyBook: ArmyBook): UnitEntryParser {
            val tokens = unitTokens(armyBook)

            val unitEntryClassifier =
                UnitEntryClassifier(
                    UnitEntryClassifier.train(
                        tokens
                    )
                )
            val unitEntryToOptionParsers =
                armyBook.entries.map { armyBookEntry ->
                    armyBookEntry.options
                    Pair<String, OptionParser>(
                        armyBookEntry.name,
                        OptionParser.build(
                            armyBook,
                            armyBookEntry,
                            tokens
                        )
                    )
                }.toMap()
            return UnitEntryParser(
                armyBook,
                unitEntryClassifier,
                unitEntryToOptionParsers

            )
        }

        private fun unitTokens(armyBook: ArmyBook): List<UnitToken> {
            val trainData = TrainingData.UnitClassifier[armyBook.name]!!
            val unitTokenizer = UnitTokenizer(armyBook)
            val trainingString =
                trainData
                    .map { ResourceUtils.readResourceAsUtf8String(it)}
                    .joinToString()
            return unitTokenizer.splitUnitTokens(trainingString).toList()
        }
    }

    fun parse(entry: String): RosterUnit {
        val unitName = unitEntryClassifier.classify(entry)
        if (unitName == "Header" || unitName == "Footer") {
            return RosterUnit(entry, unitName)
        }
        val decorator = unitDecorators[unitName] ?: throw Exception(unitName)
        val attributes = decorator.decorate(entry)
        val unitNumber = unitNumberParser.parse(entry, decorator.armyBookEntry)
        return RosterUnit(
            entry,
            unitName,
            attributes,
            unitNumber.points,
            unitNumber.modelCount,
            unitNumber.entryCount
        )
    }
}