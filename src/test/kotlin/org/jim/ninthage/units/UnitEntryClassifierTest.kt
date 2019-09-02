package org.jim.ninthage.units

import org.testng.Assert
import org.testng.annotations.Test

class UnitEntryClassifierTest {
    @Test fun testFooterNumberPattern(){
        val scope = object {
            val subject = UnitEntryClassifier.footerNumberPattern
            val falseInputs = listOf(
                "460 – Wizard, Cosmology/Alchemy, Essence of a Free Mind, Binding Scroll, Master"
            )
            val trueInputs = listOf(
                "4500",
                "4493",
                "Total: 4500",
                "4493pts"
            )
        }.apply {
            falseInputs.forEach {
                val actual = subject.matcher(it).find()
                Assert.assertFalse(actual, it)
            }
            trueInputs.forEach {
                val actual = subject.matcher(it).find()
                Assert.assertTrue(actual, it)
            }
        }
    }
}