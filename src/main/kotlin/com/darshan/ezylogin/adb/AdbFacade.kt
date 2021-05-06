package com.darshan.ezylogin.adb

import com.darshan.ezylogin.LoginComponent
import com.darshan.ezylogin.ObjectGraph
import com.darshan.ezylogin.adb.command.*
import com.darshan.ezylogin.adb.command.SvcCommand.MOBILE
import com.darshan.ezylogin.adb.command.SvcCommand.WIFI
import com.darshan.ezylogin.ui.NotificationHelper
import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.intellij.openapi.project.Project
import java.util.concurrent.Executors

object AdbFacade {
    private val EXECUTOR = Executors.newCachedThreadPool(ThreadFactoryBuilder().setNameFormat("AdbIdea-%d").build())

    fun uninstall(project: Project) = executeOnDevice(project, UninstallCommand())
    fun kill(project: Project) = executeOnDevice(project, KillCommand())
    fun enableWifi(project: Project) = executeOnDevice(project, ToggleSvcCommand(WIFI, true))
    fun disableWifi(project: Project) = executeOnDevice(project, ToggleSvcCommand(WIFI, false))
    fun enableMobile(project: Project) = executeOnDevice(project, ToggleSvcCommand(MOBILE, true))
    fun disableMobile(project: Project) = executeOnDevice(project, ToggleSvcCommand(MOBILE, false))
    fun EzyLogin(project: Project) = executeOnDevice(project, EzyLoginCommand())
    fun EzyLoginToPreviousAccount(project: Project) = executeOnDevice(project, EzyLoginToPreviousAccountCommand())
    fun EnableLayoutBoundsAction(project: Project) = executeOnDevice(project, EnableLayoutBoundsActionCommand())
    fun DisableLayoutBoundsAction(project: Project) = executeOnDevice(project, DisableLayoutBoundsActionCommand())

    private fun executeOnDevice(project: Project, runnable: Command) {
        if (AdbUtil.isGradleSyncInProgress(project)) {
            NotificationHelper.error("Gradle sync is in progress")
            return
        }

        val result = project.getComponent(ObjectGraph::class.java)
                .deviceResultFetcher
                .fetch()

        try{
            if (result != null) {
                for (device in result.devices) {
                    EXECUTOR.submit { runnable.run(project, device, result.facet, result.packageName) }
                }
            } else {
                NotificationHelper.error("No Device found")
            }
        }catch (e : Exception){
            NotificationHelper.error("ADB not found. Please Setup ADB")
        }

    }
}
