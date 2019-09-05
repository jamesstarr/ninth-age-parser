package org.jim.ninthage.units

import org.jim.ninthage.App
import org.jim.ninthage.models.ArmyBook
import org.jim.ninthage.models.ArmyBookEntry
import org.jim.ninthage.models.ArmyBookEntryOption
import org.jim.ninthage.models.RosterUnitOption
import org.jim.opennlp.SequenceObjectStream
import org.jim.opennlp.classifier.SimpleClassifier
import org.jim.opennlp.classifier.SimpleClassifierCached
import org.jim.opennlp.classifier.SimpleToken
import java.util.stream.Stream
import kotlin.streams.asStream
import kotlin.streams.toList


class OptionParser(
    private val armyBook: ArmyBook,
    val armyBookEntry: ArmyBookEntry,
    private val simpleAttributeClassifier: List<UnitOptionParser>
) {

    companion object {
        fun build(
            armyBook: ArmyBook,
            armyBookEntry: ArmyBookEntry,
            tokens: List<UnitToken>
        ): OptionParser {
            val optionClassifier =
                armyBookEntry.options.map { option ->
                    println("Option Parser ${armyBook.shortLabel} ${armyBookEntry.label} ${option.name}")
                    UnitOptionParser.build(
                        armyBook,
                        armyBookEntry,
                        option,
                        tokens
                    )
                }

            return OptionParser(armyBook, armyBookEntry, optionClassifier)
        }
    }

    fun decorate(value: String): List<RosterUnitOption> {
        return simpleAttributeClassifier.stream()
            .flatMap { it.attribute(value) }.toList()
    }
}


interface UnitOptionParser {

    companion object {

        val simpleClassifierCached = SimpleClassifierCached(
            App.HomeDirectory.resolve("cached").resolve("unitOption")
        )

        fun build(
            armyBook: ArmyBook,
            armyBookEntry: ArmyBookEntry,
            armyBookEntryOption: ArmyBookEntryOption,
            tokens: List<UnitToken>
        ): UnitOptionParser {
            return if (armyBookEntryOption.isSimple) {
                buildSimple(
                    armyBook,
                    armyBookEntry,
                    armyBookEntryOption,
                    tokens
                )
            } else {
                buildComplex(
                    armyBook,
                    armyBookEntry,
                    armyBookEntryOption,
                    tokens
                )
            }
        }

        private fun buildSimple(
            armyBook: ArmyBook,
            armyBookEntry: ArmyBookEntry,
            armyBookEntryOption: ArmyBookEntryOption,
            allTokens: List<UnitToken>
        ): UnitOptionParser {
            val tokens =
                allTokens
                    .stream()
                    .filter { it.name == armyBookEntry.label }
                    .map {
                        SimpleToken(
                            it.simpleOptions[armyBookEntryOption.name] ?: armyBookEntryOption.defaultValue,
                            it.rawBody
                        )
                    }.toList()
            if (tokens.isEmpty() || tokens.map { it.target }.toSet().size == 1) {
                return object : UnitOptionParser {
                    override fun attribute(value: String): Stream<RosterUnitOption> {
                        return sequenceOf(
                            RosterUnitOption(
                                armyBookEntryOption.name,
                                listOf(armyBookEntryOption.defaultValue)
                            )
                        ).asStream()
                    }

                }
            }
            return SimpleUnitOptionParser(
                armyBook,
                armyBookEntry,
                armyBookEntryOption,
                simpleClassifierCached.build(
                    listOf(armyBook.shortLabel, armyBookEntry.label, armyBookEntryOption.name),
                    SequenceObjectStream(tokens.asSequence())
                )
            )
        }

        private fun buildComplex(
            armyBook: ArmyBook,
            armyBookEntry: ArmyBookEntry,
            armyBookEntryOption: ArmyBookEntryOption,
            tokens: List<UnitToken>
        ): UnitOptionParser {
            val sAndC= armyBookEntryOption.selections.map { s->
                val tokenForOption =
                    tokens.map {
                        val tokenValue =
                            if(it.complexOptions.containsEntry(
                                armyBookEntryOption.name,
                                s.name
                            )){
                                "Found"
                            }else {
                                "Missing"
                            }
                        SimpleToken(tokenValue, it.rawBody)
                    }.asSequence()
                if(tokenForOption.map { it.target }.toSet().size != 2) {
                    null
                }else {
                    val classifier =
                        simpleClassifierCached.build(
                            listOf(armyBook.shortLabel, armyBookEntry.label, armyBookEntryOption.name, s.name),
                            SequenceObjectStream(tokenForOption)
                        )
                    Pair(s.name, classifier)
                }
            }.filterNotNull()
            return ComplexUnitOptionParser(armyBook,armyBookEntry,armyBookEntryOption,sAndC)
        }
    }

    fun attribute(value: String): Stream<RosterUnitOption>
}

class SimpleUnitOptionParser(
    private val armyBook: ArmyBook,
    private val armyBookEntry: ArmyBookEntry,
    private val armyBookEntryOption: ArmyBookEntryOption,
    private val optionClassifier: SimpleClassifier
) : UnitOptionParser {
    override fun attribute(value: String): Stream<RosterUnitOption> {
        val attribute = optionClassifier.classify(value)
        return sequenceOf(
            RosterUnitOption(
                armyBookEntryOption.name,
                listOf(attribute)
            )
        ).asStream()
    }
}

class ComplexUnitOptionParser(
    private val armyBook: ArmyBook,
    private val armyBookEntry: ArmyBookEntry,
    private val armyBookEntryOption: ArmyBookEntryOption,
    private val optionClassifiers: List<Pair<String,SimpleClassifier>>
) : UnitOptionParser {
    override fun attribute(value: String): Stream<RosterUnitOption> {
        val attribute =
            optionClassifiers.flatMap {(selection, classifer)->
                sequence{
                    if(classifer.classify(value) == "Found")
                        yield(selection)
                }.asIterable()
            }
        return sequenceOf(RosterUnitOption(armyBookEntryOption.name, attribute)).asStream()
    }
}