package org.jim.ninthage.data.armybook

import org.jim.ninthage.data.armybook.ArcaneCompendiums.MagicPaths
import org.jim.ninthage.data.armybook.EmpireOfSonnstahl.ChampionRangedWeapon
import org.jim.ninthage.data.armybook.StandardArmyBook.DSL_Roster.bannerEnchantmentBSBOption
import org.jim.ninthage.data.armybook.StandardArmyBook.DSL_Roster.bannerEnchantmentOption
import org.jim.ninthage.data.armybook.StandardArmyBook.DSL_Roster.character
import org.jim.ninthage.data.armybook.StandardArmyBook.DSL_Roster.pathsOfMagicOption
import org.jim.ninthage.data.armybook.dsl.*
import org.jim.ninthage.models.ArmyBookEntry
import org.jim.ninthage.models.ArmyBookEntryOption
import org.jim.ninthage.models.ArmyBookEntryOptionSelection

object EmpireOfSonnstahl {
    val KnightOrders: String = "KnightOrders"
    val ChampionRangedWeapon: String = "ChampionRangedWeapon"
    val Irregulars = "Irregulars"
    val ReplaceShieldWithGreatWeapon = "ReplaceShieldWithGreatWeapon"
    val ReplaceHalberdWithLance = "ReplaceHalberdWithLance"
    val ArcaneEngineType = "Engine"
    val Forsight = "Foresight"
    val ArcaneShield = "Arcane Shield"
    val Pistol = "Pistol"
    val BracerOfPistols = "Bracer of Pistols"
    val RepeaterGun = "Repeater Gun"
    val RepeaterPistol = "Repeater Pistol"
    val LongRifle: String = "Long Rifle"


    enum class WeaponEnchantment(
        override val label: String,
        override val points: Int
    ) : Enchantment {
        TheLightOfSonnstahl("The Light of Sonnstahl", 160),
        DeathWarrant("Death Warrant", 55),
        HammerOfWitches("Hammer of Witches", 45)
    }


    enum class ArmourEnchantments(
        override val label: String,
        override val points: Int
    ) : Enchantment {
        ImperialSeal("Imperial Seal", 100),
        Blacksteel("Blacksteel", 45)
    }

    enum class ShieldEnchantments(
        override val label: String,
        override val points: Int
    ) : Enchantment {
        WitchfireGuard("Witchfire Guard", 35),
        ShieldOfVolund("Shield of Volund", 20)
    }

    enum class Artefacts(
        override val label: String,
        override val points: Int
    ) : Enchantment {
        WinterCloak("Winter Cloak", 80),
        LocketOfSunna("Locket of Sunna", 70),
        ExemplarsFlame("Exemplar’s Flame", 60),
        KaradonsCourser("Karadon's Courser", 50),
        MantleOfUllor("Mantle of Ullor", 25),
    }

    enum class BannerEnchantments(
        override val label: String,
        override val points: Int
    ) : Enchantment {
        HouseholdStandard("Household Standard", 40),
        BannerOfUnity("Banner of Unity", 40),
        MarksmansPennant("Marksman’s Pennant", 10),
    }

    enum class Mounts(
        override val label: String
    ) : StandardArmyBook.DSL_Roster.Mount {
        Horse("Horse"),
        Pegasus("pegasus"),
        GreatGriffon("Great Griffon"),
        Dragon("Dragon"),
        AltarOfBattle("Altar of Battle")
    }


    val Version2_0 = EoS_Version2_0()


}

class EoS_Version2_0 : StandardArmyBook {
    override val shortLabel: String
        get() = "EoS"
    override val arcaneCompendium: ArcaneCompendium
        get() = ArcaneCompendiums.Version2_0
    override val customArmourEnchantments: List<Enchantment>
        get() = EmpireOfSonnstahl.ArmourEnchantments.values().toList()
    override val customArtifacts: List<Enchantment>
        get() = EmpireOfSonnstahl.Artefacts.values().toList()
    override val customBannerEnchantments: List<Enchantment>
        get() = EmpireOfSonnstahl.BannerEnchantments.values().toList()
    override val customShieldEnchantments: List<Enchantment>
        get() = EmpireOfSonnstahl.ShieldEnchantments.values().toList()
    override val customWeaponEnchantments: List<Enchantment>
        get() = EmpireOfSonnstahl.WeaponEnchantment.values().toList()


