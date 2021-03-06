package org.jim.ninthage.units

import com.google.common.base.Joiner
import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import org.jim.ninthage.ParseException
import org.jim.ninthage.models.ArmyBook
import org.jim.ninthage.models.ArmyBookEntry
import java.lang.Exception
import java.util.AbstractSet
import java.util.regex.Pattern

class UnitTokenizer(
    val armyBook: ArmyBook
) {
    /***
     * first group name
     * second group arguments
     * thrid group body
     */
    val namePattern = Pattern.compile(
        "<Unit\\s+name=(\"(?:[^\"]+\")|(?:[^\\s>]+))([^>]*)>([^<]+)"
    )

    val attributePattern =
        Pattern.compile("([^\\s=]+)(?:=(\"(?:[^\"]+\")|(?:[^\\s>]+)))?")

    fun splitUnitTokens(value: String): Sequence<UnitToken> {

        val m = namePattern.matcher(value)
        return sequence {
            while (m.find()) {
                val raw = m.group(0)!!
                val entryName = m.group(1)!!.let { clean(it) }
                val entry =
                    when (entryName) {
                        "Footer" -> ArmyBookEntry("Footer", 0)//HACK
                        "Header" -> ArmyBookEntry("Header", 0)//HACK
                        else -> armyBook.entry(entryName)
                    }
                val rawAttributes = m.group(2) ?: ""
                val rawBody = m.group(3)!!
                val attributePairs = parseAttributes(rawAttributes)
                val (usedSimple, simpleAttributes) = parseSimpleAttributes(entry, attributePairs)
                val (usedComplex, complexAttributes) = parseComplexAttributes(entry, attributePairs)
                if(usedSimple.size + usedComplex.size != attributePairs.size) {
                    val unused =
                        attributePairs.toMutableList()
                            .apply {
                                removeAll(usedComplex)
                                removeAll(usedSimple)
                            }
                    throw Exception(value+ "\n" + unused)
                }
                yield(
                    UnitToken(
                        raw,
                        rawBody,
                        rawAttributes,
                        entryName,
                        simpleAttributes,
                        complexAttributes
                    )
                )
            }
        }
    }

    private fun parseAttributes(rawAttributes: String): List<Pair<String, String>> {
        val matcher = attributePattern.matcher(rawAttributes)
        return sequence {
            while (matcher.find()) {
                val name = matcher.group(1)!!
                val value = (matcher.group(2) ?: "")
                    .let { clean(it) }
                yield(Pair(name, value))
            }
        }.toList()
    }

    private fun parseComplexAttributes(
        entry: ArmyBookEntry,
        attributes: List<Pair<String, String>>
    ): Pair<Set<Pair<String,String>>,Multimap<String, String>> {
        val options = entry.options.filter { !it.isSimple }
        val results = ArrayListMultimap.create<String, String>()
        var usedAttributes = HashSet<Pair<String,String>>()

        for (option in options) {
            val optionName = option.name
            val foundValues = attributes.filter { it.first == optionName }
            if (foundValues.isEmpty()) continue

            foundValues.forEach{ foundValue->
                usedAttributes.add(foundValue)
                //Ensure the option exists in the army book
                results.put(optionName, option.selection(foundValue.second).name)

            }
        }
        return Pair(usedAttributes, results)
    }

    private fun parseSimpleAttributes(
        entry: ArmyBookEntry,
        attributes: List<Pair<String, String>>
    ):Pair<Set<Pair<String,String>>, Map<String, String>> {
        try {
            val options = entry.options.filter { it.isSimple }
            val results = HashMap<String, String>()
            var usedAttributes =  HashSet<Pair<String,String>>()
            for (option in options) {
                val optionName = option.name
                val foundValues = attributes.filter { it.first == optionName }
                if (foundValues.size > 1) {
                    throw RuntimeException(optionName)
                }
                val foundValue =
                    if (foundValues.isEmpty()) {
                        option.defaultValue
                    } else if(foundValues.size == 1){
                        val v = foundValues.first().second
                        usedAttributes.add(foundValues.first())
                        if(v.isNotEmpty() ){
                            v
                        } else {
                            option.implicit!!
                        }
                    } else {
                        throw Exception(foundValues.toString())
                    }
                //Ensure the option exists in the army book
                results.put(optionName, option.selection(foundValue).name)
            }
            return Pair(usedAttributes, results)
        } catch (rt: java.lang.RuntimeException) {
            throw ParseException(
                "ArmyBook: " + armyBook.shortLabel + "\n" +
                        "Entry: " + entry.label + "\n" +
                        Joiner.on("\n").join(attributes),
                rt
            )
        }
    }

    private fun clean(v: String): String {
        return if (v.startsWith("\"") && v.endsWith("\"")) {
            v.substring(1, v.length - 1)
        } else if (v.startsWith("\"") || v.endsWith("\"")) {
            throw RuntimeException(v)
        } else {
            v
        }
    }


}