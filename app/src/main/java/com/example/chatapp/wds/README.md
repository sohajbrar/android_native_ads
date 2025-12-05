# WDS (WhatsApp Design System)

This directory contains the complete WDS design system ported from the WhatsApp Design System. The design system provides a comprehensive set of design tokens including colors, typography, spacing, shapes, and components that automatically adapt to light and dark modes.

## Table of Contents
- [Colors](#colors)
- [Typography](#typography)
- [Spacing](#spacing)
- [Shapes (Corner Radius)](#shapes-corner-radius)
- [Components](#components)
- [Usage](#usage)

---

## Colors

The color system is organized into three layers:

### 1. Base Colors (`BaseColors.kt`)
Primitive color tokens that form the foundation of the design system. These are raw color values organized by color families:

- **Black/White**: Alpha variations and opaque values
- **Grays**: Cool Gray, Warm Gray, Neutral Gray (50-1000 scale)
- **Brand Colors**: Green (WhatsApp brand), Emerald
- **UI Colors**: Cobalt, Sky Blue, Teal, Cream
- **Semantic Colors**: Red (negative), Yellow (warning), Orange, Pink, Purple, Brown

Example base colors:
```kotlin
BaseColors.wdsGreen500      // #1DAA61
BaseColors.wdsCoolGray600   // #5B6368
BaseColors.wdsRed400        // #EA0038
```

### 2. Semantic Color Scheme (`WdsColorScheme.kt`)
Abstract class defining semantic color tokens. These tokens have meaningful names that describe their purpose rather than their appearance:

**Content Colors:**
- `colorContentDefault` - Primary text color
- `colorContentDeemphasized` - Secondary/subtle text
- `colorContentDisabled` - Disabled state text
- `colorContentInverse` - Text on dark backgrounds
- `colorContentOnAccent` - Text on accent color backgrounds
- `colorContentActionEmphasized` - Action links

**Surface Colors:**
- `colorSurfaceDefault` - Default background
- `colorSurfaceElevatedDefault` - Elevated surfaces (cards, sheets)
- `colorSurfaceEmphasized` - Emphasized backgrounds
- `colorSurfacePressed` - Pressed state
- `colorSurfaceHighlight` - Hover/highlight state

**Accent Colors:**
- `colorAccent` - Primary brand color
- `colorAccentDeemphasized` - Subtle accent
- `colorAccentEmphasized` - Strong accent

**Feedback Colors:**
- `colorPositive` - Success states
- `colorNegative` - Error states
- `colorWarning` - Warning states

**Divider & Outline Colors:**
- `colorDivider` - Standard dividers
- `colorOutlineDeemphasized` - Subtle borders

**Chat-Specific Colors:**
- `colorBubbleSurfaceOutgoing` - Sent message bubbles
- `colorBubbleSurfaceIncoming` - Received message bubbles
- `colorChatBackgroundWallpaper` - Chat background

### 3. Light & Dark Implementations
- **`WdsSemanticLightColors.kt`**: Light mode color mappings
- **`WdsSemanticDarkColors.kt`**: Dark mode color mappings

---

## Typography

The typography system (`WdsTypography.kt`) provides a comprehensive set of text styles organized by size and purpose.

### Typography Hierarchy

**Large Titles:**
- `largeTitle1` - 32sp / Hero titles, main navigation
- `largeTitle2` - 28sp / Section headers

**Headlines:**
- `headline1` - 24sp / Page titles
- `headline2` - 22sp / Section titles

**Body Text:**
- `body1` - 16sp / Primary body text
- `body1Emphasized` - 16sp / Bold body text
- `body2` - 14sp / Secondary body text
- `body2Emphasized` - 14sp / Bold secondary text
- `body3` - 12sp / Tertiary/caption text
- `body3Emphasized` - 12sp / Bold caption text

**Chat-Specific:**
- `chatBody1` - 16sp / Chat message text
- `chatBody1Emphasized` - 16sp / Bold chat text
- `chatBody2` - 14sp / Chat metadata
- `chatBody2Emphasized` - 14sp / Bold chat metadata

### Usage Example
```kotlin
Text(
    text = "Headline",
    style = WdsTheme.typography.headline1
)

Text(
    text = "Body text",
    style = WdsTheme.typography.body1
)
```

---

## Spacing

The spacing system (`WdsDimensions.kt`) provides consistent spacing tokens for layouts.

### Spacing Scale
| Token | Value | Usage |
|-------|-------|-------|
| `wdsSizeZero` | 0dp | No spacing |
| `wdsSpacingQuarter` | 2dp | Minimal spacing |
| `wdsSpacingHalf` | 4dp | Tight spacing |
| `wdsSpacingHalfPlus` | 6dp | Small spacing |
| `wdsSpacingSingle` | 8dp | Base unit |
| `wdsSpacingSinglePlus` | 12dp | Medium spacing |
| `wdsSpacingDouble` | 16dp | Standard spacing |
| `wdsSpacingDoublePlus` | 20dp | Large spacing |
| `wdsSpacingTriple` | 24dp | Extra large spacing |
| `wdsSpacingTriplePlus` | 28dp | Extra extra large |
| `wdsSpacingQuad` | 32dp | Huge spacing |
| `wdsSpacingQuint` | 40dp | Maximum spacing |

### Usage Example
```kotlin
Column(
    modifier = Modifier.padding(WdsTheme.dimensions.wdsSpacingDouble)
) {
    Text("Item 1")
    Spacer(modifier = Modifier.height(WdsTheme.dimensions.wdsSpacingSingle))
    Text("Item 2")
}
```

---

## Shapes (Corner Radius)

The shapes system (`WdsShapes.kt`) provides consistent corner radius tokens.

### Corner Radius Scale
| Token | Value | Usage |
|-------|-------|-------|
| `none` | 0dp | Sharp corners |
| `halfPlus` | 6dp | Subtle rounding |
| `single` | 8dp | Standard rounding |
| `singlePlus` | 12dp | Medium rounding |
| `double` | 16dp | Large rounding |
| `triple` | 24dp | Extra large rounding |
| `triplePlus` | 28dp | Extra extra large |
| `circle` | 100dp | Fully rounded (pill shape) |

### Usage Example
```kotlin
Box(
    modifier = Modifier
        .clip(WdsTheme.shapes.single)
        .background(WdsTheme.colors.colorSurfaceElevatedDefault)
) {
    Text("Rounded box")
}

Button(
    onClick = { },
    shape = WdsTheme.shapes.circle
) {
    Text("Pill Button")
}
```

---

## Components

The WDS component library provides pre-built, accessible components that follow the design system.

### Available Components

#### WDSButton
A versatile button component with multiple variants, actions, and sizes.

**Variants:**
- `FILLED` - Solid background (most prominent)
- `TONAL` - Tinted background (medium prominence)
- `OUTLINE` - Transparent with border (low prominence)
- `BORDERLESS` - Transparent, no border (lowest prominence)

**Actions:**
- `NORMAL` - Default action
- `DESTRUCTIVE` - Negative actions (delete, remove)
- `MEDIA` - Media-related actions

**Sizes:**
- `SMALL` - 32dp height
- `NORMAL` - 40dp height
- `LARGE` - 48dp height

**Example:**
```kotlin
WDSButton(
    onClick = { },
    text = "Submit",
    variant = WDSButtonVariant.FILLED,
    action = WDSButtonAction.NORMAL,
    size = WDSButtonSize.NORMAL
)

// With icon
WDSButton(
    onClick = { },
    text = "Add",
    icon = Icons.Default.Add,
    variant = WDSButtonVariant.TONAL
)

// Icon only (round)
WDSButton(
    onClick = { },
    icon = Icons.Default.Favorite,
    variant = WDSButtonVariant.FILLED
)
```

#### WDSChip
Filter and input chips for selections and tags.

**Actions:**
- `DEFAULT` - Standard filter chip
- `INPUT` - Input tag (shows close when selected)
- `CLOSE` - Always shows close icon
- `DROPDOWN` - Shows dropdown arrow

**Sizes:**
- `DEFAULT` - 32dp height
- `LARGE` - 40dp height

**Example:**
```kotlin
WDSChip(
    text = "Filter",
    selected = isSelected,
    onClick = { isSelected = !isSelected },
    action = WDSChipAction.DEFAULT
)

// With icon and badge
WDSChip(
    text = "Photos",
    selected = true,
    onClick = { },
    icon = Icons.Default.Image,
    badgeText = "12"
)
```

#### WDSTextField
Single and multi-line text input fields.

**Example:**
```kotlin
// Single line
WDSSingleLineTextField(
    value = text,
    onValueChange = { text = it },
    label = "Username",
    placeholder = "Enter username"
)

// Multi-line
WDSMultiLineTextField(
    value = text,
    onValueChange = { text = it },
    label = "Description",
    minLines = 3,
    maxLines = 5
)
```

#### WDSFab
Floating action button for primary actions.

**Styles:**
- `PRIMARY` - Accent color
- `SECONDARY` - Surface color

**Sizes:**
- `SMALL` - 40dp
- `NORMAL` - 56dp
- `LARGE` - 96dp

**Example:**
```kotlin
WDSFab(
    onClick = { },
    icon = Icons.Default.Add,
    size = WDSFabSize.NORMAL,
    style = WDSFabStyle.PRIMARY,
    contentDescription = "Add"
)
```

#### Selection Controls

**WdsCheckbox:**
```kotlin
WdsCheckbox(
    checked = isChecked,
    onCheckedChange = { isChecked = it }
)
```

**WdsRadioButton:**
```kotlin
WdsRadioButton(
    selected = isSelected,
    onClick = { isSelected = true }
)
```

**WdsSwitch:**
```kotlin
WdsSwitch(
    checked = isEnabled,
    onCheckedChange = { isEnabled = it }
)
```

#### WdsDialog
Modal dialog with title, message, and actions.

**Example:**
```kotlin
WdsDialog(
    title = "Confirm Action",
    message = "Are you sure you want to proceed?",
    icon = Icons.Default.Info,
    positiveButton = WdsDialogButton(
        text = "OK",
        onClick = { }
    ),
    negativeButton = WdsDialogButton(
        text = "Cancel",
        onClick = { }
    ),
    onDismissRequest = { }
)
```

#### WDSDivider
Horizontal divider line.

**Example:**
```kotlin
WDSDivider()

// With insets
WDSDivider(
    startIndent = 16.dp,
    endIndent = 16.dp
)
```

---

## Usage

### Basic Setup

Wrap your app content with `WdsTheme`:

```kotlin
@Composable
fun MyApp() {
    WdsTheme {
        // Your app content
        MyScreen()
    }
}
```

### Accessing Design Tokens

```kotlin
@Composable
fun MyComponent() {
    // Colors
    val textColor = WdsTheme.colors.colorContentDefault
    val accentColor = WdsTheme.colors.colorAccent
    
    // Typography
    val headlineStyle = WdsTheme.typography.headline1
    val bodyStyle = WdsTheme.typography.body1
    
    // Spacing
    val padding = WdsTheme.dimensions.wdsSpacingDouble
    val margin = WdsTheme.dimensions.wdsSpacingSingle
    
    // Shapes
    val cornerShape = WdsTheme.shapes.single
    val pillShape = WdsTheme.shapes.circle
    
    Column(
        modifier = Modifier.padding(padding)
    ) {
        Text(
            text = "Title",
            style = headlineStyle,
            color = textColor
        )
        
        Box(
            modifier = Modifier
                .clip(cornerShape)
                .background(accentColor)
                .padding(margin)
        ) {
            Text(
                text = "Content",
                style = bodyStyle,
                color = WdsTheme.colors.colorContentOnAccent
            )
        }
    }
}
```

### Force Light or Dark Mode

```kotlin
WdsTheme(darkTheme = false) {
    // Always light mode
}

WdsTheme(darkTheme = true) {
    // Always dark mode
}
```

### Custom Theme

```kotlin
val customColors = object : WdsSemanticLightColors() {
    override val colorAccent = Color(0xFF00FF00)
}

val customTypography = WdsTypography(
    headline1 = TextStyle(fontSize = 28.sp)
)

WdsTheme(
    colors = customColors,
    typography = customTypography
) {
    // Uses custom theme
}
```

---

## Best Practices

1. **Always use design tokens** - Never hardcode colors, sizes, or spacing
2. **Use semantic names** - Tokens make code self-documenting
3. **Test both themes** - Always preview components in light and dark modes
4. **Maintain consistency** - Use the same spacing/sizing patterns throughout
5. **Accessibility first** - Ensure text meets WCAG contrast standards
6. **Component composition** - Build complex UIs from simple WDS components

---

## Examples

### Complete Form
```kotlin
Column(
    modifier = Modifier
        .fillMaxWidth()
        .padding(WdsTheme.dimensions.wdsSpacingDouble),
    verticalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingDouble)
) {
    Text(
        text = "Sign Up",
        style = WdsTheme.typography.headline1,
        color = WdsTheme.colors.colorContentDefault
    )
    
    WDSSingleLineTextField(
        value = username,
        onValueChange = { username = it },
        label = "Username"
    )
    
    WDSSingleLineTextField(
        value = email,
        onValueChange = { email = it },
        label = "Email"
    )
    
    Row(
        horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingSingle)
    ) {
        WdsCheckbox(
            checked = agreeToTerms,
            onCheckedChange = { agreeToTerms = it }
        )
        Text(
            text = "I agree to the terms",
            style = WdsTheme.typography.body2,
            color = WdsTheme.colors.colorContentDefault
        )
    }
    
    WDSButton(
        onClick = { },
        text = "Create Account",
        variant = WDSButtonVariant.FILLED,
        modifier = Modifier.fillMaxWidth()
    )
}
```

### Filter Chips
```kotlin
Row(
    horizontalArrangement = Arrangement.spacedBy(WdsTheme.dimensions.wdsSpacingHalf)
) {
    WDSChip(
        text = "All",
        selected = filter == 0,
        onClick = { filter = 0 }
    )
    WDSChip(
        text = "Photos",
        selected = filter == 1,
        onClick = { filter = 1 },
        icon = Icons.Default.Image
    )
    WDSChip(
        text = "Videos",
        selected = filter == 2,
        onClick = { filter = 2 },
        badgeText = "12"
    )
}
```

---

## Previews

The system includes preview support:

```kotlin
@Preview(name = "Light Mode")
@Composable
fun MyComponentPreviewLight() {
    WdsTheme(darkTheme = false) {
        MyComponent()
    }
}

@Preview(name = "Dark Mode")
@Composable
fun MyComponentPreviewDark() {
    WdsTheme(darkTheme = true) {
        MyComponent()
    }
}
```

---

## Design System Library

The app includes a built-in Design System Library screen accessible via the gear icon. This interactive showcase demonstrates:

- **Colors Screen**: All semantic and base colors
- **Typography Screen**: All text styles with examples
- **Components Screen**: All components with variants and states

Use this as a reference and testing ground for the design system.