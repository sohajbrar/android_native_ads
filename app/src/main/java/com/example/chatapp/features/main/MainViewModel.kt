package com.example.chatapp.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.initializer.DatabaseInitializer
import com.example.chatapp.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val databaseInitializer: DatabaseInitializer
) : ViewModel() {
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _isDatabaseInitialized = MutableStateFlow(false)
    val isDatabaseInitialized: StateFlow<Boolean> = _isDatabaseInitialized.asStateFlow()
    
    val conversations = chatRepository.getAllConversations()
    val users = chatRepository.getAllUsers()
    
    init {
        initializeDatabase()
    }
    
    private fun initializeDatabase() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                databaseInitializer.initializeDatabase()
                _isDatabaseInitialized.value = true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun resetDatabase() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                databaseInitializer.resetAndRepopulateDatabase()
                _isDatabaseInitialized.value = true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
