# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an Android prototype built with Jetpack Compose and the WhatsApp Design System (WDS). It provides a foundation for rapid prototyping with a complete design system including colors, typography, spacing, corner radius tokens, and reusable components.

This application is built with:
- **Android** (Kotlin)
- **Jetpack Compose** for UI
- **Room Database** for local storage
- **Hilt** for dependency injection
- **MVVM architecture** with Repository pattern

## Architecture

### Data Layer Architecture
The app uses a multi-layered architecture with Room database at its core:

1. **Database Layer** (`data/local/`)
   - `ChatDatabase`: Main Room database with 4 entities (User, Conversation, Message, ConversationParticipant)
   - DAOs provide Flow-based reactive queries for real-time updates
   - Type converters handle complex data types (Instant, MessageType, etc.)

2. **Repository Layer** (`data/repository/`)
   - `ChatRepository`: Single source of truth for all data operations
   - Provides unified interface for DAOs
   - Handles message sending with automatic conversation updates

3. **Dependency Injection** (`di/`)
   - `DatabaseModule`: Provides singleton database and DAO instances via Hilt
   - Database name: "chat_database"

### UI Architecture
The app follows WhatsApp design with custom theming:

1. **Screens** (`presentation/`)
   - `ChatListScreen`: Main chat list with filters, search, and archived chats
   - Uses ViewModels with StateFlow for reactive UI updates
   - Navigation handled via Compose Navigation

2. **Theme System** (`ui/theme/`)
   - WhatsApp WDS design system theming with custom dimensions, spacing, and elevation
   - Custom design system matching WhatsApp's design language
   - Reusable components with consistent styling


## Design System Architecture

### WDS Theme Structure
The design system is located in `app/src/main/java/com/example/chatapp/ui/theme/wds/` and consists of:

1. **Colors** (`WdsColorScheme.kt`, `WdsSemanticLightColors.kt`, `WdsSemanticDarkColors.kt`, `BaseColors.kt`)
   - Semantic colors for light and dark modes
   - Base color primitives
   - Accessed via `WdsTheme.colors`

2. **Typography** (`WdsTypography.kt`, `BaseDimensions.kt`)
   - Complete type scale (Large Titles, Headlines, Body, Chat styles)
   - Font weights, sizes, line heights, letter spacing
   - Accessed via `WdsTheme.typography`

3. **Spacing** (`WdsDimensions.kt`, `BaseDimensions.kt`)
   - Spacing tokens: Quarter (2dp), Half (4dp), Single (8dp), SinglePlus (12dp), Double (16dp), Triple (24dp), Quad (32dp), Quint (40dp)
   - Accessed via `WdsTheme.dimensions`

4. **Corner Radius** (`WdsShapes.kt`, `BaseDimensions.kt`)
   - Radius tokens: None (0dp), HalfPlus (6dp), Single (8dp), Double (16dp), Triple (24dp), TriplePlus (28dp), Circle (9999dp)
   - Accessed via `WdsTheme.shapes`

5. **Components** (`ui/theme/wds/components/`)
   - WDSButton (variants: FILLED, TONAL, OUTLINE, BORDERLESS; actions: PRIMARY, SECONDARY, MEDIA)
   - WDSChip (actions: PRIMARY, SECONDARY; sizes: SMALL, MEDIUM, LARGE)
   - WDSTextField (single-line and multi-line)
   - WDSFab (floating action button)
   - WDSDivider
   - WDSBottomSheet
   - WdsCheckbox, WdsRadioButton, WdsSwitch, WdsDialog

### Key Features
- Real-time message status (sent, delivered, read)
- Group and individual conversations
- Message types: text, photo, video, audio, file, location, voice notes
- Conversation features: pinning, muting, archiving, unread counts
- User online status tracking
- Message search and filtering

## Database Schema

The app uses Room database version 1 with these main entities:
- `UserEntity`: User profiles with online status
- `ConversationEntity`: Chat conversations with metadata
- `MessageEntity`: Individual messages with various types
- `ConversationParticipantEntity`: Links users to conversations

Foreign key relationships ensure data integrity between entities.

## Important Development Notes

- The app targets Android SDK 34 with minimum SDK 31
- Room schemas are exported to `app/schemas/` for migration tracking
- Database initialization handled by `DatabaseInitializer` with sample data generation
- All database operations are suspend functions or return Flow for reactive updates
- The app uses version catalogs (`gradle/libs.versions.toml`) for dependency management

## Build and Development Commands

### Build the application
```bash
./gradlew assembleDebug
```

### Run tests
```bash
./gradlew test
./gradlew connectedAndroidTest  # For instrumented tests
```

### Clean and rebuild
```bash
./gradlew clean build
```

