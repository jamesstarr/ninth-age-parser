package org.jim.ninthage.parser.org.jim.ninthage

import org.jim.ninthage.App
import org.jim.ninthage.ArmyBookDetector
import org.jim.ninthage.ArmyBookWriter
import org.jim.ninthage.ListFileReader
import org.jim.ninthage.TeamDetector
import org.jim.ninthage.utils.YamlUtils
import java.nio.file.Paths


fun main(args: Array<String>) {
    Parser().readAllList()
}

class Parser {
    val TeamTrain = listOf(
        "/org/jim/ninthage/training/team/ETC 2019 Master Lists v1.0.txt",
        "/org/jim/ninthage/training/team/ETC_2018-T9A_v.1.1.txt",
        "/org/jim/ninthage/training/team/2.2 T55 Listas Torneo de 4 2-1 Aranjuez Team Event.txt"
    )

    val ListTrain = listOf(
        "/org/jim/ninthage/training/list/2.0.9 Warsaw Team Tournment February.txt",
        "/org/jim/ninthage/training/list/2.2 T56 BUCKEYE 2019 LISTS.txt",
        "/org/jim/ninthage/training/list/ETC 2019 Master Lists v1.0.txt"
    )

    val ArmyBookTrain = listOf(
        "/org/jim/ninthage/cookedSingleList/2.2 T56 BUCKEYE 2019 LISTS.txt",
        "/org/jim/ninthage/training/armybook/ETC 2019 Master Lists v1.0.txt"
    )


    val Etc2018 = "/org/jim/ninthage/rawTeamList/ETC 2018 - T9A v.1.1.txt"
    val Etc2019 = "/org/jim/ninthage/rawTeamList/ETC 2019 Master Lists v1.0.txt"
    //this formatting is problematic
    val AranjuezTeam = "/org/jim/ninthage/rawTeamList/2.2 T55 Listas Torneo de 4 2-1 Aranjuez Team Event.txt"
    val WarsawTeamTourneyFeb = "/org/jim/ninthage/rawTeamList/2.0.9 Warsaw Team Tournment February.txt"

    val TeamList = listOf(
        Etc2018,
        Etc2019,
        AranjuezTeam,
        WarsawTeamTourneyFeb
    )

    val BuckEye2019 = "/org/jim/ninthage/rawSingleList/2.2 T56 BUCKEYE 2019 LISTS.txt"

    val SinglesList = listOf(
        BuckEye2019
    )

    val listFileReader = ListFileReader()

    fun readAllList() {
        val model = TeamDetector.train(ListTrain)
        val teamDetector = TeamDetector.build(model)
        val armyBookDetector = ArmyBookDetector(ArmyBookDetector.train(ArmyBookTrain))
        ArmyBookWriter.build(App.HomeDirectory.resolve("armyBook")).use { armyBookWriter ->
            SinglesList.forEach { tourney ->
                var count = 0
                teamDetector.splitTeamsFormResource(tourney).forEach {
                    val armyBook = armyBookDetector.detectArmyBook((it))
                    armyBookWriter.write(armyBook, it)
                    println(YamlUtils.YamlObjectMapper.writeValueAsString(ArmyList(armyBook, it)))
                }
            }
        }
    }
}

data class ArmyList(val armyBook:String, val raw:String)