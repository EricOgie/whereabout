package com.tees.s3186984.whereabout.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.tees.s3186984.whereabout.wutils.LOCAL_STORE

/**
 * LocalStoreManager:
 * This singleton object is responsible for managing the DataStore instance used to persist
 * key-value data. It utilizes activity context to access the DataStore.
 *
 * Components:
 * - `LOCAL_STORE`: A constant name for identifying the DataStore instance, making it accessible
 *   globally.
 * - `getLocalStore(context: Context)`: Returns the DataStore instance using `WhereaboutApp.appContext`, ensuring
 *   consistent and easy access without passing a Context.
 *
 * Rationale:
 * By utilizing a singleton pattern with a global application context, LocalStoreManager provides
 * a centralized and streamlined approach to access the app’s DataStore. This pattern minimizes
 * the need to repeatedly pass Context from different components, making the code cleaner, less
 * error-prone, and easier to maintain.
 */
object LocalStoreManager {

    private val Context.dataStore by preferencesDataStore(name = LOCAL_STORE)

    /**
     * getLocalStore:
     * Give access to a global DataStore<Preferences> instance, LocalStore
     * */
    fun getLocalStore(context: Context) : DataStore<Preferences> {
        return context.dataStore
    }
}

