package com.example.chatapp.features.updates

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.Color

object LikedStatuses {
    private val likedMap = mutableStateMapOf<String, Boolean>()

    fun isLiked(mediaUrl: String): Boolean = likedMap[mediaUrl] == true

    fun toggle(mediaUrl: String) {
        likedMap[mediaUrl] = !(likedMap[mediaUrl] ?: false)
    }
}

data class StatusMediaItem(
    val imageUrl: String,
    val timeAgo: String,
    val viewCount: Int = 0,
    val hasLikedView: Boolean = false
)

data class StatusItem(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val isMyStatus: Boolean = false,
    val isViewed: Boolean = false,
    val isHidden: Boolean = false,
    val media: List<StatusMediaItem> = emptyList()
) {
    val previewImageUrl: String? get() = media.firstOrNull()?.imageUrl
}

data class ChannelItem(
    val id: String,
    val name: String,
    val avatarData: Any,
    val avatarColor: Color = Color.Gray,
    val isVerified: Boolean = false,
    val lastMessage: String = "",
    val time: String = "",
    val unreadCount: Int = 0
)

data class RecommendedChannelItem(
    val id: String,
    val name: String,
    val avatarData: Any,
    val avatarColor: Color = Color.Gray,
    val isVerified: Boolean = false,
    val followerCount: String = ""
)

