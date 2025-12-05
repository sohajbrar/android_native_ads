package com.example.chatapp.navigation

sealed class Screen(val route: String) {
    // Main tab screens
    data object ChatList : Screen("chat_list")
    data object Calls : Screen("calls")
    data object Updates : Screen("updates")
    data object Tools : Screen("tools")
    
    // Design system screens
    data object DesignSystemLibrary : Screen("design_system_library")
    data object Colors : Screen("colors")
    data object Type : Screen("type")
    data object Components : Screen("components")
    data object Icons : Screen("icons")
}
