package com.ironheroes.sheet.model

import kotlinx.serialization.Serializable

/** The six ability scores used across the sheet. */
@Serializable
enum class Ability { STR, DEX, CON, INT, WIS, CHA }

/** Editable identity / header fields (FR-3). Glory points are free text (may be blank). */
@Serializable
data class Identity(
    val characterName: String = "",
    val player: String = "",
    val race: String = "",
    val clazz: String = "",
    val level: Int = 1,
    val origin: String = "",
    val residence: String = "",
    val status: String = "",
    val gloryPointsMax: String = "",
    val gloryPointsCurrent: String = "",
)

/** Stored ability scores only; modifiers are derived (FR-2.2/FR-4). */
@Serializable
data class AbilityScores(
    val str: Int = 10,
    val dex: Int = 10,
    val con: Int = 10,
    val int: Int = 10,
    val wis: Int = 10,
    val cha: Int = 10,
) {
    fun score(a: Ability): Int = when (a) {
        Ability.STR -> str
        Ability.DEX -> dex
        Ability.CON -> con
        Ability.INT -> int
        Ability.WIS -> wis
        Ability.CHA -> cha
    }
}

/** Per-save editable input; base = level and the governing ability are fixed per save (FR-2.5). */
@Serializable
data class SaveInput(val misc: Int = 0)

@Serializable
data class Saves(
    val fortitude: SaveInput = SaveInput(),
    val reflex: SaveInput = SaveInput(),
    val will: SaveInput = SaveInput(),
)

@Serializable
enum class SkillGroup {
    ACADEMIA,
    AGILITY,
    ATHLETICS,
    MYSTICISM,
    PERCEPTION,
    ROBBERY,
    SOCIAL,
    STEALTH,
    THEATRICS,
    WILDERNESS_LORE,
    UNGROUPED,
}

/** One skill row: fixed identity + editable inputs (FR-5). */
@Serializable
data class SkillEntry(
    val id: String,
    val name: String,
    val group: SkillGroup,
    val ability: Ability,
    val ranks: Int = 0,
    val misc: Int = 0,
)

/** Iron Heroes defense breakdown inputs; Base/Active/Passive/Touch/Flat-Footed are derived (FR-2.7). */
@Serializable
data class Defense(
    val agilityClass: Int = 0,
    val armor: Int = 0,
    val shield: Int = 0,
    val natural: Int = 0,
    val misc: Int = 0,
)

@Serializable
data class HitPoints(
    val currentHp: Int = 0,
    val maxHp: Int = 0,
    val reserveCurrent: Int = 0,
    val reserveMax: Int = 0,
    val damageReduction: Int = 0,
    val damageTaken: Int = 0,
)

/** Melee/ranged/grapple totals are derived from classBase + ability mod (FR-2.9). */
@Serializable
data class Attacks(
    val classBase: Int = 0,
    val multi1: Int = 0,
    val multi2: Int = 0,
    val multi3: Int = 0,
)

@Serializable
data class Weapon(
    val id: String,
    val name: String = "",
    val attackBonus: String = "",
    val damage: String = "",
    val critical: String = "",
    val rangeTypeSize: String = "",
)

@Serializable
data class ArmorItem(
    val id: String,
    val name: String = "",
    val defenseBonus: String = "",
    val maxDex: String = "",
    val checkPenalty: String = "",
)

@Serializable
data class EquipmentItem(
    val id: String,
    val item: String = "",
    val location: String = "",
    val quantity: String = "",
)

/** Feat/Trait descriptions are stored verbatim (NFR-3). */
@Serializable
data class Feat(val name: String, val kind: String = "", val description: String = "")

@Serializable
data class Trait(val name: String, val description: String = "")

/** Interactive class-feature counters; long rules text lives in data/RulesText (not per-character). */
@Serializable
data class ClassFeatures(
    val weaponPoolTokens: Int = 0,
    val favoriteWeapon: String = "",
    val omenTokens: Int = 0,
)

/** Serialization root for the whole character. */
@Serializable
data class Character(
    val schemaVersion: Int = 1,
    val identity: Identity = Identity(),
    val abilities: AbilityScores = AbilityScores(),
    val saves: Saves = Saves(),
    val skills: List<SkillEntry> = defaultSkillRows(),
    val defense: Defense = Defense(),
    val hitPoints: HitPoints = HitPoints(),
    val attacks: Attacks = Attacks(),
    val weapons: List<Weapon> = emptyList(),
    val armor: List<ArmorItem> = emptyList(),
    val equipment: List<EquipmentItem> = emptyList(),
    val feats: List<Feat> = emptyList(),
    val traits: List<Trait> = emptyList(),
    val classFeatures: ClassFeatures = ClassFeatures(),
)
