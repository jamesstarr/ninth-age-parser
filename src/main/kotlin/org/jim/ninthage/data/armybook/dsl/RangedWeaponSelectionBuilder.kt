package org.jim.ninthage.data.armybook.dsl

import org.jim.ninthage.data.armybook.MainBook

class RangedWeaponSelectionBuilder :SelectionBuilder(){

    fun throwingWeapon(points:Int=0) {
        selection(MainBook.ThrowingWeapon)
    }
}
