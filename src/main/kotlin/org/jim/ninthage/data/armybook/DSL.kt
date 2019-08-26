package org.jim.ninthage.data.armybook

import org.jim.ninthage.models.ArmyBookEntry
import org.jim.ninthage.models.ArmyBookEntryOption
import org.jim.ninthage.models.ArmyBookEntryOptionSelection


fun booleanOption(name: String, points: Int): ArmyBookEntryOption {
    return option("Irregulars", "False") {
        listOf(selection("True", 1))
    }
}


fun singleModel(
    name: String,
    basePoints: Int,
    attributes: () -> List<ArmyBookEntryOption> = { listOf() }
): ArmyBookEntry {
    return ArmyBookEntry(name, basePoints, 1, 0, attributes())
}

fun troop(
    name: String,
    basePoints: Int,
    startModels: Int,
    additionalModels: Int,
    pointsPerModel: Int,
    attributes: () -> List<ArmyBookEntryOption>
): ArmyBookEntry {
    return ArmyBookEntry(name, basePoints, startModels, startModels + additionalModels, attributes())
}

fun selection(name: String, points: Int = 0): ArmyBookEntryOptionSelection {
    return ArmyBookEntryOptionSelection(name, points)
}


fun unitOption(
    name: String,
    default: String,
    selections: () -> List<ArmyBookEntryOptionSelection>
): ArmyBookEntryOption {
    val selectionList = ArrayList<ArmyBookEntryOptionSelection>()
    selectionList.addAll(selections())
    selectionList.add(ArmyBookEntryOptionSelection(default, 0))
    return ArmyBookEntryOption(
        name = name,
        selections = selectionList,
        default = "None"
    )
}


fun requiredOption(
    name: String,
    selections: () -> List<ArmyBookEntryOptionSelection>
): ArmyBookEntryOption {
    val selectionList = ArrayList<ArmyBookEntryOptionSelection>()
    selectionList.addAll(selections())
    return ArmyBookEntryOption(
        name = name,
        selections = selectionList,
        default = null
    )
}

fun option(
    name: String,
    default: String,
    selections: () -> List<ArmyBookEntryOptionSelection>
): ArmyBookEntryOption {
    val selectionList = ArrayList<ArmyBookEntryOptionSelection>()
    selectionList.addAll(selections())
    selectionList.add(ArmyBookEntryOptionSelection(default, 0))
    return ArmyBookEntryOption(
        name = name,
        selections = selectionList,
        default = default
    )
}


fun battleStandardBearer(points: Int = 50): ArmyBookEntryOption {
    return ArmyBookEntryOption(
        name = "BattleStandardBear",
        selections = listOf(
            ArmyBookEntryOptionSelection("True", points),
            ArmyBookEntryOptionSelection("False", 0)
        ),
        default = "False"
    )
}


fun shield(points: Int): ArmyBookEntryOption {
    return ArmyBookEntryOption(
        name = "Shield",
        selections = listOf(
            selection("True", points),
            selection("False", 0)
        ),
        default = "False"
    )
}


fun bannerEnchantment(
    vararg armyBookBanners: ArmyBookEntryOptionSelection
): ArmyBookEntryOption {
    return option("BannerEnchantment", "None") {
        listOf(
            selection("Banner of Speed", 50),
            selection("Rending Banner", 45),
            selection("Stalker’s Standard", 45),
            selection("Banner of the Relentless Company", 40),
            selection("Banner of Discipline", 35),
            selection("Flaming Standard", 35),
            selection("Legion Standard", 25),
            selection("Aether Icon", 15)
        ).toMutableList()
            .apply { addAll(armyBookBanners.toMutableList()) }

    }
}


fun champion(): ArmyBookEntryOption {
    return option("Command", "None") {
        listOf(
            selection("C", 20)
        )
    }
}

fun championMusican(): ArmyBookEntryOption {
    return option("Command", "None") {
        listOf(
            selection("C", 20),
            selection("M", 20),
            selection("CM", 40)
        )
    }
}


fun command(): ArmyBookEntryOption {
    return option("Command", "None") {
        listOf(
            selection("C", 20),
            selection("M", 20),
            selection("S", 20),
            selection("CM", 40),
            selection("CS", 40),
            selection("MS", 20),
            selection("CMS", 60)
        )
    }
}


fun closeCombatWeapon(
    vararg selection: ArmyBookEntryOptionSelection
): ArmyBookEntryOption {
    val options = ArrayList<ArmyBookEntryOptionSelection>()
    options.addAll(selection)
    options.add(ArmyBookEntryOptionSelection("Hand Weapon", 0))
    return ArmyBookEntryOption(
        name = "CloseCombatWeapon",
        selections = options,
        default = "Hand Weapon"

    )
}

fun pairedWeapons(points: Int = 0): ArmyBookEntryOptionSelection {
    return ArmyBookEntryOptionSelection("Paired Weapons", points)
}


fun lightLance(points: Int = 0): ArmyBookEntryOptionSelection {
    return ArmyBookEntryOptionSelection("Light Lance", points)
}


fun spear(points: Int = 0): ArmyBookEntryOptionSelection {
    return ArmyBookEntryOptionSelection("Spear", points)
}

fun greatWeapon(points: Int = 0): ArmyBookEntryOptionSelection {
    return ArmyBookEntryOptionSelection("Great Weapon", points)
}

fun lance(points: Int = 0): ArmyBookEntryOptionSelection {
    return ArmyBookEntryOptionSelection("Lance", points)
}

fun halberd(points: Int = 0): ArmyBookEntryOptionSelection {
    return ArmyBookEntryOptionSelection("Halberd", points)
}

fun handWeapon(points: Int = 0): ArmyBookEntryOptionSelection {
    return ArmyBookEntryOptionSelection("Hand Weapon", points)
}