package com.example.chatapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a customer order/purchase for business tracking.
 * Used to track order history, calculate customer lifetime value, and improve insights.
 */
@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["customerId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ConversationEntity::class,
            parentColumns = ["conversationId"],
            childColumns = ["conversationId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["customerId"]),
        Index(value = ["conversationId"]),
        Index(value = ["orderDate"]),
        Index(value = ["status"])
    ]
)
data class OrderEntity(
    @PrimaryKey
    val orderId: String,
    val customerId: String,
    val conversationId: String? = null, // Optional: link to conversation where order was discussed
    
    // Order details
    val orderDate: Long = System.currentTimeMillis(),
    val orderValue: Double, // Total order amount
    val currency: String = "USD",
    val status: OrderStatus = OrderStatus.COMPLETED,
    
    // Order items (simplified)
    val itemCount: Int = 1,
    val itemsSummary: String? = null, // Brief description of what was ordered
    
    // Payment
    val paymentMethod: String? = null, // e.g., "Card", "Cash", "Bank Transfer"
    val isPaid: Boolean = true,
    
    // Fulfillment
    val fulfillmentDate: Long? = null,
    val deliveryAddress: String? = null,
    val trackingNumber: String? = null,
    
    // Notes
    val orderNotes: String? = null,
    
    // Metadata
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class OrderStatus {
    PENDING,
    CONFIRMED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    COMPLETED,
    CANCELLED,
    REFUNDED
}






