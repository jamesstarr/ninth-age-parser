package org.jim.opennlp

import opennlp.tools.dictionary.Dictionary
import opennlp.tools.sentdetect.SentenceDetectorFactory
import opennlp.tools.sentdetect.SentenceModel
import opennlp.tools.util.TrainingParameters
import org.jim.utils.ResourceUtils


class OpenNLPSplitter(
    val tokenizer: Splitter
) {
    companion object {
        fun build(model: SentenceModel): OpenNLPSplitter {
            val tokenizer = Splitter(model)
            return OpenNLPSplitter(tokenizer)
        }

        fun train(trainFiles:List<String>): SentenceModel {
            val sampleStream = JoinObjectStreams(
                trainFiles
                    .map { ResourceUtils.readResourceAsUtf8String(it) }
                    .map { SimpleObjectStream(it) }
                    .map { NoWhiteSpaceSplitTokenizer(it) }

            )


            try {
                return Splitter.train(
                            sampleStream,
                    SentenceDetectorFactory("Team", false, Dictionary(), charArrayOf('\n')),
                    TrainingParameters.defaultParams()
                )
            } finally {
                sampleStream.close()
            }
        }
    }

    fun splitFormResource(resource:String) : Sequence<String> {
        val value = ResourceUtils.readResourceAsUtf8String(resource)
        return split(value)
    }

    fun split(listOfTeams:String): Sequence<String>  {
        return sequence {
            val tokens = tokenizer.sentDetect(listOfTeams)
            for (token in tokens) {
                yield(token)
            }
        }
    }


}