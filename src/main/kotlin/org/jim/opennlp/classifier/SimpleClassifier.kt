package org.jim.opennlp.classifier

import com.google.common.base.Joiner
import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Ordering
import opennlp.tools.langdetect.DefaultLanguageDetectorContextGenerator
import opennlp.tools.langdetect.LanguageDetectorContextGenerator
import opennlp.tools.langdetect.LanguageDetectorFactory
import opennlp.tools.ml.TrainerFactory
import opennlp.tools.ml.model.Event
import opennlp.tools.ml.perceptron.PerceptronTrainer
import opennlp.tools.util.ObjectStream
import opennlp.tools.util.TrainingParameters
import opennlp.tools.util.model.ModelUtil
import opennlp.tools.util.normalizer.EmojiCharSequenceNormalizer
import opennlp.tools.util.normalizer.NumberCharSequenceNormalizer
import opennlp.tools.util.normalizer.ShrinkCharSequenceNormalizer
import opennlp.tools.util.normalizer.TwitterCharSequenceNormalizer
import opennlp.tools.util.normalizer.UrlCharSequenceNormalizer
import org.jim.opennlp.ClassifierTokenizer
import org.jim.opennlp.JoinObjectStreams
import org.jim.opennlp.SingletonObjectStream
import org.jim.utils.ResourceUtils
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.regex.Pattern

open class SimpleClassifier(val model: SimpleClassifierModel) {
    companion object {
        fun debugTraining(
            trainingFiles: List<String>,
            pattern: Pattern,
            path: Path
        ) {
            Files.createDirectories(path)

            val tokenStream = JoinObjectStreams(
                trainingFiles
                    .map { ResourceUtils.readResourceAsUtf8String(it) }
                    .map { SingletonObjectStream(it) }
                    .map { ClassifierTokenizer.build(it, pattern) }
            )
            val map =
                ArrayListMultimap.create<String, CharSequence>()
            Sequence { tokenStream.iter }
                .forEach { map.put(it.target, it.context) }
            Ordering.natural<String>().sortedCopy(map.keySet()).forEach { key ->
                Files.writeString(
                    path.resolve(key),
                    Joiner.on("\n\n").join(map.get(key))
                )
            }
        }


        fun train(
            trainingFiles: List<String>,
            pattern: Pattern,
            languageDetectorFactory: LanguageDetectorFactory = LanguageDetectorFactory()
        ): SimpleClassifierModel {
            val sampleStream = JoinObjectStreams(
                trainingFiles
                    .map { ResourceUtils.readResourceAsUtf8String(it) }
                    .map { SingletonObjectStream(it) }
                    .map { ClassifierTokenizer.build(it, pattern) }
            )

            val params = ModelUtil.createDefaultTrainingParameters()
            params.put(TrainingParameters.ALGORITHM_PARAM, PerceptronTrainer.PERCEPTRON_VALUE)
            params.put(TrainingParameters.CUTOFF_PARAM, 0)
            return train(sampleStream, languageDetectorFactory.contextGenerator)
        }

        fun build(
            tokenStream: ObjectStream<SimpleToken>,
            contextGenerator: LanguageDetectorContextGenerator
            = defaultContextGenerator()
        ): SimpleClassifier {
            val model = train(tokenStream, contextGenerator)
            return SimpleClassifier(model)
        }


        fun train(
            tokenStream: ObjectStream<SimpleToken>,
            contextGenerator: LanguageDetectorContextGenerator
            = defaultContextGenerator()
        ): SimpleClassifierModel {
            val manifestInfoEntries = HashMap<String, String>()

            val trainer = TrainerFactory.getEventTrainer(
                ModelUtil.createDefaultTrainingParameters(), manifestInfoEntries
            )

            val model = trainer.train(
                SimpleTokensToEventObjectStream(tokenStream, contextGenerator)
            )

            return SimpleClassifierModel(model, contextGenerator)
        }

        fun defaultContextGenerator(): LanguageDetectorContextGenerator {
            return DefaultLanguageDetectorContextGenerator(
                2, 5,
                EmojiCharSequenceNormalizer.getInstance(),
                UrlCharSequenceNormalizer.getInstance(),
                TwitterCharSequenceNormalizer.getInstance(),
                NumberCharSequenceNormalizer.getInstance(),
                ShrinkCharSequenceNormalizer.getInstance()
            )
        }
    }



    open fun classify(value: String): String {
        val contextGenerator = model.contextGenerator
        val eval = model.maxentModel.eval(contextGenerator.getContext(value))
        val arr = Array(eval.size) { i ->
            SimpleClassifierResult(model.maxentModel.getOutcome(i), eval[i])
        }

        Arrays.sort(arr) { o1, o2 ->
            java.lang.Double.compare(o2.confidence, o1.confidence)
        }

        return arr[0].result
    }

}

private class SimpleTokensToEventObjectStream(
    val tokenStream: ObjectStream<SimpleToken>,
    val contextGenerator: LanguageDetectorContextGenerator
):
    ObjectStream<Event> {
    override fun read(): Event? {
        return tokenStream.read()?.let {
            Event(
                it.target,
                contextGenerator.getContext(it.context)
            )
        }
    }

    override fun reset() {
        tokenStream.reset()
    }

    override fun close() {
        tokenStream.close()
    }
}