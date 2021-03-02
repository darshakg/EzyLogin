package com.darshan.ezylogin

import com.darshan.ezylogin.preference.accessor.PreferenceAccessorImpl
import com.darshan.ezylogin.preference.ApplicationPreferences
import com.darshan.ezylogin.ui.NotificationHelper
import com.intellij.ide.plugins.PluginManager
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.components.ApplicationComponent
import com.intellij.openapi.extensions.PluginId
import com.intellij.util.text.SemVer


private val pluginPackage = "com.darshan.ezylogin"

// This is more of a service locator than a proper DI framework.
// It's not used often enough in the codebase to warrant the complexity of a DI solution like dagger.
class Application : ApplicationComponent {
    private val applicationPreferencesAccessor = PreferenceAccessorImpl(PropertiesComponent.getInstance())
    private val applicationPreferences = ApplicationPreferences(applicationPreferencesAccessor)

    override fun initComponent() {
        try {
            val version = PluginManager.getPlugin(PluginId.getId(pluginPackage))!!.version!!
            applicationPreferences.savePreviousPluginVersion(SemVer.parseFromText(version)!!)
        } catch (e: Exception) {
            NotificationHelper.error("Couldn't initialize ADB Idea: ${e.message}")
        }
    }
}
