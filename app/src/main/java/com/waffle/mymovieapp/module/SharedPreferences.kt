package com.waffle.mymovieapp.module

import android.annotation.SuppressLint
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SharedPreferences(val context: Context) {
    companion object {
        private const val PREF_NAME ="waffle.mymovieapp.name"
        private const val IS_LOGIN = "is.login"
        private const val USER_NAME = "user.name"
        private const val USER_ID = "user.id"
    }
    @SuppressLint("NewApi")
    private val masterKeyAlias= MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    private val pref = EncryptedSharedPreferences.create(context, PREF_NAME, masterKeyAlias, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)


    var isLogin : Boolean
        get() = pref.getBoolean(IS_LOGIN, false)
        set(value) = pref.edit().putBoolean(IS_LOGIN,value).apply()

    var userId : String
        get() = pref.getString(USER_ID, "").toString()
        set(value) = pref.edit().putString(USER_ID,value).apply()

    var userName : String
        get() = pref.getString(USER_NAME,"").toString()
        set(value) = pref.edit().putString(USER_NAME,value).apply()

    fun resetSharedPref(){
        context.getSharedPreferences(PREF_NAME,0).edit().clear().apply()
    }

}