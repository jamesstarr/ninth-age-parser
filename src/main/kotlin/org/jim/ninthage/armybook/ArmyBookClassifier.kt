package org.jim.ninthage.armybook

import opennlp.tools.langdetect.DefaultLanguageDetectorContextGenerator
import opennlp.tools.langdetect.LanguageDetectorContextGenerator
import opennlp.tools.langdetect.LanguageDetectorFactory
import opennlp.tools.util.normalizer.CharSequenceNormalizer
import opennlp.tools.util.normalizer.EmojiCharSequenceNormalizer
import opennlp.tools.util.normalizer.NumberCharSequenceNormalizer
import opennlp.tools.util.normalizer.ShrinkCharSequenceNormalizer
import opennlp.tools.util.normalizer.TwitterCharSequenceNormalizer
import opennlp.tools.util.normalizer.UrlCharSequenceNormalizer
import org.jim.ninthage.data.TrainingData
import org.jim.opennlp.classifier.SimpleClassifier
import org.jim.opennlp.classifier.SimpleClassifierModel
import java.util.regex.Pattern

object ArmyBookClassifier {

    val separatorChars: String = """<ARMY_BOOK:(\w{2,3})>"""

    val pattern = Pattern.compile(separatorChars)

    fun train(): SimpleClassifierModel {
        return SimpleClassifier.train(
            TrainingData.ArmyBookClassifier,
            pattern,
            LanguageDetectorFactory()
        )
    }

    fun build(model: SimpleClassifierModel): SimpleClassifier {
        return object : SimpleClassifier(model) {
            override fun classify(value: String): String {
                if (value.trim().toLowerCase().startsWith("team")) {
                    return "Team"
                } else {
                    return super.classify(value)
                }
            }
        }
    }

    fun build(): SimpleClassifier {
        return build(train())
    }

    class ArmyBookLanguageDetectorFactory : LanguageDetectorFactory() {
        override fun getContextGenerator(): LanguageDetectorContextGenerator {
            return DefaultLanguageDetectorContextGenerator(
                3, 5,

                EmojiCharSequenceNormalizer.getInstance(),
                UrlCharSequenceNormalizer.getInstance(),
                TwitterCharSequenceNormalizer.getInstance(),
                NumberCharSequenceNormalizer.getInstance(),//strip numbers
                ShrinkCharSequenceNormalizer.getInstance(),
                object : CharSequenceNormalizer {
                    override fun normalize(text: CharSequence?): CharSequence {
                        return text.toString().toLowerCase()
                    }

                }//removed repeated characters
            )
        }

    }
}