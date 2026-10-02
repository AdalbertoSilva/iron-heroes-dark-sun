package com.ironheroes.sheet.data

import com.ironheroes.sheet.model.AbilityScores
import com.ironheroes.sheet.model.Attacks
import com.ironheroes.sheet.model.ArmorItem
import com.ironheroes.sheet.model.Character
import com.ironheroes.sheet.model.ClassFeatures
import com.ironheroes.sheet.model.Defense
import com.ironheroes.sheet.model.EquipmentItem
import com.ironheroes.sheet.model.Feat
import com.ironheroes.sheet.model.HitPoints
import com.ironheroes.sheet.model.Identity
import com.ironheroes.sheet.model.SaveInput
import com.ironheroes.sheet.model.Saves
import com.ironheroes.sheet.model.Trait
import com.ironheroes.sheet.model.Weapon
import com.ironheroes.sheet.model.defaultSkillRows

/**
 * The default character (Arnaldo Choziner), pre-loaded on first run (FR-13.3) and used as the
 * AC-6 verification fixture. IMPORTANT: level = 4 and saves = Saves() (all misc 0) are the single
 * source of the AC-6 save numbers (Fort 7 / Ref 6 / Will 5); do not add a non-zero save misc.
 */
object DefaultCharacter {

    /** Reasonable default ranks for Athletics + Social skills; only ranks/misc are edited. */
    private val defaultRanks: Map<String, Int> = mapOf(
        "climb" to 4,
        "jump" to 4,
        "swim" to 4,
        "bluff" to 2,
        "diplomacy" to 2,
        "gather_information" to 2,
        "intimidate" to 4,
    )

    private val seededSkills = defaultSkillRows().map { row ->
        defaultRanks[row.id]?.let { row.copy(ranks = it) } ?: row
    }

    val arnaldo: Character = Character(
        identity = Identity(
            characterName = "Arnaldo Choziner",
            player = "Patrick",
            race = "Human",
            clazz = "Weapon Master",
            level = 4,
        ),
        abilities = AbilityScores(
            str = 21,
            dex = 14,
            con = 16,
            int = 10,
            wis = 12,
            cha = 8,
        ),
        // All misc pinned to 0 — totals come solely from base(level 4) + ability mod.
        saves = Saves(
            fortitude = SaveInput(misc = 0),
            reflex = SaveInput(misc = 0),
            will = SaveInput(misc = 0),
        ),
        skills = seededSkills,
        // agilityClass 4 + armor 1 -> Total 15.
        defense = Defense(agilityClass = 4, armor = 1),
        hitPoints = HitPoints(
            currentHp = 47,
            maxHp = 47,
            reserveCurrent = 47,
            reserveMax = 47,
            damageReduction = 0,
        ),
        attacks = Attacks(classBase = 4),
        weapons = listOf(
            Weapon(
                id = "w_great_sword",
                name = "Great Sword",
                damage = "2D8",
                critical = "19+ x2",
                rangeTypeSize = "Melee",
            ),
            Weapon(
                id = "w_iron_dagger",
                name = "Adaga de ferro",
                damage = "1D4",
                critical = "x2",
                rangeTypeSize = "Melee",
            ),
        ),
        armor = listOf(
            ArmorItem(
                id = "a_boss",
                name = "Armadura Boss",
                defenseBonus = "1D6",
            ),
        ),
        equipment = listOf(
            EquipmentItem(id = "e_toolkit", item = "Kit de ferramentas", quantity = "1"),
            EquipmentItem(id = "e_cantil", item = "Cantil", location = "CHEIO", quantity = "1"),
        ),
        feats = listOf(
            Feat("Weapon Focus", "Passive", RulesText.FEAT_WEAPON_FOCUS),
            Feat("Cleave", "Passive", RulesText.FEAT_CLEAVE),
            Feat("Power Attack", "", RulesText.FEAT_POWER_ATTACK),
        ),
        traits = listOf(
            Trait("Resilient Toughness", RulesText.TRAIT_RESILIENT_TOUGHNESS),
            Trait("Mighty Build", RulesText.TRAIT_MIGHTY_BUILD),
            Trait("Child of Omen (Omen of Sacrifice)", RulesText.TRAIT_CHILD_OF_OMEN),
        ),
        classFeatures = ClassFeatures(
            weaponPoolTokens = 0,
            favoriteWeapon = "Espada Grande",
            omenTokens = 0,
        ),
    )
}