    val marshal: ArmyBookEntry =
        character(name = "Marshal", points = 160) {
            listOf(
                general(),
                battleStandardBearer(0),
                shield(5),
                closeCombatWeapon(
                    pairedWeapons(5),
                    greatWeapon(10),
                    halberd(10),
                    lance(10)
                ),
                option(MainBook.RangedWeapon, "None") {
                    listOf(
                        selection("Pistol", 5)
                    )
                },
                mount(
                    EmpireOfSonnstahl.Mounts.Horse(70),
                    EmpireOfSonnstahl.Mounts.Pegasus(75),
                    EmpireOfSonnstahl.Mounts.GreatGriffon(150),
                    EmpireOfSonnstahl.Mounts.Dragon(460)
                ),
                title(
                    selection("Great Tactician", 60),
                    selection("Imperial Prince", 175)
                ),
                bannerEnchantmentBSBOption()
            )
        }
    val knightCommander = character(name = "Knight Commander", points = 200) {
        listOf(
            general(),
            shield(5),
            closeCombatWeapon(
                selection("Calvary Pick", 30),
                greatWeapon(10),
                halberd(10),
                lance(10)
            ),
            option("Mount", "Horse") {
                listOf(
                    selection("Young Griffon", 30)
                )
            }
        )
    }

    val prelate = character(name = "Prelate", points = 160) {
        listOf(
            general(),
            armour(MainBook.HeavyArmour) {
                plateArmour(20)
            },
            shield(5),
            closeCombatWeapon(
                greatWeapon(10),
                pairedWeapons(5)
            ),
            mount(
                EmpireOfSonnstahl.Mounts.Horse(40),
                EmpireOfSonnstahl.Mounts.AltarOfBattle(370)
            )
        )
    }

    val wizard = character(name = "Wizard", points = 125) {
        listOf(

            general(),
            wizard(),

            pathsOfMagicOption(
                MagicPaths.Alchemy,
                MagicPaths.Cosmology,
                MagicPaths.Divination,
                MagicPaths.Pyromancy
            ),
            armour {
                lightArmour(5)
            },
            mount(
                selection("Horse", 20),
                selection("Pegasus", 50),
                selection("Great Griffon", 100),
                selection("Arcane Engine", 200)
            ),
            option("Engine", "None") {
                listOf(
                    selection("Foresight"),
                    selection("Arcane Shield")
                )
            }
        )
    }

    val artificer = character(name = "Artificer", points = 125) {
        listOf(
            option(MainBook.RangedWeapon, "None") {
                listOf(
                    selection("Handgun", 5),
                    selection("Repeater Pistol", 10),
                    selection("Repeater Gun", 10),
                    selection("Long Rifle", 10)
                )
            },
            mount(
                selection("Horse", 20)
            )
        )
    }
    val inquisitor = character(name = "Inquisitor", points = 130) {
        listOf(
            shield(5),
            closeCombatWeapon(
                pairedWeapons(5),
                greatWeapon(10),
                halberd(10)
            ),
            option(MainBook.RangedWeapon, "None") {
                listOf(
                    selection("Crossbow", 10),
                    selection("Bracer of Pistols", 15),
                    selection("Repeater Pistol", 25)
                )
            },
            title(
                selection("Blessed Steel", 70),
                selection("Silver Shots", 75)
            ),
            mount(
                selection("Horse", 70) // and gain Light Troops", 70)
            )
        )
    }

    val heavyInfantry =
        troop("Heavy Infantry", 145, 20, 30, 10) {
            listOf(
                closeCombatWeapon(
                    spear(1),
                    halberd(1)
                ),
                command(),
                bannerEnchantmentOption()
            )
        }

    val lightInfantry =
        troop("Light Infantry", 135, 10, 10, 13) {
            listOf(
                option(MainBook.RangedWeapon, "Crossbow") {
                    listOf(
                        selection("Handgun")
                    )
                },
                unitOption(ChampionRangedWeapon, "None") {
                    listOf(
                        selection(EmpireOfSonnstahl.RepeaterGun, 15),
                        selection(EmpireOfSonnstahl.LongRifle, 15)
                    )
                },
                command(),
                bannerEnchantmentOption()
            )
        }

