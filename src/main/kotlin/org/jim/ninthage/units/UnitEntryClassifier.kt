package org.jim.ninthage.units

import opennlp.tools.util.InsufficientTrainingDataException
import org.jim.opennlp.SequenceObjectStream
import org.jim.opennlp.classifier.SimpleClassifier
import org.jim.opennlp.classifier.SimpleClassifierModel
import org.jim.opennlp.classifier.SimpleToken
import java.util.regex.Pattern

class UnitEntryClassifier(model: SimpleClassifierModel) : SimpleClassifier(model) {
    val numberMatcher = footerNumberPattern.matcher("")

    companion object {
        val footerNumberPattern =  Pattern.compile("(?:4[ ,._]?500)|(44\\d\\d)")

    }

    override fun classify(value: String): String {
        numberMatcher.reset(value.trim())
        return if (numberMatcher.find()) {
            "Footer"
        } else {
            super.classify(value)
        }
    }

}
