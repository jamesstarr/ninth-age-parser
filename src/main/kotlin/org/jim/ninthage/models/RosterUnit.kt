package org.jim.ninthage.models

data class RosterUnit(
    val raw: String,
    val name: String,
    val options: List<RosterUnitOption> = listOf(),
    val points:Int? =null,
    val modelCount:Int =1,
    val entryCount:Int =1
){
    fun option(optionName:String):String {
        return options.find { it.name == optionName }!!.values[0]
    }
}