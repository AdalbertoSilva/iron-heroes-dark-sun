package com.ironheroes.sheet.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/** Regex accepting a (possibly negative) partial integer string, incl. empty and lone "-". */
private val intInputRegex = Regex("^-?\\d*$")

/**
 * Text field bound to an Int. Rejects non-numeric keystrokes; empty maps to [emptyValue]
 * (0 by default, or 1 for level). Clamping to real limits is the caller's helper responsibility.
 */
@Composable
fun LabeledIntField(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    emptyValue: Int = 0,
) {
    var text by remember(value) { mutableStateOf(value.toString()) }
    OutlinedTextField(
        value = text,
        onValueChange = { new ->
            if (intInputRegex.matches(new)) {
                text = new
                val parsed = if (new.isEmpty() || new == "-") emptyValue else new.toIntOrNull()
                if (parsed != null) onValueChange(parsed)
            }
        },
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier,
    )
}

/** Free-text field bound to a String (no trimming — verbatim preservation for description fields). */
@Composable
fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = singleLine,
        modifier = modifier,
    )
}

/** Read-only display of a derived value (never editable, never stored on the model). */
@Composable
fun ComputedValue(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall)
        Text(value, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun ComputedValue(label: String, value: Int, modifier: Modifier = Modifier) =
    ComputedValue(label, formatSigned(value), modifier)

/** Formats an integer as a signed modifier-style string ("+5", "0", "-1"). */
fun formatSigned(v: Int): String = if (v >= 0) "+$v" else v.toString()

/** Card that surfaces a title and, when a value is set, a section subtitle. */
@Composable
fun SectionCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            content()
        }
    }
}

/** Expandable Material3 card: header toggles animated body visibility (FR-12). */
@Composable
fun ExpandablePanel(
    title: String,
    modifier: Modifier = Modifier,
    initiallyExpanded: Boolean = false,
    content: @Composable () -> Unit,
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }
    Card(modifier = modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = { expanded = !expanded }) {
                    Text(if (expanded) "Hide" else "Show")
                }
            }
            AnimatedVisibility(visible = expanded) {
                Column(Modifier.padding(top = 4.dp)) { content() }
            }
        }
    }
}

/**
 * Interactive token counter (FR-11). − / + controls built from text glyphs (no icon artifact).
 * When [onReset] is provided a reset button is shown (Weapon Pool); omit it for Omen Tokens.
 */
@Composable
fun TokenCounter(
    label: String,
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier,
    onReset: (() -> Unit)? = null,
    resetLabel: String = "Reset",
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(label, style = MaterialTheme.typography.titleMedium, modifier = Modifier.width(160.dp))
        IconButton(onClick = onDecrement) { Text("\u2212", style = MaterialTheme.typography.titleLarge) }
        Text(count.toString(), style = MaterialTheme.typography.titleLarge)
        IconButton(onClick = onIncrement) { Text("+", style = MaterialTheme.typography.titleLarge) }
        if (onReset != null) {
            OutlinedButton(onClick = onReset) { Text(resetLabel) }
        }
    }
}

/** Small remove-row control used by editable tables. */
@Composable
fun RemoveButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClick, modifier = modifier) {
        Text("\u2715", style = MaterialTheme.typography.titleMedium)
    }
}
