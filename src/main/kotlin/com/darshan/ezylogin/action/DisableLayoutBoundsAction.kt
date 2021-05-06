package com.darshan.ezylogin.action

import com.darshan.ezylogin.adb.AdbFacade
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

class DisableLayoutBoundsAction : AdbAction() {
    override fun actionPerformed(e: AnActionEvent, project: Project) = AdbFacade.DisableLayoutBoundsAction(project)
}