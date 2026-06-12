---
version: 2.0
name: OpenWeather-atmospheric-sky
description: A photographic, immersive weather app where the screen itself is the weather. A full-bleed vertical gradient — driven by the live condition and the time of day (sunny/cloudy/rain/snow/storm × day/night) — fills edge to edge behind the status bar. Content floats on top in frosted-glass cards with white type. One warm voltage, Rausch (#ff385c), survives on the search orb and primary action so it reads against any sky. The hero is a single huge temperature; everything else is quiet glass.
platform: Android · Jetpack Compose · Material 3 · edge-to-edge

colors:
  # Voltage — the one accent that reads on every sky
  primary: "#ff385c"
  primary-active: "#e00b41"
  primary-disabled: "#ffd1da"
  on-primary: "#ffffff"

  # On-sky text (white family)
  on-sky: "#ffffff"
  on-sky-muted: "rgba(255,255,255,0.78)"
  on-sky-faint: "rgba(255,255,255,0.60)"

  # Frosted glass
  glass-fill: "rgba(255,255,255,0.16)"
  glass-fill-strong: "rgba(255,255,255,0.22)"
  glass-border: "rgba(255,255,255,0.30)"

  error: "#ffd7cf"

sky-gradients:
  clear-day: ["#1E78C2", "#4FA0DC", "#8FC5EF"]
  clear-night: ["#0B1026", "#1B2450", "#34407A"]
  clouds-day: ["#5B6B78", "#8595A1", "#AAB7C0"]
  clouds-night: ["#1B232C", "#313C47", "#49555F"]
  rain-day: ["#3E5A6E", "#5C7C92", "#7C9AAD"]
  rain-night: ["#141B26", "#26323F", "#38485A"]
  storm: ["#20242B", "#3A3F4A", "#565D6B"]
  snow-day: ["#5E7A92", "#88A6BE", "#B9CEDD"]
  snow-night: ["#273140", "#3F4D60", "#56697E"]
  mist-day: ["#8794A0", "#A9B4BD", "#C6CDD3"]
  mist-night: ["#2A323A", "#3A434C", "#4A555E"]

typography:
  hero-temp:
    role: displayLarge
    fontSize: 64sp
    fontWeight: 700
    lineHeight: 70sp
    letterSpacing: -1sp
    use: The current temperature — the loud anchor of the whole screen.
  page-title:
    role: displayMedium
    fontSize: 28sp
    fontWeight: 700
    use: Search "Where to?" title.
  location-title:
    role: displaySmall
    fontSize: 22sp
    fontWeight: 500
    use: City, Country on the hero.
  section-head:
    role: headlineMedium
    fontSize: 21sp
    fontWeight: 700
    use: "7-day forecast".
  block-title:
    role: titleMedium
    fontSize: 16sp
    fontWeight: 600
    use: Day name, stat value, place name.
  list-title:
    role: titleSmall
    fontSize: 16sp
    fontWeight: 500
    use: Day high/low, search-pill placeholder.
  body:
    role: bodyLarge
    fontSize: 16sp
    fontWeight: 400
    use: Condition description, error copy.
  meta:
    role: bodyMedium
    fontSize: 14sp
    fontWeight: 400
    use: H/L line, day sub-text, region/country.
  stat-label:
    role: labelSmall
    fontSize: 11sp
    fontWeight: 600
    textTransform: uppercase
    use: Stat-tile labels.

rounded:
  sm: 8dp
  md: 14dp
  lg: 20dp
  xl: 28dp
  full: 9999dp

spacing:
  xxs: 2dp
  xs: 4dp
  sm: 8dp
  md: 12dp
  base: 16dp
  lg: 24dp
  xl: 32dp
  xxl: 48dp
  section: 64dp

components:
  sky-backdrop:
    fill: vertical-gradient from {sky-gradients}
    bleed: edge-to-edge (behind status & nav bars)
    selectedBy: current condition + day/night
  glass-card:
    backgroundColor: "{colors.glass-fill}"
    border: "1dp {colors.glass-border}"
    textColor: "{colors.on-sky}"
    rounded: "{rounded.lg}"
    elevation: 0
    padding: "{spacing.lg}"
  stat-tile:
    extends: glass-card
    rounded: "{rounded.md}"
    padding: "{spacing.base}"
    label: "{typography.stat-label}"  # on-sky-muted
    value: "{typography.block-title}"  # on-sky
  search-bar-pill:
    backgroundColor: "{colors.glass-fill}"
    border: "1dp {colors.glass-border}"
    placeholderColor: "{colors.on-sky-muted}"
    rounded: "{rounded.full}"
    height: 60dp
    trailing: search-orb
  search-orb:
    backgroundColor: "{colors.primary}"
    iconColor: "{colors.on-primary}"
    rounded: "{rounded.full}"
    size: 44dp
  button-primary:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.on-primary}"
    rounded: "{rounded.sm}"
    minHeight: 48dp
  button-secondary-glass:
    backgroundColor: transparent
    textColor: "{colors.on-sky}"
    border: "1dp {colors.on-sky}"
    rounded: "{rounded.sm}"
    minHeight: 48dp
  weather-hero:
    title: "{typography.location-title}"   # on-sky
    description: "{typography.body}"        # on-sky-muted
    temp: "{typography.hero-temp}"          # on-sky
    iconSize: 120dp
  day-row:
    textColor: "{colors.on-sky}"
    divider: "1dp {colors.glass-border}"
    iconSize: 44dp
  text-input-glass:
    textColor: "{colors.on-sky}"
    placeholderColor: "{colors.on-sky-muted}"
    border: "1dp {colors.glass-border}"
    focusBorder: "1dp {colors.on-sky}"
    cursor: "{colors.primary}"
    rounded: "{rounded.sm}"
  place-row:
    title: "{typography.block-title}"  # on-sky
    sub: "{typography.meta}"           # on-sky-muted
    divider: "1dp {colors.glass-border}"
