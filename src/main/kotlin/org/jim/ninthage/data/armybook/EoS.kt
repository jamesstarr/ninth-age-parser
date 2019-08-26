package org.jim.ninthage.data.armybook

import org.jim.ninthage.models.ArmyBook
import org.jim.ninthage.models.ArmyBookEntry
import org.jim.ninthage.models.ArmyBookEntryOption
import org.jim.ninthage.models.ArmyBookEntryOptionSelection

object EoS {
    public val Version2_2 =
        ArmyBook(
            name = "EoS",
            entries = listOf(
                character(name = "Marshal", points = 160) {
                    listOf(
                        battleStandardBearer(0),
                        shield(5),
                        closeCombatWeapon(
                            pairedWeapons(5),
                            greatWeapon(10),
                            halberd(10),
                            lance(10)
                        ),
                        option("ShootingWeapon", "None") {
                            listOf(
                                selection("Pistol", 5)
                            )
                        },
                        mount(
                            selection("Horse", 70),
                            selection("Pegasus", 75),
                            selection("Great Griffon", 150),
                            selection("Dragon", 460)
                        ),
                        title(
                            selection("Great Tactician", 60),
                            selection("Imperial Prince", 175)
                        )
                    )
                },
                character(name = "Knight Commander", points = 200) {
                    listOf(
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
                },
                character(name = "Prelate", points = 160) {
                    listOf(
                        option("Armour", "Heavy Armour") {
                            listOf(
                                selection("Plate Armour", 20)
                            )
                        },
                        shield(5),
                        closeCombatWeapon(
                            greatWeapon(10),
                            pairedWeapons(5)
                        ),
                        mount(
                            selection("Horse", 40),
                            selection("Alter of Battle", 370)
                        )
                    )
                },
                character(name = "Wizard", points = 125) {
                    listOf(
                        option("Armour", "None") {
                            listOf(
                                selection("Light Armour", 5)
                            )
                        },
                        wizard()
                        ,
                        closeCombatWeapon(
                            greatWeapon(10),
                            pairedWeapons(5)
                        ),
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
                },
                character(name = "Artificer", points = 125) {
                    listOf(
                        option("ShootingWeapon", "None") {
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
                },
                character(name = "Inquisitor", points = 130) {
                    listOf(
                        shield(5),
                        closeCombatWeapon(
                            pairedWeapons(5),
                            greatWeapon(10),
                            halberd(10)
                        ),
                        option("ShootingWeapon", "None") {
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
                            selection("Horse and gain Light Troops", 70)
                        )
                    )
                },
                troop("Heavy Infantry", 145, 20, 30, 10) {
                    listOf(
                        closeCombatWeapon(
                            spear(1),
                            halberd(1)
                        ),
                        command(),
                        eosBannerEnchantment()
                    )
                },
                troop("Light Infantry", 135, 10, 10, 13) {
                    listOf(
                        option("ShootingWeapon", "Crossbow") {
                            listOf(
                                selection("Handgun")
                            )
                        },
                        unitOption("ChampionShootingWeapon", "None") {
                            listOf(
                                selection("Repeater Gun", 15),
                                selection("Long Rifle", 15)
                            )
                        },
                        command(),
                        eosBannerEnchantment()
                    )
                },
                troop("State Militia", 140, 10, 15, 10) {
                    listOf(
                        booleanOption("Irregulars", 1),
                        command()
                    )
                },
                troop("Electoral Cavalry", 155, 5, 10, 29) {
                    listOf(
                        shield(4),
                        closeCombatWeapon(
                            lance(4),
                            greatWeapon(4),
                            selection("Calvary Pick", 0)
                        ),
                        command(),
                        eosBannerEnchantment(),
                        booleanOption("KnightOrders", 9)
                    )
                },
                troop("Imperial Guard", 180, 15, 25, 19) {
                    listOf(
                        booleanOption("ReplaceShieldWithGreatWeapon", 3),
                        command(),
                        eosBannerEnchantment()
                    )
                },
                troop("Knights of the Sun Griffon", 290, 3, 3, 95) {
                    listOf(
                        booleanOption("ReplaceHalberdWithLance", 12),
                        command(),
                        eosBannerEnchantment()
                    )
                },
                singleModel("Arcane Engine", 290) {
                    listOf(
                        requiredOption("Engine") {
                            listOf(
                                selection("Foresight"),
                                selection("Arcane Shield")
                            )
                        }
                    )
                },
                troop("Imperial Rangers", 90, 5, 5, 12) {
                    listOf(
                        champion()
                    )
                },
                troop("Reiters", 150, 5, 5, 29) {
                    listOf(
                        option("Armour", "Light Armour") {
                            listOf(
                                selection("Heavy Armour", 4)
                            )
                        },
                        option("ShootingWeapon", "Pistol") {
                            listOf(
                                selection("Brace of Pistol", 4),
                                selection("Repeater Gun", 4)
                            )
                        },
                        championMusican(),
                        unitOption("ChampionShootingWeapon", "None") {
                            listOf(
                                selection("Repeater Pistol", 10)
                            )
                        }
                    )
                },
                singleModel("Artillery", 0) {
                    listOf(
                        requiredOption("ArtilleryWeapons") {
                            listOf(
                                selection("Mortar", 200),
                                selection("Volley Gun", 200),
                                selection("Imperial Rocketeer", 200),
                                selection("Cannon", 200)
                            )
                        }
                    )
                },
                troop("Flagellants", 200, 15, 15, 18) {
                    listOf(
                        champion()
                    )
                },
                singleModel("Steam Tank", 475)
            )
        )
}




private fun character(
    name: String,
    points: Int,
    attributes: () -> List<ArmyBookEntryOption>
): ArmyBookEntry {
    return ArmyBookEntry(name, points, 1, 1, attributes())
}



private fun wizard(): ArmyBookEntryOption {
    return option(
        "Wizard",
        "Wizard Apprentice"
    ) {
        listOf(
            selection("Wizard Adept", 75),
            selection("Wizard Master", 225)
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
        default = "None"
    )
}




private fun eosBannerEnchantment(): ArmyBookEntryOption {
    return bannerEnchantment(
        selection("Household Standard", 40),
        selection("Banner of Unity", 40),
        selection("Marksman’s Pennant", 40)
    )
}

