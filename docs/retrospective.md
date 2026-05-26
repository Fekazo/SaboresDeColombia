# Retrospectiva — Sabores de Colombia

**Proyecto Integrador · Aplicaciones Móviles · UDES**  
**Autores:** Omar Felipe Rincon Fonseca, Johan Steven Carreño Daza  
**Fecha:** Mayo 2026

---

## 1. Qué salió bien

### Arquitectura
- **Clean Architecture + MVVM** demostró ser una decisión acertada. La separación de capas permitió cambiar fuentes de datos sin modificar la UI.
- **Hilt** como DI redujo significativamente el boilerplate comparado con Dagger manual. La integración con `@HiltViewModel` simplificó la inyección en ViewModels.
- **Version Catalog** centralizó todas las dependencias en un solo archivo (`libs.versions.toml`), haciendo los upgrades predecibles.

### Desarrollo
- **Jetpack Compose** redujo aproximadamente un 60% el código de UI comparado con XML Views. La previsualización en Android Studio aceleró el diseño iterativo.
- **Navigation Compose** con rutas tipadas eliminó la necesidad de `Intent` y `Bundle` manuales.
- **StateFlow + collectAsStateWithLifecycle** proporcionó reactividad con manejo automático del ciclo de vida.

### Datos y Traducción
- **Room como caché central** fue la decisión de mayor impacto positivo. Después de la precarga inicial, todas las pantallas funcionan sin red.
- **Google Translate gratuito** (endpoint `translate_a/single`) resolvió el problema del idioma sin costo de API ni necesidad de API key.
- **Pipeline de traducción concurrente** (Semaphore + batches paralelos) redujo el tiempo de precarga de 20 minutos a ~90 segundos.

### Testing
- **MockK + coroutines-test** permitieron tests unitarios limpios de ViewModels sin dependencias reales de Room o Retrofit.
- La arquitectura Clean Architecture facilitó el mocking de repositorios.

---

## 2. Problemas encontrados

### Incompatibilidades de versiones
| Problema | Causa | Solución |
|---|---|---|
| Conflicto `kotlin-android` + `kotlin-compose` | AGP 9.1.1 incorpora Kotlin internamente | Eliminar `kotlin-android` explícito |
| KSP 2.1.21-2.0.5 no existe | Versión inventada, formato incorrecto | Usar KSP 2.3.8 (compatible con Kotlin 2.3.x de AGP) |
| `java.util.Properties` no disponible en Gradle 9.3.1 | Cambio en classpath del build script | Reemplazar con `file.readLines()` manual |
| `jlink.exe` no encontrado | Android Studio usaba JDK de VSCode (incompleto) | Configurar JDK 26 en Gradle JDK settings |

### Performance
- **Precarga secuencial inicial:** La primera implementación procesaba recetas una por una, tomando ~20 minutos. Se resolvió con pipeline de batches paralelos (5 lotes × 11 recetas).
- **Rate-limiting de Google Translate:** El endpoint gratuito rechaza llamadas excesivas. Se resolvió con `Semaphore(15)` limitando concurrencia + delay entre llamadas.

### Diseño
- **Icono por defecto:** No se diseñó un icono personalizado para la app. Se usa el icono default de Android Studio.
- **Strings hardcodeados:** ~90% del texto en Composables está hardcodeado en español, sin usar `strings.xml`. Esto dificulta la internacionalización futura.

### Testing
- **Solo 10 tests unitarios:** No se implementaron tests instrumentados (UI tests con Compose), tests de Room con base de datos en memoria, ni tests de integración de Retrofit con mock server.

---

## 3. Limitaciones del MVP

| Limitación | Impacto | Mitigación actual |
|---|---|---|
| Solo Android | No disponible en iOS | — |
| Google Translate no oficial | Podría ser descontinuado | La app funciona sin traducción (muestra inglés) |
| Sin autenticación | Favoritos solo locales, no sincronización entre dispositivos | Room local |
| Sin modo oscuro | Experiencia inconsistente con preferencias del sistema | Forzado a tema claro |
| Room sin migración | Pérdida de datos en cambios de esquema | `fallbackToDestructiveMigration()` (aceptable para MVP) |
| Sin analíticas | Sin datos de uso, retención o crashes | — |
| 4 regiones fijas | No cubre todas las regiones colombianas reales | Filtro por área de TheMealDB |
| Sin sharing | No se pueden compartir recetas | — |

---

## 4. Trabajo Futuro

### Corto plazo (próximo semestre)
1. **Edamam API:** Agregar información nutricional (calorías, proteínas, grasas) por receta.
2. **Firebase:** Implementar Analytics para medir KPIs reales, Crashlytics para monitoreo de errores.
3. **Strings externalizados:** Migrar a `strings.xml` y añadir soporte para inglés.
4. **Icono personalizado:** Diseñar un adaptive icon con la identidad visual colombiana.
5. **Modo oscuro:** Reactivar `darkTheme` con los colores ya definidos en `DarkColorScheme`.

### Mediano plazo (2-3 semestres)
6. **Kotlin Multiplatform (KMM):** Migrar la capa `domain` y `data` a shared module para compartir lógica con iOS.
7. **iOS con SwiftUI:** Implementar la capa UI en SwiftUI consumiendo el shared KMM module.
8. **Sincronización cloud:** Guardar favoritos en Firestore para sincronización entre dispositivos.
9. **Deep links:** Permitir compartir recetas vía URL (`saboresdecolombia://recipe/52772`).

### Largo plazo (profesional)
10. **Machine Learning regional:** Clasificar automáticamente recetas por región usando NLP sobre ingredientes e instrucciones.
11. **Widget:** Receta del día en la pantalla de inicio (Android App Widgets + Glance).
12. **Wear OS:** Versión para reloj con recetas rápidas y temporizador de cocina.
13. **Comunidad:** Comentarios, valoraciones y fotos de usuarios en cada receta.

---

## 5. Lecciones Aprendidas

1. **Versionar con Version Catalog desde el inicio** evita conflictos de dependencias y facilita upgrades.
2. **No subestimar la configuración inicial:** ~45 minutos se invirtieron en resolver incompatibilidades AGP-Kotlin-Hilt-KSP. Investigar compatibilidad antes de elegir versiones.
3. **Room como caché es infravalorado:** Una tabla `cached_recipes` con ~300 filas eliminó la dependencia de red para todas las pantallas.
4. **Los endpoints no oficiales son frágiles:** `translate_a/single` funciona hoy, pero el código está preparado para fallar gracefulmente (muestra original si falla).
5. **Coroutines + Semaphore** son la herramienta correcta para controlar concurrencia en Android. Evitar `GlobalScope` y preferir `coroutineScope` estructurado.
6. **Escribir tests aunque sean mínimos:** 10 tests unitarios atraparon bugs en el mapper y validaron el comportamiento de ViewModels en < 1 hora de desarrollo.
