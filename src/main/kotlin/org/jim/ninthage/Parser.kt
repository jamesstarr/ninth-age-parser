package org.jim.ninthage

import org.jim.ninthage.data.Tournament
import org.jim.ninthage.data.TrainingData
import org.jim.ninthage.models.PdFParserConfiguration
import org.jim.ninthage.models.TournamentConfiguration
import org.jim.ninthage.pdf.PdfToText
import org.jim.ninthage.utils.YamlUtils
import java.lang.Exception
import java.lang.RuntimeException


fun main(args: Array<String>) {
    Parser().readAllList()
}

class Parser {

    val pdfToText = PdfToText()
    val listFileReader = ListFileReader()

    fun readAllList() {
        val model = SimpleSplitter.train(TrainingData.ListSplitting)
        val listSplitter = SimpleSplitter.build(model)
        val armyBookDetector = SimpleClassifer(SimpleClassifer.train(TrainingData.ArmyBookClassifing))

        ArmyBookWriter.build(App.HomeDirectory.resolve("armyBook")).use { armyBookWriter ->
            Tournament.ALL.stream()
                .map { preprocess(it) }
                .map { (pathString:String, tournamentString) ->
                    try {
                    Tournment(
                        pathString,
                        listSplitter.split(tournamentString).map { armyList ->
                            val armyBook = armyBookDetector.detectArmyBook((armyList))
                            ArmyList(armyBook, armyList)
                        }.toList()
                    ) } catch (ex:Exception) {
                        throw RuntimeException(pathString,ex)
                    }
                }
                .forEach { tournment ->
                    println("-------------------------------")
                    println(tournment.fileName)
                    println("-------------------------------")
                    tournment.armyList
                        .forEach { armyList ->
                            armyBookWriter.write(armyList.armyBook, armyList.raw)
                            println(YamlUtils.YamlObjectMapper.writeValueAsString(armyList))
                        }
                }
        }
    }

    fun preprocess(configuration: TournamentConfiguration):TournamentPathAndString {
        val tournmentString = when (configuration.parser) {
            is PdFParserConfiguration -> pdfToText.convert(configuration.fileName, configuration.parser.flags)
        }
        return TournamentPathAndString(configuration.fileName, tournmentString)
    }

    data class TournamentPathAndString(val pathString:String, val tournamentString:String)
}




data class Tournment(val fileName: String, val armyList: List<ArmyList>)

data class ArmyList(val armyBook: String, val raw: String)