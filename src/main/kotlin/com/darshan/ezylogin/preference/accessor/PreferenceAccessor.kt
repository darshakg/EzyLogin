package com.darshan.ezylogin.preference.accessor

interface PreferenceAccessor {
    fun saveString(key: String, value: String)
    fun getString(key: String, defaultValue: String): String
}

