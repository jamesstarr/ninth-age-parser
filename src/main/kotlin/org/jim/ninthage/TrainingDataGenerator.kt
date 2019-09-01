package org.jim.ninthage

import org.jim.ninthage.data.armybook.ArcaneCompendiums
import org.jim.ninthage.data.armybook.Enchantment
import org.jim.ninthage.data.armybook.EmpireOfSonnstahl
import org.jim.ninthage.data.armybook.MainBook
import org.jim.ninthage.models.ArmyBook
import org.jim.ninthage.models.ArmyBookEntry
import java.nio.file.Files


val sb = StringBuilder()
fun main() {

    Files.createDirectories(
        App.HomeDirectory
            .resolve("training")
    )
    val artificerEntry = EmpireOfSonnstahl.Version2_0.entry("Artificer")
    UnitCreator(
        EmpireOfSonnstahl.Version2_0,
        artificerEntry,
        listOf(ArcaneCompendiums.Artifact, MainBook.RangedWeapon),
        listOf<Enchantment>(
            ArcaneCompendiums.Artefacts.BookOfArcaneMastery,
            ArcaneCompendiums.Artefacts.EssenceOfAFreeMind,
            ArcaneCompendiums.Artefacts.MagicalHeirloom,
            EmpireOfSonnstahl.Artefacts.ExemplarsFlame,
            EmpireOfSonnstahl.Artefacts.KaradonsCourser
        ).map { it.label }
    ).createUnit()
    createMarshal()
}

fun createMarshal(){

    val marshalEntry = EmpireOfSonnstahl.Version2_0.entry("Marshal")
    UnitCreator(
        EmpireOfSonnstahl.Version2_0,
        marshalEntry,
        listOf(ArcaneCompendiums.Artifact, MainBook.Mount),
        listOf(
            ArcaneCompendiums.Artefacts.BookOfArcaneMastery.label,
            ArcaneCompendiums.Artefacts.EssenceOfAFreeMind.label,
            ArcaneCompendiums.Artefacts.MagicalHeirloom.label,
            EmpireOfSonnstahl.Artefacts.ExemplarsFlame.label,
            EmpireOfSonnstahl.Mounts.Dragon.label
        ),
        "ArtifactsMounts"
    ).createUnit()

    UnitCreator(
        EmpireOfSonnstahl.Version2_0,
        marshalEntry,
        listOf(ArcaneCompendiums.ArmourEnchantment, MainBook.CloseCombatWeapon),
        listOf<Enchantment>(
            ArcaneCompendiums.Artefacts.BookOfArcaneMastery,
            ArcaneCompendiums.Artefacts.EssenceOfAFreeMind,
            ArcaneCompendiums.Artefacts.MagicalHeirloom,
            EmpireOfSonnstahl.Artefacts.ExemplarsFlame
        ).map { it.label },
        "ArmourCloseCombat"
    ).createUnit()
    UnitCreator(
        EmpireOfSonnstahl.Version2_0,
        marshalEntry,
        listOf(
            ArcaneCompendiums.ShieldEnchantment,
            MainBook.Shield,
            ArcaneCompendiums.Artifact
        ),
        listOf("None",
            ArcaneCompendiums.Artefacts.BookOfArcaneMastery.label,
            ArcaneCompendiums.Artefacts.EssenceOfAFreeMind.label,
            ArcaneCompendiums.Artefacts.MagicalHeirloom.label,
            EmpireOfSonnstahl.Artefacts.ExemplarsFlame.label
        ),
        "ShieldArtifact"
    ).createUnit()
    UnitCreator(
        EmpireOfSonnstahl.Version2_0,
        marshalEntry,
        listOf(
            ArcaneCompendiums.BannerEnchantment,
            MainBook.BattleStandardBear,
            MainBook.Shield
        ),
        listOf("None"
        ),
        "BSB"
    ).createUnit()
}

class UnitCreator(
    val armyBook: ArmyBook,
    val armyBookEntry: ArmyBookEntry,
    val options: List<String>,
    val excludeSelections: List<String>,
    val uid:String = ""
) {
    val sb = StringBuilder()

    fun createUnit() {
        createEntry(
            armyBookEntry,
            0,
            "<Unit name=\"${armyBookEntry.label} ",
            armyBookEntry.label
        )
        Files.writeString(
            App.HomeDirectory
                .resolve("training")
                .resolve(
                    "${armyBook.shortLabel}.${armyBookEntry.label}${uid}.generated.txt"
                ),
            sb.toString()
        )
    }

    fun createEntry(
        armyBookEntry: ArmyBookEntry,
        index: Int,
        tag: String,
        value: String
    ) {
        if (index == armyBookEntry.options.size) {
            sb.append(tag + "\n>" + value + "\n")
            return
        }
        val option = armyBookEntry.options[index]
        if (options.contains(option.name)) {
            for (s in option.selections) {
                if(excludeSelections.contains(s.name)){
                    continue
                }
                if (option.default == s.name) {
                    createEntry(armyBookEntry, index + 1, tag, value)
                } else {
                    createEntry(
                        armyBookEntry,
                        index + 1,
                        tag + "\n\t${option.name}=\"${s.name}\"",
                        value + ", ${s.name}"
                    )
                }
            }
        } else {
            createEntry(
                armyBookEntry,
                index + 1,
                tag,
                value
            )
        }
    }
}


