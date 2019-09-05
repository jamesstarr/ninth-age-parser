package org.jim.opennlp.classifier

import opennlp.tools.langdetect.LanguageDetectorContextGenerator
import opennlp.tools.ml.maxent.GISModel
import opennlp.tools.ml.model.AbstractModel
import opennlp.tools.ml.model.MaxentModel
import opennlp.tools.util.model.GenericModelSerializer
import opennlp.tools.util.model.ModelUtil
import java.nio.file.Files
import java.nio.file.Path

data class SimpleClassifierModel(
    val maxentModel: MaxentModel
) {
    companion object {
        fun read(path: Path): SimpleClassifierModel {
            val mm = Files.newInputStream(path).use {
                GenericModelSerializer().create(it)
            }
            return SimpleClassifierModel(mm)
        }
    }

    fun write(path:Path){
        Files.newOutputStream(path).use {
            GenericModelSerializer().serialize(maxentModel as AbstractModel, it)
        }
    }
}