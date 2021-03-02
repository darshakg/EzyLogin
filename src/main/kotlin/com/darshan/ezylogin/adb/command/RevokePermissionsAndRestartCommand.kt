package com.darshan.ezylogin.adb.command

class RevokePermissionsAndRestartCommand : CommandList(RevokePermissionsCommand(), StartDefaultActivityCommand(false))