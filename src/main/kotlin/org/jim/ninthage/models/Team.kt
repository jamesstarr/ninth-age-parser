package org.jim.ninthage.models

data class Team(
    val raw:String,
    val name:String,
    val lists:List<ArmyList>
)
