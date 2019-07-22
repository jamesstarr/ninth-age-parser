package org.jim.ninthage.opennlp

import opennlp.tools.util.ObjectStream

class JoinObjectStreams<T>(val subStreams:List<ObjectStream<T>>) : ObjectStream<T> {

    companion object {
        private fun <T> buildIter (subStreams:List<ObjectStream<T>>):Iterator<T> {
            return sequence {
                for (ss in subStreams) {
                    ss.reset()
                    var v = ss.read()
                    while (v != null) {
                        yield(v)
                        v = ss.read()
                    }
                }
            }.iterator()
        }
    }

    var iter:Iterator<T> = buildIter(subStreams)

    override fun read(): T? {
        if(iter.hasNext()) {
            return iter.next()//To change body of created functions use File | Settings | File Templates.
        } else {
            return null;
        }
    }

    override fun reset() {
        iter = buildIter(subStreams)
    }

    override fun close() {
        subStreams.forEach{it.close()}
    }
}