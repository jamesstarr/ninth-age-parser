package org.jim.ninthage

import org.jim.ninthage.data.Tournaments
import org.jim.ninthage.data.TrainingData
import org.jim.ninthage.models.ArmyList
import org.jim.ninthage.models.PdFParserConfiguration
import org.jim.ninthage.models.Tournament
import org.jim.ninthage.models.TournamentConfiguration
import org.jim.ninthage.opennlp.SimpleClassifer
import org.jim.ninthage.opennlp.SimpleSplitter
import org.jim.ninthage.pdf.PdfToText
import org.jim.ninthage.reports.ArmyBookReporter
import org.jim.ninthage.utils.YamlUtils
import java.lang.Exception
import java.lang.RuntimeException



class Parser {
    companion object {
        fun main(args: Array<String>) {
            Parser().readAllList()
        }
    }
    val pdfToText = PdfToText()
    val listFileReader = ListFileReader()

    fun readAllList() {
        val model = SimpleSplitter.train(TrainingData.ListSplitting)
        val listSplitter = SimpleSplitter.build(model)
        val armyBookDetector =
            SimpleClassifer(SimpleClassifer.train(TrainingData.ArmyBookClassifing))

        val armyBookReporter = ArmyBookReporter()


        ArmyBookWriter.build(App.HomeDirectory.resolve("armyBook")).use { armyBookWriter ->
            Tournaments.ALL.takeLast(1).stream()
                .map { preprocess(it) }
                .map { (pathString: String, tournamentString) ->
                    try {
                        Tournament(
                            pathString,
                            listSplitter.split(tournamentString).map { armyList ->
                                val armyBook = armyBookDetector.detectArmyBook((armyList))
                                ArmyList(armyBook, armyList)
                            }.toList()
                        )
                    } catch (ex: Exception) {
                        throw RuntimeException(pathString, ex)
                    }
                }
                .forEach { tournament ->
                    println("-------------------------------")
                    println(tournament.fileName)
                    println("-------------------------------")
                    tournament.armyList
                        .forEach { armyList ->
                            armyBookWriter.write(armyList.armyBook, armyList.raw)
                            println(YamlUtils.YamlObjectMapper.writeValueAsString(armyList))
                        }
                    armyBookReporter.processTournament(tournament)
                }
        }

        println(YamlUtils.YamlObjectMapper.writeValueAsString(armyBookReporter.buildReport()))
    }

    fun preprocess(configuration: TournamentConfiguration): TournamentPathAndString {
        val tournamentString =
            when (configuration.parser) {
                is PdFParserConfiguration ->
                    pdfToText.convert(configuration.fileName, configuration.parser.flags)
            }
        return TournamentPathAndString(configuration.fileName, tournamentString)
    }

    data class TournamentPathAndString(val pathString: String, val tournamentString: String)
}