### Generate APK
```bash
./gradlew assembleRelease
```

## Mandatory Rules for Writing Code

### 1. Always Use WdsTheme
**NEVER use hardcoded values. ALWAYS use design system tokens.**

❌ **WRONG:**
```kotlin
Text(
    text = "Hello",
    color = Color(0xFF000000),
    fontSize = 16.sp
)
Box(
    modifier = Modifier
        .padding(16.dp)
        .background(Color.White, RoundedCornerShape(8.dp))
)
```

✅ **CORRECT:**
```kotlin
Text(
    text = "Hello",
    style = WdsTheme.typography.body1,
    color = WdsTheme.colors.colorContentDefault
)
Box(
    modifier = Modifier
        .padding(WdsTheme.dimensions.wdsSpacingDouble)
        .background(
            WdsTheme.colors.colorSurfaceDefault,
            WdsTheme.shapes.single
        )
)
```

### 2. Cache Theme Lookups for Performance
When using multiple theme properties in a composable, cache them in local variables:

✅ **CORRECT:**
```kotlin
@Composable
fun MyScreen() {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Column(
        modifier = Modifier.padding(dimensions.wdsSpacingTriple)
    ) {
        Text(
            text = "Title",
            style = typography.headline1,
            color = colors.colorContentDefault
        )
        Text(
            text = "Subtitle",
            style = typography.body1,
            color = colors.colorContentDeemphasized
        )
    }
}
```

### 3. Use WDS Components
Always prefer WDS components over Material3 components when available:

❌ **WRONG:**
```kotlin
Button(onClick = { }) {
    Text("Click me")
}
```

✅ **CORRECT:**
```kotlin
WDSButton(
    onClick = { },
    text = "Click me",
    variant = WDSButtonVariant.FILLED
)
```

### 4. Color Usage Guidelines

**Semantic Colors (Use These):**
- `colorContentDefault` - Primary text and icons
- `colorContentDeemphasized` - Secondary text and icons
- `colorContentEmphasized` - Emphasized text (bold, important)
- `colorContentInverse` - Text on dark backgrounds
- `colorSurfaceDefault` - Default background
- `colorSurfaceElevatedDefault` - Elevated surfaces (cards, sheets)
- `colorSurfaceHighlight` - Highlighted areas
- `colorAccentEmphasized` - Primary actions, links
- `colorAccentDeemphasized` - Tinted backgrounds
- `colorPositive`, `colorNegative`, `colorWarning` - Status colors

**Base Colors (Rarely Use Directly):**
Only use base colors (e.g., `wdsGray50`, `wdsCobalt500`) when semantic colors don't fit the use case.

### 5. Typography Usage

**Available Styles:**
- `largeTitle1`, `largeTitle2` - Large headings
- `headline1`, `headline2` - Section headings
- `body1`, `body2` - Body text
- `body1Emphasized`, `body2Emphasized` - Bold body text
- `caption1`, `caption2` - Small text, captions
- `chatBubbleText`, `chatTimestamp` - Chat-specific styles

**Usage:**
```kotlin
Text(
    text = "Heading",
    style = WdsTheme.typography.headline1,
    color = WdsTheme.colors.colorContentDefault
)
```

### 6. Spacing Usage

**Available Tokens:**
- `wdsSpacingQuarter` (2dp) - Minimal spacing
- `wdsSpacingHalf` (4dp) - Very tight spacing
- `wdsSpacingSingle` (8dp) - Default small spacing
- `wdsSpacingSinglePlus` (12dp) - Medium-small spacing
- `wdsSpacingDouble` (16dp) - Standard spacing
- `wdsSpacingTriple` (24dp) - Large spacing
- `wdsSpacingQuad` (32dp) - Extra large spacing
- `wdsSpacingQuint` (40dp) - Maximum spacing

**Usage:**
```kotlin
Column(
    modifier = Modifier.padding(WdsTheme.dimensions.wdsSpacingTriple),
    verticalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingDouble)
)
```

### 7. Corner Radius Usage

**Available Tokens:**
- `none` - No rounding (0dp)
- `halfPlus` - Slight rounding (6dp)
- `single` - Standard rounding (8dp)
- `singlePlus` - Comfortable rounding (12dp)
- `double` - Medium rounding (16dp)
- `triple` - Large rounding (24dp)
- `triplePlus` - Extra large rounding (28dp)
- `circle` - Fully rounded (pills, circles) (100dp)

**Usage:**
```kotlin
Box(
    modifier = Modifier
        .clip(WdsTheme.shapes.double)
        .background(WdsTheme.colors.colorSurfaceElevatedDefault)
)
```

### 8. Screen Structure Pattern

When creating new screens, follow this pattern:

