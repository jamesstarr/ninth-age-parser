package org.jim.ninthage

import opennlp.tools.dictionary.Dictionary
import opennlp.tools.sentdetect.SentenceDetectorFactory
import opennlp.tools.sentdetect.SentenceDetectorME
import opennlp.tools.sentdetect.SentenceModel
import opennlp.tools.util.TrainingParameters
import org.jim.ninthage.opennlp.JoinObjectStreams
import org.jim.ninthage.opennlp.NoWhiteSpaceSplitTokenizer
import org.jim.ninthage.opennlp.SimpleObjectStream
import org.jim.ninthage.utils.ResourceUtils


class SimpleSplitter(val tokenizer: SentenceDetectorME) {
    companion object {
        fun build(model: SentenceModel):SimpleSplitter {
            val tokenizer = SentenceDetectorME(model)
            return SimpleSplitter(tokenizer)
        }

        fun train(trainFiles:List<String>): SentenceModel {
            val sampleStream = JoinObjectStreams(
                trainFiles
                    .map { ResourceUtils.readResourceAsUtf8String(it) }
                    .map { SimpleObjectStream(it) }
                    .map { NoWhiteSpaceSplitTokenizer(it) }

            )


            try {
                return SentenceDetectorME.train(
                    "Team",
                            sampleStream,
                    SentenceDetectorFactory("Team", false, Dictionary(), charArrayOf('\n')),
                    TrainingParameters.defaultParams()
                )
            } finally {
                sampleStream.close()
            }
        }
    }

    fun splitTeamsFormResource(resource:String) : Sequence<String> {
        val value = ResourceUtils.readResourceAsUtf8String(resource)
        return splitTeams(value)
    }

    fun splitTeams(listOfTeams:String): Sequence<String>  {
        return sequence {
            val tokens: Array<out String> = tokenizer.sentDetect(listOfTeams)
            for (token in tokens) {
                yield(token)
            }
        }
    }


}