package com.ironheroes.sheet

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ironheroes.sheet.data.DefaultCharacter
import com.ironheroes.sheet.persistence.createCharacterRepository
import com.ironheroes.sheet.state.CharacterState
import com.ironheroes.sheet.ui.AbilityScoresSection
import com.ironheroes.sheet.ui.ArmorSection
import com.ironheroes.sheet.ui.AttacksSection
import com.ironheroes.sheet.ui.ClassFeaturesSection
import com.ironheroes.sheet.ui.DefenseSection
import com.ironheroes.sheet.ui.EquipmentSection
import com.ironheroes.sheet.ui.FeatsSection
import com.ironheroes.sheet.ui.HitPointsSection
import com.ironheroes.sheet.ui.IdentityHeader
import com.ironheroes.sheet.ui.SavingThrowsSection
import com.ironheroes.sheet.ui.SkillsSection
import com.ironheroes.sheet.ui.TraitsSection
import com.ironheroes.sheet.ui.WeaponsSection
import kotlinx.coroutines.launch

/**
 * Root composable and single owner of MaterialTheme (FR-16.3). Each platform entry point only
 * hosts App(); no platform declares its own theme, guaranteeing consistent light/dark selection.
 * Fallback: where isSystemInDarkTheme() cannot detect the setting it returns false -> light scheme.
 */
@Composable
fun App() {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme(),
    ) {
        SheetScreen()
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
private fun SheetScreen() {
    // Repository + state constructed inside composition (safe on every platform).
    val repository = remember { createCharacterRepository() }
    val state = remember { CharacterState(DefaultCharacter.arnaldo, repository) }
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var loaded by remember { mutableStateOf(false) }

    // First run: load saved character, else keep the seeded default (FR-13.3 / AC-8).
    LaunchedEffect(Unit) {
        runCatching { state.load() }
        loaded = true
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            TopAppBar(
                title = { Text("Iron Heroes — Character Sheet") },
                actions = {
                    TextButton(onClick = { state.newBlank() }) { Text("New") }
                    TextButton(onClick = {
                        scope.launch {
                            runCatching { state.save() }
                                .onSuccess { snackbar.showSnackbar("Saved.") }
                                .onFailure { snackbar.showSnackbar("Could not save: ${it.message}") }
                        }
                    }) { Text("Save") }
                    TextButton(onClick = {
                        scope.launch {
                            runCatching { state.load() }
                                .onSuccess { snackbar.showSnackbar("Loaded.") }
                                .onFailure { snackbar.showSnackbar("Could not load: ${it.message}") }
                        }
                    }) { Text("Load") }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp),
        ) {
            IdentityHeader(state)
            AbilityScoresSection(state)
            SavingThrowsSection(state)
            SkillsSection(state)
            DefenseSection(state)
            HitPointsSection(state)
            AttacksSection(state)
            WeaponsSection(state)
            ArmorSection(state)
            EquipmentSection(state)
            FeatsSection(state)
            TraitsSection(state)
            ClassFeaturesSection(state)
        }
    }
}
