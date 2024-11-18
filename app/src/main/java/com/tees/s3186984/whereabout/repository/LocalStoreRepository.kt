package com.tees.s3186984.whereabout.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.tees.s3186984.whereabout.core.LocalStoreManager
import kotlinx.coroutines.flow.first

class LocalStoreRepository(context: Context) {

    private val localStore = LocalStoreManager.getLocalStore(context = context)

    /**
     * Saves a string value in DataStore.
     */
    suspend fun saveStringPreference(key: String, value: String) {
        localStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    /**
     * Retrieves a string value from DataStore.
     */
    suspend fun getStringPreference(key: String): String? {
        return localStore.data.first()[stringPreferencesKey(key)]
    }

    /**
     * Saves a boolean value in DataStore.
     */
    suspend fun saveBooleanPreference(key: String, value: Boolean) {
        localStore.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }

    /**
     * Retrieves a boolean value from DataStore.
     */
    suspend fun getBooleanPreference(key: String): Boolean {
        return localStore.data.first()[booleanPreferencesKey(key)] ?: false
    }

    /**
     * Saves an Int value in DataStore.
     */
    suspend fun saveIntPreference(key: String, value: Int) {
        localStore.edit { preferences ->
            preferences[intPreferencesKey(key)] = value
        }
    }

    /**
     * Retrieves an Int value from DataStore.
     */
    suspend fun getIntPreference(key: String): Int {
        return localStore.data.first()[intPreferencesKey(key)] ?: 0
    }

}