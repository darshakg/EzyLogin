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
    fun grantPermissions(project: Project) = executeOnDevice(project, GrantPermissionsCommand())
    fun revokePermissions(project: Project) = executeOnDevice(project, RevokePermissionsCommand())
    fun revokePermissionsAndRestart(project: Project) = executeOnDevice(project, RevokePermissionsAndRestartCommand())
    fun startDefaultActivity(project: Project) = executeOnDevice(project, StartDefaultActivityCommand(false))
    fun restartDefaultActivity(project: Project) = executeOnDevice(project, RestartPackageCommand())
    fun clearData(project: Project) = executeOnDevice(project, ClearDataCommand())
    fun clearDataAndRestart(project: Project) = executeOnDevice(project, ClearDataAndRestartCommand())
    fun clearDataAndRestartWithDebugger(project: Project) = executeOnDevice(project, ClearDataAndRestartWithDebuggerCommand())
    fun enableWifi(project: Project) = executeOnDevice(project, ToggleSvcCommand(WIFI, true))
    fun disableWifi(project: Project) = executeOnDevice(project, ToggleSvcCommand(WIFI, false))
    fun enableMobile(project: Project) = executeOnDevice(project, ToggleSvcCommand(MOBILE, true))
    fun disableMobile(project: Project) = executeOnDevice(project, ToggleSvcCommand(MOBILE, false))
    fun EzyLogin(project: Project) = executeOnDevice(project, EzyLoginCommand())
    fun EzyLoginToPreviousAccount(project: Project) = executeOnDevice(project, EzyLoginToPreviousAccountCommand())

    private fun executeOnDevice(project: Project, runnable: Command) {
        if (AdbUtil.isGradleSyncInProgress(project)) {
            NotificationHelper.error("Gradle sync is in progress")
            return
        }

        val result = project.getComponent(ObjectGraph::class.java)
                .deviceResultFetcher
                .fetch()
        if (result != null) {
            for (device in result.devices) {
                EXECUTOR.submit { runnable.run(project, device, result.facet, result.packageName) }
            }
        } else {
            NotificationHelper.error("No Device found")
        }
    }
}
