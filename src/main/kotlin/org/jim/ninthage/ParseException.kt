package org.jim.ninthage

import java.lang.Exception
import java.lang.RuntimeException

class ParseException(message:String, ex:Exception) : RuntimeException(message, ex)