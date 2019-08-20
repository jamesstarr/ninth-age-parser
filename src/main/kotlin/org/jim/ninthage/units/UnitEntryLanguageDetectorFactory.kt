package org.jim.ninthage.units

import opennlp.tools.langdetect.DefaultLanguageDetectorContextGenerator
import opennlp.tools.langdetect.LanguageDetectorContextGenerator
import opennlp.tools.langdetect.LanguageDetectorFactory
import opennlp.tools.util.normalizer.EmojiCharSequenceNormalizer
import opennlp.tools.util.normalizer.NumberCharSequenceNormalizer
import opennlp.tools.util.normalizer.ShrinkCharSequenceNormalizer
import opennlp.tools.util.normalizer.TwitterCharSequenceNormalizer
import opennlp.tools.util.normalizer.UrlCharSequenceNormalizer

class UnitEntryLanguageDetectorFactory: LanguageDetectorFactory(){
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