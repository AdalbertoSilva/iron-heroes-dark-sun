package com.ironheroes.sheet.model

import com.ironheroes.sheet.model.Ability.CHA
import com.ironheroes.sheet.model.Ability.CON
import com.ironheroes.sheet.model.Ability.DEX
import com.ironheroes.sheet.model.Ability.INT
import com.ironheroes.sheet.model.Ability.STR
import com.ironheroes.sheet.model.Ability.WIS
import com.ironheroes.sheet.model.SkillGroup.ACADEMIA
import com.ironheroes.sheet.model.SkillGroup.AGILITY
import com.ironheroes.sheet.model.SkillGroup.ATHLETICS
import com.ironheroes.sheet.model.SkillGroup.MYSTICISM
import com.ironheroes.sheet.model.SkillGroup.PERCEPTION
import com.ironheroes.sheet.model.SkillGroup.ROBBERY
import com.ironheroes.sheet.model.SkillGroup.SOCIAL
import com.ironheroes.sheet.model.SkillGroup.STEALTH
import com.ironheroes.sheet.model.SkillGroup.THEATRICS
import com.ironheroes.sheet.model.SkillGroup.UNGROUPED
import com.ironheroes.sheet.model.SkillGroup.WILDERNESS_LORE

/**
 * The fixed FR-5.2 skill list. Structural model data (ids/names/group/ability), ranks & misc at 0.
 * Skills are never added/removed by the UI, so this set is structurally guaranteed to be complete.
 */
fun defaultSkillRows(): List<SkillEntry> = listOf(
    // ACADEMIA
    SkillEntry("appraise", "Appraise", ACADEMIA, INT),
    SkillEntry("concentration", "Concentration", ACADEMIA, CON),
    SkillEntry("decipher_script", "Decipher Script", ACADEMIA, INT),
    SkillEntry("heal", "Heal", ACADEMIA, WIS),
    SkillEntry("knowledge_1", "Knowledge 1", ACADEMIA, INT),
    SkillEntry("knowledge_2", "Knowledge 2", ACADEMIA, INT),
    SkillEntry("knowledge_3", "Knowledge 3", ACADEMIA, INT),
    SkillEntry("knowledge_4", "Knowledge 4", ACADEMIA, INT),
    // AGILITY
    SkillEntry("balance", "Balance", AGILITY, DEX),
    SkillEntry("escape_artist", "Escape Artist", AGILITY, DEX),
    SkillEntry("tumble", "Tumble", AGILITY, DEX),
    // ATHLETICS
    SkillEntry("climb", "Climb", ATHLETICS, STR),
    SkillEntry("jump", "Jump", ATHLETICS, STR),
    SkillEntry("swim", "Swim", ATHLETICS, STR),
    // MYSTICISM
    SkillEntry("spellcraft", "SpellCraft", MYSTICISM, INT),
    SkillEntry("use_magic_device", "Use Magic Device", MYSTICISM, CHA),
    // PERCEPTION
    SkillEntry("listen", "Listen", PERCEPTION, WIS),
    SkillEntry("search", "Search", PERCEPTION, INT),
    SkillEntry("sense_motive", "Sense Motive", PERCEPTION, WIS),
    SkillEntry("spot", "Spot", PERCEPTION, WIS),
    // ROBBERY
    SkillEntry("disable_device", "Disable Device", ROBBERY, INT),
    SkillEntry("forgery", "Forgery", ROBBERY, INT),
    SkillEntry("open_lock", "Open Lock", ROBBERY, DEX),
    SkillEntry("sleight_of_hand", "Sleight of Hand", ROBBERY, DEX),
    // SOCIAL
    SkillEntry("bluff", "Bluff", SOCIAL, CHA),
    SkillEntry("diplomacy", "Diplomacy", SOCIAL, CHA),
    SkillEntry("gather_information", "Gather Information", SOCIAL, CHA),
    SkillEntry("intimidate", "Intimidate", SOCIAL, CHA),
    // STEALTH
    SkillEntry("hide", "Hide", STEALTH, DEX),
    SkillEntry("move_silently", "Move Silently", STEALTH, DEX),
    // THEATRICS
    SkillEntry("disguise", "Disguise", THEATRICS, CHA),
    SkillEntry("perform", "Perform", THEATRICS, CHA),
    // WILDERNESS LORE
    SkillEntry("handle_animal", "Handle Animal", WILDERNESS_LORE, CHA),
    SkillEntry("ride", "Ride", WILDERNESS_LORE, DEX),
    SkillEntry("survival", "Survival", WILDERNESS_LORE, WIS),
    SkillEntry("use_rope", "Use Rope", WILDERNESS_LORE, DEX),
    // UNGROUPED
    SkillEntry("craft_metal", "Craft (Metal)", UNGROUPED, INT),
    SkillEntry("profession", "Profession", UNGROUPED, WIS),
)
