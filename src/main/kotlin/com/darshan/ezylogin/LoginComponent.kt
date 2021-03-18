package com.darshan.ezylogin

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.newvfs.persistent.PersistentFS.Attributes
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "LoginComponent",
        storages = [Storage(value = "loginConfiguration.xml")])
class LoginComponent(project: Project? = null) : PersistentStateComponent<LoginComponent.State> {

    private var credentials = Credentials("","")

    @Attributes
    var username: String = ""
    var password: String = ""

    var states = State()

    @Attributes
    var credentialsList: MutableList<Credentials> = mutableListOf()

    override fun getState(): State? = this.states

    override fun loadState(state: State) =
            XmlSerializerUtil.copyBean(state, this.states)

    companion object {
        fun getInstance(): LoginComponent =
                ApplicationManager.getApplication().getComponent(LoginComponent::class.java);
    }

    class State{
        var credentialsList: MutableList<Credentials>  = mutableListOf()
        var previouslyLoggedInIndex = -1
        var username: String = ""
    }
}