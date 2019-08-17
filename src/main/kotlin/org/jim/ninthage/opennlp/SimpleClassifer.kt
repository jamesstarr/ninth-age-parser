package org.jim.ninthage.opennlp

import opennlp.tools.langdetect.LanguageDetectorFactory
import opennlp.tools.langdetect.LanguageDetectorME
import opennlp.tools.langdetect.LanguageDetectorModel
import opennlp.tools.ml.perceptron.PerceptronTrainer
import opennlp.tools.util.TrainingParameters
import opennlp.tools.util.model.ModelUtil
import org.jim.ninthage.utils.ResourceUtils

class SimpleClassifer(model:LanguageDetectorModel) {
    companion object {
        fun train(trainingFiles:List<String>): LanguageDetectorModel {
            val sampleStream = JoinObjectStreams(
                trainingFiles
                    .map { ResourceUtils.readResourceAsUtf8String(it) }
                    .map { SimpleObjectStream(it) }
                    .map { ClassiferTokenizer.build(it) }

            )

            val params = ModelUtil.createDefaultTrainingParameters()
            params.put(TrainingParameters.ALGORITHM_PARAM, PerceptronTrainer.PERCEPTRON_VALUE)
            params.put(TrainingParameters.CUTOFF_PARAM, 0)

            return LanguageDetectorME.train(sampleStream, params, LanguageDetectorFactory())
        }
    }
    val languageDetectorME = LanguageDetectorME(model)

    fun detectArmyBook(value:String):String {
        languageDetectorME
        return languageDetectorME.predictLanguage(value).lang
    }

}