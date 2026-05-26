# Defensa del Proyecto Integrador

## Portada

<div align="center">

**UNIVERSIDAD DE SANTANDER — UDES**

Facultad de Ingeniería  
Programa de Ingeniería de Sistemas  
Asignatura: Aplicaciones Móviles  
Proyecto Integrador

---

# Sabores de Colombia

### Aplicación móvil para el descubrimiento de la gastronomía colombiana

---

**Autores:**

Omar Felipe Rincon Fonseca  
Johan Steven Carreño Daza

**Docente:** Jhonatan Rolando Rey Castillo

Cúcuta, Colombia · 2026

</div>

---

## 1. Problema

Los colombianos en el extranjero y los entusiastas de la gastronomía internacional enfrentan tres barreras al buscar recetas colombianas auténticas:

1. **Autenticidad:** Las recetas en internet están modificadas, incompletas o no respetan la tradición culinaria colombiana.
2. **Idioma:** La mayoría de las recetas en bases de datos internacionales están en inglés, dificultando su preparación para hispanohablantes.
3. **Organización:** No existe una fuente centralizada que agrupe platos típicos por región colombiana (Caribe, Andina, Pacífica, Amazonía).

**Sabores de Colombia** resuelve esto ofreciendo una app móvil con:
- Recetas auténticas organizadas por región
- Traducción automática a español
- Búsqueda inteligente
- Sistema de favoritos

---

## 2. Personas de Usuario

### Valentina Torres — Usuaria Primaria
| Atributo | Valor |
|---|---|
| Edad | 28 años |
| Perfil | Colombiana viviendo en España |
| Motivación | Volver a preparar recetas tradicionales colombianas |
| Problema | Las recetas online no son auténticas y muchas están en inglés |
| Meta | Encontrar la receta exacta del ajiaco santafereño |

### Matteo Rossi — Usuario Secundario
| Atributo | Valor |
|---|---|
| Edad | 34 años |
| Perfil | Chef aficionado italiano |
| Motivación | Descubrir gastronomía colombiana |
| Problema | No conoce platos ni ingredientes locales |
| Meta | Explorar recetas por región colombiana |

---

## 3. KPIs

| Indicador | Objetivo | Resultado |
|---|---|---|
| Tiempo hasta primera receta | ≤ 3 minutos | ~90 segundos (precarga completa) |
| Retención | ≥ 40% | Pendiente de validación |
| Recetas guardadas por sesión | ≥ 1 | ✓ Funcionalidad implementada |
| Búsquedas exitosas | ≥ 85% | Búsqueda local sobre caché completo |
| Carga post-caché | ≤ 2 segundos | Room local (instantáneo) |

---

## 4. Arquitectura

```
┌──────────────────────────────────────────┐
│  UI Layer                                │
│  Compose Screens + ViewModels (StateFlow)│
├──────────────────────────────────────────┤
│  Domain Layer                            │
│  Entities + Repository Interfaces +      │
│  Use Cases (RecipeTranslator, Init)      │
├──────────────────────────────────────────┤
│  Data Layer                              │
│  Retrofit (TheMealDB, Google Translate)  │
│  Room (CachedRecipe, Favorites, Cache)   │
│  Mappers (DTO ↔ Domain ↔ Entity)        │
├──────────────────────────────────────────┤
│  DI Layer (Hilt)                         │
│  AppModule + DatabaseModule +            │
│  RepositoryModule                        │
└──────────────────────────────────────────┘

Flujo: UI → ViewModel → Repository → API / Room
```

**Ver:** `docs/architecture.md` (diagrama Mermaid completo)

---

## 5. ADR: Selección de Stack

Se evaluaron 3 alternativas:

| Alternativa | Decisión |
|---|---|
| **Flutter + Dart** | Descartado — fuera del plan de estudios, equipo sin experiencia |
| **KMM + Compose Multiplatform** | Descartado — tooling inmaduro, excede el tiempo disponible |
| **Android Nativo + Kotlin** | **Seleccionado** — dentro del plan de estudios, ecosistema maduro, soporte Google |

**Ver:** `docs/adr/ADR-001.md` (detalle completo de la decisión)

---

## 6. Stack Tecnológico

| Tecnología | Uso |
|---|---|
| Kotlin 2.1.21 | Lenguaje principal |
| Jetpack Compose + Material 3 | UI declarativa con diseño corporativo |
| Clean Architecture + MVVM | Patrón arquitectónico (4 capas) |
| Hilt 2.59.2 | Inyección de dependencias |
| Retrofit 2.11.0 + OkHttp 4.12.0 | Cliente HTTP para APIs |
| Room 2.7.1 | Base de datos local (caché y favoritos) |
| Coil 2.7.0 | Carga asíncrona de imágenes |
| Navigation Compose 2.8.9 | Navegación entre pantallas |
| StateFlow + Coroutines 1.9.0 | Estado reactivo y concurrencia |
| KSP 2.3.8 | Procesamiento de anotaciones |
| GitHub Actions | CI/CD automatizado |
| Gradle 9.3.1 + AGP 9.1.1 | Sistema de build |

