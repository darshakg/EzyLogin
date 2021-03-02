package com.darshan.ezylogin.adb.command

import com.android.ddmlib.IDevice
import com.darshan.ezylogin.Credentials
import com.darshan.ezylogin.LoginComponent
import com.darshan.ezylogin.ObjectGraph
import com.darshan.ezylogin.adb.AdbUtil
import com.darshan.ezylogin.adb.command.receiver.GenericReceiver
import com.darshan.ezylogin.ui.AccountChooseDialog
import com.darshan.ezylogin.ui.ListSelectionListener
import com.darshan.ezylogin.ui.NotificationHelper
import com.intellij.openapi.project.Project
import org.jetbrains.android.facet.AndroidFacet
import java.util.concurrent.TimeUnit

class EzyLoginCommand : Command {

    override fun run(project: Project, device: IDevice, facet: AndroidFacet, packageName: String): Boolean {
        try {
            if (AdbUtil.isAppInstalled(device, packageName)) {
                val credentialList = LoginComponent.getInstance(project).state?.credentialsList.orEmpty()
                AccountChooseDialog(credentialList,object  : ListSelectionListener{
                    override fun onItemSelected(credentials: Credentials) {
                        device.executeShellCommand("input text ${credentials.userName} && input keyevent 61 && input text ${credentials.password} && input keyevent 66", GenericReceiver(), 15L, TimeUnit.SECONDS)
                        NotificationHelper.info(String.format("<b>%s</b> forced-stop on %s", packageName, device.name))
                    }
                }).isVisible = true

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