---

## Overview

In this system **the screen is the weather**. There is no flat white canvas — a full-bleed vertical gradient fills the entire window, edge to edge, behind the status and navigation bars. The gradient is not decorative: it is *selected from the data*. The current condition (`Clear`, `Clouds`, `Rain`, `Snow`, `Thunderstorm`, `Mist`…) crossed with day-or-night (derived from sunrise/sunset vs. the observation time) picks one of the `sky-gradients`. A clear afternoon glows blue; a clear night sinks to deep indigo; a storm goes slate-dark. The user *feels* the weather before reading a number.

Content floats on top as **frosted glass** (`{component.glass-card}`) — semi-transparent white fills with a hairline white border and no shadow, so the sky shows through. All type is white: `{colors.on-sky}` for primary, `{colors.on-sky-muted}` for secondary. The one color that is *not* white is **Rausch** (`{colors.primary}` — #ff385c), kept only on the search orb and the primary button because it stays legible on every sky.

The single loud moment is still the **temperature at 64sp / 700** (`{typography.hero-temp}`). Everything else is quiet glass, letting the gradient and the one big number carry the screen.

**Key characteristics:**
- Data-driven backdrop — `{component.sky-backdrop}` is chosen by `condition × day/night`, full-bleed behind the system bars.
- Frosted-glass content — translucent white cards, hairline white borders, zero elevation. Depth comes from translucency over the gradient, not shadows.
- White type throughout — `{colors.on-sky}` / `{colors.on-sky-muted}` / `{colors.on-sky-faint}`. Light status-bar icons.
- One surviving accent — Rausch on the search orb and primary CTA only.
- One loud number — the hero temperature; all other type is modest weight.

## Sky Backdrop

The backdrop is a 2–3 stop **vertical gradient** drawn from the top of the window. It is selected as:

| Condition group | Day | Night |
|---|---|---|
| Clear | `clear-day` (blue) | `clear-night` (indigo) |
| Clouds | `clouds-day` (slate-blue) | `clouds-night` (graphite) |
| Rain / Drizzle | `rain-day` (steel) | `rain-night` (deep steel) |
| Thunderstorm | `storm` (slate-dark, both) | `storm` |
| Snow | `snow-day` (pale blue) | `snow-night` (cold navy) |
| Mist / Fog / Haze / Smoke | `mist-day` (gray) | `mist-night` (dark gray) |
| *fallback* | `clear-day` | `clear-night` |

**Day/night** is `sunrise ≤ observationTime ≤ sunset` for the current weather (defaults to day when the times are unknown, e.g. on the search screen, which uses a calm `clear-day` backdrop). Every gradient is kept deep enough that white type holds AA contrast across the whole height.

## Colors

- **Rausch** (`{colors.primary}` — #ff385c): search orb, primary button, spinner. The only non-white color.
- **On-sky** (`{colors.on-sky}` — #ffffff): all primary text and icons.
- **On-sky muted / faint** (78% / 60% white): descriptions, sub-text, labels, dividers' siblings.
- **Glass fill** (16% white) / **strong** (22%): card and pill surfaces.
- **Glass border** (30% white): the 1px hairline on every glass surface and the row dividers.
- **Error** (`{colors.error}` — #ffd7cf): a pale warm tint for error copy that stays readable on a dark sky (a saturated red would vanish into a storm gradient).

## Typography

Platform sans (substitute for Airbnb Cereal / Inter). Weights stay modest; only the temperature shouts.

| Token | Role | Size / Weight | Color |
|---|---|---|---|
| `hero-temp` | displayLarge | 64 / 700 | on-sky |
| `page-title` | displayMedium | 28 / 700 | on-sky |
| `location-title` | displaySmall | 22 / 500 | on-sky |
| `section-head` | headlineMedium | 21 / 700 | on-sky |
| `block-title` | titleMedium | 16 / 600 | on-sky |
| `list-title` | titleSmall | 16 / 500 | on-sky |
| `body` | bodyLarge | 16 / 400 | on-sky-muted |
| `meta` | bodyMedium | 14 / 400 | on-sky-muted |
| `stat-label` | labelSmall | 11 / 600 (uppercase) | on-sky-muted |

## Layout

- 4dp base (2dp micro-step): `xxs`2 `xs`4 `sm`8 `md`12 `base`16 `lg`24 `xl`32 `xxl`48 `section`64.
- The gradient `Box` fills the whole window; **content** is inset by `WindowInsets.safeDrawing` so it clears the system bars while the gradient bleeds under them.
- Screen padding: `{spacing.lg}` horizontal.
- Stat grid: 2 columns, `{spacing.md}` gutters; odd final tile stays half-width.

### Weather screen (top → bottom)
1. `{component.search-bar-pill}` (glass) → opens search.
2. `{component.weather-hero}` — location, condition, 64sp temp + live icon, H/L line.
3. Stat grid — `{component.stat-tile}` for each metric returned (feels-like, humidity, wind, cloudiness, pressure, visibility, sunrise, sunset).
4. `{typography.section-head}` + `{component.day-row}` list, divided by glass-border hairlines.

### Search screen
1. `{typography.page-title}` "Where to?".
2. `{component.text-input-glass}` — debounced city search.
3. `{component.place-row}` results; Rausch spinner; pale error copy.
Backdrop: a calm `clear-day` gradient.

## Shapes & Elevation

| Token | Value | Use |
|---|---|---|
| `{rounded.sm}` | 8dp | buttons, text input |
| `{rounded.md}` | 14dp | stat tiles |
| `{rounded.lg}` | 20dp | glass cards |
| `{rounded.full}` | 9999dp | search pill, orb, spinner |

**No drop shadows.** Glass surfaces have zero elevation — depth is the translucency + hairline border reading against the moving gradient beneath them.

## Components

**`sky-backdrop`** — Full-bleed vertical gradient chosen by condition × day/night (`Sky.gradientFor` in `core:ui`). Drawn behind the system bars.

**`glass-card`** — 16% white fill, 1px 30% white border, 20dp radius, white content, no shadow. (`GlassCard`.)

**`stat-tile`** — A `glass-card` at 14dp radius / 16dp padding: uppercase muted label over a white value. (`StatTile`.)

**`search-bar-pill`** — Glass pill, white-muted placeholder, trailing 44dp circular Rausch **`search-orb`**. (`SearchBarPill`.)

**`button-primary`** — Rausch fill, white label, 8dp, ≥48dp. (`PrimaryButton`.)

**`button-secondary-glass`** — Transparent fill, white label, 1px white outline. (`SecondaryButton`.)

**`weather-hero`** — Location title, muted condition, 64sp temperature, 120dp live icon, H/L meta line. All white.

**`day-row`** — 44dp icon, day name + (description · % rain), right-aligned high/low; glass-border divider between rows.

**`text-input-glass`** — White text, muted placeholder, 30% white border that flips to solid white on focus, Rausch cursor.

**`place-row`** — White city name over muted region/country; glass-border divider.

## Do's & Don'ts

**Do**
- Pick the backdrop from the data, never hard-code one sky.
- Keep all content white; rely on the gradient for color.
- Keep Rausch to the orb and primary action only.
- Keep gradients deep enough for white AA contrast; if a daytime sky is too pale, darken its stops rather than darkening the text.

**Don't**
- Don't put a solid opaque card over the gradient — glass must stay translucent or the effect dies.
- Don't add drop shadows; translucency is the depth.
- Don't introduce a second accent color or colored text beyond Rausch and the pale error tint.
- Don't let content slide under the status bar — only the gradient bleeds; text respects safe-drawing insets.

## Known gaps
- Per-hour gradient interpolation (golden-hour blending) is not specified — buckets are discrete.
- No animated gradient transition between conditions yet (instant swap).
- Skeleton loading uses a centered Rausch spinner over a fallback `clear-day` sky.
- Snow-day contrast is the tightest case; values are tuned for white text but bear re-checking if stops change.
