package org.jim.ninthage

import org.jim.ninthage.utils.YamlUtils


fun main(args: Array<String>) {
    Parser().readAllList()
}

class Parser {


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

    val BuckEye2019 = "/org/jim/ninthage/cookedSingleList/2.2 T56 BUCKEYE 2019 LISTS.txt"
    val Tec2019 = "/org/jim/ninthage/cookedSingleList/2.1.2 TEC 2019 lists.txt"

    val SinglesList = listOf(
        Tec2019,
        BuckEye2019
    )

    val listFileReader = ListFileReader()

    fun readAllList() {
        val model = SimpleSplitter.train(TrainingData.ListSplitting)
        val listSplitter = SimpleSplitter.build(model)
        val armyBookDetector = SimpleClassifer(SimpleClassifer.train(TrainingData.ArmyBookClassifing))

        ArmyBookWriter.build(App.HomeDirectory.resolve("armyBook")).use { armyBookWriter ->
            SinglesList.forEach { tourney ->
                listSplitter.splitTeamsFormResource(tourney).forEach { armyList ->
                    val armyBook = armyBookDetector.detectArmyBook((armyList))
                    armyBookWriter.write(armyBook, armyList)
                    println(YamlUtils.YamlObjectMapper.writeValueAsString(ArmyList(armyBook, armyList)))
                }
            }
        }
    }
}

data class ArmyList(val armyBook:String, val raw:String)