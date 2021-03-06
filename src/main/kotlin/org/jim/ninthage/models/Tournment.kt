package org.jim.ninthage.models


data class Tournament(
    val tournamentConfiguration: TournamentConfiguration,
    val rawString: String,
    val roster: List<Roster> = ArrayList(),
    val teams: List<Team> = ArrayList()
) {
    val name: String
        get() = tournamentConfiguration.name
    val isTeam: Boolean
        get() = !teams.isEmpty()
}