object StatusData {
    val statuses: List<StatusItem> = listOf(
        StatusItem("my", "Anika Chavan", avatarUrl = "android.resource://com.example.chatapp/drawable/avatar_my_status", isMyStatus = true, media = listOf(
            StatusMediaItem("https://images.unsplash.com/photo-1459411552884-841db9b3cc2a?w=720&h=1280&fit=crop", "Just now", viewCount = 5),
            StatusMediaItem("https://images.unsplash.com/photo-1463936575829-25148e1db1b8?w=720&h=960&fit=crop", "1hr ago", viewCount = 10, hasLikedView = true),
            StatusMediaItem("https://images.unsplash.com/photo-1545241047-6083a3684587?w=720&h=1280&fit=crop", "3hr ago", viewCount = 18),
            StatusMediaItem("https://images.unsplash.com/photo-1509423350716-97f9360b4e09?w=720&h=1280&fit=crop", "5hr ago", viewCount = 24, hasLikedView = true),
            StatusMediaItem("https://images.unsplash.com/photo-1416879595882-3373a0480b5b?w=720&h=1280&fit=crop", "8hr ago", viewCount = 31),
        )),
        StatusItem("1", "Maria Torres", avatarUrl = "https://i.pravatar.cc/200?img=32", media = listOf(
            StatusMediaItem("https://picsum.photos/seed/torres1/720/1280", "5 mins ago"),
            StatusMediaItem("https://picsum.photos/seed/torres2/720/960", "18 mins ago"),
            StatusMediaItem("https://picsum.photos/seed/torres3/720/1280", "1 hour ago"),
        )),
        StatusItem("2", "Sofia Hidalgo", avatarUrl = "https://i.pravatar.cc/200?img=25", media = listOf(
            StatusMediaItem("https://picsum.photos/seed/hidalgo1/720/960", "12 mins ago"),
        )),
        StatusItem("3", "Pablo Morales", avatarUrl = "https://i.pravatar.cc/200?img=12", media = listOf(
            StatusMediaItem("https://picsum.photos/seed/morales1/720/1280", "25 mins ago"),
            StatusMediaItem("https://picsum.photos/seed/morales2/720/1280", "40 mins ago"),
        )),
        StatusItem("4", "Dario De Luca", avatarUrl = "https://i.pravatar.cc/200?img=53", media = listOf(
            StatusMediaItem("https://picsum.photos/seed/deluca1/720/960", "30 mins ago"),
        )),
        StatusItem("5", "Ayesha Pawar", avatarUrl = "https://i.pravatar.cc/200?img=44", media = listOf(
            StatusMediaItem("https://picsum.photos/seed/ayesha1/720/1280", "45 mins ago"),
            StatusMediaItem("https://picsum.photos/seed/ayesha2/720/540", "2 hours ago"),
            StatusMediaItem("https://picsum.photos/seed/ayesha3/720/960", "4 hours ago"),
            StatusMediaItem("https://picsum.photos/seed/ayesha4/720/1280", "6 hours ago"),
        )),
        StatusItem("6", "Kenji Tanaka", avatarUrl = "https://i.pravatar.cc/200?img=11", media = listOf(
            StatusMediaItem("https://picsum.photos/seed/kenji1/720/960", "1 hour ago"),
            StatusMediaItem("https://picsum.photos/seed/kenji2/720/1280", "3 hours ago"),
        )),
        StatusItem("7", "Lena Fischer", avatarUrl = "https://i.pravatar.cc/200?img=5", media = listOf(
            StatusMediaItem("https://picsum.photos/seed/lena1/720/1280", "1 hour ago"),
        )),
        StatusItem("8", "Omar Hassan", avatarUrl = "https://i.pravatar.cc/200?img=68", media = listOf(
            StatusMediaItem("https://picsum.photos/seed/omar1/720/1280", "2 hours ago"),
            StatusMediaItem("https://picsum.photos/seed/omar2/720/960", "5 hours ago"),
        )),
        StatusItem("9", "Priya Sharma", avatarUrl = "https://i.pravatar.cc/200?img=49", media = listOf(
            StatusMediaItem("https://picsum.photos/seed/priya1/720/960", "2 hours ago"),
        )),
        StatusItem("10", "Lucas Martin", avatarUrl = "https://i.pravatar.cc/200?img=60", media = listOf(
            StatusMediaItem("https://picsum.photos/seed/lucas1/720/1280", "3 hours ago"),
            StatusMediaItem("https://picsum.photos/seed/lucas2/720/960", "7 hours ago"),
            StatusMediaItem("https://picsum.photos/seed/lucas3/720/1280", "10 hours ago"),
        )),
        StatusItem("11", "Fatima Al-Rashid", avatarUrl = "https://i.pravatar.cc/200?img=45", media = listOf(
            StatusMediaItem("https://picsum.photos/seed/fatima1/720/540", "3 hours ago"),
        )),
        StatusItem("12", "Jin Woo Park", avatarUrl = "https://i.pravatar.cc/200?img=14", media = listOf(
            StatusMediaItem("https://picsum.photos/seed/jinwoo1/720/1280", "4 hours ago"),
            StatusMediaItem("https://picsum.photos/seed/jinwoo2/720/1280", "8 hours ago"),
        )),
        StatusItem("13", "Elena Popov", avatarUrl = "https://i.pravatar.cc/200?img=9", media = listOf(
            StatusMediaItem("https://picsum.photos/seed/elena1/720/960", "5 hours ago"),
        )),
        StatusItem("14", "Mateo Rivera", avatarUrl = "https://i.pravatar.cc/200?img=15", media = listOf(
            StatusMediaItem("https://picsum.photos/seed/mateo1/720/1280", "6 hours ago"),
            StatusMediaItem("https://picsum.photos/seed/mateo2/720/960", "9 hours ago"),
        )),
        StatusItem("15", "Anya Petrov", avatarUrl = "https://i.pravatar.cc/200?img=26", media = listOf(
            StatusMediaItem("https://picsum.photos/seed/anya1/720/1280", "8 hours ago"),
        )),
        StatusItem("16", "Ravi Patel", avatarUrl = "https://i.pravatar.cc/200?img=57", isViewed = true, media = listOf(
            StatusMediaItem("https://picsum.photos/seed/ravi1/720/960", "10 hours ago"),
        )),
        StatusItem("17", "Chloe Dubois", avatarUrl = "https://i.pravatar.cc/200?img=20", isViewed = true, media = listOf(
            StatusMediaItem("https://picsum.photos/seed/chloe1/720/1280", "12 hours ago"),
            StatusMediaItem("https://picsum.photos/seed/chloe2/720/540", "16 hours ago"),
        )),
        StatusItem("18", "Yuki Nakamura", avatarUrl = "https://i.pravatar.cc/200?img=33", isViewed = true, media = listOf(
            StatusMediaItem("https://picsum.photos/seed/yuki1/720/1280", "18 hours ago"),
        )),
        StatusItem("hidden", "Hidden", avatarUrl = "", isHidden = true)
    )

    val myStatus: StatusItem
        get() = statuses.first { it.isMyStatus }

    val viewableStatuses: List<StatusItem>
        get() = statuses.filter { !it.isMyStatus && !it.isHidden && it.media.isNotEmpty() }

    val allViewableStatuses: List<StatusItem>
        get() = listOf(myStatus) + viewableStatuses

    const val BOOST_AFTER_USERS = 5
}

data class BoostableStatus(
    val businessName: String,
    val businessAvatarUrl: String,
    val creativeImageUrl: String
)

object BoostStatusData {
    val suggestedBoost = BoostableStatus(
        businessName = "Lucky Shrub",
        businessAvatarUrl = "android.resource://com.example.chatapp/drawable/avatar_my_status",
        creativeImageUrl = "https://images.unsplash.com/photo-1459411552884-841db9b3cc2a?w=720&h=1280&fit=crop"
    )
}
