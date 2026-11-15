# Diario espacial

App Android (Kotlin + Jetpack Compose) que consume la API abierta de la NASA y guarda los datos en Room.
Práctica Calificada 2 – Desarrollo de Soluciones Móviles – ESAN 2026-2.

- **Alumno 1 – Imágenes (APOD):** Landaveri (alandaveri-23100206)
- **Alumno 2 – Asteroides (NeoWs):** Freddy Aliaga (FreddyAliaga)

## Cómo ejecutarla
1. Genera tu clave en https://api.nasa.gov (sección "Generate API Key").
2. Agrega al final de `local.properties` (este archivo no se sube a GitHub):
   ```
   NASA_API_KEY=tu_clave
   ```
3. Sincroniza Gradle y ejecuta la app. El código lee la clave con `BuildConfig.NASA_API_KEY`.

## Estructura
- `data/remote` – Retrofit (`RetrofitInstance`) y los servicios/DTO de cada API
- `data/local` – Room: `AppDatabase`, Entities y DAOs
- `data/repository` – Repositories (API + Room)
- `presentation/<opción>` – pantallas y ViewModel (StateFlow)
- `presentation/navigation` – navegación principal con barra inferior

## Acuerdo de la base común
La **base común la prepara el Alumno 2 (Freddy)** en la rama `feature/base-comun` y se integra a `main`
con un Pull Request que revisa y aprueba el Alumno 1. Incluye:
- Dependencias (Retrofit + Gson, Room + KSP, Coil, Navigation, ViewModel) y la clave en `local.properties` + `BuildConfig`.
- `RetrofitInstance` con las **dos URL base**: `https://api.nasa.gov/` (NeoWs) y `https://science.nasa.gov/` (APOD).
  Un interceptor agrega `api_key` a todas las llamadas.
- `AppNavGraph` con la barra inferior **Imágenes / Asteroides**. Cada opción tiene su grafo interno:
  rutas `imagenes/...` (Alumno 1) y `asteroides/...` (Alumno 2).

**Cómo la integra cada uno (después de que la base esté en `main`):**
1. `git checkout main` → `git pull` → crear su rama (`feature/imagenes` o `feature/asteroides`).
2. En `RetrofitInstance` agrega **solo** su servicio (`apodApi` o `neoWsApi`).
3. En `AppNavGraph` cambia **solo** el bloque `navigation(...)` de su opción.
4. `AppDatabase` la crea el Alumno 2 con `AsteroidEntity`. El Alumno 1 agrega `ApodEntity::class` a `entities`,
   su `abstract fun apodDao()` y sube `version` en 1 (la base usa `fallbackToDestructiveMigration`).
5. Si hay conflicto en esos archivos, se conservan **las dos partes** (son líneas distintas).