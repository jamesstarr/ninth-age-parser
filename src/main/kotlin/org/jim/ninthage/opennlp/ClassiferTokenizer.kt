package org.jim.ninthage.opennlp

import com.google.common.base.Preconditions
import com.google.common.base.Splitter
import opennlp.tools.langdetect.Language
import opennlp.tools.langdetect.LanguageSample
import opennlp.tools.sentdetect.SentenceSample
import opennlp.tools.util.FilterObjectStream
import opennlp.tools.util.ObjectStream
import opennlp.tools.util.Span
import org.jim.ninthage.utils.IntegerUtils
import java.util.regex.Pattern

class ClassiferTokenizer {
    companion object {
        val separatorChars: String = """<ARMY_BOOK:(\w{2,3})>"""

        val p = Pattern.compile(separatorChars)

        fun build(
            documentStream: ObjectStream<String>
            ):ObjectStream<LanguageSample> {
            return DocumentToTokenStream(documentStream) {toLanguageTags(it)}

        }



        data class ArmyBookTag(val armyBook: String, val start: Int, val end: Int)

        fun splitToArmyBookTags(value: String): Sequence<ArmyBookTag> {

            val m = p.matcher(value)
            return sequence {
                while (m.find()) {
                    val armyBook = m.group(1)
                    val start = m.start()
                    val end = m.end()
                    yield(ArmyBookTag(armyBook, start, end))
                }
            }
        }

        fun toLanguageTags(value: String): Sequence<LanguageSample> {
            val tags = splitToArmyBookTags(value).toList()
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