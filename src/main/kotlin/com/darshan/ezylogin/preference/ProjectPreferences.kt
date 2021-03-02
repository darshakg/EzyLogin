package com.darshan.ezylogin.preference

import com.darshan.ezylogin.preference.accessor.PreferenceAccessor

private const val SELECTED_SERIALS_PROPERTY = "com.darshan.ezylogin.selecteddevices"
private const val USER_NAME = "com.darshan.ezylogin.username"

class ProjectPreferences(private val preferenceAccessor: PreferenceAccessor) {

    fun saveSelectedDeviceSerials(serials: List<String>) {
        preferenceAccessor.saveString(SELECTED_SERIALS_PROPERTY, serials.joinToString(separator = " "))
    }

    fun getSelectedDeviceSerials(): List<String> {
        return with(preferenceAccessor.getString(SELECTED_SERIALS_PROPERTY, "")) {
            if (isEmpty()) {
                emptyList()
            } else {
                split(" ")
            }
        }
    }

    fun saveAccountWithName(userName: String) {
        preferenceAccessor.saveString(USER_NAME, userName)

    }

    fun getAccounts(): String {
        return preferenceAccessor.getString(USER_NAME,"")
    }
}
