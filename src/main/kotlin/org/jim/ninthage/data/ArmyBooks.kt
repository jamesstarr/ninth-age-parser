package org.jim.ninthage.data

import org.jim.ninthage.data.armybook.BeastHeard2_0
import org.jim.ninthage.models.ArmyBook


object ArmyBooks {

    val BH = BeastHeard2_0()
    val EoS = org.jim.ninthage.data.armybook.EmpireOfSonnstahl.Version2_0

    val All = listOf(EoS, BH)

    fun get(armyBookName: String): ArmyBook {
        return All.find { it.shortLabel == armyBookName }
            ?: throw RuntimeException(armyBookName)
    }
}
