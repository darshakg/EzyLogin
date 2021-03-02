package com.darshan.ezylogin.adb.command

class ClearDataAndRestartCommand : CommandList(ClearDataCommand(), StartDefaultActivityCommand(false))
