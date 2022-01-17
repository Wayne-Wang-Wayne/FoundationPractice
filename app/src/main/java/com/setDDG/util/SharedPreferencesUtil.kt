package com.setDDG.util

import android.annotation.SuppressLint
import android.content.Context

import android.content.SharedPreferences
import com.set.app.settools.api.DataParser
import kotlin.reflect.KProperty

class SharedPreferencesUtil<T>(
    context: Context,
    private val dataName: String,
    private val key: String,
    private val default: T,
    private val cls: Class<*>?) {
    constructor(context: Context, dataName: String, key: String, default: T) : this(context,
        dataName, key, default, null)

    companion object {
        val _SP_SETTING = "Setting"

        val PREF_FontSize = "PREF_FontSize"

        val _SP_TOKEN = "Token"

        val PREF_KeycloakToken = "PREF_PREF_KeycloakToken"

        val PREF_SeteToken = "PREF_SeteToken"

        val PREF_TESTING = "Testing"
    }

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(dataName, Context.MODE_PRIVATE)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return getSharePreferences(key, default, cls)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putSharePreferences(key, value)
    }

    @SuppressLint("CommitPrefEdits")
    fun putSharePreferences(key: String, value: T) = with(prefs.edit()) {
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            else -> putString(key, DataParser.getGson()
                .toJson(value))
        }.apply()
    }

    @Suppress("UNCHECKED_CAST")
    fun getSharePreferences(key: String, value: T, cls: Class<*>?): T? = with(prefs) {
        try {
            val res: Any? = when (value) {
                is String -> getString(key, value) as String
                is Int -> getInt(key, value)
                is Boolean -> getBoolean(key, value)
                is Long -> getLong(key, value)
                is Float -> getFloat(key, value)
                else -> {
                    DataParser.getGson()
                        .fromJson(getString(key, value.toString()), cls)
                }
            }
            return res as T
        } catch (e: Exception) {

        }
        return null
    }

}

