package org.jim.ninthage.data

object TrainingData {
    val TeamSplitting = listOf(
        "/org/jim/ninthage/training/team/ETC 2019 Master Lists v1.0.txt",
        "/org/jim/ninthage/training/team/ETC_2018-T9A_v.1.1.txt",
        "/org/jim/ninthage/training/team/2.2 T55 Listas Torneo de 4 2-1 Aranjuez Team Event.txt"
    )

    val ListSplitting = listOf(
        "/org/jim/ninthage/training/list/2.0.9 Warsaw Team Tournment February.txt",
        "/org/jim/ninthage/training/list/2.2 T56 BUCKEYE 2019 LISTS.txt",
        "/org/jim/ninthage/training/list/ETC 2019 Master Lists v1.0.txt",
        "/org/jim/ninthage/training/list/2.1.2 TEC 2019 lists.txt",
        "/org/jim/ninthage/training/list/2.1.3Steadfast & Furious.txt"
    )

    val ArmyBookClassifier = listOf(
        "/org/jim/ninthage/training/armybook/2.1.2 TEC 2019 lists.txt",
        "/org/jim/ninthage/training/armybook/2.1.3Steadfast & Furious.txt",
        "/org/jim/ninthage/training/armybook//2.2 T56 BUCKEYE 2019 LISTS.txt",
        "/org/jim/ninthage/training/armybook/ETC 2019 Master Lists v1.0.txt"

    )

    val UnitClassifier = mapOf(
        Pair("BH", listOf("/org/jim/ninthage/training/unit/BH/BH.txt")),
        Pair(
            "EoS",
            listOf(
                "/org/jim/ninthage/training/unit/EoS/EoS.txt",
                "/org/jim/ninthage/training/unit/EoS/EoS.Artificer.generated.txt",
                "/org/jim/ninthage/training/unit/EoS/EoS.MarshalArmourCloseCombat.generated.txt",
                "/org/jim/ninthage/training/unit/EoS/EoS.MarshalArtifactsMounts.generated.txt",
                "/org/jim/ninthage/training/unit/EoS/EoS.MarshalShieldArtifact.generated.txt"
            )
        )
    )
}