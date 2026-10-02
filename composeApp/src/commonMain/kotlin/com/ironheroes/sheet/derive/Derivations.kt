package com.ironheroes.sheet.derive

import com.ironheroes.sheet.model.Ability
import com.ironheroes.sheet.model.AbilityScores
import com.ironheroes.sheet.model.Character
import com.ironheroes.sheet.model.SkillEntry
import kotlin.math.floor
import kotlin.math.min

/**
 * Pure derivation functions. No state, no side effects — identical on every platform (NFR-2).
 * Derived values are never stored on the model; these functions are the single source of truth.
 */

/** FR-2.4: ability modifier = floor((score - 10) / 2). Floors toward -inf (score 7 -> -2, 8 -> -1). */
fun abilityModifier(score: Int): Int = floor((score - 10) / 2.0).toInt()

fun abilityModifier(a: AbilityScores, which: Ability): Int = abilityModifier(a.score(which))

// FR-2.5: save total = base(level) + governing ability mod + misc. Fort=Con, Ref=Dex, Will=Wis.
fun fortitude(c: Character): Int =
    c.identity.level + abilityModifier(c.abilities, Ability.CON) + c.saves.fortitude.misc

fun reflex(c: Character): Int =
    c.identity.level + abilityModifier(c.abilities, Ability.DEX) + c.saves.reflex.misc

fun will(c: Character): Int =
    c.identity.level + abilityModifier(c.abilities, Ability.WIS) + c.saves.will.misc

// FR-2.6: skill total = governing ability mod + ranks + misc.
fun skillModifier(c: Character, s: SkillEntry): Int = abilityModifier(c.abilities, s.ability)

fun skillTotal(c: Character, s: SkillEntry): Int = skillModifier(c, s) + s.ranks + s.misc

// FR-2.7: defense breakdown. See design §4 input->display mapping.
fun defenseBase(): Int = 10

fun defenseTotal(c: Character): Int =
    defenseBase() + c.defense.agilityClass + c.defense.armor + c.defense.shield +
        c.defense.natural + c.defense.misc

/** Active = agility/class dodge component. */
fun defenseActive(c: Character): Int = c.defense.agilityClass

/** Passive = item/body components incl. misc so the columns sum to Total. */
fun defensePassive(c: Character): Int =
    c.defense.armor + c.defense.shield + c.defense.natural + c.defense.misc

/** Touch = drop item/body (armor/shield/natural), keep agility/class + misc. */
fun defenseTouch(c: Character): Int = defenseBase() + c.defense.agilityClass + c.defense.misc

/** Flat-Footed = lose the Active (agility/class) component. */
fun defenseFlatFooted(c: Character): Int = defenseTotal(c) - c.defense.agilityClass

/** Itemized sub-components of Passive (rendered indented under Passive, not summed into Total again). */
fun defenseNatural(c: Character): Int = c.defense.natural

fun defenseShield(c: Character): Int = c.defense.shield

// FR-2.9: attack bonuses.
fun meleeAttack(c: Character): Int = c.attacks.classBase + abilityModifier(c.abilities, Ability.STR)

fun rangedAttack(c: Character): Int = c.attacks.classBase + abilityModifier(c.abilities, Ability.DEX)

fun grappleAttack(c: Character): Int = c.attacks.classBase + abilityModifier(c.abilities, Ability.STR)

/** FR-11.4 / AC-15: Expert Strike bonus capped at class level, floored at 0. */
fun expertStrikeBonus(tokensSpent: Int, classLevel: Int): Int =
    min(tokensSpent, classLevel).coerceAtLeast(0)
