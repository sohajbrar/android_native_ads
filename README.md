# WA Business Broadcast

A WhatsApp Business Android sandbox built with Jetpack Compose — a high-fidelity prototype for the Business Broadcast feature, built on top of [Somya's WhatsApp Business vibe coded templates](https://drive.google.com/drive/folders/1P6NuF5AqXPFO-NG3v1-8V9rAp_lbyCaH). Thanks Somya!

## Overview

WA Business Broadcast is a production-quality prototype built with Jetpack Compose and WDS (WhatsApp Design System). It implements the end-to-end Business Broadcast flow — from audience creation and recipient selection through message composition, review, and sending.

### Key Flows

**1. Create New Business Broadcast**

`+` FAB → **New business broadcast** → **New audiences** → Select recipients → Compose message → Review & send

**2. Broadcast Home**

Tools tab → **Business broadcasts** → View sent broadcasts, audiences, and message credits

## Architecture

```
app/src/main/java/com/example/chatapp/
├── features/broadcast/                        # Business Broadcast feature
│   ├── BroadcastHomeScreen.kt                 # Landing page: credits, broadcasts & audiences tabs
│   ├── BroadcastHomeViewModel.kt              # Loads broadcast conversations & sent messages
│   ├── NewBusinessBroadcastScreen.kt          # Audience selection entry (new/suggested/existing)
│   ├── SelectRecipientsScreen.kt              # Recipient picker (contact lists & individual contacts)
│   ├── BroadcastChatScreen.kt                 # Broadcast conversation view with composer
│   ├── BroadcastDraftScreen.kt                # Message preview/draft with optional CTA button
│   ├── BroadcastReviewScreen.kt               # Final review: credits, cost, disclaimers, send/schedule
│   ├── BroadcastInfoScreen.kt                 # Broadcast details: recipients, linked lists, delete
│   ├── BroadcastViewModel.kt                  # Chat screen ViewModel (message persistence)
│   └── BroadcastInfoViewModel.kt              # Info screen ViewModel (conversation & participants)
├── features/                                  # Other feature screens
│   ├── chatlist/                              # Chat list (main tab)
│   ├── chat/                                  # Chat conversation
│   ├── chatinfo/                              # Contact/group info
│   ├── newchat/                               # New chat creation
│   ├── tools/                                 # Business tools
│   └── main/                                  # Main view model
├── data/                                      # Data layer
│   ├── local/                                 # Room database, DAOs, entities, converters
│   ├── repository/ChatRepository.kt           # Single source of truth
│   ├── generator/ChatDataGenerator.kt         # Sample data
│   └── initializer/DatabaseInitializer.kt     # DB setup
├── navigation/Screen.kt                       # Navigation routes
├── wds/                                       # WhatsApp Design System
│   ├── theme/                                 # Color tokens & themes
│   ├── tokens/                                # Spacing, shapes, typography
│   └── components/                            # Reusable UI components
└── di/DatabaseModule.kt                       # Hilt DI module
```

### Broadcast Flow

```
BroadcastHomeScreen
    │
    ├─ [FAB +] ──→ NewBusinessBroadcastScreen
    │                   │
    │                   └─ [New audience] ──→ SelectRecipientsScreen
    │                                             │
    │                                             └─ [Next] ──→ BroadcastChatScreen
    │                                                              │
    │                                                              ├─ [Compose → Send] ──→ BroadcastDraftScreen
    │                                                              │                            │
    │                                                              │                            └─ [Next] ──→ BroadcastReviewScreen
    │                                                              │                                              │
    │                                                              │                                              └─ [Send now] ──→ BroadcastChatScreen
    │                                                              │
    │                                                              └─ [Header tap] ──→ BroadcastInfoScreen
    │
    └─ [Tap broadcast] ──→ BroadcastChatScreen (existing conversation)
```

### ViewModels

| ViewModel | Scope | Responsibility |
|-----------|-------|----------------|
| `BroadcastHomeViewModel` | Home screen | Loads broadcast conversations and sent messages from repository |
| `BroadcastViewModel` | Chat screen | Manages message persistence via `SavedStateHandle` conversation ID |
| `BroadcastInfoViewModel` | Info screen | Loads conversation details, participants, and handles deletion |

`SelectRecipientsScreen` reuses `NewChatViewModel` from the `newchat` feature for contact access and broadcast conversation creation.

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

## Key Components

### Broadcast Screens
- `BroadcastHomeScreen` — Credits progress, broadcasts tab, audiences tab, FAB
- `NewBusinessBroadcastScreen` — New/suggested/existing audience selection
- `SelectRecipientsScreen` — Contact list and individual contact picker with selection chips
- `BroadcastChatScreen` — Conversation view with system messages and composer
- `BroadcastDraftScreen` — Message preview with optional CTA button
- `BroadcastReviewScreen` — Credits summary, cost, legal disclaimers, send/schedule actions
- `BroadcastInfoScreen` — Audience details, recipients list, linked lists, delete action

### WDS Components
- `WDSTopBar` — Top app bar with title and actions
- `WDSBottomBar` — Bottom tab navigation
- `WDSButton` — Multi-variant (Filled, Tonal, Outline, Borderless)
- `WDSChip` — Filter and input chips
- `WDSTextField` — Single-line and multi-line text fields
- `WDSSearchBar` — Search input bar
- `WDSFab` — Floating action button
- `WDSTabRow` — Horizontal tab row with indicator
- `WDSToast` — Slide-up toast notification with auto-dismiss
- `WDSChatListItem` — Chat list row
- `WDSContentRow` — Generic content row
- `WDSListRow` — Standard list row
- `WDSSystemMessage` — System message bubble
- `WDSDivider` / `WDSSectionDivider` — Dividers
- `WDSBottomSheet` — Bottom sheet
- `WDSContextMenu` — Popup menu
- `WdsCheckbox` / `WdsRadioButton` / `WdsSwitch` — Selection controls
- `WdsDialog` / `WdsComingSoonDialog` — Modal dialogs

## License

This is a prototype project for internal testing and design iteration.

---

Built with Cursor
