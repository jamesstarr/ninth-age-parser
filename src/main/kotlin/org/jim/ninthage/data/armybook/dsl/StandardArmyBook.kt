package org.jim.ninthage.data.armybook.dsl

import org.jim.ninthage.data.armybook.ArcaneCompendium
import org.jim.ninthage.data.armybook.ArcaneCompendiums
import org.jim.ninthage.data.armybook.MainBook
import org.jim.ninthage.data.armybook.dsl.StandardArmyBook.DSL_Roster.troopLegacy
import org.jim.ninthage.models.*

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
            attributes: CharacterOptionBuilder.() -> Unit
        ): ArmyBookEntry {
            val builder = CharacterOptionBuilder(this)
            attributes.invoke(builder)

            return ArmyBookEntry(
                name,
                points,
                0,
                1,
                1,
                ArrayList<ArmyBookEntryOption>().apply {
                    addAll(builder.build())
                    add(armourEnchantmentOption())
                    add(artifacts())
                    add(shieldEnchantmentOption())
                    add(weaponEnchantmentOption())
                }
            )
        }


        fun StandardArmyBook.troop(
            name: String,
            startingPoints: Int,
            pointsPerModel: Int,
            startModels: Int,
            maxModels: Int,
            attributes: TroopOptionBuilder.() -> Unit
        ): ArmyBookEntry {

            val basePoints = startingPoints - (startModels * pointsPerModel)

            val builder = TroopOptionBuilder(this)
            attributes.invoke(builder)


            return ArmyBookEntry(
                name,
                basePoints,
                pointsPerModel,
                startModels,
                maxModels,
                builder.build()
            )
        }

        fun StandardArmyBook.troopLegacy(
            name: String,
            startingPoints: Int,
            startModels: Int,
            additionalModels: Int,
            pointsPerModel: Int,
            attributes: TroopOptionBuilder.() -> Unit
        ): ArmyBookEntry {
            val basePoints = startingPoints - (startModels * pointsPerModel)

            val builder = TroopOptionBuilder(this)
            attributes.invoke(builder)


            return ArmyBookEntry(
                name,
                basePoints,
                pointsPerModel,
                startModels,
                startModels + additionalModels,
                builder.build()
            )

        }

        fun StandardArmyBook.singleModel(
            name: String,
            basePoints: Int,
            options: OptionBuilder.() -> Unit = {}
        ): ArmyBookEntry {

            val builder = OptionBuilder(this)
            options.invoke(builder)

            return ArmyBookEntry(name, basePoints, 0, 1, 1, builder.build())
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
            return ArmyBookEntryOption(
                ArcaneCompendiums.ArmourEnchantment,
                ArrayList<ArmyBookEntryOptionSelection>().apply {
                    val cae = customArmourEnchantments
                        .map { it.toSelection() }
                    val bae = arcaneCompendium.armourEnchantment
                        .map { it.toSelection() }
                    add(ArmyBookEntryOptionSelection(MainBook.None))

                    addAll(bae)
                    addAll(cae)
                }.toList(),
                MainBook.None
            )
        }


        private fun StandardArmyBook.shieldEnchantmentOption(): ArmyBookEntryOption {
            return ArmyBookEntryOption(
                ArcaneCompendiums.ShieldEnchantment,
                ArrayList<ArmyBookEntryOptionSelection>().apply {
                    val cae = customShieldEnchantments
                        .map { it.toSelection() }
                    val bae = arcaneCompendium.shieldEnchantments
                        .map { it.toSelection() }
                    add(ArmyBookEntryOptionSelection(MainBook.None))

                    addAll(bae)
                    addAll(cae)
                }.toList(),
                MainBook.None
            )
        }
    }

    private fun StandardArmyBook.weaponEnchantmentOption(): ArmyBookEntryOption {
        return ArmyBookEntryOption(
            ArcaneCompendiums.WeaponEnchantment,
            ArrayList<ArmyBookEntryOptionSelection>().apply {
                val cae = customWeaponEnchantments
                    .map { it.toSelection() }
                val bae = arcaneCompendium.weaponEnchantments
                    .map { it.toSelection() }
                add(ArmyBookEntryOptionSelection(MainBook.None))

                addAll(bae)
                addAll(cae)
            }.toList(),
            MainBook.None
        )
    }


    interface Mount {
        val label: String
        operator fun invoke(points: Int): ArmyBookEntryOptionSelection {
            return ArmyBookEntryOptionSelection(label, points)
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