```kotlin
@Composable
fun MyScreen(
    onNavigateBack: () -> Unit,
    // Other navigation callbacks
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Screen Title", style = typography.headline2) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = colors.colorContentDefault
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.colorSurfaceDefault,
                    titleContentColor = colors.colorContentDefault
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = dimensions.wdsSpacingTriple),
            verticalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingDouble)
        ) {
            // Content here
        }
    }
}
```

### 9. Component Usage Examples

**Button:**
```kotlin
WDSButton(
    onClick = { /* action */ },
    text = "Primary Action",
    variant = WDSButtonVariant.FILLED // or TONAL, OUTLINE, BORDERLESS
)
```

**Chip:**
```kotlin
WDSChip(
    text = "Filter",
    selected = isSelected,
    onClick = { /* action */ },
    action = WDSChipAction.PRIMARY,
    size = WDSChipSize.MEDIUM
)
```

**Bottom Sheet:**
```kotlin
WDSBottomSheet(
    onDismissRequest = { /* dismiss */ },
    headline = "Sheet Title",
    bodyText = "Description text",
    items = listOf(
        WDSBottomSheetItem(
            icon = Icons.Outlined.Info,
            text = "Item description"
        )
    ),
    primaryButtonText = "Confirm",
    onPrimaryClick = { /* action */ }
)
```

### 10. Optimization Best Practices

1. **Cache theme lookups** at the start of composables
2. **Use `remember`** for static lists and data
3. **Extract large composables** into separate functions
4. **Use `key` parameter** in `LazyColumn`/`LazyRow` items
5. **Avoid creating objects** inside composable lambdas

Example:
```kotlin
@Composable
fun OptimizedScreen() {
    val colors = WdsTheme.colors // Cache
    val dimensions = WdsTheme.dimensions

    val items = remember { // Memoize static data
        listOf("Item 1", "Item 2", "Item 3")
    }

    LazyColumn {
        itemsIndexed(
            items = items,
            key = { index, item -> item } // Add keys
        ) { index, item ->
            ItemRow(item = item) // Extract composable
        }
    }
}
```

### 11. Navigation Pattern

Use the existing navigation structure:
```kotlin
// In Screen.kt
sealed class Screen(val route: String) {
    object MyNewScreen : Screen("my_new_screen")
}

// In MainActivity.kt AppNavigation
composable(Screen.MyNewScreen.route) {
    MyNewScreen(
        onNavigateBack = { navController.popBackStack() }
    )
}
```

### 12. Import Organization

Organize imports in this order:
1. Android/AndroidX imports
2. Compose imports (animation, foundation, material, runtime, ui)
3. Navigation imports
4. Project-specific imports (navigation, screens, theme, components)

Use explicit imports, not wildcards.

## Common Patterns

### List with Dividers
```kotlin
LazyColumn {
    itemsIndexed(items) { index, item ->
        ItemContent(item)
        if (index < items.lastIndex) {
            WDSDivider()
        }
    }
}
```

### Section Headers
```kotlin
Text(
    text = "SECTION HEADER",
    style = WdsTheme.typography.caption1.copy(
        fontWeight = FontWeight.Medium
    ),
    color = WdsTheme.colors.colorContentDeemphasized,
    modifier = Modifier.padding(
        vertical = WdsTheme.dimensions.wdsSpacingSingle
    )
)
```

### Card/Surface Pattern
```kotlin
Surface(
    modifier = Modifier.fillMaxWidth(),
    color = WdsTheme.colors.colorSurfaceElevatedDefault,
    shape = WdsTheme.shapes.double
) {
    Column(
        modifier = Modifier.padding(WdsTheme.dimensions.wdsSpacingDouble)
    ) {
        // Content
    }
}
```

## Testing and Previews

Always add preview functions for new composables:
```kotlin
@Preview(showBackground = true, name = "Light Mode")
@Composable
fun MyScreenPreviewLight() {
    WdsTheme(darkTheme = false) {
        MyScreen(onNavigateBack = {})
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun MyScreenPreviewDark() {
    WdsTheme(darkTheme = true) {
        MyScreen(onNavigateBack = {})
    }
}
```

## Development Principles

### Code Style and Conventions
- Follow existing Kotlin conventions and patterns in the codebase
- Use Compose's declarative UI patterns consistently
- Maintain single responsibility principle in ViewModels and Repositories
- Keep UI components pure and stateless where possible
- Use Flow for reactive data streams, suspend functions for one-time operations

### Database Operations
- Always use Repository layer - never access DAOs directly from ViewModels
- Maintain referential integrity through proper foreign key relationships
- Use transactions for multi-table operations to ensure consistency
- Implement soft deletes for messages to preserve conversation history
- Handle database migrations carefully to preserve user data

