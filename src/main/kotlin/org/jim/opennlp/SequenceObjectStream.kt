package org.jim.opennlp

import opennlp.tools.util.ObjectStream

class SequenceObjectStream<TOKEN>(val seq:Sequence<TOKEN>):ObjectStream<TOKEN>{
    var iter = seq.iterator()
    override fun read(): TOKEN? {
        return if(iter.hasNext()){
            iter.next()
        } else {
            null
        }
    }

    override fun reset() {
        iter = seq.iterator()
    }
}