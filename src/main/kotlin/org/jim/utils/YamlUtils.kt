package org.jim.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory



object YamlUtils {
    val YamlObjectMapper = ObjectMapper(YAMLFactory())

}