---

## 7. Flujo de Usuario

```
1. Splash (precarga)
   ↓ Barra de progreso: "Traduciendo 145/312 recetas"
2. Home
   ├── Regiones de Colombia (Caribe, Andina, Pacífica, Amazonía)
   ├── Botón "Ver todas las recetas"
   └── Recetas populares
3. Región → Lista de recetas por área
4. Receta → Detalle
   ├── Imagen 16:9
   ├── Ingredientes con cantidades (traducidos)
   ├── Instrucciones paso a paso (traducidas)
   └── FAB Agregar/Quitar favorito
5. Todas las recetas → Búsqueda local + Orden A-Z / Z-A
6. Favoritos → Lista guardada en Room
   └── Botón eliminar por receta
```

---

## 8. CI/CD

Pipeline en GitHub Actions (`ci.yml`):

```
push/PR → lint → test → assembleDebug → upload APK
```

3 jobs secuenciales con reportes de test como artifacts.

---

## 9. Preguntas Probables para la Defensa

### ¿Por qué Room y no SQLite directo?

Room es parte de Android Jetpack y ofrece:
- Verificación de queries en tiempo de compilación (KSP)
- Integración nativa con Coroutines (`suspend`, `Flow`)
- Migraciones automáticas con `fallbackToDestructiveMigration()`
- Eliminación de boilerplate SQL manual
- Soporte oficial de Google

SQLite directo requeriría escribir queries manuales, manejar cursores y no ofrece verificación en compilación.

### ¿Por qué MVVM y no MVP o MVI?

MVVM es el patrón recomendado por Google para Android con Compose:
- `ViewModel` sobrevive a cambios de configuración (rotación)
- `StateFlow` permite que la UI reaccione automáticamente a cambios de estado
- Separación clara: la UI solo observa estado, el ViewModel contiene la lógica
- MVI añadiría complejidad innecesaria para un proyecto de este alcance
- MVP requeriría interfaces View-Contract adicionales (más boilerplate)

### ¿Por qué Hilt y no Koin o Dagger manual?

- Hilt es la recomendación oficial de Google sobre Dagger
- Generación de código en tiempo de compilación (mejor rendimiento que Koin en runtime)
- `@HiltViewModel` + `@AndroidEntryPoint` reducen boilerplate
- Integración nativa con Navigation Compose (`hiltViewModel()`)
- Koin es más simple pero usa reflection en runtime (menor rendimiento)

### ¿Cómo funciona la caché de traducción?

```
1. Texto a traducir (ej: "chicken")
2. Consulta TranslationCacheDao → Room (SQL SELECT)
3. ¿Encontrado? → Retorna texto traducido (0ms, 0 API calls)
4. ¿No encontrado? → GET translate.googleapis.com/translate_a/single
5. Respuesta: [[["pollo", "chicken", null, null, 1]], null, "en"]
6. Extrae "pollo" ← response[0][0][0]
7. Extrae "en" ← response[2] (idioma origen)
8. Si sourceLanguage == "es" → retorna texto original (ya está en español)
9. Guarda en Room: INSERT INTO translation_cache VALUES ('chicken', 'pollo')
10. Retorna "pollo"
```

La caché garantiza que cada texto se traduce una sola vez. La app funciona sin internet después de la precarga inicial.

### ¿Qué pasa sin internet?

La app tiene 2 modos de funcionamiento:

**Con internet (primera ejecución):**
- Splash → Precarga todas las recetas de TheMealDB
- Traduce todo al español vía Google Translate
- Guarda en Room: `cached_recipes`, `translation_cache`

**Sin internet (ejecuciones posteriores):**
- Splash → Detecta caché existente (0 API calls)
- Home, regiones, búsqueda, detalle, favoritos: todo desde Room
- La app es completamente funcional offline después de la primera carga

**Sin internet (primera ejecución):**
- Muestra error y lista vacía (requiere conexión inicial)

### ¿Qué mejorarían con más tiempo?

1. **Edamam API:** Integración de datos nutricionales por receta
2. **Firebase:** Autenticación de usuarios + Analytics + Crashlytics
3. **KMM:** Migración a Kotlin Multiplatform para soportar iOS
4. **Clasificación regional avanzada:** Machine learning para categorizar recetas automáticamente por región
5. **Compartir recetas:** Deep links + sharing intents
6. **Widget:** Receta del día en la pantalla de inicio
7. **Modo oscuro:** Alternar entre tema claro y oscuro (actualmente forzado a claro)
8. **Strings externalizados:** Migrar strings hardcodeados a `strings.xml` para i18n

---
