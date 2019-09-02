package org.jim.ninthage.reports

import com.google.common.base.Joiner
import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Ordering
import org.jim.ninthage.App
import org.jim.ninthage.data.ArmyBooks
import org.jim.ninthage.models.ArmyBook
import org.jim.ninthage.models.ArmyBookEntry
import org.jim.ninthage.models.Roster
import org.jim.ninthage.models.RosterUnit
import org.jim.ninthage.models.RosterUnitOption
import org.jim.ninthage.models.Tournament
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

class TournamentReportPrinter(
    val dir: Path = App.HomeDirectory.resolve("tournaments")
) {
    val armyBookReporter = ArmyBookReporter()

    fun printTournament(tournament: Tournament) {
        val tournamentDir = dir.resolve(tournament.name)
        Files.createDirectories(tournamentDir)
        Files.writeString(
            tournamentDir.resolve("raw.txt"),
            tournament.rawString
        )

        printTeams(tournament, tournamentDir)
        printArmyBooks(tournament, tournamentDir)
        printUnitEntry(tournament, tournamentDir)
        printAnalytics(tournament, tournamentDir)
    }

    private fun printAnalytics(tournament: Tournament, tournamentDir: Path) {
        EmpireOfSonnstahlTournamentAnalysis().apply {
            analysis(tournament)
            print(tournament.name, tournamentDir.resolve(tournament.name + ".csv"))
        }
    }

    private fun printTeams(tournament: Tournament, tournamentDir: Path) {
        if (!tournament.isTeam) {
            return
        }
        val teamDir = tournamentDir.resolve("teams")
        Files.createDirectories(teamDir)

        for (team in tournament.teams) {
            val teamFile = teamDir.resolve("${team.name}.txt")
            Files.writeString(
                teamFile,
                armyListsToString(team.lists),
                Charsets.UTF_8
            )
        }
    }

    private fun printArmyBooks(tournament: Tournament, tournamentDir: Path) {
        val armyBookToLists =
            ArrayListMultimap.create<String, Roster>()
        tournament.roster.forEach { armyBookToLists.put(it.armyBook, it) }


        val armyBookCount = armyBookReporter.tournamentToCount(tournament)
        Files.writeString(
            tournamentDir.resolve("armyBook.txt"),
            Joiner.on("\n")
                .join(armyBookCount.entries.map { (armyBook, count) -> "$armyBook, $count" })
        )

        val armyBookDir = tournamentDir.resolve("armyBook")
        Files.createDirectories(armyBookDir)

        armyBookToLists.keySet().sorted()
            .forEach { key ->
                Files.writeString(
                    armyBookDir.resolve("$key.txt"),
                    armyListsToString(armyBookToLists.get(key)),
                    Charsets.UTF_8
                )
            }
    }

    private fun armyListsToString(rosters: List<Roster>): String {
        return Joiner.on("\n\n\n")
            .join(rosters.map { it.raw })
    }

    private fun printUnitEntry(tournament: Tournament, tournamentDir: Path) {
        val armyBooks = tournament.roster.map { it.armyBook }.toHashSet().sorted()
        val unitDir = tournamentDir.resolve("unitEntry")
        Files.createDirectories(unitDir)
        for (armyBookStr in armyBooks) {

            val unitEntries =
                tournament.roster
                    .stream()
                    .filter { it.armyBook == armyBookStr }
                    .flatMap { it.units.stream() }
                    .sorted(
                        Ordering.natural<String>()
                            .onResultOf{input -> input!!.label }
                    )
                    .map { unit ->
                        printUnit(armyBookStr, unit)
                    }

                    .collect(Collectors.toList())
            Files.writeString(
                unitDir.resolve("$armyBookStr.txt"),
                Joiner.on("\n").join(unitEntries)
            )


        }
    }

    fun printUnit(
        armyBookStr: String,
        unit: RosterUnit
    ): String {
        return if (armyBookStr != "EoS") {
            "<Unit name=\"${unit.label}\">"
        } else if (unit.label == "Header" || unit.label == "Footer") {
            "<Unit name=\"${unit.label}\">${unit.raw}"
        } else {
            val armyBook = ArmyBooks.get(armyBookStr)
            val armyBookEntry = armyBook.entry(unit.label)
            val sb =
                java.lang.StringBuilder("<Unit name=\"${unit.label}\" ")
            printCount(armyBookEntry, unit, sb)
            unit.options.forEach {
                printOption(
                    armyBook,
                    armyBookEntry,
                    it,
                    sb
                )

            }

            sb.append("\n>${unit.raw.trim()}").toString()
        }
    }

    fun printCount(
        entry: ArmyBookEntry,
        unit: RosterUnit,
        sb: StringBuilder
    ) {
        if (unit.entryCount != 1) {
            sb
                .append("EntryCount=").append(unit.entryCount).append(" ")
        }
        if (entry.minCount == 1 && unit.modelCount == 1) {
            return
        }
        sb.append("ModelCount=").append(unit.modelCount).append(" ")
    }

    fun printOption(
        armyBook: ArmyBook,
        entry: ArmyBookEntry,
        rosterUnitOption: RosterUnitOption,
        sb: StringBuilder
    ): StringBuilder {
        val abOption = entry.option(rosterUnitOption.label)
        return if (rosterUnitOption.values.isEmpty()) {
            sb
        } else if (rosterUnitOption.values.size == 1) {
            val value = rosterUnitOption.values[0]
            if (value == abOption.default) {
                sb
            } else if (value == abOption.implicit) {
                sb.append("\n\t").append(abOption.name)
            } else {
                sb.append("\n\t").append(abOption.name)
                    .append("=")
                    .append(quoteIfNecessary(value))
            }
        } else {
            rosterUnitOption.values.forEach { v ->
                sb.append("\n\t").append(abOption.name).append("=").append(quoteIfNecessary(v))
            }
            sb
        }
    }

    fun quoteIfNecessary(value: String): String {
        return if (value.contains(Regex("\\s"))) {
            "\"${value}\""
        } else {
            value
        }
    }

    fun isDefault(
        armyBook: String,
        entry: String,
        option: String,
        values: List<String>
    ): Boolean {
        val ab = ArmyBooks.get(armyBook)
        val selections = ArmyBooks
            .get(armyBook)
            .entry(entry)
            .option(option)
        val dv = selections.default
        return if (null == dv) {
            false
        } else {
            dv == values[0]
        }
    }
}