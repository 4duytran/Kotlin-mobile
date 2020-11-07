package com.example.nestixbook.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys


private const val TOKEN_NAME = "token_login"


class PreferenceManager(con: Context) {

    // Create or retrieve the Master Key for encryption/decryption
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    // Initialize/open an instance of EncryptedSharedPreferences
    private val sharedPreferences = EncryptedSharedPreferences.create(
        "secret_shared_prefs",
        masterKeyAlias,
        con,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun putPrefs(value:String) {
        return sharedPreferences.edit().putString(TOKEN_NAME, value).apply()
    }

    fun getPrefs(): String? {
        return sharedPreferences.getString(TOKEN_NAME, null)
    }

    fun verify(): Boolean = sharedPreferences.contains(TOKEN_NAME)

    fun clear() {
        return sharedPreferences.edit().remove(TOKEN_NAME).apply()
    }

}