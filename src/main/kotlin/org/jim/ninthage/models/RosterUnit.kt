package org.jim.ninthage.models

data class RosterUnit(
    val raw: String,
    val name: String,
    val rosterUnitOptions: List<RosterUnitOption> = listOf()
)