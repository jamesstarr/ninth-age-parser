package org.jim.ninthage.units

import opennlp.tools.util.InsufficientTrainingDataException
import org.jim.opennlp.SequenceObjectStream
import org.jim.opennlp.classifier.SimpleClassifier
import org.jim.opennlp.classifier.SimpleClassifierCached
import org.jim.opennlp.classifier.SimpleClassifierModel
import org.jim.opennlp.classifier.SimpleToken
import java.nio.file.Path

class UnitEntryClassiferBuilder(
    val cachedSimpleClassifier: SimpleClassifierCached
) {
    fun build(names:List<String>, tokens: List<UnitToken>): UnitEntryClassifier {
        try {
            val processedTokens = tokens
                .map { SimpleToken(it.name, it.rawBody) }
                .let { SequenceObjectStream(it.asSequence()) }
            val sc = cachedSimpleClassifier.train(names, processedTokens)

            return UnitEntryClassifier(sc)
        } catch (insufficientTrainingDataException: InsufficientTrainingDataException) {
            throw insufficientTrainingDataException
        }
    }
}