# Verification Record

All commands were run from the project root `c:\Users\AdalS\Projects\iron_heroes_dark_sun` with
`JAVA_HOME` set to `C:\Program Files\Android\Android Studio\jbr` (JDK 21).

---

## Step 1 — Gradle wrapper generation

**Command:**
```powershell
& "$env:TEMP\gradle-dl\gradle-8.11.1\bin\gradle.bat" wrapper --gradle-version 8.11.1 --distribution-type bin
```
**Result:** BUILD SUCCESSFUL. Wrapper files generated:
`gradle/wrapper/gradle-wrapper.jar` (43 583 bytes), `gradle/wrapper/gradle-wrapper.properties`,
`gradlew`, `gradlew.bat`.

**Command (verify wrapper):**
```powershell
.\gradlew.bat --version
```
**Result:**
```
Gradle 8.11.1
Build time: 2024-11-20 16:56:46 UTC
Launcher JVM: 21.0.6 (JetBrains s.r.o.)
OS: Windows 11 10.0 amd64
```
✅ Gradle wrapper version confirmed: 8.11.1.

---

## Step 2 — Discover wasmJs task names

**Command:**
```powershell
.\gradlew.bat :composeApp:tasks --all
```
**Result:** BUILD SUCCESSFUL.

Confirmed wasmJs browser task names:
- `wasmJsBrowserDistribution` — production distribution (static files)
- `wasmJsBrowserDevelopmentRun` — development server
- `wasmJsBrowserDevelopmentWebpack` — development webpack bundle
- `wasmJsBrowserProductionWebpack` — production webpack bundle
- `wasmJsBrowserTest` — karma/browser tests

---

## Step 3 — Shared + desktop compile (AC-3)

**Command:**
```powershell
.\gradlew.bat :composeApp:compileKotlinJvm
```
**Result:** BUILD SUCCESSFUL (0 errors, deprecation warnings only on `ExperimentalWasmDsl`).
✅ Shared model, derivations, data, persistence, state, and UI compile cleanly on JVM.

---

## Step 4 — commonTest unit tests (AC-6, AC-9)

**Command:**
```powershell
.\gradlew.bat :composeApp:jvmTest
```
**Result:** BUILD SUCCESSFUL in 1m 44s.

Test report: `composeApp/build/test-results/jvmTest/TEST-com.ironheroes.sheet.DerivationTest.xml`

| Test case | Result |
|---|---|
| `abilityModifiers_matchKnownGoodNumbers[jvm]` (Str 21→+5, Con 16→+3, Cha 8→-1) | ✅ PASS |
| `savingThrows_matchKnownGoodNumbers[jvm]` (Fort 7, Ref 6, Will 5) | ✅ PASS |
| `defenseTotal_isFifteen[jvm]` (Defense 15) | ✅ PASS |
| `hitPoints_areFortySeven[jvm]` (HP 47) | ✅ PASS |
| `expertStrike_isCappedAtClassLevel[jvm]` (expertStrikeBonus(10,4)==4) | ✅ PASS |
| `serialization_roundTripsIntact[jvm]` (JSON encode→decode == arnaldo) | ✅ PASS |

**Total: 6 tests, 0 failures, 0 errors, 0 skipped.**

---

## Step 5 — wasmJs compile (AC-4)

**Command:**
```powershell
.\gradlew.bat :composeApp:compileKotlinWasmJs
```
**Result:** BUILD SUCCESSFUL.
✅ Web target (Kotlin/Wasm) compiles cleanly.

**Command (web distribution):**
```powershell
.\gradlew.bat :composeApp:wasmJsBrowserDistribution
```
**Result:** BUILD SUCCESSFUL in ~10 minutes.
Output directory: `composeApp/build/dist/wasmJs/productionExecutable/`.
✅ wasmJs browser distribution produced.

---

## Step 6 — Android compile and APK assembly (AC-5)

**Environment:** Android SDK present at `%LOCALAPPDATA%\Android\Sdk`, platform `android-35`,
build-tools `35.0.0`. `local.properties` contains `sdk.dir`.

**Command (compile):**
```powershell
.\gradlew.bat :composeApp:compileDebugKotlinAndroid
```
**Result:** BUILD SUCCESSFUL.
✅ Android Kotlin compilation succeeds.

**Command (APK assembly — best-effort):**
```powershell
.\gradlew.bat :composeApp:assembleDebug
```
**Result:** BUILD SUCCESSFUL in ~2.5 minutes.
APK: `composeApp/build/outputs/apk/debug/composeApp-debug.apk`.
✅ Full Android APK assembled successfully.

---

## Step 7 — Full build integration (AC-2)

**Command:**
```powershell
.\gradlew.bat build
```
**Result:** BUILD SUCCESSFUL in ~7.7 minutes (all targets, all tests, lint).
✅ The full `build` lifecycle completes without errors across desktop, web, and Android.

---

## Source file integrity

**xlsx untouched:** `Ficha Iron Heroes Character Sheet.xlsx` — LastWriteTime and size unchanged from
before the project was created. The build never reads or writes it.

---

## Manual acceptance items (require a running platform — not automated here)

These require launching the app and are documented in README.md as the manual acceptance pass:

- **AC-7** Live recompute: edit an ability score → modifier, saves, skills update immediately.
- **AC-8** First run: with no saved data, Arnaldo Choziner loads automatically.
- **AC-10** Per-platform persistence: Save → load round-trip with real storage.
- **AC-12** Token interactivity: Weapon Pool increment/decrement/reset; Omen increment/decrement only.
- **AC-17** Dark-mode: system dark mode → dark scheme; light → light scheme.

---

## Summary

| Acceptance Criterion | Status | Evidence |
|---|---|---|
| AC-2 (Gradle build) | ✅ | `.\gradlew.bat build` — BUILD SUCCESSFUL |
| AC-3 (desktop/shared compile) | ✅ | `compileKotlinJvm` — BUILD SUCCESSFUL |
| AC-4 (web compile) | ✅ | `compileKotlinWasmJs` + `wasmJsBrowserDistribution` — BUILD SUCCESSFUL |
| AC-5 (Android compile + APK) | ✅ | `compileDebugKotlinAndroid` + `assembleDebug` — BUILD SUCCESSFUL |
| AC-6 (known-good derivation numbers) | ✅ | 6/6 tests pass in `jvmTest` |
| AC-9 (JSON round-trip) | ✅ | `serialization_roundTripsIntact` — PASS |
| xlsx untouched | ✅ | File unchanged |
