# Sabores de Colombia

Aplicación móvil Android para el descubrimiento y exploración de la gastronomía colombiana. Permite buscar, explorar por región, guardar favoritos y traducir recetas automáticamente al español usando Google Translate con caché local.

---

## Problem Statement

Los colombianos en el extranjero y los entusiastas de la gastronomía internacional enfrentan dos problemas:

1. **Autenticidad:** Las recetas colombianas disponibles en internet suelen estar modificadas o incompletas.
2. **Idioma:** La mayoría de las recetas están en inglés, lo que dificulta su preparación para hispanohablantes.
3. **Organización:** No existe una fuente centralizada que agrupe platos típicos por región colombiana.

**Sabores de Colombia** resuelve esto ofreciendo una app móvil con recetas auténticas, organizadas por región, con traducción automática a español y búsqueda inteligente.

---

## Autores

| Nombre | Rol |
|---|---|
| Omar Felipe Rincon Fonseca | Desarrollador Android, Arquitecto de Software |
| Johan Steven Carreño Daza | Desarrollador Android, Documentador Técnico |

**Universidad:** Universidad de Santander — UDES  
**Programa:** Ingeniería de Sistemas  
**Asignatura:** Aplicaciones Móviles — Proyecto Integrador  
**Lugar:** Cúcuta, Colombia · 2026

---

## Personas de Usuario

### Valentina Torres — Usuario Primario
- **Edad:** 28
- **Perfil:** Colombiana viviendo en España
- **Motivación:** Volver a preparar recetas tradicionales colombianas
- **Problema:** Las recetas online no son auténticas y muchas están en inglés
- **Meta:** Encontrar la receta exacta del ajiaco santafereño

### Matteo Rossi — Usuario Secundario
- **Edad:** 34
- **Perfil:** Chef aficionado italiano
- **Motivación:** Descubrir gastronomía colombiana
- **Problema:** No conoce platos ni ingredientes locales
- **Meta:** Explorar recetas por región colombiana

---

## KPIs

| Indicador | Objetivo | Estado |
|---|---|---|
| Tiempo hasta primera receta | ≤ 3 minutos | ✓ Cacheado en ~90s |
| Retención | ≥ 40% | Pendiente de validación |
| Recetas guardadas por sesión | ≥ 1 | ✓ |
| Búsquedas exitosas | ≥ 85% | ✓ (búsqueda local) |
| Carga post-caché | ≤ 2 segundos | ✓ (Room local) |

---

## Arquitectura

```
┌─────────────────────────────────────────┐
│  UI Layer (Compose + ViewModel)         │
│  feature/splash, home, detail, search,  │
│  favorites, allrecipes, regionlist      │
├─────────────────────────────────────────┤
│  Domain Layer (Use Cases + Entities)    │
│  RecipeTranslator, InitializeAppUseCase │
│  Recipe, Ingredient                     │
├─────────────────────────────────────────┤
│  Data Layer (Repositories)              │
│  Retrofit (TheMealDB, Google Translate) │
│  Room (CachedRecipe, Favorites, Cache)  │
│  Mappers (DTO ↔ Domain ↔ Entity)       │
├─────────────────────────────────────────┤
│  DI Layer (Hilt Modules)                │
│  AppModule, DatabaseModule,             │
│  RepositoryModule                       │
└─────────────────────────────────────────┘

Flujo: UI → ViewModel → UseCase → Repository → API / Room
```

---

## Stack Tecnológico

| Tecnología | Versión | Uso |
|---|---|---|
| Kotlin | 2.1.21 | Lenguaje principal |
| Android Gradle Plugin | 9.1.1 | Build system |
| Gradle | 9.3.1 | Gestor de dependencias |
| Jetpack Compose BOM | 2024.09.00 | UI declarativa |
| Material 3 | — | Sistema de diseño |
| Hilt | 2.59.2 | Inyección de dependencias |
| KSP | 2.3.8 | Procesamiento de anotaciones |
| Retrofit | 2.11.0 | Cliente HTTP |
| OkHttp | 4.12.0 | Cliente HTTP (interceptors) |
| Room | 2.7.1 | Base de datos local |
| Coil | 2.7.0 | Carga de imágenes |
| Navigation Compose | 2.8.9 | Navegación entre pantallas |
| StateFlow | — | Manejo de estado reactivo |
| Coroutines | 1.9.0 | Concurrencia |
| Gson | 2.11.0 | Serialización JSON |
| GitHub Actions | — | CI/CD |
| **Target SDK** | 36 | Android 15 |
| **Min SDK** | 26 | Android 8.0 (95%+ dispositivos) |

---

## Estructura del Proyecto

```
app/src/main/java/com/lab/saboresdecolombia/
├── SaboresDeColombiaApp.kt          ← Application (Hilt)
├── MainActivity.kt                  ← Entry point (NavHost)
├── navigation/
│   ├── NavRoutes.kt                 ← 7 rutas definidas
│   └── AppNavGraph.kt              ← NavHost con destinos
├── di/
│   ├── AppModule.kt                 ← Network (Retrofit, OkHttp)
│   ├── DatabaseModule.kt            ← Room (DB, DAOs)
│   └── RepositoryModule.kt         ← Interface bindings
├── core/
│   ├── domain/
│   │   ├── model/Recipe.kt          ← Entidad de dominio
│   │   ├── repository/              ← Interfaces de repositorio
│   │   └── usecase/                 ← Casos de uso
│   └── data/
│       ├── local/                   ← Room: DAOs, Entities, DB
│       ├── remote/                  ← Retrofit: APIs, DTOs
│       ├── mapper/                  ← DTO ↔ Domain ↔ Entity
│       └── repository/              ← Implementaciones
├── feature/
│   ├── splash/                      ← Splash con progreso
│   ├── home/                        ← Home con regiones
│   ├── allrecipes/                  ← Todas las recetas (A-Z/Z-A)
│   ├── regionlist/                  ← Lista por región
│   ├── detail/                      ← Detalle de receta
│   ├── search/                      ← Búsqueda
│   └── favorites/                   ← Favoritos (Room)
└── ui/
    └── theme/                       ← Color, Type, Theme (M3)
```

