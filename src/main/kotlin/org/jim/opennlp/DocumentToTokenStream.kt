package org.jim.opennlp

import opennlp.tools.util.ObjectStream

interface DocumentTokenizer

class DocumentToTokenStream<DOC, TOKEN>(
    val docStream: ObjectStream<DOC>,
    val documentParser:(DOC)->Sequence<TOKEN>
): ObjectStream<TOKEN> {

    val seq: Sequence<TOKEN> = sequence{
        var doc = docStream.read()
        while(doc != null) {
            yieldAll(documentParser(doc))
            doc = docStream.read()
        }
    }
    var iterator = seq.iterator()
    override fun read(): TOKEN? {
        return if(iterator.hasNext()){
            iterator.next()
        } else {
            null
        }
    }

    override fun reset() {
        docStream.reset()
        iterator = seq.iterator()
    }

    override fun close() {
        docStream.close()
    }
}