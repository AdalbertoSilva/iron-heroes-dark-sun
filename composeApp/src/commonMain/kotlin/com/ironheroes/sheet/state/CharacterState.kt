package com.ironheroes.sheet.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ironheroes.sheet.model.ArmorItem
import com.ironheroes.sheet.model.Character
import com.ironheroes.sheet.model.EquipmentItem
import com.ironheroes.sheet.model.Weapon
import com.ironheroes.sheet.persistence.CharacterRepository

/**
 * Single mutable holder wrapping the immutable [Character]. Every edit produces a new Character
 * via copy(...), and because [character] is Compose state, any composable reading it recomposes
 * automatically (FR-2.3). Clamping lives here (design §7.3/§7.4).
 */
class CharacterState(
    initial: Character,
    private val repository: CharacterRepository? = null,
) {
    var character by mutableStateOf(initial)
        private set

    // --- Identity ---
    fun setName(v: String) = update { it.copy(identity = it.identity.copy(characterName = v)) }
    fun setPlayer(v: String) = update { it.copy(identity = it.identity.copy(player = v)) }
    fun setRace(v: String) = update { it.copy(identity = it.identity.copy(race = v)) }
    fun setClazz(v: String) = update { it.copy(identity = it.identity.copy(clazz = v)) }
    fun setLevel(v: Int) = update { it.copy(identity = it.identity.copy(level = v.coerceIn(1, 40))) }
    fun setOrigin(v: String) = update { it.copy(identity = it.identity.copy(origin = v)) }
    fun setResidence(v: String) = update { it.copy(identity = it.identity.copy(residence = v)) }
    fun setStatus(v: String) = update { it.copy(identity = it.identity.copy(status = v)) }
    fun setGloryMax(v: String) = update { it.copy(identity = it.identity.copy(gloryPointsMax = v)) }
    fun setGloryCurrent(v: String) = update { it.copy(identity = it.identity.copy(gloryPointsCurrent = v)) }

    // --- Ability scores (clamp 0..99) ---
    fun setStr(v: Int) = update { it.copy(abilities = it.abilities.copy(str = v.coerceIn(0, 99))) }
    fun setDex(v: Int) = update { it.copy(abilities = it.abilities.copy(dex = v.coerceIn(0, 99))) }
    fun setCon(v: Int) = update { it.copy(abilities = it.abilities.copy(con = v.coerceIn(0, 99))) }
    fun setInt(v: Int) = update { it.copy(abilities = it.abilities.copy(int = v.coerceIn(0, 99))) }
    fun setWis(v: Int) = update { it.copy(abilities = it.abilities.copy(wis = v.coerceIn(0, 99))) }
    fun setCha(v: Int) = update { it.copy(abilities = it.abilities.copy(cha = v.coerceIn(0, 99))) }

    // --- Saves (misc allows negatives) ---
    fun setFortMisc(v: Int) = update { it.copy(saves = it.saves.copy(fortitude = it.saves.fortitude.copy(misc = v))) }
    fun setRefMisc(v: Int) = update { it.copy(saves = it.saves.copy(reflex = it.saves.reflex.copy(misc = v))) }
    fun setWillMisc(v: Int) = update { it.copy(saves = it.saves.copy(will = it.saves.will.copy(misc = v))) }

    // --- Skills (ranks clamp >= 0, misc allows negatives) ---
    fun setSkillRanks(id: String, v: Int) = update { c ->
        c.copy(skills = c.skills.map { if (it.id == id) it.copy(ranks = v.coerceAtLeast(0)) else it })
    }

    fun setSkillMisc(id: String, v: Int) = update { c ->
        c.copy(skills = c.skills.map { if (it.id == id) it.copy(misc = v) else it })
    }

    // --- Defense components (misc allows negatives; others clamp >= 0) ---
    fun setAgilityClass(v: Int) = update { it.copy(defense = it.defense.copy(agilityClass = v.coerceAtLeast(0))) }
    fun setArmor(v: Int) = update { it.copy(defense = it.defense.copy(armor = v.coerceAtLeast(0))) }
    fun setShield(v: Int) = update { it.copy(defense = it.defense.copy(shield = v.coerceAtLeast(0))) }
    fun setNatural(v: Int) = update { it.copy(defense = it.defense.copy(natural = v.coerceAtLeast(0))) }
    fun setDefenseMisc(v: Int) = update { it.copy(defense = it.defense.copy(misc = v)) }

    // --- Hit points ---
    fun setCurrentHp(v: Int) = update { it.copy(hitPoints = it.hitPoints.copy(currentHp = v)) }
    fun setMaxHp(v: Int) = update { it.copy(hitPoints = it.hitPoints.copy(maxHp = v.coerceAtLeast(0))) }
    fun setReserveCurrent(v: Int) = update { it.copy(hitPoints = it.hitPoints.copy(reserveCurrent = v)) }
    fun setReserveMax(v: Int) = update { it.copy(hitPoints = it.hitPoints.copy(reserveMax = v.coerceAtLeast(0))) }
    fun setDamageReduction(v: Int) = update { it.copy(hitPoints = it.hitPoints.copy(damageReduction = v.coerceAtLeast(0))) }
    fun setDamageTaken(v: Int) = update { it.copy(hitPoints = it.hitPoints.copy(damageTaken = v.coerceAtLeast(0))) }

    // --- Attacks ---
    fun setClassBase(v: Int) = update { it.copy(attacks = it.attacks.copy(classBase = v)) }
    fun setMulti1(v: Int) = update { it.copy(attacks = it.attacks.copy(multi1 = v)) }
    fun setMulti2(v: Int) = update { it.copy(attacks = it.attacks.copy(multi2 = v)) }
    fun setMulti3(v: Int) = update { it.copy(attacks = it.attacks.copy(multi3 = v)) }

    // --- Weapons ---
    fun addWeapon() = update { it.copy(weapons = it.weapons + Weapon(id = "w_${nextId()}")) }
    fun removeWeapon(id: String) = update { c -> c.copy(weapons = c.weapons.filterNot { it.id == id }) }
    fun updateWeapon(w: Weapon) = update { c -> c.copy(weapons = c.weapons.map { if (it.id == w.id) w else it }) }

    // --- Armor ---
    fun addArmor() = update { it.copy(armor = it.armor + ArmorItem(id = "a_${nextId()}")) }
    fun removeArmor(id: String) = update { c -> c.copy(armor = c.armor.filterNot { it.id == id }) }
    fun updateArmor(a: ArmorItem) = update { c -> c.copy(armor = c.armor.map { if (it.id == a.id) a else it }) }

    // --- Equipment ---
    fun addEquipment() = update { it.copy(equipment = it.equipment + EquipmentItem(id = "e_${nextId()}")) }
    fun removeEquipment(id: String) = update { c -> c.copy(equipment = c.equipment.filterNot { it.id == id }) }
    fun updateEquipment(e: EquipmentItem) = update { c -> c.copy(equipment = c.equipment.map { if (it.id == e.id) e else it }) }

    // --- Class-feature tokens ---
    fun setFavoriteWeapon(v: String) = update { it.copy(classFeatures = it.classFeatures.copy(favoriteWeapon = v)) }
    fun incWeaponPool() = update { it.copy(classFeatures = it.classFeatures.copy(weaponPoolTokens = it.classFeatures.weaponPoolTokens + 1)) }
    fun decWeaponPool() = update { it.copy(classFeatures = it.classFeatures.copy(weaponPoolTokens = (it.classFeatures.weaponPoolTokens - 1).coerceAtLeast(0))) }
    fun resetWeaponPool() = update { it.copy(classFeatures = it.classFeatures.copy(weaponPoolTokens = 0)) }
    fun incOmen() = update { it.copy(classFeatures = it.classFeatures.copy(omenTokens = it.classFeatures.omenTokens + 1)) }
    fun decOmen() = update { it.copy(classFeatures = it.classFeatures.copy(omenTokens = (it.classFeatures.omenTokens - 1).coerceAtLeast(0))) }

    // --- Lifecycle ---
    fun newBlank() {
        character = Character()
    }

    fun replace(c: Character) {
        character = c
    }

    suspend fun load() {
        repository?.load()?.let { character = it }
    }

    suspend fun save() {
        repository?.save(character)
    }

    suspend fun clear() {
        repository?.clear()
    }

    private inline fun update(block: (Character) -> Character) {
        character = block(character)
    }

    private var idCounter = 0
    private fun nextId(): String = "${(idCounter++)}_${idSeed++}"

    private companion object {
        var idSeed = 0
    }
}
