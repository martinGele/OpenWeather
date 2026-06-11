# OpenWeather

A small Android weather app that fetches forecast data from the [OpenWeatherMap API](https://openweathermap.org/api). The project is built as a multi-module Android application following Clean Architecture principles, with a strict separation of concerns between modules.

> ℹ️ **Note**
> [Claude](https://claude.ai) was used during the initial setup to help sanity-check the separation of concerns between the modules (which layer depends on which). The architecture and implementation decisions are otherwise the author's own.

## Modularization

The codebase is split into independent Gradle modules grouped by responsibility. The dependency direction always points **inward** toward the domain — outer layers know about inner layers, never the reverse.

```
:app
 ├── :core:ui                     (design system: theme, colors, dimens, type)
 ├── :core:network                (Retrofit/OkHttp setup, safe API calls)
 ├── :core:common                 (shared dispatchers, cross-cutting helpers)
 └── :feature:forecast
      ├── :domain                 (pure Kotlin/JVM — models, use cases, repository contracts)
      ├── :data                   (API, DTOs, mappers, repository implementations)
      └── :presentation           (Compose UI, ViewModel, UI contract/state)
```

### Module responsibilities

| Module | Type | Responsibility | Knows about |
| --- | --- | --- | --- |
| `:app` | Android application | Wires the graph together, hosts `MainActivity` + `Application`, provides backend config (base URL + auth). | `:core:*`, all `:feature` layers |
| `:core:ui` | Android library | Pure design system — theming, spacing, radii, fonts. No domain or data knowledge. | nothing |
| `:core:network` | Android library | Retrofit / OkHttp wiring, the `safeApiCall` wrapper, and the `NetworkResult` type. | nothing app-specific |
| `:core:common` | Android library | Shared cross-cutting concerns such as coroutine dispatcher providers. | nothing app-specific |
| `:feature:forecast:domain` | **Pure JVM library** | Business models, use cases, and repository **interfaces**. Framework-free. | nothing |
| `:feature:forecast:data` | Android library | `WeatherApi`, DTOs, mappers, and the repository **implementation**. | `:domain`, `:core:network`, `:core:common` |
| `:feature:forecast:presentation` | Android library | Compose `WeatherScreen`, `WeatherViewModel`, and the UI state/event contract. | `:domain` only |

### Separation of concerns

- **The domain is the center.** `:feature:forecast:domain` is a plain Kotlin/JVM module with no Android, Retrofit, or Compose dependencies. It defines `Weather`, `GetWeatherUseCase`, and the `WeatherRepository` interface. Everything else depends on it; it depends on nothing.
- **Data implements, presentation consumes.** The `:data` module provides the concrete `WeatherRepositoryImpl` (network → DTO → domain model via mappers). The `:presentation` module only ever sees the domain interface — it has no idea Retrofit or DTOs exist.
- **The UI layer is framework-only.** `:core:ui` is a self-contained design system and is explicitly forbidden from importing any model, use case, or repository.
- **App is the only place the graph is assembled.** `:app` pulls in `:feature:forecast:data` on the compile classpath so Hilt can aggregate its `@InstallIn` modules, and supplies environment config (base URL, API key interceptor) into `:core:network`.

This layering keeps each concern testable in isolation and means a change to, say, the networking client never leaks into business logic or UI.

## Technology

| Concern | Technology |
| --- | --- |
| Language | [Kotlin](https://kotlinlang.org/) |
| UI | [Jetpack Compose](https://developer.android.com/jetpack/compose) + Material 3 |
| Architecture | Clean Architecture (domain / data / presentation) with MVI-style UI contract |
| Async | [Kotlin Coroutines & Flow](https://kotlinlang.org/docs/coroutines-overview.html) |
| Dependency Injection | [Hilt](https://dagger.dev/hilt/) (Dagger) + KSP |
| Networking | [Retrofit](https://square.github.io/retrofit/) + [OkHttp](https://square.github.io/okhttp/) (logging interceptor) |
| Serialization | [Gson](https://github.com/google/gson) |
| Build | [Gradle](https://gradle.org/) with Kotlin DSL, a [version catalog](gradle/libs.versions.toml), and custom **convention plugins** in `build-logic/` |
| Testing | JUnit 4, [Turbine](https://github.com/cashapp/turbine), `kotlinx-coroutines-test`, Compose UI test |

### Build logic & convention plugins

Common build configuration is centralized in the `build-logic/` composite build instead of being copy-pasted across every module's `build.gradle.kts`. Each module simply applies the convention plugin that matches its role:

- `openweather.android.application` — the app module
- `openweather.android.library` — standard Android library modules
- `openweather.android.compose` — adds the Compose toolchain
- `openweather.android.feature` — bundles library + Compose + Hilt for feature modules
- `openweather.android.hilt` — adds Hilt + KSP
- `openweather.jvm.library` — pure Kotlin/JVM modules (e.g. the domain layer)

All dependency versions are declared in [`gradle/libs.versions.toml`](gradle/libs.versions.toml).

## Getting started

1. Get a free API key from [OpenWeatherMap](https://home.openweathermap.org/api_keys).
2. Add it to your `local.properties` (gitignored, never committed):

   ```properties
   OPENWEATHER_API_KEY=your_key_here
   ```

   CI can instead supply it via `-POPENWEATHER_API_KEY=...` or an environment variable.
3. Build and run:

   ```bash
   ./gradlew :app:installDebug
   ```

## Project requirements

- **compileSdk / targetSdk:** 36
- **minSdk:** 24
- **JDK:** 17+ (resolved via the Foojay toolchain resolver)
