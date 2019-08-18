package org.jim.opennlp

import opennlp.tools.util.ObjectStream

class SimpleObjectStream(val token:String) : ObjectStream<String> {
    var ready = true

    override fun read(): String? {
        if(ready) {
            ready = false
            return token
        } else {
            return null
        }
    }

    override fun reset() {
        ready = true;
    }
}
