package org.jim.pdf

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.jim.utils.ResourceUtils
import java.util.*


class PdfToText {
    companion object {
        object SteadFastAndFurious : TextExtractorConfiguration("/org/jim/ninthage/rawSingleList/2.1.3Steadfast & Furious.pdf") {
            override fun textStripper(): PDFTextStripper {
                val textStripper = PDFTextStripper()
                //textStripper.addMoreFormatting = true
                textStripper.lineSeparator = "\n"
                return textStripper
            }

        }

        object Tec2019 : TextExtractorConfiguration("/org/jim/ninthage/rawSingleList/2.1.2 TEC 2019 lists.pdf") {
            override fun textStripper(): PDFTextStripper {
                val textStripper = PDFTextStripper()
                //textStripper.addMoreFormatting = true
                textStripper.lineSeparator = "\n"
                return textStripper
            }

        }
    }

    fun convert(pathString:String, flags:EnumSet<PdfParserFlags>): String {
        ResourceUtils.resourceAsInputStream(pathString).use { inputStream ->
            PDDocument.load(inputStream).use { doc ->
                val textStripper = textConverter(flags)
                return textStripper.getText(doc)
            }
        }
    }


    private fun textConverter(flags:Set<PdfParserFlags>): PDFTextStripper {
        val textStripper = PDFTextStripper()
        if (flags.contains(PdfParserFlags.NewLineOnPages)) {
            textStripper.pageEnd = "\n\n"
            textStripper.articleEnd = "\n\n"
        }
        return textStripper
    }
}

abstract class TextExtractorConfiguration(val pathString: String) {
    abstract fun textStripper(): PDFTextStripper
}