package org.jim.ninthage

import org.jim.ninthage.armybook.ArmyBookClassifier
import org.jim.ninthage.data.Tournaments
import org.jim.ninthage.data.TrainingData
import org.jim.ninthage.models.PdFParserConfiguration
import org.jim.ninthage.models.Roster
import org.jim.ninthage.models.TextFile
import org.jim.ninthage.models.Tournament
import org.jim.ninthage.models.TournamentConfiguration
import org.jim.ninthage.reports.ArmyBookReporter
import org.jim.ninthage.reports.ArmyBookWriterPool
import org.jim.ninthage.reports.TournamentReportPrinter
import org.jim.ninthage.roster.RosterParser
import org.jim.ninthage.roster.RosterSplitter
import org.jim.ninthage.team.TeamGrouper
import org.jim.opennlp.classifier.SimpleClassifier
import org.jim.pdf.PdfToText
import org.jim.utils.ResourceUtils
import java.nio.file.Files
import kotlin.streams.toList


class Parser {
    val pdfToText = PdfToText()
    val listSplitter = RosterSplitter.simpleSplitter()

    val teamGrouper = TeamGrouper()
    val unitSplitter = RosterParser.build()

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

        val data = ArmyBookWriterPool.build(App.HomeDirectory.resolve("armyBook")).use { armyBookWriter ->
            Tournaments.ALL.stream()
                .filter { it.isWellFormed }
                .map { preprocess(it) }
                .map { tournament ->
                    try {
                        val armyLists =
                            listSplitter.split(tournament.rawString)
                                .map { armyList ->
                                    unitSplitter.parseRoster(armyList)
                                }.toList()

                        tournament.copy(
                            roster = armyLists
                        )
                    } catch (ex: Exception) {
                        throw RuntimeException(tournament.name, ex)
                    }
                }
                .map { teamGrouper.groupTeams(it) }
                .map { tournament ->
                    tournamentReportPrinter.printTournament(tournament)
                    println("-------------------------------")
                    println(tournament.tournamentConfiguration.name)
                    println("-------------------------------")
                    tournament.roster
                        .forEach { armyList ->
                            armyBookWriter.write(armyList.armyBook, armyList.raw)
                            //println(YamlUtils.YamlObjectMapper.writeValueAsString(armyList))
                        }
                    Files.writeString(
                        tournamentDirectory.resolve("./${tournament.tournamentConfiguration.name}.txt"),
                        tournament.rawString,
                        Charsets.UTF_8
                    )
                    armyBookReporter.processTournament(tournament)
                    tournament
                }
        }

        val counts = data
            .flatMap { it.roster.stream() }
            .filter { it.armyBook == "EoS" }
            .map { roster ->
                val cowboys = countCowboys(roster)
                val utilities = countUtilityCharacters(roster)
                if(utilities > 3) {
                    println(roster.raw)
                    println()
                }
                Pair(cowboys, utilities)
            }.sorted({(a, b),(x,y)-> Integer.compare(a, x) * 2 + Integer.compare(b,y)}).toList()


        println("Counting ${counts.size}")
        counts.forEach{ println(it.first.toString()+"  "+it.second)}

        //println(YamlUtils.YamlObjectMapper.writeValueAsString(armyBookReporter.buildReport()))


    }

    fun countCowboys(roster: Roster): Int {
        return roster.units
            .filter { unit ->
                if(unit.label == "Knight Commander"){
                    true
                } else if (unit.label == "Prelate" && unit.option("Mount") == "Altar of Battle") {
                    true
                } else if (unit.label == "Marshall" && unit.option("Mount") == "Great Griffon"){
                    true
                } else if(unit.label == "Inquisitor"){
                    true
                } else {
                    false
                }
            }
            .count()
    }

    fun countUtilityCharacters(roster: Roster): Int {
        return roster.units
            .filter { unit ->
                if (unit.label == "Prelate" && !(unit.option("Mount") == "Altar of Battle")) {
                    true
                } else if (unit.label == "Marshal" && !(unit.option("Mount") == "Great Griffon")){
                    true
                } else {
                    false
                }
            }
            .count()
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





