package org.jim.ninthage.data.armybook

import org.jim.ninthage.models.ArmyBook
import org.jim.ninthage.models.ArmyBookEntry
import org.jim.ninthage.models.ArmyBookEntryOption
import org.jim.ninthage.models.ArmyBookEntryOptionSelection
import org.jim.ninthage.models.RosterUnit

interface StandardArmyBook : ArmyBook {
    val arcaneCompendium: ArcaneCompendium
    val customArmourEnchantments: List<Enchantment>
    val customArtifacts: List<Enchantment>
    val customBannerEnchantments: List<Enchantment>
    val customShieldEnchantments: List<Enchantment>
    val customWeaponEnchantments: List<Enchantment>


    val enchantments: List<Enchantment>
        get() =
            ArrayList<Enchantment>().apply {
                addAll(customArmourEnchantments)
                addAll(customWeaponEnchantments)
                addAll(customBannerEnchantments)
                addAll(customArtifacts)
            }.toList()

    object DSL_Roster {


        fun StandardArmyBook.character(
            name: String,
            points: Int,
            attributes: () -> List<ArmyBookEntryOption>
        ): ArmyBookEntry {
            return ArmyBookEntry(
                name,
                points,
                0,
                1,
                1,
                ArrayList<ArmyBookEntryOption>().apply {
                    val attrs = attributes()
                    addAll(attrs)
                    add(armourEnchantmentOption())
                    add(artifacts())
                    add(shieldEnchantmentOption())
                    add(weaponEnchantmentOption())
                }
            )
        }

        private fun StandardArmyBook.artifactsOption():ArmyBookEntryOption {
            return option(ArcaneCompendiums.BannerEnchantment, "None") {
                ArrayList<Enchantment>().apply {
                    addAll(customArtifacts)
                    addAll(arcaneCompendium.artefacts)
                }.map { it.toSelection() }.toList()
            }
        }


        private fun StandardArmyBook.artifacts(): ArmyBookEntryOption {
            return ArmyBookEntryOption(
                ArcaneCompendiums.Artifact,
                ArrayList<Enchantment>().apply {
                    addAll(customArtifacts)
                    addAll(arcaneCompendium.artefacts)
                }.map { it.toSelection() }.toList(),
                null,
                null,
                0,
                2
            )
        }

        private fun StandardArmyBook.armourEnchantmentOption(): ArmyBookEntryOption {
            return option(ArcaneCompendiums.ArmourEnchantment, "None") {
                ArrayList<Enchantment>().apply {
                    addAll(customArmourEnchantments)
                    addAll(arcaneCompendium.armourEnchantment)
                }.map { it.toSelection() }.toList()
            }
        }


        fun StandardArmyBook.bannerEnchantmentOption(): ArmyBookEntryOption {
            return option(ArcaneCompendiums.BannerEnchantment, "None") {
                ArrayList<Enchantment>().apply {
                    addAll(customBannerEnchantments)
                    addAll(arcaneCompendium.bannerEnchantments)
                }.map { it.toSelection() }.toList()
            }
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

        private fun StandardArmyBook.shieldEnchantmentOption(): ArmyBookEntryOption {
            return option(ArcaneCompendiums.ShieldEnchantment, "None") {
                ArrayList<Enchantment>().apply {
                    addAll(customShieldEnchantments)
                    addAll(arcaneCompendium.shieldEnchantments)
                }.map { it.toSelection() }.toList()
            }
        }

        private fun StandardArmyBook.weaponEnchantmentOption(): ArmyBookEntryOption {
            return option(ArcaneCompendiums.WeaponEnchantment, "None") {
                ArrayList<Enchantment>().apply {
                    addAll(customWeaponEnchantments)
                    addAll(arcaneCompendium.weaponEnchantments)
                }.map { it.toSelection() }.toList()
            }
        }

        interface Mount {
            val label:String
            operator fun invoke(points:Int):ArmyBookEntryOptionSelection {
                return selection(label, points)
            }

        }

        fun pathsOfMagicOption (
            vararg paths:ArcaneCompendiums.MagicPaths
        ):ArmyBookEntryOption {
            return ArmyBookEntryOption(
                ArcaneCompendiums.MagicPath,
                paths.map { selection(it.name) },
                null,
                null,
                1,
                2
            )
        }
    }

    object Utilities {

        fun totalMagicItems(armyBook: StandardArmyBook, rosterUnit: RosterUnit): Int {
            val enchantments =
                armyBook.enchantments
                    .map { Pair(it.label, it.points) }
                    .toMap()
            return rosterUnit.options.stream()
                .flatMap { option ->
                    option.values.stream()
                }.mapToInt { enchantments.getOrDefault(it, 0) }
                .sum()
        }
    }


}

