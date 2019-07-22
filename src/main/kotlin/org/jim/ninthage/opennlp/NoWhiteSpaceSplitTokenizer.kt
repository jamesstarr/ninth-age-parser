package org.jim.ninthage.opennlp

import com.google.common.base.Splitter
import opennlp.tools.sentdetect.SentenceSample
import opennlp.tools.util.FilterObjectStream
import opennlp.tools.util.ObjectStream
import opennlp.tools.util.Span

class NoWhiteSpaceSplitTokenizer(
    samples: ObjectStream<String>,
    val separatorChars:String = "<SPLIT>"
) : FilterObjectStream<String, SentenceSample>(samples) {
    override fun read(): SentenceSample? {
        val sampleString = samples.read()

        return if (sampleString != null) {
            splitToSpans(sampleString)

        } else {
            null
        }
    }


    fun splitToSpans (value:String) : SentenceSample {
        val subStrings = Splitter.on(separatorChars).splitToList(value)
        val builder = StringBuilder()

        val spans = Array(subStrings.size) {
            val x = builder.length
            val subString = subStrings[it]
            builder.append(subString)
            Span(x, builder.length)
        }
        return SentenceSample(
            builder,
            *spans
        )
    }
}