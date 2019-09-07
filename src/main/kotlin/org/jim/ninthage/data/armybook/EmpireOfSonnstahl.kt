package org.jim.ninthage.data.armybook

import org.jim.ninthage.data.armybook.ArcaneCompendiums.MagicPaths
import org.jim.ninthage.data.armybook.EmpireOfSonnstahl.ChampionRangedWeapon
import org.jim.ninthage.data.armybook.dsl.Enchantment
import org.jim.ninthage.data.armybook.dsl.Mount
import org.jim.ninthage.data.armybook.dsl.StandardArmyBook
import org.jim.ninthage.data.armybook.dsl.StandardArmyBook.DSL_Roster.character
import org.jim.ninthage.data.armybook.dsl.StandardArmyBook.DSL_Roster.singleModel
import org.jim.ninthage.data.armybook.dsl.StandardArmyBook.DSL_Roster.troopLegacy
import org.jim.ninthage.models.ArmyBookEntry

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
        ExemplarsFlame("Exemplar's Flame", 60),
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
    ) : Mount {
        Horse("Horse"),
        Pegasus("Pegasus"),
        GreatGriffon("Great Griffon"),
        Dragon("Dragon"),
        AltarOfBattle("Altar of Battle"),
        ArcaneEngine("Arcane Engine")
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


    val marshal = character(name = "Marshal", points = 160) {
        general()
        battleStandardBearer(0)
        shield(5)
        closeCombatWeapon {
            pairedWeapons(5)
            greatWeapon(10)
            halberd(10)
            lance(10)
        }
        rangedWeapon {
            selection("Pistol", 5)
        }

        mount(
            EmpireOfSonnstahl.Mounts.Horse(70),
            EmpireOfSonnstahl.Mounts.Pegasus(75),
            EmpireOfSonnstahl.Mounts.GreatGriffon(150),
            EmpireOfSonnstahl.Mounts.Dragon(460)
        )
        title {
            selection("Great Tactician", 60)
            selection("Imperial Prince", 175)
        }
    }
    val knightCommander: ArmyBookEntry = character("Knight Commander", 200) {
        general()
        shield(5)
        closeCombatWeapon {
            selection("Cavalry Pick", 30)
            greatWeapon(10)
            halberd(10)
            lance(10)
        }

        optionWithDefault("Mount", "Horse") {
            selection("Young Griffon", 30)
        }
    }

    val prelate = character(name = "Prelate", points = 160) {
        general()
        armour {
            heavyArmour { default() }
            plateArmour(20)
        }
        shield(5)
        closeCombatWeapon {
            greatWeapon(10)
            pairedWeapons(5)
        }
        mount(
            EmpireOfSonnstahl.Mounts.Horse(40),
            EmpireOfSonnstahl.Mounts.AltarOfBattle(370)
        )
    }

    val wizard = character(name = "Wizard", points = 125) {
        general()
        wizard()
        pathsOfMagicOption(
            MagicPaths.Alchemy,
            MagicPaths.Cosmology,
            MagicPaths.Divination,
            MagicPaths.Pyromancy
        )
        armour {
            lightArmour(5)
        }
        mount(
            EmpireOfSonnstahl.Mounts.Horse(20),
            EmpireOfSonnstahl.Mounts.Pegasus(50),
            EmpireOfSonnstahl.Mounts.GreatGriffon(100),
            EmpireOfSonnstahl.Mounts.ArcaneEngine(200)
        )
        optionWithDefault("Engine", "None") {
            selection("Foresight")
            selection("Arcane Shield")
        }

    }

    val artificer = character(name = "Artificer", points = 125) {
        rangedWeapon {
            selection("Handgun", 5)
            selection("Repeater Pistol", 10)
            selection("Repeater Gun", 10)
            selection("Long Rifle", 10)
        }
        mount(
            EmpireOfSonnstahl.Mounts.Horse(20)
        )
    }
    val inquisitor = character(name = "Inquisitor", points = 130) {

        shield(5)
        closeCombatWeapon {
            pairedWeapons(5)
            greatWeapon(10)
            halberd(10)
        }
        rangedWeapon {
            selection("Crossbow", 10)
            selection("Bracer of Pistols", 15)
            selection("Repeater Pistol", 25)
        }
        title {
            selection("Blessed Steel", 70)
            selection("Silver Shots", 75)
        }
        mount(
            EmpireOfSonnstahl.Mounts.Horse(70) // and gain Light Troops", 70)
        )
    }

    val heavyInfantry =
        troopLegacy("Heavy Infantry", 145, 20, 30, 10) {
            closeCombatWeapon {
                spear(1)
                halberd(1)
            }
            command()
            bannerEnchantmentOption()

        }

    val lightInfantry =
        troopLegacy("Light Infantry", 135, 10, 10, 13) {

            rangedWeapon {
                selection("Crowssbow") { default() }
                selection("Handgun")
            }
            unitOption(ChampionRangedWeapon, "None") {

                selection(EmpireOfSonnstahl.RepeaterGun, 15)
                selection(EmpireOfSonnstahl.LongRifle, 15)

            }
            command()
            bannerEnchantmentOption()

        }

    val stateMilitia =
        troopLegacy("State Militia", 140, 10, 15, 10) {
            implicitOption(EmpireOfSonnstahl.Irregulars, 1)
            command()
        }
    val electoralCavalry =
        troopLegacy("Electoral Cavalry", 155, 5, 10, 29) {
            shield(4)
            closeCombatWeapon {
                lance(4)
                greatWeapon(4)
                selection("Cavalry Pick", 0)
            }
            command()
            bannerEnchantmentOption()
            implicitOption(EmpireOfSonnstahl.KnightOrders, 9, "Knight Orders")
        }

    val imperialGuard =
        troopLegacy("Imperial Guard", 180, 15, 25, 19) {
            implicitOption(
                EmpireOfSonnstahl.ReplaceShieldWithGreatWeapon,
                3,
                "Replace Shield with Great Weapon"
            )
            command()
            bannerEnchantmentOption()
        }
    val knightOfTheSunGriffon =
        troopLegacy("Knights of the Sun Griffon", 290, 3, 3, 95) {
            implicitOption(
                EmpireOfSonnstahl.ReplaceHalberdWithLance,
                12,
                "Replace Halberd with Lance"
            )
            command()
            bannerEnchantmentOption()
        }
    val arcaneEngine =
        singleModel("Arcane Engine", 290) {
            requiredOption(EmpireOfSonnstahl.ArcaneEngineType) {
                selection(EmpireOfSonnstahl.Forsight)
                selection(EmpireOfSonnstahl.ArcaneShield)
            }
        }
    val imperialRangers =
        troopLegacy("Imperial Rangers", 90, 5, 5, 12) {

            champion()
        }
    val reiters =
        troopLegacy("Reiters", 150, 5, 5, 29) {
            armour {
                lightArmour { default() }
                heavyArmour(4)
            }
            rangedWeapon {
                selection(EmpireOfSonnstahl.Pistol) { default() }
                selection(EmpireOfSonnstahl.BracerOfPistols, 4)
                selection(EmpireOfSonnstahl.RepeaterGun, 4)
            }
            championMusican()
            unitOption(ChampionRangedWeapon, MainBook.None) {
                selection(EmpireOfSonnstahl.RepeaterPistol, 10)
            }
        }
    val artillery = singleModel("Artillery", 0) {
        requiredOption("ArtilleryWeapons") {
            listOf(
                selection("Mortar", 200),
                selection("Volley Gun", 200),
                selection("Imperial Rocketeer", 160),
                selection("Cannon", 250)
            )
        }
    }
    val flagellants =
        troopLegacy("Flagellants", 200, 15, 15, 18) {
            champion()
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






