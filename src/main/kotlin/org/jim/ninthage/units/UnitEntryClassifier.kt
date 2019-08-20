package org.jim.ninthage.units

import opennlp.tools.langdetect.LanguageDetectorModel
import org.jim.ninthage.data.TrainingData
import org.jim.opennlp.SimpleClassifier
import java.util.regex.Pattern

object UnitEntryClassifier {

    val separatorChars: String = """<Unit\s+name="([^"]+)"\s*>"""

    val pattern = Pattern.compile(separatorChars)

    fun train(): Map<String,LanguageDetectorModel> {
        return TrainingData.UnitFClassifer.mapValues{(armyBook, path)->
            SimpleClassifier.train(listOf(path), pattern, UnitEntryLanguageDetectorFactory())
        }
    }

    fun build(model: LanguageDetectorModel): SimpleClassifier {
        return object: SimpleClassifier(model){
            val numberPattern = Pattern.compile("\\d[ ,._]?\\d\\d\\d")
            override fun classify(value:String): String {
                return if(numberPattern.matcher(value.trim()).matches()){
                    "Footer"
                } else {
                    super.classify(value)
                }
            }
        }

    }

    fun build(): Map<String,SimpleClassifier> {
        return train().mapValues { (ab, model) -> build(model)}
    }


}