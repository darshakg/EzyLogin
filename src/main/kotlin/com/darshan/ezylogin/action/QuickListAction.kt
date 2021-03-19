package com.darshan.ezylogin.action

import com.darshan.ezylogin.LoginComponent
import com.intellij.ide.actions.QuickSwitchSchemeAction
import com.intellij.openapi.actionSystem.*
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
        addAction("com.darshan.ezylogin.action.EzyLoginAction", group)
        addAction("com.darshan.ezylogin.action.EnableLayoutBoundsAction", group)
        addAction("com.darshan.ezylogin.action.DisableLayoutBoundsAction", group)
        if (LoginComponent.getInstance().state?.previouslyLoggedInIndex != -1) {
            addAction("com.darshan.ezylogin.action.EzyLoginToPreviouslyLoggedInAccountAction", group)
        }
    }

    private fun addAction(actionId: String, toGroup: DefaultActionGroup) {
        // add action to group if it is available
        ActionManager.getInstance().getAction(actionId)?.let {
            if(actionId == "com.darshan.ezylogin.action.EzyLoginToPreviouslyLoggedInAccountAction"){
                it.templatePresentation.text = getTitleForPreviouslyLoggedInAction()
            }
            toGroup.add(it)
        }
    }

    private fun getTitleForPreviouslyLoggedInAction() : String{
        LoginComponent.getInstance().state?.let {
            return "Login to ${it.credentialsList[it.previouslyLoggedInIndex].userName}"
        }
        return "Login To Previously Logged In Account"
    }

    override fun isEnabled() = true
    override fun getPopupTitle(e: AnActionEvent) = "ADB Operations Popup"
}