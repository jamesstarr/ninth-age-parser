package org.jim.ninthage.data.armybook.dsl

import org.jim.ninthage.models.ArmyBookEntryOptionSelection


open class SelectionBuilder {
    val result = ArrayList<Flags>()

    fun selection(name: String, points: Int = 0, flags:Flags.()->Unit={}) {
        val f = Flags(name, points)
        flags.invoke(f)
        result.add(f)
    }




    fun build():MutableList<ArmyBookEntryOptionSelection> =
        result
            .map{ ArmyBookEntryOptionSelection(it.label, it.points)}
            .toMutableList()
}

class Flags(
    val label:String, val points:Int
) {
    var isDefault = false
    var isImplicit = false

    fun default() {
        isDefault = true
    }

    fun implicit() {
        isImplicit = true
    }
}