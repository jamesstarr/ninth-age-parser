package org.jim.ninthage.models

data class ArmyList(
    val raw: String,
    val armyBook: String,
    val unitEntries: List<UnitEntry> = ArrayList()
)