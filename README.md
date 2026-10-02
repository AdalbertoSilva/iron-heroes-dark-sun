# Iron Heroes Character Sheet

Interactive digital character sheet for **Iron Heroes** (Dark Sun campaign), built with
**Compose Multiplatform** (Kotlin). One shared codebase runs on desktop (JVM), web (Kotlin/Wasm), and
Android. The default character **Arnaldo Choziner** (Weapon Master, level 4) is pre-loaded on first run.

---

## Prerequisites

| Requirement | Version | Notes |
|---|---|---|
| JDK | 17 or higher | The Android Studio bundled JBR (JDK 21) works perfectly. |
| `JAVA_HOME` | set to your JDK | e.g. `C:\Program Files\Android\Android Studio\jbr` |
| Android SDK | optional | Required only for Android build/run. Install via Android Studio. |
| `local.properties` | optional (git-ignored) | Required for Android; see Android section below. |

The Gradle wrapper (`gradlew.bat` / `gradlew`) is committed — no system Gradle installation needed.

Set `JAVA_HOME` before running any Gradle task, for example in PowerShell:

```powershell
$env:JAVA_HOME = 'C:\Program Files\Android\Android Studio\jbr'
```

---

## Running — Desktop (JVM)

```powershell
$env:JAVA_HOME = 'C:\Program Files\Android\Android Studio\jbr'
.\gradlew.bat :composeApp:run
```

This opens a native desktop window. The character is auto-saved to
`<userHome>/.iron-heroes/iron-heroes-character.json` when you press **Save**; it reloads automatically
on next launch.

---

## Running — Web (Kotlin/Wasm)

```powershell
$env:JAVA_HOME = 'C:\Program Files\Android\Android Studio\jbr'
.\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun
```

This starts a local development server and opens the app in your default browser.
For a production distribution (static files you can host anywhere):

```powershell
.\gradlew.bat :composeApp:wasmJsBrowserDistribution
# Output: composeApp/build/dist/wasmJs/productionExecutable/
```

**Browser requirement:** the web target uses Kotlin/Wasm with WasmGC. You need a current version of
Chrome, Firefox, Edge, or Safari — all major browsers ship WasmGC support. Data is persisted to
`localStorage` under the key `iron-heroes-character.json`. In private/incognito mode, `localStorage`
may be unavailable; if so, the app falls back to the default character in memory and shows a snackbar
on save failures.

---

## Running — Android

1. Install the Android SDK (Android Studio is the easiest way).
2. Create a git-ignored `local.properties` at the project root:
   ```
   sdk.dir=C\:\\Users\\<YourName>\\AppData\\Local\\Android\\Sdk
   ```
3. Open the project in Android Studio and run the `composeApp` configuration, **or** run from the
   command line:
   ```powershell
   $env:JAVA_HOME = 'C:\Program Files\Android\Android Studio\jbr'
   .\gradlew.bat :composeApp:assembleDebug
   # APK: composeApp/build/outputs/apk/debug/
   ```
   Install on a device or emulator:
   ```powershell
   adb install composeApp\build\outputs\apk\debug\composeApp-debug.apk
   ```

**Android SDK not available?** The shared Kotlin compilation (`compileDebugKotlinAndroid`) still
succeeds; only packaging/installation requires the full SDK. This limitation is not a blocker — the
desktop and web targets are fully self-contained.

---

## Dark-mode / Theming

The app reads the system light/dark preference via `isSystemInDarkTheme()` and applies Material 3's
built-in `darkColorScheme()` or `lightColorScheme()` accordingly. This works automatically on Android
(system setting), desktop (OS setting on Windows 10+/macOS), and web (browser `prefers-color-scheme`).
**Fallback:** on any platform or browser where the preference cannot be detected, `isSystemInDarkTheme()`
returns `false` and the **light** scheme is used.

---

## Default character

**Arnaldo Choziner** — Human Weapon Master, level 4.

| Ability | Score | Modifier |
|---|---|---|
| Strength | 21 | +5 |
| Dexterity | 14 | +2 |
| Constitution | 16 | +3 |
| Intelligence | 10 | +0 |
| Wisdom | 12 | +1 |
| Charisma | 8 | −1 |

Saving throws: Fortitude +7, Reflex +6, Will +5.
Defense: 15 (Base 10, Active/Agility 4, Armor 1).
HP: 47.

---

## Running the tests

Unit tests live in `commonTest` and run via the JVM test runner:

```powershell
$env:JAVA_HOME = 'C:\Program Files\Android\Android Studio\jbr'
.\gradlew.bat :composeApp:jvmTest
# HTML report: composeApp/build/reports/tests/jvmTest/index.html
```

The suite verifies the known-good derivation numbers (ability modifiers, saving throws, defense total,
HP) and a full JSON serialization round-trip for the default character.

---

## Project structure

```
iron_heroes_dark_sun/
├─ Ficha Iron Heroes Character Sheet.xlsx  # source-of-truth reference (never modified by the build)
├─ composeApp/src/
│  ├─ commonMain/   ─ shared UI + domain (model, derive, data, persistence, state, ui)
│  ├─ commonTest/   ─ DerivationTest (AC-6 known-good numbers + round-trip)
│  ├─ jvmMain/      ─ desktop entry + file-based persistence
│  ├─ wasmJsMain/   ─ web entry + localStorage persistence
│  └─ androidMain/  ─ Android Activity/Application + filesDir persistence
└─ README.md
```
