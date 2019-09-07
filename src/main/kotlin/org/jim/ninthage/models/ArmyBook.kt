package org.jim.ninthage.models

import com.google.common.base.Joiner

interface ArmyBook{
    val shortLabel: String
    val entries: List<ArmyBookEntry>

    fun entry(name: String): ArmyBookEntry {
        return entries.find { it.label == name }
            ?: throw RuntimeException(name)
    }
}

data class ArmyBookEntry(
    val label: String,
    val points: Int,
    val pointsPerModel: Int = 0,
    val minCount: Int = 1,
    val maxCount: Int = 1,
    val options: List<ArmyBookEntryOption> = listOf()
) {
    fun option(optionName: String): ArmyBookEntryOption {
        return options.find { it.name == optionName }
            ?: throw RuntimeException(optionName)
    }
}

data class ArmyBookEntryOption(
    val name: String,
    val selections: List<ArmyBookEntryOptionSelection>,
    val default: String? = null,
    val implicit: String? = null,
    val minSelection: Int = 1,
    val maxSelection: Int = 1,
    val costPerModel:Boolean = true
) {
    val isSimple: Boolean = maxSelection == 1
    val defaultValue: String
        get() = default ?: throw RuntimeException(name)

    fun isDefault(selection: ArmyBookEntryOptionSelection):Boolean {
        return if(default == null) {
             false
        } else {
            default == selection.name
        }
    }

    fun selection(name: String): ArmyBookEntryOptionSelection {
        return selections.find { it.name == name }
            ?: throw RuntimeException(name+ ":\n"
                    +Joiner.on(", ")
                .join(selections.map { it.name })
            )
    }
}

data class ArmyBookEntryOptionSelection(
    val name: String,
    val pointsPerModel: Int = 0,
    val points: Int = 0
)



