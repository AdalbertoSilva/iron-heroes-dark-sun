package com.ironheroes.sheet.persistence

import com.ironheroes.sheet.model.Character
import kotlinx.browser.localStorage
import org.w3c.dom.get
import org.w3c.dom.set

/**
 * Web backend: browser localStorage keyed by iron-heroes-character.json. Missing or unavailable
 * storage is treated as "no saved data" (-> null) so the default is seeded; save failures surface
 * to the caller's runCatching path.
 */
class LocalStorageCharacterRepository : CharacterRepository {

    override suspend fun load(): Character? {
        val text = try {
            localStorage[CHARACTER_STORAGE_KEY]
        } catch (e: Throwable) {
            println("Warn: localStorage unavailable, loading default: ${e.message}")
            null
        } ?: return null
        return try {
            CharacterJson.decodeFromString<Character>(text)
        } catch (e: Throwable) {
            println("Warn: saved data was unreadable, loading default: ${e.message}")
            null
        }
    }

    override suspend fun save(character: Character) {
        localStorage[CHARACTER_STORAGE_KEY] = CharacterJson.encodeToString(character)
    }

    override suspend fun clear() {
        localStorage.removeItem(CHARACTER_STORAGE_KEY)
    }
}

actual fun createCharacterRepository(): CharacterRepository = LocalStorageCharacterRepository()
