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
        val op = options.find { it.name == optionName }
            ?: throw RuntimeException("$label:$optionName")
        return op.values[0]
    }
}