package org.jim.ninthage.reports

import org.jim.ninthage.models.Tournament
import java.nio.file.Files
import java.nio.file.Path

class TournamentReportPrinter(
    val dir: Path
) {
    val armyBookReporter = ArmyBookReporter()

    fun printTournament(tournament: Tournament){
        val tournamentDir = dir.resolve(tournament.name)
        Files.createDirectories(tournamentDir)

    }
}