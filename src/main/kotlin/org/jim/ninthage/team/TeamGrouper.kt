package org.jim.ninthage.team

import com.google.common.collect.Lists
import org.jim.ninthage.models.Roster
import org.jim.ninthage.models.Team
import org.jim.ninthage.models.Tournament

class TeamGrouper {
    fun groupTeams(tournament: Tournament): Tournament {
        return if (
            tournament.roster.stream()
                .filter { it.isTeam }
                .findAny().isEmpty
        ) {
            tournament
        } else {
            tournament.copy(
                roster = tournament.roster.filter { !it.isTeam },
                teams = teamIter(tournament.roster).toList()
            )
        }
    }


    fun teamIter(list: List<Roster>): Sequence<Team> {
        return sequence {
            var currentList = Lists.newArrayList<Roster>()
            var team =
                if (list.first().isTeam) {
                    list.first().raw
                } else {
                    throw Exception("Expect Team Token")
                }

            for (i in 1 until list.size) {
                val armyList = list[i]
                if (armyList.isTeam && armyList.raw != team) {
                    yield(
                        Team(
                            team,
                            team.trim().split("\n")[0].removePrefix("Team "),
                            currentList
                        )
                    )
                    currentList.clear()
                    team = armyList.raw
                } else {
                    currentList.add(armyList)
                }
            }
        }
    }
}

private val Roster.isTeam: Boolean
    get() = armyBook == "Team"

