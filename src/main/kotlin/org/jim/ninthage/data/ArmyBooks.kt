package org.jim.ninthage.data

import org.jim.ninthage.models.ArmyBook
import org.jim.ninthage.models.ArmyBookEntry
import org.jim.ninthage.models.ArmyBookEntryOption
import org.jim.ninthage.models.ArmyBookEntryOptionSelection
import java.lang.RuntimeException


object ArmyBooks {
    val EoS = org.jim.ninthage.data.armybook.EoS.Version2_2

    val All = listOf(EoS)

    fun get(armyBookName: String): ArmyBook {
        return All.find { it.name == armyBookName }
            ?: throw RuntimeException(armyBookName)
    }
}
