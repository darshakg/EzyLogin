package com.darshan.ezylogin.adb.command

import com.android.ddmlib.IDevice
import com.darshan.ezylogin.adb.AdbUtil
import com.darshan.ezylogin.adb.command.receiver.GenericReceiver
import com.darshan.ezylogin.ui.NotificationHelper
import com.darshan.ezylogin.ui.NotificationHelper.error
import com.darshan.ezylogin.ui.NotificationHelper.info
import com.intellij.openapi.project.Project
import org.jetbrains.android.facet.AndroidFacet
import java.util.concurrent.TimeUnit

class KillCommand : Command {
    override fun run(project: Project, device: IDevice, facet: AndroidFacet, packageName: String): Boolean {
        try {
            if (AdbUtil.isAppInstalled(device, packageName)) {
                device.executeShellCommand("am force-stop $packageName", GenericReceiver(), 15L, TimeUnit.SECONDS)
                info(String.format("<b>%s</b> forced-stop on %s", packageName, device.name))
                return true
            } else {
                error(String.format("<b>%s</b> is not installed on %s", packageName, device.name))
            }
        } catch (e1: Exception) {
            error("Kill fail... " + e1.message)
        }
        return false
    }
}