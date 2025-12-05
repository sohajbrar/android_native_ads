package com.example.chatapp.data.local.dao

import androidx.room.*
import com.example.chatapp.data.local.entity.OrderEntity
import com.example.chatapp.data.local.entity.OrderStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders: List<OrderEntity>)
    
    @Update
    suspend fun updateOrder(order: OrderEntity)
    
    @Delete
    suspend fun deleteOrder(order: OrderEntity)
    
    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    suspend fun getOrderById(orderId: String): OrderEntity?
    
    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    fun getOrderByIdFlow(orderId: String): Flow<OrderEntity?>
    
    @Query("SELECT * FROM orders WHERE customerId = :customerId ORDER BY orderDate DESC")
    fun getOrdersByCustomer(customerId: String): Flow<List<OrderEntity>>
    
    @Query("SELECT * FROM orders WHERE conversationId = :conversationId ORDER BY orderDate DESC")
    fun getOrdersByConversation(conversationId: String): Flow<List<OrderEntity>>
    
    @Query("SELECT * FROM orders ORDER BY orderDate DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>
    
    @Query("SELECT * FROM orders WHERE status = :status ORDER BY orderDate DESC")
    fun getOrdersByStatus(status: OrderStatus): Flow<List<OrderEntity>>
    
    @Query("SELECT COUNT(*) FROM orders WHERE customerId = :customerId")
    suspend fun getOrderCountForCustomer(customerId: String): Int
    
    @Query("SELECT SUM(orderValue) FROM orders WHERE customerId = :customerId AND isPaid = 1")
    suspend fun getTotalOrderValueForCustomer(customerId: String): Double?
    
    @Query("SELECT AVG(orderValue) FROM orders WHERE customerId = :customerId AND isPaid = 1")
    suspend fun getAverageOrderValueForCustomer(customerId: String): Double?
    
    @Query("SELECT MAX(orderDate) FROM orders WHERE customerId = :customerId")
    suspend fun getLastOrderDateForCustomer(customerId: String): Long?
    
    @Query("""
        SELECT * FROM orders 
        WHERE customerId = :customerId 
        ORDER BY orderDate DESC 
        LIMIT 1
    """)
    suspend fun getLastOrderForCustomer(customerId: String): OrderEntity?
    
    @Query("""
        SELECT * FROM orders 
        WHERE orderDate >= :startDate AND orderDate <= :endDate
        ORDER BY orderDate DESC
    """)
    fun getOrdersInDateRange(startDate: Long, endDate: Long): Flow<List<OrderEntity>>
    
    @Query("SELECT SUM(orderValue) FROM orders WHERE orderDate >= :startDate AND isPaid = 1")
    suspend fun getTotalRevenueFromDate(startDate: Long): Double?
    
    @Query("DELETE FROM orders WHERE customerId = :customerId")
    suspend fun deleteOrdersForCustomer(customerId: String)
    
    @Query("DELETE FROM orders")
    suspend fun deleteAllOrders()
}






