package org.jim.ninthage

import org.jim.ninthage.data.armybook.ArcaneCompendiums
import org.jim.ninthage.data.armybook.dsl.Enchantment
import org.jim.ninthage.data.armybook.EmpireOfSonnstahl
import org.jim.ninthage.data.armybook.MainBook
import org.jim.ninthage.models.ArmyBook
import org.jim.ninthage.models.ArmyBookEntry
import org.jim.ninthage.models.ArmyBookEntryOption
import java.lang.Exception
import java.nio.file.Files


val sb = StringBuilder()
fun main() {

    try {
        Files.createDirectories(
            App.HomeDirectory
                .resolve("training")
        )
        createArtificer()
        createKnightCommander()
        createPrelate()
        createMarshal()
        createInquisitor()
        createWizard()
        createEntry(EmpireOfSonnstahl.Version2_0, EmpireOfSonnstahl.Version2_0.heavyInfantry)
        createEntry(EmpireOfSonnstahl.Version2_0, EmpireOfSonnstahl.Version2_0.lightInfantry)
        createEntry(EmpireOfSonnstahl.Version2_0, EmpireOfSonnstahl.Version2_0.electoralCavalry)
        createEntry(EmpireOfSonnstahl.Version2_0, EmpireOfSonnstahl.Version2_0.stateMilitia)
        createEntry(EmpireOfSonnstahl.Version2_0, EmpireOfSonnstahl.Version2_0.imperialGuard)
        createEntry(EmpireOfSonnstahl.Version2_0, EmpireOfSonnstahl.Version2_0.knightOfTheSunGriffon)
        createEntry(EmpireOfSonnstahl.Version2_0, EmpireOfSonnstahl.Version2_0.reiters)
    }catch (ex:Exception) {
        ex.printStackTrace()
    }


}

fun createEntry(armyBook:ArmyBook, entry:ArmyBookEntry) {
    UnitCreator(
        armyBook,
        entry
    ).createUnit()
}

fun createWizard() {
    val exclude =
        listOf<Enchantment>(
            ArcaneCompendiums.Artefacts.CrownOfWizardKing,
            ArcaneCompendiums.ArmourEnchantments.GhostlyGuard,
            EmpireOfSonnstahl.ArmourEnchantments.Blacksteel,
            EmpireOfSonnstahl.ArmourEnchantments.ImperialSeal
        )
            .map { it.label }
            .toMutableList()
    exclude.add("None")

    CharacterCreator(
        EmpireOfSonnstahl.Version2_0,
        EmpireOfSonnstahl.Version2_0.wizard,
        listOf(
            ArcaneCompendiums.Artifact,
            MainBook.General,
            ArcaneCompendiums.MagicPath
        ),
        exclude,
        "Artifacts"

    ).createUnit()

    CharacterCreator(
        EmpireOfSonnstahl.Version2_0,
        EmpireOfSonnstahl.Version2_0.wizard,
        listOf(
            "Armour",
            ArcaneCompendiums.ArmourEnchantment,
            ArcaneCompendiums.MagicPath
        ),
        exclude,
        "Armour"
    ).createUnit()

    CharacterCreator(
        EmpireOfSonnstahl.Version2_0,
        EmpireOfSonnstahl.Version2_0.wizard,
        listOf(
            ArcaneCompendiums.WeaponEnchantment,
            ArcaneCompendiums.MagicPath
        ),
        exclude,
        "Weapons"
    ).createUnit()


    CharacterCreator(
        EmpireOfSonnstahl.Version2_0,
        EmpireOfSonnstahl.Version2_0.wizard,
        listOf(
            ArcaneCompendiums.WeaponEnchantment,
            ArcaneCompendiums.MagicPath
        ),
        exclude,
        "Weapons"
    ).createUnit()


    CharacterCreator(
        EmpireOfSonnstahl.Version2_0,
        EmpireOfSonnstahl.Version2_0.wizard,
        listOf(
            "Mount",
            ArcaneCompendiums.MagicPath
        ),
        ArrayList<String>().apply {
            addAll(exclude)
            add("Arcane Engine")
        },
        "mounts"
    ).createUnit()

    CharacterCreator(
        EmpireOfSonnstahl.Version2_0,
        EmpireOfSonnstahl.Version2_0.wizard,
        listOf(
            "Mount",
            "Engine",
            ArcaneCompendiums.WizardLevel,
            ArcaneCompendiums.MagicPath
        ),
        ArrayList<String>().apply {
            addAll(exclude)
            add("Horse")
            add("Great Griffon")
            add("Pegasus")
        },
        "engine"
    ).createUnit()

}

