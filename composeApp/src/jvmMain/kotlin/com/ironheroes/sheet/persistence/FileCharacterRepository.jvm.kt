package com.ironheroes.sheet.persistence

import com.ironheroes.sheet.model.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

/**
 * Desktop backend: writes <userHome>/.iron-heroes/iron-heroes-character.json atomically
 * (tmp file + move-replace). Corrupt/unreadable JSON is renamed aside and treated as "no data".
 */
class FileCharacterRepository : CharacterRepository {

    private val dir = File(System.getProperty("user.home"), ".iron-heroes")
    private val file = File(dir, CHARACTER_STORAGE_KEY)

    override suspend fun load(): Character? = withContext(Dispatchers.IO) {
        if (!file.exists()) return@withContext null
        val text = try {
            file.readText()
        } catch (e: IOException) {
            System.err.println("Warn: could not read character file: ${e.message}")
            return@withContext null
        }
        try {
            CharacterJson.decodeFromString<Character>(text)
        } catch (e: Exception) {
            System.err.println("Warn: saved data was unreadable, loading default: ${e.message}")
            runCatching {
                file.renameTo(File(dir, "$CHARACTER_STORAGE_KEY.corrupt-${System.currentTimeMillis()}"))
            }
            null
        }
    }

    override suspend fun save(character: Character) = withContext(Dispatchers.IO) {
        if (!dir.exists()) dir.mkdirs()
        val tmp = File(dir, "$CHARACTER_STORAGE_KEY.tmp")
        tmp.writeText(CharacterJson.encodeToString(character))
        if (file.exists()) file.delete()
        if (!tmp.renameTo(file)) {
            // Fallback for platforms/filesystems where rename fails.
            file.writeText(tmp.readText())
            tmp.delete()
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            if (file.exists()) file.delete()
        }
    }
}

actual fun createCharacterRepository(): CharacterRepository = FileCharacterRepository()
