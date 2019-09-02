package org.jim.ninthage.units

import opennlp.tools.util.InsufficientTrainingDataException
import org.jim.opennlp.SequenceObjectStream
import org.jim.opennlp.classifier.SimpleClassifier
import org.jim.opennlp.classifier.SimpleClassifierModel
import org.jim.opennlp.classifier.SimpleToken
import java.util.regex.Pattern

class UnitEntryClassifier(model: SimpleClassifierModel) : SimpleClassifier(model) {
    val numberMatcher = numberPattern.matcher("")

    companion object {
        val numberPattern =  Pattern.compile("4[ ,._]?(500)|(4\\d\\d)")
        fun train(tokens: List<UnitToken>): SimpleClassifierModel {
            try {
                return train(
                    tokens
                        .map { SimpleToken(it.name, it.rawBody) }
                        .let { SequenceObjectStream<SimpleToken>(it.asSequence()) }
                )
            } catch (insufficientTrainingDataException: InsufficientTrainingDataException) {
                throw insufficientTrainingDataException
            }
        }


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