fun createPrelate() {
    CharacterCreator(
        EmpireOfSonnstahl.Version2_0,
        EmpireOfSonnstahl.Version2_0.prelate,
        listOf(
            ArcaneCompendiums.Artifact,
            "Mount",
            MainBook.General
        ),
        listOf<Enchantment>(
            ArcaneCompendiums.Artefacts.BookOfArcaneMastery,
            ArcaneCompendiums.Artefacts.EssenceOfAFreeMind,
            ArcaneCompendiums.Artefacts.MagicalHeirloom,
            EmpireOfSonnstahl.Artefacts.ExemplarsFlame,
            EmpireOfSonnstahl.Artefacts.KaradonsCourser
        ).map { it.label }
    ).createUnit()

    CharacterCreator(
        EmpireOfSonnstahl.Version2_0,
        EmpireOfSonnstahl.Version2_0.prelate,
        listOf(
            ArcaneCompendiums.WeaponEnchantment,
            ArcaneCompendiums.ShieldEnchantment,
            "Shield"
        ),
        listOf(
            "None"
        ),
        "SwordAndBoard"
    ).createUnit()


    CharacterCreator(
        EmpireOfSonnstahl.Version2_0,
        EmpireOfSonnstahl.Version2_0.prelate,
        listOf(
            MainBook.Armour,
            ArcaneCompendiums.ArmourEnchantment,
            MainBook.CloseCombatWeapon
        ),
        listOf(
            "None"
        ),
        "CCAndArmour"
    ).createUnit()
}


fun createKnightCommander() {
    CharacterCreator(
        EmpireOfSonnstahl.Version2_0,
        EmpireOfSonnstahl.Version2_0.knightCommander,
        listOf(
            ArcaneCompendiums.Artifact,
            "Mount",
            MainBook.General
        ),
        listOf<Enchantment>(
            ArcaneCompendiums.Artefacts.BookOfArcaneMastery,
            ArcaneCompendiums.Artefacts.EssenceOfAFreeMind,
            ArcaneCompendiums.Artefacts.MagicalHeirloom,
            EmpireOfSonnstahl.Artefacts.ExemplarsFlame,
            EmpireOfSonnstahl.Artefacts.KaradonsCourser
        ).map { it.label }
    ).createUnit()

    CharacterCreator(
        EmpireOfSonnstahl.Version2_0,
        EmpireOfSonnstahl.Version2_0.knightCommander,
        listOf(
            ArcaneCompendiums.WeaponEnchantment,
            ArcaneCompendiums.ShieldEnchantment,
            "Shield"
        ),
        listOf(
            "None"
        ),
        "SwordAndBoard"
    ).createUnit()


    CharacterCreator(
        EmpireOfSonnstahl.Version2_0,
        EmpireOfSonnstahl.Version2_0.knightCommander,
        listOf(
            ArcaneCompendiums.ArmourEnchantment,
            MainBook.CloseCombatWeapon
        ),
        listOf(
            "None"
        ),
        "CCAndArmour"
    ).createUnit()
}

fun createInquisitor() {
    CharacterCreator(
        EmpireOfSonnstahl.Version2_0,
        EmpireOfSonnstahl.Version2_0.inquisitor,
        listOf(
            ArcaneCompendiums.Artifact,
            MainBook.RangedWeapon
        ),
        listOf<Enchantment>(
            ArcaneCompendiums.Artefacts.BookOfArcaneMastery,
            ArcaneCompendiums.Artefacts.EssenceOfAFreeMind,
            ArcaneCompendiums.Artefacts.MagicalHeirloom,
            EmpireOfSonnstahl.Artefacts.ExemplarsFlame,
            EmpireOfSonnstahl.Artefacts.KaradonsCourser
        ).map { it.label },
        "artifactsRange",
        100
    ).createUnit()

    CharacterCreator(
        EmpireOfSonnstahl.Version2_0,
        EmpireOfSonnstahl.Version2_0.inquisitor,
        listOf(
            MainBook.CloseCombatWeapon,
            MainBook.Shield,
            MainBook.Mount,
            MainBook.Title
        ),
        listOf<Enchantment>(
            ArcaneCompendiums.Artefacts.BookOfArcaneMastery,
            ArcaneCompendiums.Artefacts.EssenceOfAFreeMind,
            ArcaneCompendiums.Artefacts.MagicalHeirloom,
            EmpireOfSonnstahl.Artefacts.ExemplarsFlame,
            EmpireOfSonnstahl.Artefacts.KaradonsCourser
        ).map { it.label },
        "CloseCombatWeapon",
        100
    ).createUnit()


    CharacterCreator(
        EmpireOfSonnstahl.Version2_0,
        EmpireOfSonnstahl.Version2_0.inquisitor,
        listOf(
            ArcaneCompendiums.WeaponEnchantment,
            ArcaneCompendiums.ShieldEnchantment,
            MainBook.Shield
        ),
        listOf<Enchantment>(
            ArcaneCompendiums.Artefacts.BookOfArcaneMastery,
            ArcaneCompendiums.Artefacts.EssenceOfAFreeMind,
            ArcaneCompendiums.Artefacts.MagicalHeirloom,
            EmpireOfSonnstahl.Artefacts.ExemplarsFlame,
            EmpireOfSonnstahl.Artefacts.KaradonsCourser
        ).map { it.label },
        "WSEnchantment",
        100
    ).createUnit()
}

