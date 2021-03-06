package org.jim.ninthage.roster

import org.jim.ninthage.base.Splitter
import org.jim.ninthage.data.TrainingData
import org.jim.opennlp.OpenNLPSplitter

object RosterSplitter {
    fun openNLPListSplitter(): Splitter {
        val model = OpenNLPSplitter.train(TrainingData.ListSplitting)
        val splitter = OpenNLPSplitter.build(model)
        return object : Splitter {
            override fun split(value: String): Sequence<String> {
                return splitter.split(value)
            }

        }
    }

    fun simpleSplitter(): Splitter {
        return object : Splitter {
            override fun split(value: String): Sequence<String> {
                val splitValue =
                    com.google.common.base.Splitter.onPattern("\n[ \t]*\n([ \t]*\n)+")
                        .omitEmptyStrings()
                        .split(value)
                        .filter { it.isNotBlank() }
                return Sequence { splitValue.iterator() }
            }
        }
    }
}
