package org.jim.opennlp

import opennlp.tools.langdetect.Language
import opennlp.tools.langdetect.LanguageSample
import opennlp.tools.util.ObjectStream
import java.util.regex.Pattern

class ClassifierTokenizer(val pattern: Pattern) {
    companion object {

        fun build(
            documentStream: ObjectStream<String>,
            pattern:Pattern
            ):ObjectStream<LanguageSample> {
            return DocumentToTokenStream(documentStream) {
                toLanguageTags(it, pattern)
            }

        }



        data class ArmyBookTag(val armyBook: String, val start: Int, val end: Int)

        fun splitToArmyBookTags(value: String, pattern:Pattern): Sequence<ArmyBookTag> {

            val m = pattern.matcher(value)
            return sequence {
                while (m.find()) {
                    val armyBook = m.group(1)
                    val start = m.start()
                    val end = m.end()
                    yield(ArmyBookTag(armyBook, start, end))
                }
            }
        }

        fun toLanguageTags(value: String, pattern:Pattern): Sequence<LanguageSample> {
            val tags = splitToArmyBookTags(value,pattern).toList()
            if (tags.isEmpty()) {
                return sequence {}
            }
            return sequence {
                for (i in 0.until(tags.size - 1)) {
                    val sample = tags[i]
                    val nextSample = tags[i + 1]
                    yield(LanguageSample(Language(sample.armyBook), value.substring(sample.end, nextSample.start)))
                }
                val (armyBook, _, end) = tags.last()
                yield(LanguageSample(Language(armyBook), value.substring(end)))

            }
        }
    }


}