package com.darshan.ezylogin.adb

import com.android.ddmlib.IDevice
import com.darshan.ezylogin.preference.ProjectPreferences
import com.darshan.ezylogin.ui.NotificationHelper

class UseSameDevicesHelper constructor(private val projectPreferences: ProjectPreferences, private val bridge: Bridge) {

    var previouslyConnectedDevices: List<IDevice>? = null

    fun getRememberedDevices(): List<IDevice> {
        val selectedDeviceSerials = projectPreferences.getSelectedDeviceSerials()
        NotificationHelper.error(selectedDeviceSerials.toString())
        val currentlyConnectedDevices = bridge.connectedDevices()

        if (currentlyConnectedDevices == previouslyConnectedDevices) {
            val rememberedDevices = currentlyConnectedDevices.filter { selectedDeviceSerials.contains(it.serialNumber) }
            if (rememberedDevices.size == selectedDeviceSerials.size) {
                return rememberedDevices
            }
        }

        return emptyList()
    }

    fun rememberDevices() {
        previouslyConnectedDevices = bridge.connectedDevices()
    }
}