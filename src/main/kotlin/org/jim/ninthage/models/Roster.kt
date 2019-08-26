package org.jim.ninthage.models

data class Roster(
    val raw: String,
    val armyBook: String,
    val rosterUnits: List<RosterUnit> = ArrayList()
)