package org.jim.ninthage.models

import java.lang.Exception
import java.lang.RuntimeException

data class ArmyBook(
    val name:String,
    val entries:List<ArmyBookEntry>
) {
    fun entry(name:String):ArmyBookEntry {
        return entries.find { it.name == name }
            .let {
                if(it==null) {
                    throw RuntimeException(name)
                } else {
                    it
                }
            }
    }
}

data class ArmyBookEntry(
    val name:String,
    val points:Int,
    val minCount:Int = 1,
    val maxCount:Int = 1,
    val options:List<ArmyBookEntryOption> = listOf()
) {
    fun option(optionName:String):ArmyBookEntryOption {
        return options.find{it.name == optionName}
            ?: throw RuntimeException(optionName)
    }
}

data class ArmyBookEntryOption (
    val name:String,
    val selections:List<ArmyBookEntryOptionSelection>,
    val default:String?,
    val maxSelection:Int = 1
) {
    val isSimple:Boolean = maxSelection ==1
    val defaultValue:String
        get() = default ?:
            throw RuntimeException(name)
    fun selection(name:String): ArmyBookEntryOptionSelection {
        return selections.find { it.name == name } ?: throw RuntimeException(name)
    }
}

data class ArmyBookEntryOptionSelection (
    val name:String,
    val pointsPerModel:Int = 0,
    val points:Int = 0,
    val options:List<ArmyBookEntryOptionSelection> = listOf()
)



