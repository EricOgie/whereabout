package com.tees.s3186984.whereabout.repository


import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

/**
 * FireStoreRepository provides utility functions for interacting with Firebase Firestore.
 * It includes methods for querying, saving, and batching documents with error handling.
 */
class FireStoreRepository {
    // Firestore instance - global to the class
    val firestore = FirebaseFirestore.getInstance()

    /**
     * Fetches documents from a Firestore collection based on a filter.
     *
     * @param T The type of the document model to map the result to.
     * @param collectionName The name of the Firestore collection.
     * @param filter A pair representing the field name and its value to filter by.
     * @return A list of mapped objects of type T, or an empty list if an error occurs.
     */
    suspend inline fun <reified T> getDocuments(collectionName: String, filter: Pair<String, Any>): List<T> {
        return try {
            val (fieldName, value) = filter
            // Query Firestore for documents matching the filter
            val querySnapshot: QuerySnapshot = firestore.collection(collectionName)
                .whereEqualTo(fieldName, value).limit(20)
                .get().await()

            // Map the query results to a list of objects of type T
            querySnapshot.documents.mapNotNull { documentSnapshot -> documentSnapshot.toObject<T>() }

        }catch (e: Exception){
            emptyList()
        }
    }


    /**
     * Fetches a single document from a Firestore collection by document name.
     *
     * @param T The type of the document model to map the result to.
     * @param collectionName The name of the Firestore collection.
     * @param documentName The name (ID) of the document to fetch.
     * @return The mapped object of type T if the document exists, or null otherwise.
     */
    suspend inline fun <reified T> getSingleDocument(collectionName: String, documentName: String): T? {
        return try {
            // Fetch the document snapshot
            val documentSnapshot: DocumentSnapshot = firestore.collection(collectionName)
                .document(documentName).get().await()

            if (documentSnapshot.exists()) {
                documentSnapshot.toObject<T>()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }


    /**
     * Saves a single document to a Firestore collection.
     *
     * @param T The type of the document data to save.
     * @param collectionName The name of the Firestore collection.
     * @param documentData The data of the document to save.
     * @return True if the operation is successful, false otherwise.
     */
    suspend inline fun <reified T : Any> saveSingleDocument(
        collectionName: String,
        documentData: T,
    ): Boolean {
        return try {
            // Add the document to the specified collection
            firestore.collection(collectionName)
                .add(documentData)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }


    /**
     * Saves multiple documents to a Firestore collection in a batch operation.
     *
     * @param T The type of the document data to save.
     * @param collectionName The name of the Firestore collection.
     * @param documentsData A list of document data to save.
     * @return True if the operation is successful, false otherwise.
     */
    suspend inline fun <reified T : Any> saveMultipleDocuments(
        collectionName: String,
        documentsData: List<T>
    ): Boolean {
        return try {
            val batch = firestore.batch()

            // Stage each document in a batch
            documentsData.forEach { documentData ->
                val documentReference = firestore.collection(collectionName).document()
                batch.set(documentReference, documentData)
            }

            // Commit the staged documents in batch
            batch.commit().await()
            true
        } catch (e: Exception) {
            false
        }
    }

}