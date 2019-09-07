package org.jim.ninthage.data.armybook.dsl

import org.jim.ninthage.models.ArmyBookEntryOptionSelection

interface Mount {
    val label: String
    operator fun invoke(points: Int): ArmyBookEntryOptionSelection {
        return ArmyBookEntryOptionSelection(label, points)
    }

}