fun createArtificer() {
    val artificerEntry = EmpireOfSonnstahl.Version2_0.entry("Artificer")
    CharacterCreator(
        EmpireOfSonnstahl.Version2_0,
        artificerEntry,
        listOf(ArcaneCompendiums.Artifact, MainBook.RangedWeapon),
        listOf<Enchantment>(
            ArcaneCompendiums.Artefacts.BookOfArcaneMastery,
            ArcaneCompendiums.Artefacts.EssenceOfAFreeMind,
            ArcaneCompendiums.Artefacts.MagicalHeirloom,
            EmpireOfSonnstahl.Artefacts.ExemplarsFlame,
            EmpireOfSonnstahl.Artefacts.KaradonsCourser
        ).map { it.label },maxMagicPoints = 50
    ).createUnit()
}

fun createMarshal() {

    val marshalEntry = EmpireOfSonnstahl.Version2_0.entry("Marshal")
    CharacterCreator(
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

    CharacterCreator(
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
    CharacterCreator(
        EmpireOfSonnstahl.Version2_0,
        marshalEntry,
        listOf(
            ArcaneCompendiums.ShieldEnchantment,
            MainBook.Shield,
            ArcaneCompendiums.Artifact
        ),
        listOf(
            "None",
            ArcaneCompendiums.Artefacts.BookOfArcaneMastery.label,
            ArcaneCompendiums.Artefacts.EssenceOfAFreeMind.label,
            ArcaneCompendiums.Artefacts.MagicalHeirloom.label,
            EmpireOfSonnstahl.Artefacts.ExemplarsFlame.label
        ),
        "ShieldArtifact"
    ).createUnit()
    CharacterCreator(
        EmpireOfSonnstahl.Version2_0,
        marshalEntry,
        listOf(
            ArcaneCompendiums.BannerEnchantment,
            MainBook.BattleStandardBear,
            MainBook.Shield
        ),
        listOf(
            "None"
        ),
        "BSB"
    ).createUnit()
}

class UnitCreator(
    val armyBook: ArmyBook,
    val armyBookEntry: ArmyBookEntry,
    val uid: String = ""
) {
    val sb = StringBuilder()

    fun createUnit() {
        createEntry(
            armyBookEntry,
            0,
            "<Unit name=\"${armyBookEntry.label}\" ",
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
        for (s in option.selections) {
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
    }
}


class CharacterCreator(
    val armyBook: ArmyBook,
    val armyBookEntry: ArmyBookEntry,
    val options: List<String>,
    val excludeSelections: List<String>,
    val uid: String = "",
    val maxMagicPoints: Int = 200
) {
    val sb = StringBuilder()

    fun createUnit() {
        createEntry(
            armyBookEntry,
            0,
            "<Unit name=\"${armyBookEntry.label}\" ",
            armyBookEntry.label,
            0
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
        value: String,
        magicPoints: Int
    ) {
        if (magicPoints > maxMagicPoints) {
            return
        } else if (index == armyBookEntry.options.size) {
            sb.append(tag + "\n>" + value + "\n")
            return
        }
        val option = armyBookEntry.options[index]
        if (options.contains(option.name)) {
            for (s in option.selections) {
                if (excludeSelections.contains(s.name)) {
                    continue
                }
                if (option.default == s.name) {
                    createEntry(armyBookEntry, index + 1, tag, value, magicPoints)
                } else {
                    createEntry(
                        armyBookEntry,
                        index + 1,
                        tag + "\n\t${option.name}=\"${s.name}\"",
                        value + ", ${s.name}",
                        0 + if (isMagic(option)) {
                            s.points
                        } else 0
                    )
                }
            }
        } else {
            createEntry(
                armyBookEntry,
                index + 1,
                tag,
                value,
                magicPoints
            )
        }
    }

    private fun isMagic(option: ArmyBookEntryOption): Boolean {
        return option.name in setOf(
            ArcaneCompendiums.ArmourEnchantment,
            ArcaneCompendiums.WeaponEnchantment,
            ArcaneCompendiums.ShieldEnchantment,
            ArcaneCompendiums.Artifact,
            ArcaneCompendiums.BannerEnchantment
        )
    }
}


