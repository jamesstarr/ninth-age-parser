package org.jim.ninthage.units

import org.jim.ninthage.data.TrainingData
import org.jim.ninthage.models.ArmyBook
import org.jim.ninthage.models.RosterUnit
import org.jim.utils.ResourceUtils


class UnitEntryParser(
    private val armyBook: ArmyBook,
    private val unitEntryClassifier: UnitEntryClassifier,
    private val unitDecorators: Map<String, OptionParser>
) {


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
            val trainingString = ResourceUtils.readResourceAsUtf8String(trainData)
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
        return RosterUnit(entry, unitName, attributes)
    }
}