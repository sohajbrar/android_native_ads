package com.example.chatapp.features.newchat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.local.entity.UserEntity
import com.example.chatapp.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewChatUiState())
    val uiState: StateFlow<NewChatUiState> = _uiState.asStateFlow()

    init {
        loadContacts()
    }

    private fun loadContacts() {
        viewModelScope.launch {
            chatRepository.getAllUsers().collect { users ->
                val currentUserId = "user_1"
                val contacts = users
                    .filter { it.userId != currentUserId && !it.avatarUrl.isNullOrEmpty() }
                    .sortedBy { it.displayName.lowercase() }

                _uiState.update { it.copy(contacts = contacts, isLoading = false) }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { state ->
            val filtered = if (query.isEmpty()) {
                state.contacts
            } else {
                state.contacts.filter { user ->
                    user.displayName.contains(query, ignoreCase = true) ||
                        user.username.contains(query, ignoreCase = true)
                }
            }
            state.copy(searchQuery = query, filteredContacts = filtered)
        }
    }
}

data class NewChatUiState(
    val contacts: List<UserEntity> = emptyList(),
    val filteredContacts: List<UserEntity>? = null,
    val searchQuery: String = "",
    val isLoading: Boolean = true
) {
    val displayContacts: List<UserEntity>
        get() = filteredContacts ?: contacts
}
