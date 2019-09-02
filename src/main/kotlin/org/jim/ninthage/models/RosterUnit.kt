package org.jim.ninthage.models

import java.lang.RuntimeException

data class RosterUnit(
    val raw: String,
    val label: String,
    val options: List<RosterUnitOption> = listOf(),
    val points:Int? =null,
    val modelCount:Int =1,
    val entryCount:Int =1
){
    fun option(optionName:String):String {
        val op = options.find { it.label == optionName }
            ?: throw RuntimeException("$label:$optionName")
        return op.values[0]
    }

    fun hasSelection(
        optionLabel:String,
        selectionLabel:String
    ):Boolean {
        for (option in options) {
            if(option.label == optionLabel) {
                return option.values.contains(selectionLabel)
            }
        }
        return false
    }
}