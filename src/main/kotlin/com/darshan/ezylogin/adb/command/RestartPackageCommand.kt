package com.darshan.ezylogin.adb.command

class RestartPackageCommand : CommandList(KillCommand(), StartDefaultActivityCommand(false))