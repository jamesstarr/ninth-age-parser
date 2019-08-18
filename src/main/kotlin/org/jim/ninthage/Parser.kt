package org.jim.ninthage

import org.jim.ninthage.armybook.ArmyBookClassifier
import org.jim.ninthage.data.Tournaments
import org.jim.ninthage.data.TrainingData
import org.jim.ninthage.models.ArmyList
import org.jim.ninthage.models.PdFParserConfiguration
import org.jim.ninthage.models.TextFile
import org.jim.ninthage.models.Tournament
import org.jim.ninthage.models.TournamentConfiguration
import org.jim.opennlp.SimpleClassifier
import org.jim.pdf.PdfToText
import org.jim.ninthage.reports.ArmyBookReporter
import org.jim.utils.ResourceUtils
import org.jim.utils.YamlUtils
import java.lang.Exception
import java.lang.RuntimeException
import java.nio.file.Files


class Parser {
    val pdfToText = PdfToText()
    val listSplitter = ListSplitter.simpleSplitter()
    val armyBookDetector = ArmyBookClassifier.build()

    val tournamentDirectory = App.HomeDirectory.resolve("tournament")

    fun readAllList() {
        SimpleClassifier.debugTraining(
            TrainingData.ArmyBookClassifier,
            App.HomeDirectory
                .resolve("training")
                .resolve("army_book")
        )

        val armyBookReporter = ArmyBookReporter()

        Files.createDirectories(tournamentDirectory)

        ArmyBookWriter.build(App.HomeDirectory.resolve("armyBook")).use { armyBookWriter ->
            Tournaments.ALL.stream()
                .filter{it.isWellFormed}
                .map { preprocess(it) }
                .map { (config, tournamentString) ->
                    try {
                        Tournament(
                            config,
                            tournamentString,
                            listSplitter.split(tournamentString).map { armyList ->
                                val armyBook = armyBookDetector.detectArmyBook((armyList))
                                ArmyList(armyBook, armyList)
                            }.toList()
                        )
                    } catch (ex: Exception) {
                        throw RuntimeException(config.name, ex)
                    }
                }
                .forEach { tournament ->
                    println("-------------------------------")
                    println(tournament.tournamentConfiguration.name)
                    println("-------------------------------")
                    tournament.armyList
                        .forEach { armyList ->
                            armyBookWriter.write(armyList.armyBook, armyList.raw)
                            println(YamlUtils.YamlObjectMapper.writeValueAsString(armyList))
                        }
                    Files.writeString(
                        tournamentDirectory.resolve("./${tournament.tournamentConfiguration.name}.txt"),
                        tournament.rawString,
                        Charsets.UTF_8
                    )
                    armyBookReporter.processTournament(tournament)
                }
        }

        println(YamlUtils.YamlObjectMapper.writeValueAsString(armyBookReporter.buildReport()))
    }

    fun preprocess(configuration: TournamentConfiguration): TournamentConfigurationAndString {
        val tournamentString =
            when (configuration.parser) {
                is PdFParserConfiguration ->
                    pdfToText.convert(configuration.fileName, configuration.parser.flags)
                is TextFile ->
                    ResourceUtils.readResourceAsUtf8String(configuration.parser.textFile)
            }
        return TournamentConfigurationAndString(configuration, tournamentString)
    }

    data class TournamentConfigurationAndString(val configuration: TournamentConfiguration, val tournamentString: String)
}





