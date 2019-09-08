package org.jim.ninthage.data.armybook

import org.jim.ninthage.data.armybook.dsl.Enchantment
import org.jim.ninthage.data.armybook.dsl.Mount
import org.jim.ninthage.data.armybook.dsl.OptionBuilder
import org.jim.ninthage.data.armybook.dsl.StandardArmyBook
import org.jim.ninthage.data.armybook.dsl.StandardArmyBook.DSL_Roster.character
import org.jim.ninthage.data.armybook.dsl.StandardArmyBook.DSL_Roster.singleModel
import org.jim.ninthage.data.armybook.dsl.StandardArmyBook.DSL_Roster.troop
import org.jim.ninthage.data.armybook.dsl.StandardArmyBook.DSL_Roster.troopLegacy
import org.jim.ninthage.models.ArmyBookEntry

object BeastHerd {
    enum class WeaponEnchantments(
        override val label: String,
        override val points: Int
    ) : Enchantment {
        HawthorneCurse("Hawthorne Curse", 70),
        AncestralCarvings("Ancestral Carvings", 50),
        TwinHungers("Twin Hungers", 40),
        FatalFolly("Fatal Folly", 35)
    }

    enum class ArmourEnchantments(
        override val label: String,
        override val points: Int
    ) : Enchantment {
        AaghorsAffliction("Aaghor's Affliction", 100),
        TrickstersCunning("Trickster's Cunning", 60),
        WildForm("Wild Form", 35)
    }

    enum class ShieldEnchantments(
        override val label: String,
        override val points: Int
    ) : Enchantment {
        ObscuringFog("Obscuring Fog", 20)
    }

    enum class BannerEnchantments(
        override val label: String,
        override val points: Int
    ) : Enchantment {
        BannerOfTheWildHerd("Banner of the Wild Herd", 50)
    }

    enum class Artefacts(
        override val label: String,
        override val points: Int
    ) : Enchantment {
        DarkRain("Dark Rain", 80),
        SeedOfTheDarkForest("Seed of the Dark Forest", 60),
        PillagerIcon("Pillager Icon", 55),
        EyeOfDominance("Eye of Dominance", 30),
        InscribingBurin("Inscribing Burin", 30),
        CrownOfHorns("Crown of Horns", 25)
    }

    enum class Mounts(
        override val label: String
    ) : Mount {
        RaidingChariot("Raiding Chariot"),
        RazortuskChariot(" Razortusk Chariot")
    }
}

class BeastHeard2_0 : StandardArmyBook {
    override val shortLabel: String
        get() = "BH"
    override val arcaneCompendium: ArcaneCompendium
        get() = ArcaneCompendiums.Version2_0
    override val customArmourEnchantments: List<Enchantment>
        get() = BeastHerd.ArmourEnchantments.values().toList()
    override val customArtifacts: List<Enchantment>
        get() = BeastHerd.Artefacts.values().toList()
    override val customBannerEnchantments: List<Enchantment>
        get() = BeastHerd.BannerEnchantments.values().toList()
    override val customShieldEnchantments: List<Enchantment>
        get() = BeastHerd.ShieldEnchantments.values().toList()
    override val customWeaponEnchantments: List<Enchantment>
        get() = BeastHerd.WeaponEnchantments.values().toList()

    val BeastLord =
        character(name = "Beast Lord", points = 160) {
            general()
            implicitOption("HuntingCall", 25, "Hunting Call")
            shield(5)
            armour {
                lightArmour { default() }
                heavyArmour(15)
            }
            rangedWeapon {
                throwingWeapon(5)
            }
            closeCombatWeapon {
                pairedWeapons(10)
                greatWeapon(20)
                lance(20)
                selection("Beast Axe", 25)
            }
            mount(
                BeastHerd.Mounts.RaidingChariot(90),
                BeastHerd.Mounts.RazortuskChariot(125)
            )
        }

