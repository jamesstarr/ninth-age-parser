package org.jim.ninthage.models

import java.util.*

sealed class ParserConfiguration{
    companion object {
        val Pdf =
            PdFParserConfiguration(
                EnumSet.noneOf(PdfParserFlags::class.java)
            )
        val PdfNewLineOnPage = PdFParserConfiguration(
            EnumSet.of(PdfParserFlags.NewLineOnPages))
    }
}

data class PdFParserConfiguration (
    val flags : EnumSet<PdfParserFlags>
): ParserConfiguration()