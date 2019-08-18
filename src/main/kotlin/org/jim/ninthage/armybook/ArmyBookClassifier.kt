package org.jim.ninthage.armybook

import opennlp.tools.langdetect.DefaultLanguageDetectorContextGenerator
import opennlp.tools.langdetect.LanguageDetectorContextGenerator
import opennlp.tools.langdetect.LanguageDetectorFactory
import opennlp.tools.langdetect.LanguageDetectorModel
import opennlp.tools.util.normalizer.EmojiCharSequenceNormalizer
import opennlp.tools.util.normalizer.NumberCharSequenceNormalizer
import opennlp.tools.util.normalizer.ShrinkCharSequenceNormalizer
import opennlp.tools.util.normalizer.TwitterCharSequenceNormalizer
import opennlp.tools.util.normalizer.UrlCharSequenceNormalizer
import org.jim.ninthage.data.TrainingData
import org.jim.opennlp.SimpleClassifier

object ArmyBookClassifier {
    fun train(): LanguageDetectorModel {
        return SimpleClassifier.train(
            TrainingData.ArmyBookClassifier,
            LanguageDetectorFactory()
        )
    }

    fun build(model:LanguageDetectorModel):SimpleClassifier {
        return SimpleClassifier(model)
    }

    fun build():SimpleClassifier {
        return SimpleClassifier(train())
    }

    class ArmyBookLanguageDetectorFactory: LanguageDetectorFactory(){
        override fun getContextGenerator(): LanguageDetectorContextGenerator {
            return DefaultLanguageDetectorContextGenerator(
                4, 6,
                EmojiCharSequenceNormalizer.getInstance(),
                UrlCharSequenceNormalizer.getInstance(),
                TwitterCharSequenceNormalizer.getInstance(),
                NumberCharSequenceNormalizer.getInstance(),//strip numbers
                ShrinkCharSequenceNormalizer.getInstance()//removed repeated characters
            )
        }

    }
}