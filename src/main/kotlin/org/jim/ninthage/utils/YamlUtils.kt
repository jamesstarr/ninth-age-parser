package org.jim.ninthage.utils

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory



object YamlUtils {
    val YamlObjectMapper = ObjectMapper(YAMLFactory())

}