    val BeastChieftain =
        character("Beast Chieftain", 120) {
            general()
            ambush(10)
            implicitOption("HuntingCall", 25, "Hunting Call")
            battleStandardBearer()
            implicitOption("GreaterTotemBearer", 85, "Greater Totem Bearer")
            shield(5)
            armour {
                lightArmour { default() }
                heavyArmour(10)
            }
            rangedWeapon {
                throwingWeapon(5)
            }
            closeCombatWeapon {
                pairedWeapons(5)
                greatWeapon(10)
                lance(10)
                selection("Beast Axe", 15)
            }
            mount(
                BeastHerd.Mounts.RaidingChariot(90)
            )
        }
    val Soothsayer = character("Soothsayer", 155) {
        general()
        ambush(5)
        armour {
            lightArmour(5)
        }
        mount(BeastHerd.Mounts.RaidingChariot(20))
        wizard()
        pathsOfMagicOption(
            ArcaneCompendiums.MagicPaths.Druidism,
            ArcaneCompendiums.MagicPaths.Evocation,
            ArcaneCompendiums.MagicPaths.Shamanism
        )
    }
    val MinotaurWarlord = character("Minotaur Warlord", 490){
        general()
        armour {
            lightArmour { default() }
            heavyArmour(10)
        }
        shield(10)
        closeCombatWeapon {
            pairedWeapons(15)
            greatWeapon(15)
            selection("Beast Axe", 30)
        }
    }
    val MinotuarChieftain = character("Minotaur Chieftain", 220) {
        general()
        battleStandardBearer()
        implicitOption("GreaterTotemBearer", 85, "Greater Totem Bearer")
        shield(10)
        armour {
            lightArmour { default() }
            heavyArmour(10)
        }
        closeCombatWeapon {
            pairedWeapons(10)
            greatWeapon(20)
            selection("Beast Axe", 20)
        }
    }
    val CentuarChieftain = character("Centaur Chieftain", 220) {
        general()
        ambush(10)
        battleStandardBearer()
        implicitOption("GreaterTotemBearer", 85, "Greater Totem Bearer")
        shield(5)
        armour {
            lightArmour { default() }
            heavyArmour(10)
        }
        rangedWeapon {
            throwingWeapon(5)
        }
        closeCombatWeapon {
            pairedWeapons(10)
            greatWeapon(20)
            lance(20)
            selection("Beast Axe", 25)
        }
    }

    val WildhornHeard = troop("Wildhorn Herd", 150, 10, 15, 50){
        ambush(20)
        closeCombatWeapon {
            pairedWeapons(2)
            selection("Throwing Weapon", 2)
            selection("Paired Weapons and Throwing Weapon", 4)
        }
        command()
        totemBearer()
        bannerEnchantmentOption()
    }

    val MongrelHerd = troop("Mongrel Herd", 140, 8, 20, 50){
        ambush(20)
        closeCombatWeapon {
            spear()
        }
        command()
        bannerEnchantmentOption()
    }

    val MongrelRaiders = troop("Mongrel Raiders", 95, 7, 10, 20){
        implicitOption("AmbushAndScout", 20, "Ambush and Scout")
        championMusican()
    }

    val FeralHounds = troop("Feral Hounds", 80, 8, 5, 20){

    }

    val LonghornHerd = troop("Longhorn Herd", 155, 23, 10, 40){
        ambush(20)
        closeCombatWeapon {
            halberd{default()}
            greatWeapon()
        }
        command()
        totemBearer()
        bannerEnchantmentOption()
    }

    val Minotaurs = troop("Minotaurs", 235, 78, 3, 10){
        shield(6)
        closeCombatWeapon {
            greatWeapon (10)
            pairedWeapons(10)
        }
        command()
        totemBearer()
        bannerEnchantmentOption()
    }

    val Centaurs = troop("Centaurs", 165, 25, 5, 15){
        implicitOption("Ambush", 3)
        rangedWeapon {
            throwingWeapon(4)
        }
        closeCombatWeapon {
            greatWeapon (3)
            pairedWeapons(4)
            lance(8)
        }
        command()
        totemBearer()
        bannerEnchantmentOption()
    }

    val RaidingChairot = troop("Raiding Chariot", 110, 110, 1,3) {

    }

    val RazortuskHerd = troop("Razortusk Herd", 100, 62, 1, 10){

    }

    val RazortuskChariot = singleModel("Razortusk Chariot", 230){

    }

    val BriarBeast = singleModel("Briar Beast", 120)

    val Gargoyles = troop("Gargoyles", 135, 17, 5,10){
        implicitOptionUnit("Scout", 15)
    }

    val Cyclops = singleModel("Cyclops", 355)
    val Gortach = singleModel("Gortach", 475)
    val Jabberwock = singleModel("Jabberwock", 340)
    val BeastGaint= singleModel("Beast Giant", 300) {
        implicitOption("BigBrother", 35, "Big Brother")
        closeCombatWeapon {
            selection("Uprooted Tree", 10)
            selection("Beer Barrel", 20)
            selection("Giant Club", 30)
        }
    }

    override val entries: List<ArmyBookEntry> =
        listOf(
            BeastLord,
            BeastChieftain,
            Soothsayer,
            MinotaurWarlord,
            MinotuarChieftain,
            CentuarChieftain,
            WildhornHeard,
            MongrelHerd,
            MongrelRaiders,
            FeralHounds,
            LonghornHerd,
            Minotaurs,
            Centaurs,
            RaidingChairot,
            RazortuskHerd,
            RazortuskChariot,
            BriarBeast,
            Gargoyles,
            Cyclops,
            Gortach,
            Jabberwock,
            BeastGaint
        )
}

fun OptionBuilder.ambush(points: Int) {
    this.implicitOptionUnit("Ambush", points)
}

fun OptionBuilder.totemBearer(){
    optionWithDefault("TotemBearer", "None"){
        selection("Black Wing Totem", 15)
        selection("Blooded Horn Totem", 15)
        selection("Clouded Eye Totem " , 15)
        selection("Gnarled Hide Totem", 15)
    }
}