package org.jim.ninthage.data.armybook.dsl

import org.jim.ninthage.data.armybook.MainBook
import org.jim.ninthage.models.ArmyBookEntryOptionSelection

class CloseCombatWeaponSelectionBuilder : SelectionBuilder() {


    fun pairedWeapons(points: Int = 0,flags:Flags.()->Unit={}) {
        return selection(MainBook.PairedWeapons, points)
    }


    fun lightLance(points: Int = 0,flags:Flags.()->Unit={}) {
        return selection(MainBook.LightLance, points)
    }


    fun spear(points: Int = 0,flags:Flags.()->Unit={}) {
        return selection(MainBook.Spear, points)
    }

    fun greatWeapon(points: Int = 0,flags:Flags.()->Unit={}) {
        return selection(MainBook.GreatWeapon, points)
    }

    fun lance(points: Int = 0,flags:Flags.()->Unit={}) {
        return selection(MainBook.Lance, points)

    }

    fun halberd(points: Int = 0,flags:Flags.()->Unit={}) {
        return selection(MainBook.Halberd, points)
    }

    fun handWeapon(points: Int = 0,flags:Flags.()->Unit={}) {
        return selection(MainBook.HandWeapon, points)
    }
}