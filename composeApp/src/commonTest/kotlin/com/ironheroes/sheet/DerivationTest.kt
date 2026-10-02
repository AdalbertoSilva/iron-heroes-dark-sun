package com.ironheroes.sheet

import com.ironheroes.sheet.data.DefaultCharacter
import com.ironheroes.sheet.derive.abilityModifier
import com.ironheroes.sheet.derive.defenseTotal
import com.ironheroes.sheet.derive.expertStrikeBonus
import com.ironheroes.sheet.derive.fortitude
import com.ironheroes.sheet.derive.reflex
import com.ironheroes.sheet.derive.will
import com.ironheroes.sheet.persistence.CharacterJson
import kotlin.test.Test
import kotlin.test.assertEquals

class DerivationTest {

    private val arnaldo = DefaultCharacter.arnaldo

    @Test
    fun abilityModifiers_matchKnownGoodNumbers() {
        assertEquals(5, abilityModifier(21), "Str 21 -> +5")
        assertEquals(3, abilityModifier(16), "Con 16 -> +3")
        assertEquals(-1, abilityModifier(8), "Cha 8 -> -1")
        // Floor toward -inf edge cases.
        assertEquals(-2, abilityModifier(7), "score 7 -> -2")
        assertEquals(-1, abilityModifier(9), "score 9 -> -1")
    }

    @Test
    fun savingThrows_matchKnownGoodNumbers() {
        assertEquals(7, fortitude(arnaldo), "Fortitude 4 + Con(+3)")
        assertEquals(6, reflex(arnaldo), "Reflex 4 + Dex(+2)")
        assertEquals(5, will(arnaldo), "Will 4 + Wis(+1)")
    }

    @Test
    fun defenseTotal_isFifteen() {
        assertEquals(15, defenseTotal(arnaldo), "10 + agility/class 4 + armor 1")
    }

    @Test
    fun hitPoints_areFortySeven() {
        assertEquals(47, arnaldo.hitPoints.maxHp, "HP 47")
    }

    @Test
    fun expertStrike_isCappedAtClassLevel() {
        assertEquals(4, expertStrikeBonus(tokensSpent = 10, classLevel = 4), "capped at level 4")
        assertEquals(2, expertStrikeBonus(tokensSpent = 2, classLevel = 4), "below cap")
        assertEquals(0, expertStrikeBonus(tokensSpent = -3, classLevel = 4), "floored at 0")
    }

    @Test
    fun serialization_roundTripsIntact() {
        val encoded = CharacterJson.encodeToString(arnaldo)
        val decoded = CharacterJson.decodeFromString<com.ironheroes.sheet.model.Character>(encoded)
        assertEquals(arnaldo, decoded, "round-trip must preserve all fields")
    }
}