    val stateMilitia =
        troop("State Militia", 140, 10, 15, 10) {
            listOf(
                implicitOption(EmpireOfSonnstahl.Irregulars, 1),
                command()
            )
        }
    val electoralCavalry =
        troop("Electoral Cavalry", 155, 5, 10, 29)
        {
            listOf(
                shield(4),
                closeCombatWeapon(
                    lance(4),
                    greatWeapon(4),
                    selection("Calvary Pick", 0)
                ),
                command(),
                bannerEnchantmentOption(),
                implicitOption(EmpireOfSonnstahl.KnightOrders, 9, "Knight Orders")
            )
        }

    val imperialGuard =
        troop("Imperial Guard", 180, 15, 25, 19) {
            listOf(
                implicitOption(
                    EmpireOfSonnstahl.ReplaceShieldWithGreatWeapon,
                    3,
                    "Replace Shield with Great Weapon"
                ),
                command(),
                bannerEnchantmentOption()
            )
        }
    val knightOfTheSunGriffon =
        troop("Knights of the Sun Griffon", 290, 3, 3, 95) {
            listOf(
                implicitOption(
                    EmpireOfSonnstahl.ReplaceHalberdWithLance,
                    12,
                    "Replace Halberd with Lance"
                ),
                command(),
                bannerEnchantmentOption()
            )
        }
    val arcaneEngine =
        singleModel("Arcane Engine", 290) {
            listOf(
                requiredOption(EmpireOfSonnstahl.ArcaneEngineType) {
                    listOf(
                        selection(EmpireOfSonnstahl.Forsight),
                        selection(EmpireOfSonnstahl.ArcaneShield)
                    )
                }
            )
        }
    val imperialRangers =
        troop("Imperial Rangers", 90, 5, 5, 12) {
            listOf(
                champion()
            )
        }
    val reiters =
        troop("Reiters", 150, 5, 5, 29) {
            listOf(
                option(MainBook.Armour, MainBook.LightArmour) {
                    listOf(
                        selection(MainBook.HeavyArmour, 4)
                    )
                },
                option(MainBook.RangedWeapon, EmpireOfSonnstahl.Pistol) {
                    listOf(
                        selection(EmpireOfSonnstahl.BracerOfPistols, 4),
                        selection(EmpireOfSonnstahl.RepeaterGun, 4)
                    )
                },
                championMusican(),
                unitOption(ChampionRangedWeapon, MainBook.None) {
                    listOf(
                        selection(EmpireOfSonnstahl.RepeaterPistol, 10)
                    )
                }
            )
        }
    val artillery = singleModel("Artillery", 0) {
        listOf(
            requiredOption("ArtilleryWeapons") {
                listOf(
                    selection("Mortar", 200),
                    selection("Volley Gun", 200),
                    selection("Imperial Rocketeer", 160),
                    selection("Cannon", 250)
                )
            }
        )
    }
    val flagellants =
        troop("Flagellants", 200, 15, 15, 18)
        {
            listOf(
                champion()
            )
        }
    val steamTank = singleModel("Steam Tank", 475)

    override val entries: List<ArmyBookEntry>
        get() =
            listOf(
                marshal,
                knightCommander,
                prelate,
                wizard,
                inquisitor,
                artificer,
                heavyInfantry,
                lightInfantry,
                stateMilitia,
                electoralCavalry,
                imperialGuard,
                knightOfTheSunGriffon,
                arcaneEngine,
                imperialRangers,
                reiters,
                artillery,
                flagellants,
                steamTank
            )
}


private fun wizard(): ArmyBookEntryOption {
    return option(
        ArcaneCompendiums.WizardLevel,
        ArcaneCompendiums.Apprentice
    ) {
        listOf(
            selection(ArcaneCompendiums.Adept, 75),
            selection(ArcaneCompendiums.Master, 225)
        )
    }
}

private fun title(vararg selection: ArmyBookEntryOptionSelection): ArmyBookEntryOption {
    return option("Title", "None") {
        selection.toList()
    }
}

private fun mount(vararg selection: ArmyBookEntryOptionSelection): ArmyBookEntryOption {
    val selectionList = ArrayList<ArmyBookEntryOptionSelection>()
    selectionList.addAll(selection)
    selectionList.add(ArmyBookEntryOptionSelection("None", 0))
    return ArmyBookEntryOption(
        name = "Mount",
        selections = selectionList,
        default = "None",
        implicit = null,
        minSelection = 1
    )
}



