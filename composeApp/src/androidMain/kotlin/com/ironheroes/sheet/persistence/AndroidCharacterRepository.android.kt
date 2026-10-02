package com.ironheroes.sheet.persistence

import com.ironheroes.sheet.SheetApplication
import com.ironheroes.sheet.model.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

/**
 * Android backend: writes iron-heroes-character.json into the app's private filesDir.
 * The Context comes from the SheetApplication singleton (initialized before any Activity),
 * so it is always available when this repository is constructed inside App() composition.
 */
class AndroidCharacterRepository : CharacterRepository {

    private val file: File
        get() = File(SheetApplication.instance.filesDir, CHARACTER_STORAGE_KEY)

    override suspend fun load(): Character? = withContext(Dispatchers.IO) {
        val f = file
        if (!f.exists()) return@withContext null
        val text = try {
            f.readText()
        } catch (e: IOException) {
            null
        } ?: return@withContext null
        try {
            CharacterJson.decodeFromString<Character>(text)
        } catch (e: Exception) {
            runCatching {
                f.renameTo(
                    File(
                        SheetApplication.instance.filesDir,
                        "$CHARACTER_STORAGE_KEY.corrupt-${System.currentTimeMillis()}",
                    ),
                )
            }
            null
        }
    }

    override suspend fun save(character: Character) {
        withContext(Dispatchers.IO) {
            file.writeText(CharacterJson.encodeToString(character))
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            val f = file
            if (f.exists()) f.delete()
        }
    }
}

actual fun createCharacterRepository(): CharacterRepository = AndroidCharacterRepository()
