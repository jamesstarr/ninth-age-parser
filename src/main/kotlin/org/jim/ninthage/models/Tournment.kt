package org.jim.ninthage.models

import java.util.*
import kotlin.collections.ArrayList


data class Tournament(
    val tournamentConfiguration: TournamentConfiguration,
    val rawString:String,
    val armyList: List<ArmyList> = ArrayList(),
    val teams:List<Team> = ArrayList()
    ) {
    val name:String
        get() = tournamentConfiguration.name
    val isTeam:Boolean
        get() = !teams.isEmpty()
}
