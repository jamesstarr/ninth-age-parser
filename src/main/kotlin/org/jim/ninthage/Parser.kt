package org.jim.ninthage

import org.jim.ninthage.armybook.ArmyBookClassifier
import org.jim.ninthage.data.Tournaments
import org.jim.ninthage.data.TrainingData
import org.jim.ninthage.models.PdFParserConfiguration
import org.jim.ninthage.models.TextFile
import org.jim.ninthage.models.Tournament
import org.jim.ninthage.models.TournamentConfiguration
import org.jim.opennlp.SimpleClassifier
import org.jim.pdf.PdfToText
import org.jim.ninthage.reports.ArmyBookReporter
import org.jim.ninthage.reports.TournamentReportPrinter
import org.jim.ninthage.team.TeamGrouper
import org.jim.ninthage.units.UnitEntrySplitter
import org.jim.utils.ResourceUtils
import org.jim.utils.YamlUtils
import java.lang.Exception
import java.lang.RuntimeException
import java.nio.file.Files


class Parser {
    val pdfToText = PdfToText()
    val listSplitter = ListSplitter.simpleSplitter()
    val armyBookDetector = ArmyBookClassifier.build()
    val teamGrouper = TeamGrouper()
    val unitSplitter = UnitEntrySplitter()

    val tournamentDirectory = App.HomeDirectory.resolve("tournament")

    fun readAllList() {
        SimpleClassifier.debugTraining(
            TrainingData.ArmyBookClassifier,
            ArmyBookClassifier.pattern,
            App.HomeDirectory
                .resolve("training")
                .resolve("army_book")
        )
        val tournamentReportPrinter = TournamentReportPrinter()
        val armyBookReporter = ArmyBookReporter()

        Files.createDirectories(tournamentDirectory)

        ArmyBookWriter.build(App.HomeDirectory.resolve("armyBook")).use { armyBookWriter ->
            Tournaments.ALL.stream()
                .filter{it.isWellFormed}
                .map { preprocess(it) }
                .map { tournament ->
                    try {
                        val armyLists =
                            listSplitter.split(tournament.rawString)
                            .map { armyList ->
                                val armyBook =
                                    armyBookDetector.classify((armyList))
                                unitSplitter.parseArmyList(armyList, armyBook)
                            }.toList()

                        tournament.copy(
                            armyList = armyLists
                        )
                    } catch (ex: Exception) {
                        throw RuntimeException(tournament.name, ex)
                    }
                }
                .map { teamGrouper.groupTeams(it) }
                .forEach { tournament ->
                    tournamentReportPrinter.printTournament(tournament)
                    println("-------------------------------")
                    println(tournament.tournamentConfiguration.name)
                    println("-------------------------------")
                    tournament.armyList!!
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

    fun preprocess(configuration: TournamentConfiguration): Tournament {
        val tournamentString =
            when (configuration.parser) {
                is PdFParserConfiguration ->
                    pdfToText.convert(configuration.fileName, configuration.parser.flags)
                is TextFile ->
                    ResourceUtils.readResourceAsUtf8String(configuration.parser.textFile)
            }
        return Tournament(configuration, tournamentString)
    }
}





