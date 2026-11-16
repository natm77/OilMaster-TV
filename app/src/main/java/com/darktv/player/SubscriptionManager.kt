package com.darktv.player

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

object SubscriptionManager {
    private const val PREFS_NAME = "oil_sub_prefs"
    private const val KEY_SUB_ID = "sub_id"
    private const val API_BASE = "http://152.55.178.68:8080"

    fun getStoredSubId(context: Context): String? {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(KEY_SUB_ID, null)
    }

    fun saveSubId(context: Context, subId: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().putString(KEY_SUB_ID, subId).apply()
    }

    suspend fun verifySubscription(subId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = URL("$API_BASE/status/$subId")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            val responseCode = connection.responseCode
            responseCode == 200
        } catch (e: Exception) {
            false
        }
    }
}
