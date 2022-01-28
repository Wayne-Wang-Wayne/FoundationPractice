package com.foundationPractice.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.set.app.settools.api.DataParser


/**
 * 用getSimpleValue可以正確取出儲存型態為基本型態或普通自定義class的值，
 * 但ArrayList<自定義class>無法直接存，
 * 但可以用下面註解掉的getComplicatedValue方法即可work
 */
class SharedPreferencesUtil(
    context: Context,
    private val dataName: String,
//    private val key: String,
//    private val default: T,
//    private val cls: Class<*>?
) {
//    constructor(context: Context, dataName: String, key: String, default: T) : this(context,
//        dataName, key, default, null)


    companion object {
        //sharedPreference名稱
        val _SP_SETTING = "Setting"

        val PREF_FontSize = "PREF_FontSize"

        val _SP_TOKEN = "Token"

        val PREF_KeycloakToken = "PREF_PREF_KeycloakToken"

        val PREF_SeteToken = "PREF_SeteToken"

        val PREF_TESTING = "Testing"

        //sharedPreference Key


    }

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(dataName, Context.MODE_PRIVATE)
    }

    //    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
//        return getSharePreferences(key, default, cls)
//    }
//
//    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
//        putSharePreferences(key, value)
//    }
    fun <T> getSimpleValue(
        key: String, defaultValue: T, cls: Class<*>?): T? {
        return getSharePreferences(key, defaultValue, cls)
    }

    fun <T> setValue(
        key: String, value: T) {
        putSharePreferences(key, value)
    }


    @SuppressLint("CommitPrefEdits")
    private fun <T> putSharePreferences(key: String, value: T) = with(prefs.edit()) {
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
    private fun <T> getSharePreferences(key: String, defaultValue: T, cls: Class<*>?): T? =
        with(prefs) {
            try {
                val res: Any? = when (defaultValue) {
                    is String -> getString(key, defaultValue) as String
                    is Int -> getInt(key, defaultValue)
                    is Boolean -> getBoolean(key, defaultValue)
                    is Long -> getLong(key, defaultValue)
                    is Float -> getFloat(key, defaultValue)
                    else -> {
                        DataParser.getGson()
                            .fromJson(getString(key, defaultValue.toString()), cls)
                    }

                }
                return res as T
            } catch (e: Exception) {

            }
            return null
        }

    /**
     * 如想正確取出ArrayList<>等較complicated的值，可以下面方法依樣畫葫蘆客製
     */
//    fun getComplicatedValue(key: String): ArrayList<TestModel> = with(prefs) {
//        val arrayList: ArrayList<TestModel>
//        val type = object : TypeToken<ArrayList<TestModel>>() {}.type
//        arrayList = DataParser.getGson()
//            .fromJson(getString(key, ""), type)
//        return arrayList
//    }

}

