# WhatsApp Business — Native Ads Prototype

A high-fidelity Android prototype for the WhatsApp Business **Ad Creation** and **Manage Ads** flows, built with Jetpack Compose and the WhatsApp Design System (WDS). This prototype covers the complete end-to-end journey — from media selection and ad design through audience targeting, budget configuration, ad review, and post-creation ad management with performance tracking.

## Key Flows

### 1. Ad Creation Flow

A multi-step stepper flow with animated progress bar, accessible from the Tools tab or Manage Ads screen:

**Media Selection** → **Design Ad** → **Audience** → **Budget & Duration** → **Review & Create**

- **Media Selection** — Choose from catalog items, business profile, status updates, gallery photos/videos, device camera, or product catalog
- **Design Ad** — Preview the ad creative with selected media, edit call-to-action text
- **Preview Ad** — Full Instagram and Facebook ad preview with selected media
- **Audience** — Select suggested audience or saved custom audiences, with Advantage+ audience support
- **Budget & Duration** — Set daily budget with slider, choose between continuous or fixed-duration campaigns
- **Review Ad** — Summary of all settings with inline editing, final "Create ad" action with slide-down dismiss animation

### 2. Audience Targeting

- **Suggested audience** with Advantage+ audience toggle and details card (location, minimum age)
- **Create new audience** — Location, interests, age range slider, gender chips, Advantage+ toggle
- **Edit audience** — Pre-populated fields for both default and saved audiences
- **Edit Location** — Regional mode (search and select cities/countries with chips) and Local mode (map placeholder with radius slider and "Use my location")
- **Edit Interests** — Browse categories (Business, Entertainment, Fitness, etc.), search across all interests, selected/suggested sections

### 3. Manage Ads

Accessed from the Tools tab, this screen provides a dashboard for all ads:

- **Metrics overview** — Total reach, amount spent, conversations started
- **For you** — Horizontally scrollable recommendation cards with ad performance insights
- **Your ads** — List of all ads with status (Active, Paused, Completed, In Review), thumbnails, metrics per row
- **Ad Details** — Full ad summary with preview, audience, budget, and performance breakdown
- **Ad Performance** — Tabular metrics breakdown (reach, impressions, clicks, conversations, cost)
- **Create ad FAB** — Initiates the ad creation flow; newly created ads appear at top with "In review" status and snackbar notification

## Architecture

```
app/src/main/java/com/example/chatapp/
├── features/advertise/                          # Native Ads feature
│   ├── MediaSelectionScreen.kt                  # Media source picker (catalog, gallery, camera, status)
│   ├── MediaPickerBottomSheet.kt                # Gallery media picker with grid, tabs, date grouping
│   ├── ChooseStatusScreen.kt                    # Full-screen WhatsApp status media picker
│   ├── ChooseCatalogScreen.kt                   # Full-screen catalog media picker by category
│   ├── DesignAdScreen.kt                        # Ad creative editor with media preview
│   ├── PreviewAdScreen.kt                       # Instagram & Facebook ad preview tabs
│   ├── AudienceScreen.kt                        # Audience selection with radio options & details cards
│   ├── CreateNewAudienceScreen.kt               # Create/edit audience (location, interests, age, gender)
│   ├── EditLocationScreen.kt                    # Regional & local location editor with search
│   ├── EditInterestsScreen.kt                   # Interest browser with categories, search, suggestions
│   ├── BudgetScreen.kt                          # Daily budget slider & duration options
│   ├── ReviewAdScreen.kt                        # Final review summary with inline edit navigation
│   ├── ManageAdsScreen.kt                       # Ad dashboard with metrics, recommendations, ad list
│   ├── AdDetailsScreen.kt                       # Individual ad details and actions
│   ├── AdPerformanceScreen.kt                   # Tabular performance metrics breakdown
│   ├── AdCreationProgressBar.kt                 # Animated stepper progress bar
│   ├── AdCreationViewModel.kt                   # Shared state across the ad creation flow
│   └── CreatedAdsStore.kt                       # Singleton store for persisting created ads
├── features/                                    # Other feature screens
│   ├── chatlist/                                # Chat list (main tab)
│   ├── chat/                                    # Chat conversation
│   ├── chatinfo/                                # Contact/group info
│   ├── newchat/                                 # New chat creation
│   ├── tools/                                   # Business tools tab
│   ├── broadcast/                               # Business broadcast
│   └── main/                                    # Main view model
├── data/                                        # Data layer
│   ├── local/                                   # Room database, DAOs, entities
│   ├── repository/ChatRepository.kt             # Single source of truth
│   └── generator/ChatDataGenerator.kt           # Sample data
├── navigation/Screen.kt                         # All navigation routes
├── wds/                                         # WhatsApp Design System
│   ├── theme/                                   # Color tokens, business dark theme
│   ├── tokens/                                  # Spacing, shapes, typography
│   └── components/                              # Reusable UI components with tap sounds
└── di/DatabaseModule.kt                         # Hilt DI module
```

