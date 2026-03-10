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
    data object SelectRecipients : Screen("select_recipients")
    
    data object BroadcastHome : Screen("broadcast_home")
    data object BroadcastChat : Screen("broadcast_chat")
    data object BroadcastInfo : Screen("broadcast_info")
    data object BroadcastDraft : Screen("broadcast_draft")
    data object BroadcastReview : Screen("broadcast_review")
    data object MessageDetails : Screen("message_details")

    // Advertise flow screens
    data object AdvertiseMediaSelection : Screen("advertise_media_selection")
    data object AdvertiseDesignAd : Screen("advertise_design_ad")
    data object AdvertiseAudience : Screen("advertise_audience")
    data object AdvertiseBudget : Screen("advertise_budget")
    data object AdvertiseReviewAd : Screen("advertise_review_ad")
    data object AdvertisePreview : Screen("advertise_preview")
    data object CreateNewAudience : Screen("create_new_audience")
    data object EditLocation : Screen("edit_location")
    data object EditInterests : Screen("edit_interests")
    data object ChooseStatus : Screen("choose_status")
    data object ChooseCatalog : Screen("choose_catalog")

    // Review-mode screens (opened from ReviewAdScreen, dismissible with X)
    data object ReviewDesignAd : Screen("review_design_ad")
    data object ReviewAudience : Screen("review_audience")
    data object ReviewBudget : Screen("review_budget")

    data object ManageAds : Screen("manage_ads")
    data object AdDetails : Screen("ad_details")
    data object AdPreviewFromDetails : Screen("ad_preview_from_details")
    data object AdPerformance : Screen("ad_performance")

    // Design system screens
    data object DesignSystemLibrary : Screen("design_system_library")
    data object Colors : Screen("colors")
    data object Type : Screen("type")
    data object Components : Screen("components")
    data object Icons : Screen("icons")
}
