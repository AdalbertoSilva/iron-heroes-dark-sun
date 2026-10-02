package com.ironheroes.sheet.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ironheroes.sheet.data.RulesText
import com.ironheroes.sheet.derive.abilityModifier
import com.ironheroes.sheet.derive.defenseActive
import com.ironheroes.sheet.derive.defenseBase
import com.ironheroes.sheet.derive.defenseFlatFooted
import com.ironheroes.sheet.derive.defenseNatural
import com.ironheroes.sheet.derive.defensePassive
import com.ironheroes.sheet.derive.defenseShield
import com.ironheroes.sheet.derive.defenseTotal
import com.ironheroes.sheet.derive.defenseTouch
import com.ironheroes.sheet.derive.expertStrikeBonus
import com.ironheroes.sheet.derive.fortitude
import com.ironheroes.sheet.derive.grappleAttack
import com.ironheroes.sheet.derive.meleeAttack
import com.ironheroes.sheet.derive.rangedAttack
import com.ironheroes.sheet.derive.reflex
import com.ironheroes.sheet.derive.skillModifier
import com.ironheroes.sheet.derive.skillTotal
import com.ironheroes.sheet.derive.will
import com.ironheroes.sheet.model.Ability
import com.ironheroes.sheet.model.SkillGroup
import com.ironheroes.sheet.state.CharacterState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IdentityHeader(state: CharacterState) {
    val id = state.character.identity
    SectionCard("Identity") {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            LabeledTextField("Character Name", id.characterName, state::setName, Modifier.width(220.dp))
            LabeledTextField("Player", id.player, state::setPlayer, Modifier.width(160.dp))
            LabeledTextField("Race", id.race, state::setRace, Modifier.width(140.dp))
            LabeledTextField("Class", id.clazz, state::setClazz, Modifier.width(180.dp))
            LabeledIntField("Level", id.level, state::setLevel, Modifier.width(100.dp), emptyValue = 1)
            LabeledTextField("Origin", id.origin, state::setOrigin, Modifier.width(160.dp))
            LabeledTextField("Residence", id.residence, state::setResidence, Modifier.width(160.dp))
            LabeledTextField("Status", id.status, state::setStatus, Modifier.width(160.dp))
            LabeledTextField("Glory Points (Max)", id.gloryPointsMax, state::setGloryMax, Modifier.width(150.dp))
            LabeledTextField("Glory Points (Current)", id.gloryPointsCurrent, state::setGloryCurrent, Modifier.width(150.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AbilityScoresSection(state: CharacterState) {
    val c = state.character
    SectionCard("Ability Scores") {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AbilityScoreField("Strength", c.abilities.str, state::setStr)
            AbilityScoreField("Dexterity", c.abilities.dex, state::setDex)
            AbilityScoreField("Constitution", c.abilities.con, state::setCon)
            AbilityScoreField("Intelligence", c.abilities.int, state::setInt)
            AbilityScoreField("Wisdom", c.abilities.wis, state::setWis)
            AbilityScoreField("Charisma", c.abilities.cha, state::setCha)
        }
    }
}

@Composable
private fun AbilityScoreField(label: String, score: Int, onChange: (Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        LabeledIntField(label, score, onChange, Modifier.width(140.dp))
        ComputedValue("Mod", abilityModifier(score))
    }
}

@Composable
fun SavingThrowsSection(state: CharacterState) {
    val c = state.character
    SectionCard("Saving Throws") {
        SaveRow("Fortitude (Con)", c.identity.level, abilityModifier(c.abilities, Ability.CON), c.saves.fortitude.misc, fortitude(c), state::setFortMisc)
        SaveRow("Reflex (Dex)", c.identity.level, abilityModifier(c.abilities, Ability.DEX), c.saves.reflex.misc, reflex(c), state::setRefMisc)
        SaveRow("Will (Wis)", c.identity.level, abilityModifier(c.abilities, Ability.WIS), c.saves.will.misc, will(c), state::setWillMisc)
    }
}

@Composable
private fun SaveRow(label: String, base: Int, abilityMod: Int, misc: Int, total: Int, onMisc: (Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 4.dp)) {
        Text(label, modifier = Modifier.width(150.dp))
        ComputedValue("Base", base.toString())
        ComputedValue("Ability", formatSigned(abilityMod))
        LabeledIntField("Misc", misc, onMisc, Modifier.width(100.dp))
        ComputedValue("Total", formatSigned(total))
    }
}

@Composable
fun SkillsSection(state: CharacterState) {
    val c = state.character
    SectionCard("Skills") {
        for (group in SkillGroup.entries) {
            val rows = c.skills.filter { it.group == group }
            if (rows.isEmpty()) continue
            Text(
                group.name.replace('_', ' '),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 10.dp, bottom = 2.dp),
            )
            for (s in rows) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 2.dp)) {
                    Text(s.name, modifier = Modifier.width(160.dp))
                    Text("(${s.ability.name})", modifier = Modifier.width(60.dp), style = MaterialTheme.typography.bodySmall)
                    ComputedValue("Mod", formatSigned(skillModifier(c, s)))
                    LabeledIntField("Ranks", s.ranks, { state.setSkillRanks(s.id, it) }, Modifier.width(90.dp))
                    LabeledIntField("Misc", s.misc, { state.setSkillMisc(s.id, it) }, Modifier.width(90.dp))
                    ComputedValue("Total", formatSigned(skillTotal(c, s)))
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DefenseSection(state: CharacterState) {
    val c = state.character
    SectionCard("Defense") {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            LabeledIntField("Agility/Class", c.defense.agilityClass, state::setAgilityClass, Modifier.width(130.dp))
            LabeledIntField("Armor", c.defense.armor, state::setArmor, Modifier.width(110.dp))
            LabeledIntField("Shield", c.defense.shield, state::setShield, Modifier.width(110.dp))
            LabeledIntField("Natural", c.defense.natural, state::setNatural, Modifier.width(110.dp))
            LabeledIntField("Misc", c.defense.misc, state::setDefenseMisc, Modifier.width(110.dp))
        }
        Column(Modifier.padding(top = 8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ComputedValue("Base", defenseBase().toString())
                ComputedValue("Active", defenseActive(c).toString())
                ComputedValue("Touch", defenseTouch(c).toString())
                ComputedValue("Flat-Footed", defenseFlatFooted(c).toString())
                ComputedValue("Total", defenseTotal(c).toString())
            }
            // Passive aggregate with Natural/Shield as indented sub-items (not summed into Total again).
            ComputedValue("Passive (armor+shield+natural+misc)", defensePassive(c).toString())
            Column(Modifier.padding(start = 24.dp)) {
                ComputedValue("↳ Natural", defenseNatural(c).toString())
                ComputedValue("↳ Shield", defenseShield(c).toString())
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HitPointsSection(state: CharacterState) {
    val hp = state.character.hitPoints
    SectionCard("Hit Points") {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            LabeledIntField("Current HP", hp.currentHp, state::setCurrentHp, Modifier.width(120.dp))
            LabeledIntField("Max HP", hp.maxHp, state::setMaxHp, Modifier.width(120.dp))
            LabeledIntField("Reserve (Current)", hp.reserveCurrent, state::setReserveCurrent, Modifier.width(140.dp))
            LabeledIntField("Reserve (Max)", hp.reserveMax, state::setReserveMax, Modifier.width(140.dp))
            LabeledIntField("Damage Reduction", hp.damageReduction, state::setDamageReduction, Modifier.width(150.dp))
            LabeledIntField("Damage Taken", hp.damageTaken, state::setDamageTaken, Modifier.width(140.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AttacksSection(state: CharacterState) {
    val c = state.character
    SectionCard("Attacks") {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            LabeledIntField("Class Base", c.attacks.classBase, state::setClassBase, Modifier.width(120.dp))
            ComputedValue("Melee (Str)", meleeAttack(c))
            ComputedValue("Ranged (Dex)", rangedAttack(c))
            ComputedValue("Grapple (Str)", grappleAttack(c))
            LabeledIntField("Multi 1", c.attacks.multi1, state::setMulti1, Modifier.width(100.dp))
            LabeledIntField("Multi 2", c.attacks.multi2, state::setMulti2, Modifier.width(100.dp))
            LabeledIntField("Multi 3", c.attacks.multi3, state::setMulti3, Modifier.width(100.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WeaponsSection(state: CharacterState) {
    SectionCard("Weapons") {
        for (w in state.character.weapons) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                LabeledTextField("Name", w.name, { state.updateWeapon(w.copy(name = it)) }, Modifier.width(180.dp))
                LabeledTextField("Attack Bonus", w.attackBonus, { state.updateWeapon(w.copy(attackBonus = it)) }, Modifier.width(130.dp))
                LabeledTextField("Damage", w.damage, { state.updateWeapon(w.copy(damage = it)) }, Modifier.width(110.dp))
                LabeledTextField("Critical", w.critical, { state.updateWeapon(w.copy(critical = it)) }, Modifier.width(110.dp))
                LabeledTextField("Range/Type/Size", w.rangeTypeSize, { state.updateWeapon(w.copy(rangeTypeSize = it)) }, Modifier.width(150.dp))
                RemoveButton({ state.removeWeapon(w.id) })
            }
        }
        OutlinedButton(onClick = state::addWeapon, modifier = Modifier.padding(top = 8.dp)) { Text("Add Weapon") }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ArmorSection(state: CharacterState) {
    SectionCard("Armor / Protective Items") {
        for (a in state.character.armor) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                LabeledTextField("Name", a.name, { state.updateArmor(a.copy(name = it)) }, Modifier.width(180.dp))
                LabeledTextField("Defense Bonus", a.defenseBonus, { state.updateArmor(a.copy(defenseBonus = it)) }, Modifier.width(140.dp))
                LabeledTextField("Max Dex", a.maxDex, { state.updateArmor(a.copy(maxDex = it)) }, Modifier.width(110.dp))
                LabeledTextField("Check Penalty", a.checkPenalty, { state.updateArmor(a.copy(checkPenalty = it)) }, Modifier.width(130.dp))
                RemoveButton({ state.removeArmor(a.id) })
            }
        }
        OutlinedButton(onClick = state::addArmor, modifier = Modifier.padding(top = 8.dp)) { Text("Add Armor") }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EquipmentSection(state: CharacterState) {
    SectionCard("Equipment & Inventory") {
        for (e in state.character.equipment) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                LabeledTextField("Item", e.item, { state.updateEquipment(e.copy(item = it)) }, Modifier.width(220.dp))
                LabeledTextField("Location", e.location, { state.updateEquipment(e.copy(location = it)) }, Modifier.width(150.dp))
                LabeledTextField("Quantity", e.quantity, { state.updateEquipment(e.copy(quantity = it)) }, Modifier.width(110.dp))
                RemoveButton({ state.removeEquipment(e.id) })
            }
        }
        OutlinedButton(onClick = state::addEquipment, modifier = Modifier.padding(top = 8.dp)) { Text("Add Item") }
    }
}

@Composable
fun FeatsSection(state: CharacterState) {
    SectionCard("Feats") {
        for (f in state.character.feats) {
            Column(Modifier.padding(vertical = 4.dp)) {
                val title = if (f.kind.isNotBlank()) "${f.name} (${f.kind})" else f.name
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(f.description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun TraitsSection(state: CharacterState) {
    SectionCard("Traits") {
        for (t in state.character.traits) {
            Column(Modifier.padding(vertical = 4.dp)) {
                Text(t.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(t.description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClassFeaturesSection(state: CharacterState) {
    val c = state.character
    val cf = c.classFeatures
    SectionCard("Class Features") {
        LabeledTextField("Favorite Weapon", cf.favoriteWeapon, state::setFavoriteWeapon, Modifier.width(240.dp))

        // Interactive token trackers (FR-11).
        TokenCounter(
            label = "Weapon Pool Tokens",
            count = cf.weaponPoolTokens,
            onIncrement = state::incWeaponPool,
            onDecrement = state::decWeaponPool,
            onReset = state::resetWeaponPool,
            resetLabel = "Reset (target change)",
        )
        TokenCounter(
            label = "Omen Tokens",
            count = cf.omenTokens,
            onIncrement = state::incOmen,
            onDecrement = state::decOmen,
            // No reset — Omen Tokens accumulate and never expire.
        )

        // Expert Strike bonus preview, capped at class level (AC-15).
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 4.dp)) {
            Text("Expert Strike bonus (spending all Weapon Pool tokens):")
            ComputedValue(
                "Bonus",
                formatSigned(expertStrikeBonus(cf.weaponPoolTokens, c.identity.level)),
            )
        }

        // Verbatim expandable panels (FR-12).
        ExpandablePanel("Weapon Pool") {
            Text(RulesText.WEAPON_POOL_SHORT, style = MaterialTheme.typography.bodyMedium)
            Text(RulesText.WEAPON_POOL_FULL, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 6.dp))
        }
        ExpandablePanel("Favorite Weapon") {
            Text(RulesText.FAVORITE_WEAPON_FULL, style = MaterialTheme.typography.bodyMedium)
        }
        ExpandablePanel("Expert Strike") {
            Text(RulesText.EXPERT_STRIKE_SHORT, style = MaterialTheme.typography.bodyMedium)
            Text(RulesText.EXPERT_STRIKE_FULL, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 6.dp))
        }
        ExpandablePanel("Mighty Blow") {
            Text(RulesText.MIGHTY_BLOW_SHORT, style = MaterialTheme.typography.bodyMedium)
            Text(RulesText.MIGHTY_BLOW_FULL, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 6.dp))
        }
    }
}
