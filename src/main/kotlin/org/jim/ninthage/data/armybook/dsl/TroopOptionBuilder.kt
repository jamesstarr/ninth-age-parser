package org.jim.ninthage.data.armybook.dsl

import org.jim.ninthage.data.armybook.ArcaneCompendiums
import org.jim.ninthage.data.armybook.MainBook
import org.jim.ninthage.models.ArmyBookEntryOption
import org.jim.ninthage.models.ArmyBookEntryOptionSelection

class TroopOptionBuilder(val armyBook: StandardArmyBook) : OptionBuilder(armyBook) {

    fun champion() {
        optionWithDefault("Command", "None") {
            selection("C", 20)
        }
    }


    fun championMusican() {
        optionWithDefault("Command", "None") {
            selection("C", 20)
            selection("M", 20)
            selection("CM", 40)
        }
    }


    fun command() {
        optionX<SelectionBuilder>("Command") {
            selection(MainBook.None, 0) { default() }
            selection("C", 20)
            selection("M", 20)
            selection("S", 20)
            selection("CM", 40)
            selection("CS", 40)
            selection("MS", 20)
            selection("CMS", 60) { implicit() }
        }
    }


    fun StandardArmyBook.bannerEnchantmentOption() {
        result.add(
            ArmyBookEntryOption(
                ArcaneCompendiums.BannerEnchantment,
                ArrayList<ArmyBookEntryOptionSelection>().apply {
                    add(ArmyBookEntryOptionSelection(MainBook.None))
                    addAll(customBannerEnchantments.map {it.toSelection()} )
                    addAll(arcaneCompendium.bannerEnchantments.map { it.toSelection() })
                },
                default = MainBook.None,
                costPerModel = false
            )
        )
    }

    fun unitOption(
        name: String,
        default: String,
        selections: SelectionBuilder.() -> Unit
    ) {
        val sb = SelectionBuilder()
        selections.invoke(sb)
        val selectionList = ArrayList<ArmyBookEntryOptionSelection>()
        selectionList.addAll(sb.build())
        selectionList.add(ArmyBookEntryOptionSelection(default, 0))
        result.add(
            ArmyBookEntryOption(
                name = name,
                selections = selectionList,
                default = "None",
                implicit = null,
                minSelection = 1
            )
        )
    }

}