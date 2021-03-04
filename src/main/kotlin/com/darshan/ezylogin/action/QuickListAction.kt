package com.darshan.ezylogin.action

import com.darshan.ezylogin.LoginComponent
import com.darshan.ezylogin.adb.AdbUtil
import com.intellij.ide.actions.QuickSwitchSchemeAction
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project

class QuickListAction : QuickSwitchSchemeAction(), DumbAware {
    override fun fillActions(project: Project?, group: DefaultActionGroup, dataContext: DataContext) {

        if (project == null) {
            return
        }

        addAction("com.darshan.ezylogin.action.UninstallAction", group)
        addAction("com.darshan.ezylogin.action.KillAction", group)
        addAction("com.darshan.ezylogin.action.StartAction", group)
        addAction("com.darshan.ezylogin.action.RestartAction", group)
        addAction("com.darshan.ezylogin.action.ClearDataAction", group)
        addAction("com.darshan.ezylogin.action.ClearDataAndRestartAction", group)
        if (AdbUtil.isDebuggingAvailable) {
            group.addSeparator()
            addAction("com.darshan.ezylogin.action.StartWithDebuggerAction", group)
            addAction("com.darshan.ezylogin.action.RestartWithDebuggerAction", group)
        }
        addAction("com.darshan.ezylogin.action.EzyLoginAction", group)
        if (LoginComponent.getInstance().state?.previouslyLoggedInIndex != -1) {
            addAction("com.darshan.ezylogin.action.EzyLoginToPreviouslyLoggedInAccountAction", group)
        }
    }


    private fun addAction(actionId: String, toGroup: DefaultActionGroup) {
        // add action to group if it is available
        ActionManager.getInstance().getAction(actionId)?.let {
            toGroup.add(it)
        }
    }

    override fun isEnabled() = true
    override fun getPopupTitle(e: AnActionEvent) = "ADB Operations Popup"
}