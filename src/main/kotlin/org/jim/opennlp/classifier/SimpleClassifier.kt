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
import opennlp.tools.util.normalizer.*
import org.jim.opennlp.ClassifierTokenizer
import org.jim.opennlp.JoinObjectStreams
import org.jim.opennlp.SingletonObjectStream
import org.jim.utils.ResourceUtils
import java.lang.StringBuilder
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.regex.Pattern

open class SimpleClassifier(val model: SimpleClassifierModel) {

    companion object {
        val contextGenerator = defaultContextGenerator()

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
            pattern: Pattern
        ): SimpleClassifierModel {
            val sampleStream = JoinObjectStreams(
                trainingFiles
                    .map { ResourceUtils.readResourceAsUtf8String(it) }
                    .map { SingletonObjectStream(it) }
                    .map { ClassifierTokenizer.build(it, pattern) }
            )


            return train(sampleStream)
        }

        fun build(
            tokenStream: ObjectStream<SimpleToken>,
            contextGenerator: LanguageDetectorContextGenerator
            = defaultContextGenerator()
        ): SimpleClassifier {
            val model = train(tokenStream)
            return SimpleClassifier(model)
        }


        fun train(
            tokenStream: ObjectStream<SimpleToken>
        ): SimpleClassifierModel {
            val manifestInfoEntries = HashMap<String, String>()

            val trainer = TrainerFactory.getEventTrainer(
                ModelUtil.createDefaultTrainingParameters(), manifestInfoEntries
            )

            val model = trainer.train(
                SimpleTokensToEventObjectStream(tokenStream, contextGenerator)
            )

            return SimpleClassifierModel(model)
        }

        fun defaultContextGenerator(): LanguageDetectorContextGenerator {
            return DefaultLanguageDetectorContextGenerator(
                2, 5,
                EmojiCharSequenceNormalizer.getInstance(),
                UrlCharSequenceNormalizer.getInstance(),
                TwitterCharSequenceNormalizer.getInstance(),
                SingleNumbersOnlyNormalizer(),
                ShrinkCharSequenceNormalizer.getInstance(),
                ToLowerCaseNormalizer()
            )
        }
    }

    class ToLowerCaseNormalizer: CharSequenceNormalizer {
        override fun normalize(text: CharSequence?): CharSequence {
            return text.toString().toLowerCase()
        }
    }


    class SingleNumbersOnlyNormalizer: CharSequenceNormalizer {
        val nunberMatcher = Pattern.compile("(\\d+)(\\d)").matcher("")
        override fun normalize(text: CharSequence?): CharSequence {
            val stringBuilder = StringBuilder(text!!.length)
            var index = 0
            nunberMatcher.reset(text)
            while(nunberMatcher.find()) {
                val start = nunberMatcher.start()
                val end = nunberMatcher.end()
                stringBuilder.append(text, index, start + 1)
                val value= nunberMatcher.group(1).toInt()
                val value2  =nunberMatcher.group(2).toInt()
                stringBuilder.append("")
                index = end
            }
            stringBuilder.append(text, index)
            return text.toString().toLowerCase()
        }

    }

    class SigFigNumberNormalizer: CharSequenceNormalizer {
        val nunberMatcher = Pattern.compile("(\\d+)(\\d)").matcher("")
        override fun normalize(text: CharSequence?): CharSequence {
            val stringBuilder = StringBuilder(text!!.length)
            var index = 0
            nunberMatcher.reset(text)
            while(nunberMatcher.find()) {
                val start = nunberMatcher.start()
                val end = nunberMatcher.end()
                stringBuilder.append(text, index, start + 1)
                val value= nunberMatcher.group(1).toInt()
                val value2  =nunberMatcher.group(2).toInt()
                stringBuilder.append(value+ if(value2 < 5) 0 else 1).append("0")
                index = end
            }
            stringBuilder.append(text, index)
            return text.toString().toLowerCase()
        }

    }


    open fun classify(value: String): String {
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