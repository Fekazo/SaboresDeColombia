# Sabores de Colombia 🇨🇴

Aplicación Android para explorar recetas y platillos de distintas culturas, con traducción automática al español, información nutricional y sistema de favoritos.

---

## Descripción

Sabores de Colombia es una app Android desarrollada con Kotlin y Jetpack Compose que permite al usuario descubrir platillos organizados por región, ver su información completa (ingredientes, pasos de preparación, información nutricional) y guardar sus favoritos localmente. Todos los datos provenientes de la API son traducidos automáticamente al español y persistidos en caché local para funcionamiento offline.

---

## Funcionalidades

- Exploración por regiones (Andina, Caribe, Pacífico, Orinoquía, Amazonía)
- Listado de todos los platillos disponibles con ordenamiento A→Z / Z→A
- Platillos populares con ranking de posición (Top 5)
- Vista de detalle con imagen, ingredientes, instrucciones y nutrición
- Búsqueda por nombre en español o inglés
- Favoritos guardados localmente con Room
- Traducción automática al español de nombres, área, instrucciones e ingredientes
- Precarga y traducción completa durante la pantalla de inicio
- Soporte offline mediante caché con TTL

---

## Arquitectura

El proyecto implementa **Clean Architecture** con patrón **MVVM**, organizado en las siguientes capas:

```
data/
├── local/          → Room (entidades, DAOs, base de datos, migraciones)
├── remote/         → Retrofit (servicios API, DTOs)
├── mapper/         → Conversión DTO ↔ Dominio ↔ Entidad
└── repository/     → Implementaciones de los repositorios

domain/
├── model/          → Modelos de negocio (Meal, Favorite, MealSummary, etc.)
├── repository/     → Interfaces de repositorios
└── util/           → Result sealed class

ui/
├── screen/         → Pantallas Composable
├── viewmodel/      → ViewModels por pantalla
├── state/          → Estados de UI (sealed classes)
├── components/     → Componentes reutilizables
├── navigation/     → Grafo de navegación y BottomNavBar
└── theme/          → Colores, tipografía y tema

di/                 → Módulos Hilt (Database, Network, Repository)
```

---

## Tecnologías utilizadas

| Categoría | Tecnología |
|---|---|
| Lenguaje | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Arquitectura | MVVM + Clean Architecture |
| Inyección de dependencias | Hilt |
| Base de datos local | Room |
| Networking | Retrofit + OkHttp |
| Parseo JSON | Moshi |
| Imágenes | Coil |
| Navegación | Navigation Compose |
| Concurrencia | Coroutines + StateFlow |
| Traducción | Google Translate API (gtx) |
| Nutrición | Edamam API |
| Platillos | TheMealDB API |

---

## APIs utilizadas

### TheMealDB
- Sin autenticación requerida
- `random.php` → platillos populares
- `search.php?s={letra}` → todos los platillos por letra
- `search.php?s={query}` → búsqueda por nombre
- `lookup.php?i={id}` → detalle de platillo

### Edamam
- Requiere `app_id` y `app_key`
- `recipes/v2?q={nombre}` → información nutricional

### Google Translate (gtx)
- Sin autenticación requerida
- Detección automática del idioma de origen
- Traducción al español

---

## Configuración

### Requisitos
- Android Studio Ladybug o superior
- JDK 17 (recomendado: Eclipse Adoptium JDK 17)
- Android SDK 35
- Conexión a internet para la carga inicial

### Credenciales Edamam

En `app/build.gradle.kts`, reemplaza los valores por defecto con tus credenciales de [Edamam](https://developer.edamam.com/):

```kotlin
buildConfigField("String", "EDAMAM_APP_ID", "\"TU_APP_ID\"")
buildConfigField("String", "EDAMAM_APP_KEY", "\"TU_APP_KEY\"")
```

### JDK en gradle.properties

Si el proyecto no compila por problemas con `jlink`, asegúrate de tener configurado el JDK correcto:

```properties
org.gradle.java.home=C:\\Program Files\\Android\\Android Studio\\jbr
```

---

## Pantallas

| Pantalla | Descripción |
|---|---|
| Splash | Carga y traduce todos los datos al iniciar |
| Inicio | Tarjetas de regiones para explorar |
| Populares | Top 5 platillos con número de posición |
| Todos los platillos | Lista completa con ordenamiento |
| Detalle | Imagen, ingredientes, preparación y nutrición |
| Búsqueda | Búsqueda en caché local (español) y API |
| Favoritos | Platillos guardados localmente |

---

## Estructura del proyecto

```
SaboresDeColombia/
├── app/
│   ├── src/main/java/com/previo/p2/
│   │   ├── data/
│   │   ├── domain/
│   │   ├── ui/
│   │   ├── di/
│   │   ├── MainActivity.kt
│   │   └── SaboresDeColombiaApp.kt
│   └── schemas/           → Esquemas de Room exportados
├── gradle/
│   └── libs.versions.toml
├── gradle.properties
└── README.md
```

---

## Notas importantes

- La primera vez que se abre la app, el splash puede tardar varios minutos mientras descarga y traduce todos los platillos disponibles (~300). Las siguientes aperturas son instantáneas gracias al caché.
- El caché tiene un TTL de 30 minutos. Pasado ese tiempo se recargan los datos.
- La información nutricional requiere credenciales válidas de Edamam para funcionar.