package com.darshan.ezylogin

import java.io.Serializable


class Credentials() : Serializable{


    constructor(userName : String, pass : String) : this() {
        this.userName = userName
        this.password = pass
    }

    var userName: String = ""
        get() = field
        set(value) {
            field = value
        }

    var password : String = ""
        get() = field
        set(value) {
            field = value
        }

    override fun toString(): String {
        return "Credentials(userName='$userName', password='$password')"
    }

}