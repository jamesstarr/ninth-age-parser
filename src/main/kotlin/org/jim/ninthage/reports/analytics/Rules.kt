package org.jim.ninthage.reports.analytics

import org.jim.ninthage.data.armybook.dsl.Enchantment
import org.jim.ninthage.models.ArmyBook
import org.jim.ninthage.models.ArmyBookEntry
import org.jim.ninthage.models.Roster
import org.jim.ninthage.models.RosterUnit
import org.jim.ninthage.models.Tournament


object Rules {

}


object RulesDSL {

    fun ArmyBook.countEnchantments(enchantmentLabel:String,enchantments: Array<out Enchantment>):Sequence<RosterUnitCounterRule> {
        val armyBook = this

        return enchantments.map { enchantment ->
            RosterUnitCounterRule(
                "special item",
                enchantment.label,
                {armyBook.isArmyBook(it)},
                {unit-> unit.hasSelection(enchantmentLabel, enchantment.label)}
            )
        }.asSequence()
    }

    fun ArmyBook.countUnitEntry(
        entry: ArmyBookEntry
    ):Sequence<RosterUnitCounterRule> {
        return sequenceOf(
            RosterUnitCounterRule(
                entry.label,
                "base cost",
                { roster -> roster.armyBook == this.shortLabel },
                { unit -> unit.label == entry.label }
            )
        )
    }

    fun ArmyBook.countBigSmall(
        entry: ArmyBookEntry,
        smallCountUpperBound: Int
        ):Sequence<RosterUnitCounterRule> {

        return sequence{
            yieldAll(countByModels(entry, "small unit (${entry.minCount}-${smallCountUpperBound})"){it <= smallCountUpperBound})
            yieldAll(countByModels(entry, "large unit (${smallCountUpperBound}-${entry.maxCount})"){it > smallCountUpperBound})
        }
    }

    fun ArmyBook.countByModels(
        entry: ArmyBookEntry,
        ruleName: String,
        modelsFilter: (Int) -> Boolean
    ): Sequence<RosterUnitCounterRule> {
        val armyBook = this

        return sequenceOf(RosterUnitCounterRule(
            entry.label,
            ruleName,
            {armyBook.isArmyBook(it)},
            { unit -> unit.label == entry.label && modelsFilter.invoke(unit.modelCount) }
        ))
    }

    fun ArmyBook.countOptions(entry: ArmyBookEntry, vararg optionLabels: String): Sequence<RosterUnitCounterRule> {
        val armyBook = this


        return sequence {
            for(optionLabel in optionLabels) {
                val option = entry.option(optionLabel)
                for (selection in option.selections) {
                    if (option.isDefault(selection)) {
                        continue
                    }
                    yield(RosterUnitCounterRule(
                        entry.label,
                        selection.name,
                        { armyBook.isArmyBook(it) },
                        { unit -> unit.label == entry.label && unit.option(option.name) == selection.name }
                    ))
                }
            }
        }
    }


    fun ArmyBook.countOption(entry: ArmyBookEntry, optionStr: String): Sequence<RosterUnitCounterRule> {
        val option = entry.option(optionStr)
        val armyBook = this
        return sequence {
            for(selection in option.selections) {
                if(option.isDefault(selection)){
                    continue
                }
                yield(RosterUnitCounterRule(
                    entry.label,
                    selection.name,
                    {armyBook.isArmyBook(it)},
                    { unit -> unit.label == entry.label && unit.option(option.name) == selection.name }
                ))
            }
        }
    }


    fun ArmyBook.isArmyBook(roster:Roster):Boolean {
        return  this.shortLabel == roster.armyBook
    }
}

class RosterUnitCounterRule(
    val ruleCat: String,
    val ruleName: String,
    val rosterFilter: (Roster) -> Boolean,
    val unitFilter: (RosterUnit) -> Boolean
) {
    var count = 0
    var rosterCount = 0


    fun processRule(tournament: Tournament) {
        tournament.roster
            .filter(rosterFilter)
            .forEach { roster ->
                val units = roster.units.filter(unitFilter)
                if (units.size > 0) {
                    rosterCount += 1
                    count += units.sumBy { unit -> unit.entryCount }
                }
            }
    }
}