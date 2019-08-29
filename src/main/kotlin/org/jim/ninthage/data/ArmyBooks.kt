package org.jim.ninthage.data

import org.jim.ninthage.models.ArmyBook


object ArmyBooks {
    val EoS = org.jim.ninthage.data.armybook.EmpireOfSonnstahl.Version2_0

    val All = listOf(EoS)

    fun get(armyBookName: String): ArmyBook {
        return All.find { it.name == armyBookName }
            ?: throw RuntimeException(armyBookName)
    }
}