### UI/UX Principles
- Maintain WhatsApp and Material 3 design consistency throughout the app. When WhatsApp pattern exists, prefer that over Material 3
- Preserve WhatsApp design patterns for user familiarity
- Ensure smooth animations and transitions using Compose
- Handle loading and error states gracefully in all screens
- Support both light and dark themes consistently


### Performance Considerations
- Use lazy loading for conversation lists and messages
- Implement pagination for large message histories
- Cache user avatars and media efficiently using Coil
- Minimize recompositions by using stable data classes
- Use remember and derivedStateOf appropriately in Composables

### Security and Privacy
- Never log sensitive user data or message content
- Implement proper data validation at repository level
- Handle user permissions appropriately for media access
- Prepare for end-to-end encryption implementation
- Sanitize all user inputs before database operations

### Testing Strategy
- Write unit tests for ViewModels and Repository methods
- Test Room DAOs with instrumented tests
- Verify Compose UI behavior with UI tests
- Mock database operations in ViewModel tests
- Ensure proper error handling in all test scenarios

### Material Icons Guidelines
**IMPORTANT**: When using Material Icons from the Material Icons Extended library:

#### Default Icon Settings
**ALWAYS use the Outlined variant with these settings:**
```
'FILL' 0      - Outlined (not filled)
'wght' 400    - Default weight
'GRAD' 0      - Default grade
'opsz' 24     - 24dp optical size
```

#### Implementation
```kotlin
// ✅ CORRECT - Use Outlined variant by default
import androidx.compose.material.icons.outlined.*
Icon(
    imageVector = Icons.Outlined.IconName,
    contentDescription = "Description",
    tint = WdsTheme.colors.colorContentDefault
)

// ❌ WRONG - Don't use Rounded or Filled unless specifically requested
Icon(imageVector = Icons.Rounded.IconName, ...)
Icon(imageVector = Icons.Filled.IconName, ...)
```

#### Icon Guidelines
- ✅ **Default to Outlined**: Always use `Icons.Outlined.*` unless user specifically requests another variant
- ✅ **Import outlined icons**: Add `import androidx.compose.material.icons.outlined.*` when using icons
- ✅ **24dp size**: Default icon size is 24dp (can be adjusted with `Modifier.size()` if needed)
- ✅ **Use WDS colors**: Always tint icons with `WdsTheme.colors` for consistency
- ✅ **Content description**: Always provide descriptive `contentDescription` for accessibility
- ✅ **Browse icons**: Reference [fonts.google.com/icons](https://fonts.google.com/icons) for available icons

#### When to Use Other Variants
- **Filled**: Only when user explicitly requests filled icons or for selected/active states
- **Rounded**: Only when user explicitly requests rounded style
- **Sharp**: Only when user explicitly requests sharp/angular style

### Figma Design Implementation Guidelines
**IMPORTANT**: When implementing designs from Figma references:

#### Custom Icons from Figma
- **Always use exact SVG paths** provided in Figma designs - do NOT improvise or substitute with other icons
- When user provides Figma SVG icons, implement them exactly as provided using `ImageVector.Builder`
- Convert Figma SVG paths directly to Compose paths without modifications
- Preserve exact colors, dimensions, and path data from the original SVG

#### General Figma Guidelines
- Follow Figma UI references as closely as possible for all visual elements
- Maintain exact spacing, padding, and layout specifications from Figma
- When in doubt about any design element, ask for clarification rather than making assumptions

# WhatsApp Android Prototype Template - LLM Rules

## Project Overview
This is an Android prototype template built with Jetpack Compose and the WhatsApp Design System (WDS). It provides a foundation for rapid prototyping with a complete design system including colors, typography, spacing, corner radius tokens, and reusable components.





## Summary Checklist

When writing new code, ensure:
- ✅ All colors use `WdsTheme.colors` semantic colors
- ✅ All text uses `WdsTheme.typography` styles
- ✅ All spacing uses `WdsTheme.dimensions` tokens
- ✅ All corner radius uses `WdsTheme.shapes` tokens
- ✅ All icons use `Icons.Outlined.*` variant by default (FILL 0, wght 400, GRAD 0, opsz 24)
- ✅ WDS components are used instead of Material3 when available
- ✅ Theme properties are cached in local variables
- ✅ Static data is wrapped in `remember`
- ✅ Imports are organized and explicit
- ✅ Preview functions are included
- ✅ Code follows the established patterns

## Additional Resources

- Design system documentation: `app/src/main/java/com/example/chatapp/ui/theme/wds/README.md`
- Example screens: `ColorsScreen.kt`, `TypeScreen.kt`, `ComponentsScreen.kt`
- Main activity pattern: `MainActivity.kt`
