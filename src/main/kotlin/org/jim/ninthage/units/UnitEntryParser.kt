package org.jim.ninthage.units

import org.jim.ninthage.App
import org.jim.ninthage.data.TrainingData
import org.jim.ninthage.models.ArmyBook
import org.jim.ninthage.models.RosterUnit
import org.jim.opennlp.classifier.SimpleClassifierCached
import org.jim.utils.ResourceUtils


class UnitEntryParser(
    private val armyBook: ArmyBook,
    private val unitEntryClassifier: UnitEntryClassifier,
    private val unitDecorators: Map<String, OptionParser>
) {
    val unitNumberParser = UnitNumberParser()

    companion object {
        val simpleClassifierCached = SimpleClassifierCached(
            App.HomeDirectory.resolve("cached").resolve("unitEntry")
        )
        val unitEntryClassiferBuilder = UnitEntryClassiferBuilder(simpleClassifierCached)
        fun build(armyBook: ArmyBook): UnitEntryParser {
            val tokens = unitTokens(armyBook)

            val unitEntryClassifier =
                unitEntryClassiferBuilder.build(listOf(armyBook.shortLabel),tokens)
            val unitEntryToOptionParsers =
                armyBook.entries.map { armyBookEntry ->
                    println("Army Book Entry ${armyBook.shortLabel} ${armyBookEntry.label}")
                    armyBookEntry.options
                    Pair<String, OptionParser>(
                        armyBookEntry.label,
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
            val trainData = TrainingData.UnitClassifier[armyBook.shortLabel]!!
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