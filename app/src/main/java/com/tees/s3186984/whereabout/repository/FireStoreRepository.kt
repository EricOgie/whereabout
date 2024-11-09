package com.tees.s3186984.whereabout.repository


import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
            e.printStackTrace()
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
            e.printStackTrace()
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
        documentId: String
    ): Boolean {
        return try {
            // Add the document to the specified collection
            firestore.collection(collectionName)
                .document(documentId)
                .set(documentData)
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
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
        documentsData: List<Pair<String, T>>
    ): Boolean {
        return try {
            val batch = firestore.batch()

            // Stage each document in a batch
            documentsData.forEach { (documentId, documentData) ->
                val documentReference = firestore.collection(collectionName).document(documentId)
                batch.set(documentReference, documentData)
            }

            // Commit the staged documents in batch
            batch.commit().await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    /**
     * Edits an existing document in a Firestore collection.
     *
     * @param collectionName The name of the Firestore collection.
     * @param documentID The name (ID) of the document to edit.
     * @param updateData A map containing the updated data to set in the document.
     * @return True if the operation is successful, false otherwise.
     */
    suspend fun updateDocument(
        collectionName:String,
        documentID:String,
        updateData: Map<String, Any>
    ): Boolean{

        return try {
            // Reference to the document to be updated
            val documentReference = firestore.collection(collectionName).document(documentID)

            // Update the document with the provided data
            documentReference.update(updateData).await()
            true

        } catch (e: Exception){
            e.printStackTrace()
            false
        }
    }

    /**
     * Deletes a document from a Firestore collection.
     *
     * @param collectionName The name of the Firestore collection.
     * @param documentName The name (ID) of the document to delete.
     * @return True if the operation is successful, false otherwise.
     */
    suspend fun deleteDocument(
        collectionName: String,
        documentName: String
    ): Boolean{

        return try {
            // Reference to the document to be deleted
            val documentReference = firestore.collection(collectionName).document(documentName)
            // Delete the document
            documentReference.delete().await()
            true
        }catch (e: Exception){
            e.printStackTrace()
            false
        }
    }


    /**
     * Updates a Firestore document based on multiple field conditions.
     *
     * Note: This approach is not ideal for real-world applications as Firestore
     * does not natively support composite queries with multiple `whereEqualTo`
     * clauses in a scalable way. This method assumes that the combination of
     * field conditions uniquely identifies a single document.
     *
     * For production, consider restructuring your database to use unique document IDs
     * or compound indexes to simplify querying and updating operations.
     *
     * This function is provided for demonstration purposes only.
     */
    suspend fun updateDocumentByFields(
        collectionName: String,
        conditions: List<Pair<String, Any>>,
        updateData: Map<String, Any>
    ): Boolean {
        return try {
            // Start the query as a Query, not CollectionReference
            var query: Query = firestore.collection(collectionName)

            // Add conditions to the query
            for (condition in conditions) {
                query = query.whereEqualTo(condition.first, condition.second)
            }

            // Execute the query
            val queryResult = query.get().await()

            // Check if a matching document was found
            if (queryResult.isEmpty) {
                return false
            }

            // Update the first matching document
            val documentToBeUpdated = queryResult.documents.first()
            documentToBeUpdated.reference.update(updateData).await()
            true

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }




}