package org.jim.opennlp.classifier

import opennlp.tools.langdetect.LanguageDetectorContextGenerator
import opennlp.tools.ml.model.MaxentModel

data class SimpleClassifierModel(
    val maxentModel: MaxentModel,
    val contextGenerator: LanguageDetectorContextGenerator
) {
}