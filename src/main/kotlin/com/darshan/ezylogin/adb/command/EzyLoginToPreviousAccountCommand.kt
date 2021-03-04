package com.darshan.ezylogin.adb.command

import com.android.ddmlib.IDevice
import com.darshan.ezylogin.LoginComponent
import com.darshan.ezylogin.adb.AdbUtil
import com.darshan.ezylogin.adb.command.receiver.GenericReceiver
import com.darshan.ezylogin.ui.NotificationHelper
import com.intellij.openapi.project.Project
import org.jetbrains.android.facet.AndroidFacet
import java.util.concurrent.TimeUnit

class EzyLoginToPreviousAccountCommand : Command {

    override fun run(project: Project, device: IDevice, facet: AndroidFacet, packageName: String): Boolean {
        try {
            if (AdbUtil.isAppInstalled(device, packageName)) {
                val previousLoggedInIndex = LoginComponent.getInstance().state?.previouslyLoggedInIndex
                val credentialsList = LoginComponent.getInstance().state?.credentialsList.orEmpty()
                previousLoggedInIndex?.let {
                    if (it != -1 && credentialsList.isNotEmpty()) {
                        val credential = credentialsList[it]
                        device.executeShellCommand("input text ${credential.userName} && input keyevent 61 && input text ${credential.password} && input keyevent 66", GenericReceiver(), 15L, TimeUnit.SECONDS)
                        NotificationHelper.info(String.format("<b>%s</b> forced-stop on %s", packageName, device.name))
                    }
                }
                return true
            } else {
                NotificationHelper.error(String.format("<b>%s</b> is not installed on %s", packageName, device.name))
            }
        } catch (e1: Exception) {
            NotificationHelper.error("Kill fail... " + e1.message)
        }
        return false
    }
}