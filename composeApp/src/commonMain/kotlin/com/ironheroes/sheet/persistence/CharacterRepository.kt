package com.ironheroes.sheet.persistence

import com.ironheroes.sheet.model.Character

/**
 * Common persistence seam (FR-13.2). Implementations are provided per platform via the
 * expect/actual [createCharacterRepository]. All failures are non-fatal and recoverable:
 * [load] returns null when there is no readable saved character (caller seeds the default).
 */
interface CharacterRepository {
    /** Returns the saved character, or null when nothing is saved / the data is unreadable. */
    suspend fun load(): Character?

    /** Persists the character. May throw on unrecoverable I/O; callers wrap in runCatching. */
    suspend fun save(character: Character)

    /** Removes any saved character. */
    suspend fun clear()
}

/** Platform-specific repository factory (desktop file / web localStorage / Android filesDir). */
expect fun createCharacterRepository(): CharacterRepository
