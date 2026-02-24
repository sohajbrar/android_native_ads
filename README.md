# WA Business Broadcast

A WhatsApp Business Android sandbox built with Jetpack Compose — a high-fidelity prototype environment for testing features and design iterations.

## Overview

WA Business Broadcast is a production-quality prototype built from the ground up with Jetpack Compose, designed to serve as a sandbox environment for rapid prototyping and feature testing. It implements WDS (WhatsApp Design System) for the Business app experience.

## Architecture

```
app/src/main/java/com/example/chatapp/
├── MainActivity.kt                        # App entry point & navigation
├── ChatApplication.kt                     # Hilt application class
├── navigation/
│   └── Screen.kt                          # Navigation routes
├── data/                                  # Data layer
│   ├── local/                             # Room database
│   │   ├── ChatDatabase.kt
│   │   ├── dao/                           # Data access objects
│   │   ├── entity/                        # Database entities
│   │   └── converter/                     # Type converters
│   ├── repository/
│   │   └── ChatRepository.kt             # Single source of truth
│   ├── generator/
│   │   └── ChatDataGenerator.kt          # Sample data
│   └── initializer/
│       └── DatabaseInitializer.kt         # DB setup
├── di/
│   └── DatabaseModule.kt                  # Hilt DI module
├── features/                              # Top-level screens
│   ├── chatlist/                          # Chat list (main tab)
│   ├── chat/                              # Chat conversation
│   ├── chatinfo/                          # Contact/group info
│   ├── newchat/                           # New chat creation
│   ├── broadcast/                         # Business broadcast flow
│   ├── tools/                             # Business tools
│   └── main/                              # Main view model
├── ui/
│   └── screens/                           # Design system showcase
│       ├── DesignSystemLibraryScreen.kt
│       ├── ColorsScreen.kt
│       ├── TypeScreen.kt
│       ├── ComponentsScreen.kt
│       └── IconsScreen.kt
└── wds/                                   # WhatsApp Design System
    ├── theme/                             # Color tokens & themes
    │   ├── BaseColors.kt
    │   ├── WdsColorScheme.kt
    │   ├── WdsSemanticLightColors.kt
    │   ├── WdsSemanticBusinessLightColors.kt
    │   ├── WdsSemanticDarkColors.kt
    │   ├── WdsFonts.kt
    │   └── WdsTheme.kt
    ├── tokens/                            # Spacing, shapes, typography
    │   ├── BaseDimensions.kt
    │   ├── WdsDimensions.kt
    │   ├── WdsShapes.kt
    │   └── WdsTypography.kt
    └── components/                        # Reusable UI components
        ├── WDSButton.kt
        ├── WDSChip.kt
        ├── WDSTextField.kt
        ├── WDSFab.kt
        ├── WDSBottomSheet.kt
        ├── WDSBottomBar.kt
        ├── WDSTopBar.kt
        ├── WDSSearchBar.kt
        ├── WDSChatListItem.kt
        ├── WDSContentRow.kt
        ├── WDSListRow.kt
        ├── WDSContextMenu.kt
        ├── WDSDivider.kt
        ├── WDSSectionDivider.kt
        ├── WDSSystemMessage.kt
        ├── WdsCheckbox.kt
        ├── WdsRadioButton.kt
        ├── WdsSwitch.kt
        └── WdsDialog.kt
```

## Design System (WDS)

### Color Tokens

| Token | Description | Business Light |
|-------|-------------|----------------|
| **Content** | | |
| `colorContentDefault` | Main text/icons | #1A1A1A |
| `colorContentDeemphasized` | Secondary text | #667781 |
| `colorContentInverse` | Text on dark bg | #FFFFFF |
| **Surface** | | |
| `colorSurfaceDefault` | Main background | #FFFFFF |
| `colorSurfaceElevatedDefault` | Cards/sheets | #FFFFFF |
| `colorSurfaceHighlight` | Highlighted areas | #F0F2F5 |
| **Accent (Business)** | | |
| `colorAccent` | Primary actions | #171616 |
| `colorAccentDeemphasized` | Selected/tinted bg | #F7F5F3 |
| `colorAccentEmphasized` | Emphasized actions | #262524 |
| **Always Branded** | | |
| `colorPositive` | Badges, status, timestamps | #1DAA61 |
| **Chat** | | |
| `colorBubbleSurfaceOutgoing` | Sent messages | #D9FDD3 |
| `colorBubbleSurfaceIncoming` | Received messages | #FFFFFF |
| **Feedback** | | |
| `colorNegative` | Errors/destructive | #CF0A2C |
| `colorWarning` | Warning states | #F0A03C |

### Typography Tokens

| Style | Size / Weight | Usage |
|-------|--------------|-------|
| `largeTitle1` | 28pt Bold | Large headings |
| `largeTitle2` | 24pt Bold | Secondary headings |
| `headline1` | 20pt Bold | Section headers |
| `headline2` | 18pt Bold | Subsection headers |
| `body1` | 16pt Regular | Body text |
| `body1Emphasized` | 16pt Bold | Emphasized body |
| `body2` | 14pt Regular | Secondary body |
| `body2Emphasized` | 14pt Bold | Emphasized secondary |
| `caption1` | 12pt Regular | Small text |
| `caption2` | 11pt Regular | Meta text |
| `chatBubbleText` | 16pt Regular | Message bubbles |
| `chatTimestamp` | 11pt Regular | Message timestamps |

