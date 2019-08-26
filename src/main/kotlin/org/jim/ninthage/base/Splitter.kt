package org.jim.ninthage.base

interface Splitter {
    fun split(value: String): Sequence<String>
}