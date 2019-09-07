package org.jim.ninthage.data.armybook.dsl

import org.jim.ninthage.models.ArmyBookEntryOptionSelection

interface Enchantment {
    val label:String
    val points:Int

    fun toSelection():ArmyBookEntryOptionSelection{
        return ArmyBookEntryOptionSelection(label, points)
    }
}