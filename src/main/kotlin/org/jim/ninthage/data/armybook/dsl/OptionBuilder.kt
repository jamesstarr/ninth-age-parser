package org.jim.ninthage.data.armybook.dsl

import org.jim.ninthage.data.armybook.ArcaneCompendiums
import org.jim.ninthage.data.armybook.MainBook
import org.jim.ninthage.models.ArmyBookEntryOption
import org.jim.ninthage.models.ArmyBookEntryOptionSelection

open class OptionBuilder(private val armyBook: StandardArmyBook) {
    protected val result = ArrayList<ArmyBookEntryOption>()

    inline fun <reified SB> optionX(
        label: String,
        selections: SB.() -> Unit
    ) where SB : SelectionBuilder {
        val sb = SB::class.java.getConstructor().newInstance()
        selections(sb)
        sb.result.find { it.isDefault }
        val defaultValue = sb.result.find { it.isDefault }?.label
        val implicitValue = sb.result.find { it.isImplicit }?.label
        val values = sb.build()
        if (defaultValue == null) {
            values.add(ArmyBookEntryOptionSelection(MainBook.None))
        }

        result.add(
            ArmyBookEntryOption(
                label,
                values,
                defaultValue ?: MainBook.None,
                implicitValue,
                1,
                1
            )
        )
    }

    inline fun <reified SB> option(
        name: String,
        defaultValue: String? = null,
        implicit: String? = null,
        minSelection: Int = 1,
        maxSelection: Int = 1,
        selections: SB.() -> Unit
    ) where SB : SelectionBuilder {
        val sb = SB::class.java.getConstructor().newInstance()
        selections(sb)

        result.add(
            ArmyBookEntryOption(
                name,
                sb.build().toMutableList().apply {
                    if (null != defaultValue) {
                        add(ArmyBookEntryOptionSelection(defaultValue))
                    }
                },
                defaultValue,
                implicit,
                minSelection,
                maxSelection
            )
        )
    }


    fun requiredOption(
        name: String,
        selections: SelectionBuilder.() -> Unit
    ) {
        option(
            name = name,
            selections = selections,
            defaultValue = null,
            implicit = null,
            minSelection = 1
        )
    }

    fun optionWithDefault(
        name: String,
        defaultValue: String? = null,
        selections: SelectionBuilder.() -> Unit
    ) {
        option(name, defaultValue, selections = selections)

    }

    fun title(
        selections: SelectionBuilder.() -> Unit
    ) {
        optionX<SelectionBuilder>("Title", selections)
    }


    fun shield(points: Int) {
        implicitOption("Shield", points)
    }

    fun throwingWeapon(points: Int) {
        implicitOption("ThrowingWeapon", 5, "Throwing Weapon")
    }

    fun rangedWeapon(
        selection: RangedWeaponSelectionBuilder.() -> Unit
    ) {
        optionX<RangedWeaponSelectionBuilder>(
            MainBook.RangedWeapon,
            selections = selection
        )
    }

    fun closeCombatWeapon(
        selection: CloseCombatWeaponSelectionBuilder.() -> Unit
    ) {
        val options = ArrayList<ArmyBookEntryOptionSelection>()
        val sb = CloseCombatWeaponSelectionBuilder()
        selection.invoke(sb)
        options.addAll(sb.build())
        options.add(ArmyBookEntryOptionSelection("Hand Weapon", 0))
        result.add(
            ArmyBookEntryOption(
                name = MainBook.CloseCombatWeapon,
                selections = options,
                default = "Hand Weapon",
                implicit = null,
                minSelection = 1
            )
        )
    }


    fun implicitOption(name: String, points: Int, implicitValue: String = name) {
        result.add(
            ArmyBookEntryOption(
                name = name,
                selections = listOf(
                    ArmyBookEntryOptionSelection(implicitValue, points),
                    ArmyBookEntryOptionSelection("None", 0)
                ),
                default = "None",
                implicit = implicitValue,
                minSelection = 1
            )
        )
    }

    fun implicitOptionUnit(name: String, points: Int, implicitValue: String = name) {
        result.add(
            ArmyBookEntryOption(
                name = name,
                selections = listOf(
                    ArmyBookEntryOptionSelection(implicitValue, points),
                    ArmyBookEntryOptionSelection("None", 0)
                ),
                default = "None",
                implicit = implicitValue,
                minSelection = 1,
                costPerModel = false
            )
        )
    }


    fun armour(
        armourOption: ArmourSelectionBuilder.() -> Unit
    ) {
        optionX<ArmourSelectionBuilder>(MainBook.Armour, armourOption)
    }


    fun pathsOfMagicOption(
        vararg paths: ArcaneCompendiums.MagicPaths
    ) {
        result.add(
            ArmyBookEntryOption(
                ArcaneCompendiums.MagicPath,
                paths.map { ArmyBookEntryOptionSelection(it.name) },
                null,
                null,
                1,
                2
            )
        )
    }


    class ArmourSelectionBuilder : SelectionBuilder() {
        fun lightArmour(points: Int = 0, flags: Flags.() -> Unit = {}) = selection(MainBook.LightArmour, points)
        fun heavyArmour(points: Int = 0, flags: Flags.() -> Unit = {}) = selection(MainBook.HeavyArmour, points)
        fun plateArmour(points: Int = 0, flags: Flags.() -> Unit = {}) = selection(MainBook.PlateArmour, points)
    }


    fun build(): List<ArmyBookEntryOption> = result.toList()
}