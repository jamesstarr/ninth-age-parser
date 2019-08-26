package org.jim.ninthage.reports

import com.google.common.collect.Ordering
import org.jim.ninthage.models.Tournament
import java.nio.file.Files
import java.nio.file.Path

class ArmyBookReporter {

    val overall: MutableMap<String, Int> = LinkedHashMap()
    val tournaments: MutableList<ArmyBookCount> = ArrayList()

    fun processTournament(tournament:Tournament) {
        processTournament(tournament, overall )
        val tournamentCount:MutableMap<String, Int> = LinkedHashMap()
        processTournament(tournament, tournamentCount)
        tournaments +=
            ArmyBookCount(
                tournament.tournamentConfiguration.name,
                tournamentCount
            )

    }

    fun tournamentToCount(tournament: Tournament):Map<String, Int> {
        val tournamentCount:MutableMap<String, Int> = LinkedHashMap()
        processTournament(tournament, tournamentCount)
        tournamentCount.toSortedMap(Ordering.natural())
        return tournamentCount
    }

    fun processTournament(tournament:Tournament, bookAndCount:MutableMap<String, Int>){
        tournament.roster.forEach { armyBook ->
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

data class ArmyBookCount(val name:String, val counts:Map<String, Int>)

data class ArmyBookReport(
    val overall:Map<String, Int>,
    val tournaments: List<ArmyBookCount>
)