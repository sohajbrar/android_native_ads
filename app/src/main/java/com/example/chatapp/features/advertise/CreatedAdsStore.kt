package com.example.chatapp.features.advertise

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Singleton

data class CreatedAd(
    val id: String,
    val adName: String,
    val mediaUri: String?,
    val audienceLocation: String,
    val audienceAgeRange: String,
    val audienceGender: String,
    val dailyBudget: Int,
    val durationDays: Int,
    val totalBudget: Int,
    val createdAt: Long = System.currentTimeMillis()
)

@Singleton
class CreatedAdsStore @Inject constructor() {
    val ads = mutableListOf<CreatedAd>().toMutableStateList()
    var pendingSnackbar by mutableStateOf(false)
    var selectedAd by mutableStateOf<AdItem?>(null)

    fun addAd(ad: CreatedAd) {
        ads.add(0, ad)
        pendingSnackbar = true
    }

    fun onSnackbarShown() {
        pendingSnackbar = false
    }
}

@HiltViewModel
class ManageAdsViewModel @Inject constructor(
    val store: CreatedAdsStore
) : ViewModel()