---

## APIs Utilizadas

### 1. TheMealDB (Principal)
- **URL:** `https://www.themealdb.com/api/json/v1/1/`
- **Endpoints:** `search.php`, `lookup.php`, `filter.php`, `random.php`
- **Uso:** Obtener recetas por región, búsqueda, detalle, aleatorias

### 2. Google Translate (Complementaria)
- **URL:** `https://translate.googleapis.com/translate_a/single`
- **Parámetros:** `client=gtx&sl=auto&tl=es&dt=t&q=<texto>`
- **Uso:** Traducción gratuita inglés→español con detección automática de idioma
- **Nota:** Endpoint no oficial, gratuito, sin API key requerida

---

## Instalación

### Prerrequisitos
- Android Studio Ladybug (2024.2.1+) o superior
- JDK 21+
- Android SDK 36
- Gradle 9.3.1+ (incluido con el wrapper)

### Pasos

```bash
# 1. Clonar repositorio
git clone <url-del-repositorio>
cd SaboresDeColombia

# 2. Configurar local.properties
# Asegurar que apunte al Android SDK correcto:
# sdk.dir=C\:\\Users\\<usuario>\\AppData\\Local\\Android\\Sdk

# 3. Sincronizar Gradle
./gradlew build

# 4. Ejecutar en emulador o dispositivo
./gradlew installDebug
```

### Configuración JDK
Si Android Studio usa un JDK incorrecto, configurar en:
`File → Settings → Build Tools → Gradle → Gradle JDK → JDK 21+`

---

## Compilación

```bash
# APK debug
./gradlew assembleDebug
# Salida: app/build/outputs/apk/debug/app-debug.apk

# APK release (sin firma)
./gradlew assembleRelease

# Limpiar y reconstruir
./gradlew clean assembleDebug

# Ejecutar tests
./gradlew test

# Lint
./gradlew lint
```

---

## CI/CD

GitHub Actions configurado en `.github/workflows/ci.yml`:
- **Trigger:** push a `main`/`master`
- **Steps:** checkout → JDK 21 → assembleDebug → upload APK artifact

```yaml
name: CI Build
on:
  push:
    branches: [ main, master ]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { java-version: '21', distribution: 'temurin' }
      - run: ./gradlew assembleDebug
      - uses: actions/upload-artifact@v4
        with: { name: app-debug, path: app/build/outputs/apk/debug/app-debug.apk }
```

---

## Flujo Principal

```
1. App inicia → Splash con barra de progreso
2. Precarga: fetch 26 letras (a-z) de TheMealDB
3. Traducción: Google Translate (inglés→español) con caché Room
4. Almacenamiento: todo guardado en Room (cached_recipes)
5. Navega a Home
6. Home:
   - Regiones de Colombia (Caribe, Andina, Pacífica, Amazonía)
   - Botón "Ver todas las recetas"
   - Recetas populares
7. Región → Lista de recetas filtradas por área
8. Receta → Detalle (imagen, ingredientes, instrucciones)
9. FAB → Agregar/quitar favoritos (Room)
10. Búsqueda → Filtro local sobre caché
11. Favoritos → Lista de recetas guardadas, swipe/delete
```

---

## Trabajo Futuro

- Integración con Edamam API (datos nutricionales)
- Firebase (autenticación, analíticas, crashlytics)
- Soporte iOS vía Kotlin Multiplatform (KMM)
- Clasificación regional avanzada con machine learning
- Filtros por tipo de comida, tiempo de preparación, dificultad
- Compartir recetas en redes sociales
- Modo offline completo con sincronización
- Widget de receta del día

---

## Conclusiones

- **Clean Architecture + MVVM** probó ser una arquitectura sólida para separar responsabilidades y facilitar el testing.
- **Room como caché central** permitió que todas las pantallas funcionaran con datos locales después de la precarga inicial, eliminando la dependencia de red.
- **Google Translate gratuito** resolvió el problema del idioma sin costo de API, con un sistema de caché que evita traducciones repetidas.
- **Pipeline de traducción concurrente** (5 lotes × 11 recetas) redujo el tiempo de precarga de 20 minutos a ~90 segundos.
- El proyecto implementa el **100% de las funcionalidades MVP** definidas inicialmente.

---

## Referencias

- [TheMealDB API](https://www.themealdb.com/api.php)
- [Google Translate (unofficial)](https://translate.googleapis.com/)
- [Jetpack Compose Docs](https://developer.android.com/compose)
- [Hilt Dependency Injection](https://developer.android.com/training/dependency-injection/hilt-android)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Retrofit](https://square.github.io/retrofit/)
- [Material 3 Design](https://m3.material.io/)
- [Clean Architecture (Robert C. Martin)](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
