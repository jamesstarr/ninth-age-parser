package org.jim.ninthage.units

import com.google.common.collect.Multimap

data class UnitToken(
    val raw: String,
    val rawBody: String,
    val rawAttributes: String,
    val name: String,
    val simpleOptions: Map<String, String>,
    val complexOptions: Multimap<String, String>
)
