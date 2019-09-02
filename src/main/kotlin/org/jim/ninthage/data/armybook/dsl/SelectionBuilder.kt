package org.jim.ninthage.data.armybook.dsl

import org.jim.ninthage.models.ArmyBookEntryOptionSelection


open class SelectionBuilder {
    val result = ArrayList<ArmyBookEntryOptionSelection>()



    fun selection(name: String, points: Int = 0) {
        result.add(ArmyBookEntryOptionSelection(name, points))
    }


    fun build():List<ArmyBookEntryOptionSelection> = result.toList()
}