### Navigation

The ad creation flow uses a **nested navigation graph** (`advertise_flow`) to scope a shared `AdCreationViewModel` across all stepper screens. Back navigation returns to the correct origin (Tools tab or Manage Ads) based on entry point.

```
ToolsScreen / ManageAdsScreen
    │
    └─ [Create Ad FAB] ──→ MediaSelectionScreen
                               │
                               ├─ [Gallery] ──→ MediaPickerBottomSheet ──→ DesignAdScreen
                               ├─ [Camera] ──→ Device Camera ──→ DesignAdScreen
                               ├─ [Status] ──→ ChooseStatusScreen ──→ DesignAdScreen
                               ├─ [Catalog] ──→ ChooseCatalogScreen ──→ DesignAdScreen
                               └─ [Catalog item / Business] ──→ DesignAdScreen
                                                                    │
                                                                    ├─ [Preview] ──→ PreviewAdScreen
                                                                    └─ [Next] ──→ AudienceScreen
                                                                                      │
                                                                                      ├─ [Create new] ──→ CreateNewAudienceScreen
                                                                                      │                       ├─ [Locations] ──→ EditLocationScreen
                                                                                      │                       └─ [Interests] ──→ EditInterestsScreen
                                                                                      └─ [Next] ──→ BudgetScreen
                                                                                                        │
                                                                                                        └─ [Next] ──→ ReviewAdScreen
                                                                                                                         │
                                                                                                                         └─ [Create ad] ──→ ManageAdsScreen (slide-down dismiss)

ManageAdsScreen
    │
    └─ [Tap ad row] ──→ AdDetailsScreen
                            │
                            ├─ [See details] ──→ AdPerformanceScreen
                            └─ [Ad preview] ──→ PreviewAdScreen (close button)
```

### State Management

| Component | Type | Responsibility |
|-----------|------|----------------|
| `AdCreationViewModel` | HiltViewModel | Shared state for entire ad creation flow — media, audience, budget, duration, progress |
| `CreatedAdsStore` | Singleton | Persists created ads across navigation, tracks selected ad for details |
| `ManageAdsViewModel` | HiltViewModel | Provides access to `CreatedAdsStore` for Manage Ads screens |

### Key Design Decisions

- **WhatsApp Business dark theme** — Custom `WdsSemanticBusinessDarkColors` with white/light accent colors instead of green, matching the real WhatsApp Business app
- **System tap sounds** — Global `clickableWithSound` modifier applied to all interactive elements throughout the app
- **Card border styling** — Centralized `wdsCardBorder()` component with 30% opacity for consistent, subtle card borders
- **Tab transitions** — No animation between main Chats/Tools tabs for seamless switching
- **Progress bar animation** — Animates forward on "Next" and backward on "Back", tracking state in ViewModel

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

Alternatively, download the latest APK from the [GitHub Actions](https://github.com/sohajbrar/android_native_ads/actions) build artifacts.

### Vibe Coding with AI

1. Open the project folder in **Cursor**
2. The AI assistant will follow WDS rules via `CLAUDE.md`
3. Prompt Cursor to make changes
4. Return to Android Studio and rebuild

## License

This is a prototype project for internal testing and design iteration.

---

Built with Cursor
