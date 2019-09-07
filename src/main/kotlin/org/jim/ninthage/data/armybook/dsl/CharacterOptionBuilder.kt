package org.jim.ninthage.data.armybook.dsl

import org.jim.ninthage.data.armybook.ArcaneCompendiums
import org.jim.ninthage.data.armybook.MainBook
import org.jim.ninthage.models.ArmyBookEntryOption
import org.jim.ninthage.models.ArmyBookEntryOptionSelection

class CharacterOptionBuilder(
    val armyBook: StandardArmyBook
) : OptionBuilder(armyBook) {

    fun general(points: Int = 0) {
        implicitOption("General", points)
    }


    fun battleStandardBearer(points: Int = 50) {
        implicitOption(MainBook.BattleStandardBear, points, "Battle Standard Bear")
        bannerEnchantmentBSBOption()
    }


    fun bannerEnchantmentBSBOption() {
        val banners = ArrayList<Enchantment>().apply {
            addAll(armyBook.customBannerEnchantments)
            addAll(armyBook.arcaneCompendium.bannerEnchantments)
        }.map { it.toSelection() }.toList()
        result.add(
            ArmyBookEntryOption(
                ArcaneCompendiums.BannerEnchantment,
                banners,
                null,
                null,
                0,
                2
            )
        )
    }

    fun StandardArmyBook.bannerEnchantmentBSBOption(): ArmyBookEntryOption {
        val banners = ArrayList<Enchantment>().apply {
            addAll(customBannerEnchantments)
            addAll(arcaneCompendium.bannerEnchantments)
        }.map { it.toSelection() }.toList()
        return ArmyBookEntryOption(
            ArcaneCompendiums.BannerEnchantment,
            banners,
            null,
            null,
            0,
            2
        )
    }


    fun wizard() {
        optionWithDefault(
            ArcaneCompendiums.WizardLevel,
            ArcaneCompendiums.Apprentice
        ) {
            listOf(
                selection(ArcaneCompendiums.Adept, 75),
                selection(ArcaneCompendiums.Master, 225)
            )
        }
    }

    fun mount(vararg selection: ArmyBookEntryOptionSelection) {
        val selectionList = ArrayList<ArmyBookEntryOptionSelection>()
        selectionList.addAll(selection)
        selectionList.add(ArmyBookEntryOptionSelection("None", 0))
        result.add(
            ArmyBookEntryOption(
                name = "Mount",
                selections = selectionList,
                default = "None",
                implicit = null,
                minSelection = 1
            )
        )
    }
}