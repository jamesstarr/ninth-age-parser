package org.jim.ninthage.models

data class TournamentConfiguration(
    val fileName: String,
    val parser: ParserConfiguration,
    val isWellFormed: Boolean = false
) {
    val name: String = fileName.substring(
        fileName.lastIndexOf("/") + 1,
        fileName.lastIndexOf('.')
    )
}