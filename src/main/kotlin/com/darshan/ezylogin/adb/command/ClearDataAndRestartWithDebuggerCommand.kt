package com.darshan.ezylogin.adb.command

class ClearDataAndRestartWithDebuggerCommand : CommandList(ClearDataCommand(), StartDefaultActivityCommand(true))
