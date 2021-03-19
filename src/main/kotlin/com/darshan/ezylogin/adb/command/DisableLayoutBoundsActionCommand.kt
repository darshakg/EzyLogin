package com.darshan.ezylogin.adb.command

import com.android.ddmlib.IDevice
import com.darshan.ezylogin.adb.command.receiver.GenericReceiver
import com.darshan.ezylogin.ui.NotificationHelper
import com.intellij.openapi.project.Project
import org.jetbrains.android.facet.AndroidFacet
import java.util.concurrent.TimeUnit

class DisableLayoutBoundsActionCommand : Command {
    override fun run(project: Project, device: IDevice, facet: AndroidFacet, packageName: String): Boolean {

        try {
            device.executeShellCommand("setprop debug.layout false ; service call activity 1599295570", GenericReceiver(), 10L, TimeUnit.SECONDS)
            NotificationHelper.info(String.format("<b>%s</b> Layout bounds disabled on %s", packageName, device.name))
            return true
        } catch (e1: Exception) {
            NotificationHelper.error("Layout bound disable failed... " + e1.message)
        }
        return false
    }
}