### Spacing Tokens

| Token | Value | Usage |
|-------|-------|-------|
| `wdsSpacingQuarter` | 2dp | Tight spacing |
| `wdsSpacingHalf` | 4dp | Compact elements |
| `wdsSpacingSingle` | 8dp | Small gaps |
| `wdsSpacingSinglePlus` | 12dp | Medium spacing |
| `wdsSpacingDouble` | 16dp | Standard padding |
| `wdsSpacingTriple` | 24dp | Section spacing |
| `wdsSpacingQuad` | 32dp | Large spacing |
| `wdsSpacingQuint` | 40dp | Maximum spacing |

### Corner Radius Tokens

| Token | Value | Usage |
|-------|-------|-------|
| `none` | 0dp | No rounding |
| `halfPlus` | 6dp | Slight rounding |
| `single` | 8dp | Standard rounding |
| `singlePlus` | 12dp | Comfortable rounding |
| `double` | 16dp | Medium rounding |
| `triple` | 24dp | Large rounding |
| `triplePlus` | 28dp | Extra large rounding |
| `circle` | 100dp | Pills, circles |

## Getting Started

### Requirements

- Android Studio (latest stable)
- JDK 17+
- Min SDK 31 (Android 12)

### Building

1. Clone the repository
2. Open in Android Studio
3. Wait for Gradle sync to complete
4. Build and run (Shift+F10)

### Vibe Coding with AI

1. Open the project folder in **Cursor**
2. The AI assistant will automatically follow WDS rules via `CLAUDE.md`
3. Prompt Cursor to make changes
4. Return to Android Studio and rebuild

## Design Principles

- **Token-First**: All styling uses WDS tokens — no hardcoded values
- **Component-Based**: Modular, reusable WDS components
- **Business Identity**: Warm gray accents with persistent green branding
- **Performance**: Optimized recomposition, lazy loading, cached theme lookups
- **Fidelity**: Pixel-perfect WhatsApp Business implementation

## Rules for AI Agents

**These are mandatory. Apply to every line of code.**

### Design System Usage

```kotlin
// ALWAYS USE TOKENS
val colors = WdsTheme.colors
val dimensions = WdsTheme.dimensions
val typography = WdsTheme.typography

Text(
    text = "Hello",
    style = typography.headline1,
    color = colors.colorContentDefault
)

Box(
    modifier = Modifier
        .padding(dimensions.wdsSpacingDouble)
        .background(colors.colorSurfaceDefault, WdsTheme.shapes.single)
)

// NEVER HARDCODE
Text(
    text = "Hello",
    fontSize = 24.sp,             // NO arbitrary sizes
    color = Color(0xFF000000)     // NO hex colors
)
```

### Token Reference

- **Colors**: Use `WdsTheme.colors.*` (see table above)
- **Typography**: Use `WdsTheme.typography.*` (see table above)
- **Spacing**: Use `WdsTheme.dimensions.*` (see table above)
- **Shapes**: Use `WdsTheme.shapes.*` (see table above)
- **Icons**: Use `Icons.Outlined.*` by default, 24dp, with `WdsTheme.colors` tints
- **Components**: Check existing WDS components before creating new ones

### Component Pattern

```kotlin
@Composable
fun NewComponent(
    title: String,
    modifier: Modifier = Modifier
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Column(
        modifier = modifier.padding(dimensions.wdsSpacingDouble),
        verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingSingle)
    ) {
        Text(
            text = title,
            style = typography.headline1,
            color = colors.colorContentDefault
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NewComponentPreview() {
    WdsTheme { NewComponent(title = "Example") }
}
```

## For Designers

Just describe what you want. The AI will automatically use correct WDS tokens, find existing components, and follow established patterns.

Example prompts:

- "Build a broadcast message composer"
- "Create a contact selection screen"
- "Add a business tools dashboard"
- "Make a chat info page with media sections"

**Icon reference**: [Material Symbols and Icons](https://fonts.google.com/icons)

## Key Components

### Navigation
- `WDSTopBar` — Top app bar with title and actions
- `WDSBottomBar` — Bottom tab navigation

### Input
- `WDSButton` — Multi-variant (Filled, Tonal, Outline, Borderless)
- `WDSChip` — Filter and input chips
- `WDSTextField` — Single-line and multi-line text fields
- `WDSSearchBar` — Search input bar
- `WDSFab` — Floating action button

### Display
- `WDSChatListItem` — Chat list row
- `WDSContentRow` — Generic content row
- `WDSListRow` — Standard list row
- `WDSSystemMessage` — System message bubble
- `WDSDivider` / `WDSSectionDivider` — Dividers

### Controls
- `WdsCheckbox` / `WdsRadioButton` / `WdsSwitch` — Selection controls
- `WdsDialog` — Modal dialog
- `WDSBottomSheet` — Bottom sheet
- `WDSContextMenu` — Popup menu

## Additional Resources

- **WDS Docs**: `app/src/main/java/com/example/chatapp/wds/README.md`
- **AI Rules**: `CLAUDE.md`
- **Design Library**: In-app via 3-dot menu

## License

This is a prototype project for internal testing and design iteration.

---

Built with Cursor
