package com.example.chatapp.navigation

sealed class Screen(val route: String) {
    // Main tab screens
    data object ChatList : Screen("chat_list")
    data object Calls : Screen("calls")
    data object Updates : Screen("updates")
    data object Tools : Screen("tools")
    
    // Feature screens
    data object NewChat : Screen("new_chat")
    data object NewBusinessBroadcast : Screen("new_business_broadcast")
    data object SelectContact : Screen("select_contact")
    
    data object BroadcastChat : Screen("broadcast_chat")
    data object BroadcastDraft : Screen("broadcast_draft")
    data object BroadcastReview : Screen("broadcast_review")

    // Design system screens
    data object DesignSystemLibrary : Screen("design_system_library")
    data object Colors : Screen("colors")
    data object Type : Screen("type")
    data object Components : Screen("components")
    data object Icons : Screen("icons")
}
