package org.jim.opennlp

import com.google.common.base.Joiner
import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Ordering
import opennlp.tools.langdetect.DefaultLanguageDetectorContextGenerator
import opennlp.tools.langdetect.LanguageDetectorContextGenerator
import opennlp.tools.langdetect.LanguageDetectorFactory
import opennlp.tools.langdetect.LanguageDetectorME
import opennlp.tools.langdetect.LanguageDetectorModel
import opennlp.tools.ml.perceptron.PerceptronTrainer
import opennlp.tools.util.TrainingParameters
import opennlp.tools.util.model.ModelUtil
import opennlp.tools.util.normalizer.EmojiCharSequenceNormalizer
import opennlp.tools.util.normalizer.NumberCharSequenceNormalizer
import opennlp.tools.util.normalizer.ShrinkCharSequenceNormalizer
import opennlp.tools.util.normalizer.TwitterCharSequenceNormalizer
import opennlp.tools.util.normalizer.UrlCharSequenceNormalizer
import org.jim.utils.ResourceUtils
import java.nio.file.Files
import java.nio.file.Path

open class SimpleClassifier(model:LanguageDetectorModel) {
    companion object {
        fun debugTraining(trainingFiles:List<String>, path: Path){
            Files.createDirectories(path)

            val tokenStream =JoinObjectStreams(
                trainingFiles
                    .map { ResourceUtils.readResourceAsUtf8String(it) }
                    .map { SimpleObjectStream(it) }
                    .map { ClassifierTokenizer.build(it) }
            )
            val map =
                ArrayListMultimap.create<String,CharSequence>()
            Sequence { tokenStream.iter }
                .forEach { map.put(it.language.lang, it.context) }
            Ordering.natural<String>().sortedCopy(map.keySet()).forEach{ key ->
                Files.writeString(
                    path.resolve(key),
                    Joiner.on("\n\n").join(map.get(key))
                )
            }
        }


        fun train(
            trainingFiles:List<String>,
            languageDetectorFactory: LanguageDetectorFactory = LanguageDetectorFactory()
        ): LanguageDetectorModel {
            val sampleStream = JoinObjectStreams(
                trainingFiles
                    .map { ResourceUtils.readResourceAsUtf8String(it) }
                    .map { SimpleObjectStream(it) }
                    .map { ClassifierTokenizer.build(it) }
            )

            val params = ModelUtil.createDefaultTrainingParameters()
            params.put(TrainingParameters.ALGORITHM_PARAM, PerceptronTrainer.PERCEPTRON_VALUE)
            params.put(TrainingParameters.CUTOFF_PARAM, 0)

            return LanguageDetectorME.train(
                sampleStream,
                params,
                languageDetectorFactory
            )
        }
    }
    val languageDetectorME = LanguageDetectorME(model)

    open fun detectArmyBook(value:String):String {
        languageDetectorME
        return languageDetectorME.predictLanguage(value).lang
    }

}