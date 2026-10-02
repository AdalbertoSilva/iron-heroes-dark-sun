package com.ironheroes.sheet.persistence

import kotlinx.serialization.json.Json

/** Shared JSON codec (FR-13.1). Forward-tolerant via ignoreUnknownKeys + schemaVersion. */
val CharacterJson: Json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    encodeDefaults = true
}

/** Storage key / filename used by every platform backend. */
const val CHARACTER_STORAGE_KEY: String = "iron-heroes-character.json"
