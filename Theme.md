# Voice of Muslim — Theme Guide

## Premium Dark Palette

| Token | Color | Usage |
|-------|-------|--------|
| `BackgroundDeep` | `#0A0E14` | Root scaffold background |
| `BackgroundElevated` | `#121820` | Cards, navigation bar |
| `GoldPrimary` | `#D4AF37` | Headings, accents, quotes |
| `GreenPrimary` / `GreenBright` | `#2E7D32` / `#43A047` | Primary CTAs, prayer ready |
| Status rings | Red / Yellow / Green | Member prayer states |

## Typography

- **Arabic headings & hadith**: `AmiriFontFamily` (`FontFamily.Serif` stand-in; bundle Amiri font assets for production).
- **Body UI**: Material default sans.

## Spacing (`core/theme/Spacing.kt`)

All layouts use multiples of **8dp**: `sm = 8`, `md = 16`, `lg = 24`, etc.

## Components

- `RichDarkBackground` — vertical gradient shell
- `GlassTextField` — glassmorphic auth inputs with gold focus border
- `VoiceOfMuslimTheme` — wraps `MaterialTheme` with dark color scheme

## Responsive layout

- Scrollable columns with `widthIn(max = 520.dp)` for auth and `720.dp` for main features
- Avoid fixed full-screen widths; prefer `fillMaxWidth()` + max width caps for desktop
