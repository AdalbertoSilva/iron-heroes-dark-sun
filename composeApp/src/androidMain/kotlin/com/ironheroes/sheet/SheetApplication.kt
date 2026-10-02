package com.ironheroes.sheet

import android.app.Application

/**
 * Holds an application-scoped singleton so the persistence backend can reach filesDir without an
 * Activity. onCreate runs before any Activity, so instance is set before App() composition builds
 * the repository — no UninitializedPropertyAccessException on the real app path.
 */
class SheetApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: SheetApplication
            private set
    }
}
