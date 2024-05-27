package com.example.unibites.coupon

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.unibites.ui.home.Coupon
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

val Context.dataStore by preferencesDataStore(name = "coupon_preferences")

class CouponPreferences(private val context: Context) {
    companion object {
        private val COUPON_DATA_KEY = stringPreferencesKey("coupon_data")
        private val LAST_CACHE_TIME_KEY = stringPreferencesKey("last_cache_time")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun saveCoupons(coupons: List<Coupon>) {
        val json = Gson().toJson(coupons)
        context.dataStore.edit { preferences ->
            preferences[COUPON_DATA_KEY] = json
            preferences[LAST_CACHE_TIME_KEY] = LocalDate.now().toString()
        }
    }

    fun getCoupons(): List<Coupon>? {
        val json = runBlocking {
            context.dataStore.data.map { preferences ->
                preferences[COUPON_DATA_KEY]
            }.first()
        }
        return json?.let { Gson().fromJson(it, Array<Coupon>::class.java).toList() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun shouldInvalidateCache(): Boolean {
        val lastCacheTime = runBlocking {
            context.dataStore.data.map { preferences ->
                preferences[LAST_CACHE_TIME_KEY]
            }.first()
        }
        val lastCacheDate = LocalDate.parse(lastCacheTime)
        return LocalDate.now().isAfter(lastCacheDate)
    }
}