package org.jim.ninthage.reports

import com.google.common.base.Function
import com.google.common.base.Joiner
import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Ordering
import org.jim.ninthage.App
import org.jim.ninthage.data.ArmyBooks
import org.jim.ninthage.models.Roster
import org.jim.ninthage.models.RosterUnit
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
        for (armyBook in armyBooks) {
            val unitEntries =
                tournament.roster
                    .stream()
                    .filter { it.armyBook == armyBook }
                    .flatMap { it.rosterUnits.stream() }
                    .sorted(
                        Ordering.natural<String>()
                            .onResultOf(object : Function<RosterUnit, String> {
                                override fun apply(input: RosterUnit?): String? {
                                    return input!!.name
                                }
                            })
                    )
                    .map { entry ->
                        "<Unit name=\"${entry.name}\" " +
                                "${Joiner.on(" ")
                                    .join(
                                        entry.rosterUnitOptions
                                            .filter {
                                                !isDefault(
                                                    armyBook,
                                                    entry.name,
                                                    it.name,
                                                    it.values
                                                )
                                            }
                                            .map { it.name + "=" + it.values[0] })
                                }>${entry.raw}"
                    }

                    .collect(Collectors.toList())
            Files.writeString(
                unitDir.resolve("$armyBook.txt"),
                Joiner.on("\n").join(unitEntries)
            )


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