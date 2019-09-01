package org.jim.ninthage.units

import org.jim.ninthage.data.armybook.EmpireOfSonnstahl
import org.testng.Assert

class UnitNumberParserTest {

    @org.testng.annotations.Test
    fun testParse() {
        val context = object {
            val subject = UnitNumberParser()
            val eos = EmpireOfSonnstahl.Version2_0
        }.run {

            Assert.assertEquals(
                subject.parse(
                    "2x 195 - 2x 5x Electoral Cavalry, Shield, Musician",
                    eos.electoralCavalry
                ),
                UnitNumberParser.UnitNumber(195, 5, 2)
            )
            Assert.assertEquals(
                subject.parse(" 2x1 Imperial Rocketeers 160", eos.artillery),
                UnitNumberParser.UnitNumber(160, 1, 2)
            )
        }
    }


}