package org.jim.ninthage.data.armybook

import org.jim.ninthage.models.ArmyBookEntryOption
import org.jim.ninthage.models.ArmyBookEntryOptionSelection

interface ArcaneCompendium {
    val armourEnchantment: List<Enchantment>
    val artefacts: List<Enchantment>
    val bannerEnchantments: List<Enchantment>
    val shieldEnchantments: List<Enchantment>
    val weaponEnchantments: List<Enchantment>
}

object ArcaneCompendiums {
    val Version2_0 = object : ArcaneCompendium {
        override val armourEnchantment: List<Enchantment>
            get() = ArmourEnchantments.values().toList()
        override val artefacts: List<Enchantment>
            get() = Artefacts.values().toList()
        override val bannerEnchantments: List<Enchantment>
            get() = BannerEnchantments.values().toList()
        override val shieldEnchantments: List<Enchantment>
            get() = ShieldEnchantments.values().toList()
        override val weaponEnchantments: List<Enchantment>
            get() = WeaponEnchantments.values().toList()

    }

    val ArmourEnchantment = "ArmourEnchantment"
    val BannerEnchantment = "BannerEnchantment"
    val ShieldEnchantment = "ShieldEnchantment"
    val WeaponEnchantment = "WeaponEnchantment"
    val Artifact = "Artefact"
    val MagicPath = "Path"

    enum class ArmourEnchantments(
        override val label: String,
        override val points: Int
    ) : Enchantment {
        DeathCheater("Death Cheater", 100),
        DestinysCall("Destiny's Call", 70),
        EssenceOfMithril("Essence of Mithril", 60),
        GhostlyGuard("Ghostly Guard", 40),
        BasultInfusion("Basalt Infusion", 35),
        AlchmeistAloy("Alchemist’s Alloy", 15),

    }

    enum class ShieldEnchantments(
        override val label: String,
        override val points: Int
    ) : Enchantment {
        DuskForge("Dusk Forge", 50),
        WillowWard("Willow's Ward", 15)

    }

    enum class WeaponEnchantments(
        override val label: String,
        override val points: Int
    ) : Enchantment {
        BlessedInscription("Blessed Inscriptions", 65),
        TouchOfGreatness("Touch of Greatness", 50),
        TitanicMight("Titanic Might", 65),
        ShieldBreaker("Shield Breaker", 40),
        HeroHeart("Hero's Heart", 60),
        SupernaturalDexterity("Supernatural Dexterity", 30),
        KingSlayer("King Slayer", 60),
        CleansingLight("Cleansing Light", 20)

    }

    enum class Artefacts(
        override val label: String,
        override val points: Int
    ) : Enchantment {
        CrownOfAutocracy("Crown of Autocracy", 70),
        BookOfArcaneMastery("Book of Arcane Mastery", 60),
        BindingScroll("Binding Scroll", 55),
        EssenceOfAFreeMind("Essence of a Free Mind", 55),
        CrownOfWizardKing("Crown of the Wizard King", 25),
        MagicalHeirloom("Magical Heirloom", 50),
        TalismanOfShielding("Talisman of Shielding", 50),
        TalismanOfTheVoid("Talisman of the Void", 50),
        LightningVambraces("Lightning Vambraces", 45),
        RangersBoots("Ranger's Boot", 45),
        RodOfBattle("Rod of Battle", 45),
        CrystalBall("Crystal Ball", 40),
        SceptreOfPower("Sceptre of Power", 40),
        DragonStaff("Dragon Staff", 30),
        ObsidianRock("Obsidian Rock", 25),
        DragonfireGem("Dragonfire Gem", 20),
        LuckyCharm("Lucky Charm", 10),
        PotionOfStrength("Potion of Strength", 10),
        PotionOfSwiftness("Potion of Swiftness", 10)
    }

    enum class BannerEnchantments(override val label: String, override val points: Int) : Enchantment {
        BannerOfSpeed("Banner of Speed", 50),
        RendingBanner("Rending Banner", 45),
        StalkersStandard("Stalker’s Standard", 45),
        BannerOfTheRelentlessCompany("Banner of the Relentless Company", 40),
        BannerOfDiscipline("Banner of Discipline", 35),
        FlamingStandard("Flaming Standard", 35),
        LegionStandard("Legion Standard", 25),
        AetherIcon("Aether Icon", 15);

        companion object {
            fun getForUnit(
                vararg enchantments: ArmyBookEntryOptionSelection
            ): ArmyBookEntryOption {
                return option(
                    BannerEnchantment,
                    "None"
                ) {
                    ArrayList<ArmyBookEntryOptionSelection>()
                        .apply {
                            addAll(values().map { selection(it.label, it.points) })
                            addAll(enchantments)
                        }
                }
            }

            fun getForBsb(
                vararg enchantments: ArmyBookEntryOptionSelection
            ): ArmyBookEntryOption {
                return ArmyBookEntryOption(
                    BannerEnchantment,
                    ArrayList<ArmyBookEntryOptionSelection>()
                        .apply {
                            values().map { selection(it.label, it.points) }
                            addAll(enchantments)
                        },
                    null,
                    null,
                    1,
                    2
                )
            }
        }
    }

    enum class MagicPaths {
        Alchemy,
        Cosmology,
        Divination,
        Druidism,
        Evocation,
        Occultism,
        Pyromancy,
        Shamanism,
        Thaumaturgy,
        Witchcraft
    }

}

