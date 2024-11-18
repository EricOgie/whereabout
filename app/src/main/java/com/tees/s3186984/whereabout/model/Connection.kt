package com.tees.s3186984.whereabout.model

/**
 * Embedding the Device data in models like User and Connection introduces redundancy,
 * since the same device information might exist in two different places (e.g., in User
 * and in Connection). This could lead to potential data inconsistencies.
 *
 * However, this trade-off is accepted in order to avoid multiple queries, as Firebase
 * does not support joins or complex query operations (e.g., querying connections and
 * separately fetching device details from a different collection).
 *
 * By embedding the device information, we ensure that querying the connections
 * is fast and efficient, without the need for additional lookups. This approach
 * favors performance and simplicity in data retrieval, but requires careful management
 * of data consistency.
 */
data class Connection (
    val connectionId: String = "",                                          // Unique identifier for the connection
    val tag: String = "",                                                  // Tag name of the connection e.g mom's phone
    val ownerId: String = "",                                             // The ID of the user viewing the connection
    val connectedUserId: String = "",                                    // The ID of the other user referenced by the connection
    val connectedUserFirstName: String = "",                            // First name of the other user referenced by connection
    val connectedUserDevice: Device = Device(),                        // The device ID associated with the connected user
    val status: ConnectionStatus = ConnectionStatus.ACTIVE,           // ACTIVE or BROKEN
    val createdAt: Long = System.currentTimeMillis(),                // Timestamp when the connection was created
    val updatedAt: Long = System.currentTimeMillis()                // Timestamp when the connection was updated
)

/**
 * Embedding the Device data in models like User and Connection introduces redundancy,
 * since the same device information might exist in two different places (e.g., in User
 * and in Connection). This could lead to potential data inconsistencies.
 *
 * However, this trade-off is accepted in order to avoid multiple queries, as Firebase
 * does not support joins or complex query operations (e.g., querying connections and
 * separately fetching device details from a different collection).
 *
 * By embedding the device information, we ensure that querying the connections
 * is fast and efficient, without the need for additional lookups. This approach
 * favors performance and simplicity in data retrieval, but requires careful management
 * of data consistency.
 */
data class ConnectionRequest (
    val initiatorId: String,
    val initiatorFirstName: String,
    val initiatorDevice: Device,
    val createdAt: Long,             // Timestamp when the connection was created
)

enum class ConnectionStatus {
    ACTIVE,  // Both users have the connection active
    BROKEN  // At least one users have deleted or broken the connection
}

