package org.jim.ninthage.team

import com.google.common.collect.Lists
import org.jim.ninthage.models.ArmyList
import org.jim.ninthage.models.Team
import org.jim.ninthage.models.Tournament
import java.lang.Exception

class TeamGrouper {
    fun groupTeams(tournament: Tournament): Tournament {
        return if (
            tournament.armyList.stream()
                .filter { it.isTeam }
                .findAny().isEmpty
        ) {
            tournament
        } else {
            tournament.copy(
                armyList = tournament.armyList.filter { !it.isTeam },
                teams = teamIter(tournament.armyList).toList()
            )
        }
    }


    fun teamIter(list: List<ArmyList>): Sequence<Team> {
        return sequence {
            var currentList = Lists.newArrayList<ArmyList>()
            var team =
                if (list.first().isTeam) {
                    list.first().raw
                } else {
                    throw Exception("Expect Team Token")
                }

            for (i in 1 until list.size) {
                val armyList = list[i]
                if (armyList.isTeam) {
                    yield(Team(team, currentList))
                    currentList.clear()
                    team = armyList.raw
                } else {
                    currentList.add(armyList)
                }
            }
        }
    }
}

private val ArmyList.isTeam: Boolean
    get() = armyBook == "Team"

