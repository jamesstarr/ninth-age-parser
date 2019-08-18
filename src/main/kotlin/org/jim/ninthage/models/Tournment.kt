package org.jim.ninthage.models


data class Tournament(
    val tournamentConfiguration: TournamentConfiguration,
    val rawString:String,
    val armyList: List<ArmyList>
)
