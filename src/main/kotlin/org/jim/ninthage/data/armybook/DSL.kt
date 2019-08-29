package org.jim.ninthage.data.armybook

import org.jim.ninthage.models.ArmyBookEntry
import org.jim.ninthage.models.ArmyBookEntryOption
import org.jim.ninthage.models.ArmyBookEntryOptionSelection


interface Enchantment{
    val label:String
    val points:Int
    fun toSelection():ArmyBookEntryOptionSelection {
        return ArmyBookEntryOptionSelection(label, points)
    }
}

inline fun <reified T, reified R> option(
    name:String
): ArmyBookEntryOption
where T:Enchantment,
      T:Enum<T>,
      R:Enchantment,
      R:Enum<R>{
    enumValues<T>()
    return option(
        name,
        "None"
    ) {
        ArrayList<ArmyBookEntryOptionSelection>().apply {
            addAll(enumValues<T>().map { it.toSelection() })
            addAll(enumValues<R>().map { it.toSelection() })
        }
    }
}

fun booleanOption(name: String, points: Int): ArmyBookEntryOption {
    return option(name, "False") {
        listOf(selection("True", points))
    }
}


fun singleModel(
    name: String,
    basePoints: Int,
    attributes: () -> List<ArmyBookEntryOption> = { listOf() }
): ArmyBookEntry {
    return ArmyBookEntry(name, basePoints, 0,1, 0, attributes())
}

fun troop(
    name: String,
    startingPoints: Int,
    startModels: Int,
    additionalModels: Int,
    pointsPerModel: Int,
    attributes: () -> List<ArmyBookEntryOption>
): ArmyBookEntry {
    val basePoints = startingPoints - (startModels * pointsPerModel)
    return ArmyBookEntry(name, basePoints, pointsPerModel, startModels, startModels + additionalModels, attributes())
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
        default = "None",
        implicit = null,
        minSelection = 1
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
        default = null,
        implicit = null,
        minSelection = 1
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
        default = default,
        implicit = null,
        minSelection = 1
    )
}


fun battleStandardBearer(points: Int = 50): ArmyBookEntryOption {
    return ArmyBookEntryOption(
        name = MainBook.BattleStandardBear,
        selections = listOf(
            ArmyBookEntryOptionSelection("Battle Standard Bear", points),
            ArmyBookEntryOptionSelection("None", 0)
        ),
        default = "None",
        implicit = "Battle Standard Bear",
        minSelection = 1
    )
}


fun shield(points: Int): ArmyBookEntryOption {
    return ArmyBookEntryOption(
        name = "Shield",
        selections = listOf(
            selection("Shield", points),
            selection("None", 0)
        ),
        default = "None",
        implicit = "Shield",
        minSelection = 1
    )
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
        name = MainBook.CloseCombatWeapon,
        selections = options,
        default = "Hand Weapon",
        implicit = null,
        minSelection = 1

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