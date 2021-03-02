package com.darshan.ezylogin.action

import com.darshan.ezylogin.adb.AdbFacade
import com.darshan.ezylogin.adb.AdbUtil
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

class ClearDataAndRestartWithDebuggerAction : AdbAction() {
    override fun actionPerformed(e: AnActionEvent, project: Project) = AdbFacade.clearDataAndRestartWithDebugger(project)

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = AdbUtil.isDebuggingAvailable
    }
}
