package org.jim.ninthage.reports

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.jim.ninthage.data.armybook.ArcaneCompendiums
import org.jim.ninthage.data.armybook.EmpireOfSonnstahl
import org.jim.ninthage.data.armybook.EoS_Version2_0
import org.jim.ninthage.data.armybook.MainBook
import org.jim.ninthage.models.ArmyBook
import org.jim.ninthage.models.Tournament
import org.jim.ninthage.reports.analytics.RosterUnitCounterRule
import org.jim.ninthage.reports.analytics.RulesDSL.countBigSmall
import org.jim.ninthage.reports.analytics.RulesDSL.countEnchantments
import org.jim.ninthage.reports.analytics.RulesDSL.countOption
import org.jim.ninthage.reports.analytics.RulesDSL.countOptions
import org.jim.ninthage.reports.analytics.RulesDSL.countUnitEntry
import java.nio.file.Files
import java.nio.file.Path


class EmpireOfSonnstahlTournamentAnalysis(
    val armyBook: EoS_Version2_0 = EoS_Version2_0()
) {
    var rosterCount: Long = 0
    val rules: List<RosterUnitCounterRule> =
        with(armyBook){
            listOf(
                countEnchantments(ArcaneCompendiums.WeaponEnchantment, EmpireOfSonnstahl.WeaponEnchantment.values()),
                countEnchantments(ArcaneCompendiums.ArmourEnchantment, EmpireOfSonnstahl.ArmourEnchantments.values()),
                countEnchantments(ArcaneCompendiums.ShieldEnchantment, EmpireOfSonnstahl.ShieldEnchantments.values()),
                countEnchantments(ArcaneCompendiums.Artifact, EmpireOfSonnstahl.Artefacts.values()),
                countEnchantments(ArcaneCompendiums.BannerEnchantment, EmpireOfSonnstahl.BannerEnchantments.values()),
                countUnitEntry(marshal),
                countOptions(marshal, MainBook.BattleStandardBear,MainBook.Shield, MainBook.CloseCombatWeapon, MainBook.RangedWeapon, "Title", MainBook.Mount),
                countUnitEntry(knightCommander),
                countOptions(knightCommander, MainBook.Shield, MainBook.CloseCombatWeapon,MainBook.Mount),
                countUnitEntry(prelate),
                countOptions(prelate, MainBook.Armour, MainBook.Shield, MainBook.CloseCombatWeapon, MainBook.Mount),
                countUnitEntry(wizard),
                countOptions(wizard, ArcaneCompendiums.WizardLevel, MainBook.Armour, MainBook.Mount, EmpireOfSonnstahl.ArcaneEngineType),
                countUnitEntry(artificer),
                countOptions(artificer, MainBook.RangedWeapon, MainBook.Mount),
                countUnitEntry(inquisitor),
                countOptions(inquisitor, MainBook.Shield, MainBook.CloseCombatWeapon, MainBook.RangedWeapon, MainBook.Title),
                countBigSmall(heavyInfantry, 30),
                countOption(heavyInfantry, MainBook.CloseCombatWeapon),
                countBigSmall(lightInfantry, 13),
                countOption(lightInfantry, MainBook.RangedWeapon),
                countOption(lightInfantry, EmpireOfSonnstahl.ChampionRangedWeapon),
                countBigSmall(electoralCavalry, 8),
                countOption(electoralCavalry, MainBook.Shield),
                countOption(electoralCavalry, MainBook.CloseCombatWeapon),
                countOption(electoralCavalry, EmpireOfSonnstahl.KnightOrders),
                countBigSmall(stateMilitia, 15),
                countOption(stateMilitia, EmpireOfSonnstahl.Irregulars),
                countBigSmall(imperialGuard, 23),
                countOption(imperialGuard, EmpireOfSonnstahl.ReplaceShieldWithGreatWeapon),
                countBigSmall(knightOfTheSunGriffon, 4),
                countOption(knightOfTheSunGriffon, EmpireOfSonnstahl.ReplaceHalberdWithLance),
                countBigSmall(imperialRangers, 7),
                countBigSmall(reiters, 7),
                countOption(reiters, MainBook.RangedWeapon),
                countOption(reiters, EmpireOfSonnstahl.ChampionRangedWeapon),
                countUnitEntry(artillery),
                countOption(artillery, "ArtilleryWeapons"),
                countBigSmall(flagellants, 20),
                countUnitEntry(steamTank)
            ).flatMap { it.asIterable() }
        }

    fun analysis(tournament: Tournament) {
        for (rule in rules) {
            rule.processRule(tournament)
        }
        rosterCount += tournament.roster.stream()
            .filter { it.armyBook == armyBook.shortLabel }
            .count()
    }

    fun print(name: String, path: Path) {
        Files.newBufferedWriter(path, Charsets.UTF_8).use { writer ->
            val csvWriter = CSVFormat.DEFAULT.print(writer)
            RosterCSVPrinter(
                csvWriter,
                armyBook,
                name,
                rosterCount,
                rules
            ).print()


        }
    }

}

private class RosterCSVPrinter(
    val csvPrinter: CSVPrinter,
    val armyBook: ArmyBook,
    val name: String,
    val rosterCount: Long,
    val rules: List<RosterUnitCounterRule>
) {
    fun print() {
        printHeader()
        printRules()
    }

    private fun printRules() {
        for (rule in rules) {
            csvPrinter.apply {
                print("")
                print(rule.ruleCat)
                print(rule.ruleName)
                print("")
                print(rule.count)
                print(rule.rosterCount)
                println()
            }
        }
    }

    private fun printHeader() {
        csvPrinter.apply {
            print("Tourneys entered:")
            print(name)
            print("Number of ${armyBook.shortLabel} Lists:")
            print(rosterCount)
            print("Number of usages:")
            print("Number of armies taking this at least once")
            println()
        }
    }
}