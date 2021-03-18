package com.darshan.ezylogin.ui

import com.darshan.ezylogin.Credentials
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.intellij.openapi.project.Project
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter

object JsonFileUtil {

    private const val CREDENTIALS_IDE_LOCAL_FILE_NAME = "ezyLoginGeneratedCredentials.json"


    fun insertCredentialListToJsonFile(project: Project, credentialsList: List<Credentials>) {
        try {
            val file = File(project.basePath + "/.idea/$CREDENTIALS_IDE_LOCAL_FILE_NAME")
            NotificationHelper.error("File  ${file.path}")
            if (!file.exists()) {
                file.createNewFile()
                NotificationHelper.error("File created${file.path}")
            }
            val printWriter = PrintWriter(FileOutputStream(file))
            printWriter.println(getJsonArrayFrom(credentialsList))
            printWriter.close()
        } catch (e: Exception) {
            NotificationHelper.error("Not able to create credential file with exception ${e.localizedMessage}")
        }
    }

    private fun getJsonArrayFrom(credentialsList: List<Credentials>) : JsonObject {
        val jsonArray = JsonArray()
        NotificationHelper.error(jsonArray.toString())
        credentialsList.forEach {
            val jsonObject  =   JsonObject().apply {
                addProperty("userName", it.userName)
                addProperty("password",it.password)
            }
            jsonArray.add(jsonObject)
        }
        return JsonObject().apply {
            add("credentials",jsonArray)
        }
    }

    fun clearCredentialFile(project: Project) {
        try {
            val file = File(project.basePath + "/.idea/$CREDENTIALS_IDE_LOCAL_FILE_NAME")
            if (file.exists()) {
                val printWriter = PrintWriter(FileOutputStream(file))
                printWriter.print("")
                printWriter.close()
            }
        } catch (e: Exception) {
            NotificationHelper.error("Not able to clear credential file with exception ${e.localizedMessage}")
        }
    }
}