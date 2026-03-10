package com.example.chatapp.features.advertise

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlin.math.roundToInt

data class SavedAudience(
    val id: String,
    val name: String,
    val location: String,
    val interests: String,
    val ageMin: Int,
    val ageMax: Int,
    val gender: String,
    val advantagePlusEnabled: Boolean = true
)

@HiltViewModel
class AdCreationViewModel @Inject constructor(
    private val createdAdsStore: CreatedAdsStore
) : ViewModel() {

    // Budget & Duration
    var budgetSliderPosition by mutableFloatStateOf(0.15f)
    var durationSliderPosition by mutableFloatStateOf(0.21f)
    var isSetDuration by mutableStateOf(false)

    val dailyBudget: Int
        get() = (90 + (20000 - 90) * budgetSliderPosition).roundToInt()

    val durationDays: Int
        get() = (1 + (30 - 1) * durationSliderPosition).roundToInt()

    val totalBudget: Int
        get() = dailyBudget * durationDays

    val estimatedReachLow: String
        get() = formatNumber((totalBudget * 1.5).roundToInt())

    val estimatedReachHigh: String
        get() = formatNumber((totalBudget * 3.9).roundToInt())

    // Audience — default audience state
    var defaultLocation by mutableStateOf("United States")
    var defaultInterests by mutableStateOf("")
    var defaultAgeMin by mutableStateOf(18)
    var defaultAgeMax by mutableStateOf(65)
    var defaultGender by mutableStateOf("All")
    var defaultAdvantagePlusEnabled by mutableStateOf(true)
    var specialAdChecked by mutableStateOf(false)

    // Audience — active selection (used by ReviewAdScreen and flow)
    val audienceLocation: String
        get() = selectedAudience?.location ?: defaultLocation
    val audienceAgeRange: String
        get() {
            val a = selectedAudience
            return if (a != null) "${a.ageMin} - ${a.ageMax}+" else "$defaultAgeMin - $defaultAgeMax+"
        }
    val audienceGender: String
        get() = selectedAudience?.gender ?: defaultGender
    val audienceInterests: String
        get() = selectedAudience?.interests ?: defaultInterests

    // Audience — saved audiences & selection
    val savedAudiences = mutableListOf<SavedAudience>().toMutableStateList()
    var selectedAudienceId by mutableStateOf<String?>(null) // null = Default

    val selectedAudience: SavedAudience?
        get() = savedAudiences.find { it.id == selectedAudienceId }

    // Tracks which audience is currently being edited (null = none, "default" = default, UUID = saved)
    var editingAudienceId by mutableStateOf<String?>(null)

    // Temporary locations being edited in EditLocationScreen
    val editingLocations = mutableListOf<String>().toMutableStateList()

    // Temporary interests being edited in EditInterestsScreen
    val editingInterests = mutableListOf<String>().toMutableStateList()

    fun selectAudience(audience: SavedAudience?) {
        selectedAudienceId = audience?.id
    }

    fun saveNewAudience(audience: SavedAudience) {
        savedAudiences.add(audience)
        selectAudience(audience)
    }

    fun updateDefaultAudience(
        location: String,
        interests: String,
        ageMin: Int,
        ageMax: Int,
        gender: String,
        advantagePlusEnabled: Boolean = true
    ) {
        defaultLocation = location
        defaultInterests = interests
        defaultAgeMin = ageMin
        defaultAgeMax = ageMax
        defaultGender = gender
        defaultAdvantagePlusEnabled = advantagePlusEnabled
        selectedAudienceId = null
    }

    fun updateSavedAudience(updated: SavedAudience) {
        val idx = savedAudiences.indexOfFirst { it.id == updated.id }
        if (idx >= 0) {
            savedAudiences[idx] = updated
        }
    }

    // Media
    var selectedMediaUri by mutableStateOf<String?>(null)

    // Progress bar state (tracks last displayed progress for back-navigation animation)
    var currentProgress by mutableFloatStateOf(0f)

    fun createAd() {
        createdAdsStore.addAd(
            CreatedAd(
                id = UUID.randomUUID().toString(),
                adName = "New Ad",
                mediaUri = selectedMediaUri,
                audienceLocation = audienceLocation,
                audienceAgeRange = audienceAgeRange,
                audienceGender = audienceGender,
                dailyBudget = dailyBudget,
                durationDays = durationDays,
                totalBudget = totalBudget
            )
        )
    }
}

internal fun formatNumber(number: Int): String = when {
    number >= 1000 -> {
        val thousands = number / 1000.0
        if (thousands == thousands.toLong().toDouble()) "${thousands.toLong()}K"
        else "${"%.1f".format(thousands)}K"
    }
    else -> number.toString()
}
