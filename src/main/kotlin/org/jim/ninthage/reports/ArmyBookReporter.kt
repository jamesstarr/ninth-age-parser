package org.jim.ninthage.reports

import org.jim.ninthage.models.Tournament
import java.io.Closeable

class ArmyBookReporter {

    val overall: MutableMap<String, Int> = HashMap()
    val tournaments: MutableList<TournamentArmyBookCount> = ArrayList()

    fun processTournament(tournament:Tournament) {
        processTournament(tournament, overall )
        val tournamentCount:MutableMap<String, Int> = HashMap()
        processTournament(tournament, tournamentCount)
        tournaments +=
            TournamentArmyBookCount(
                tournament.fileName,
                tournamentCount
            )

    }

    private fun processTournament(tournament:Tournament, bookAndCount:MutableMap<String, Int>){
        tournament.armyList.forEach { armyBook ->
            val count = bookAndCount.getOrDefault(armyBook.armyBook, 0)
            bookAndCount[armyBook.armyBook] = count + 1
        }
    }

    fun buildReport():ArmyBookReport {
        return ArmyBookReport(
            overall.toMap(),
            tournaments.toList()
        )
    }

}

data class TournamentArmyBookCount(val name:String ,val counts:Map<String, Int>)

data class ArmyBookReport(
    val overall:Map<String, Int>,
    val tournaments: List<TournamentArmyBookCount>
)