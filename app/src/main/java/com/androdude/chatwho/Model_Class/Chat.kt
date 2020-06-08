package com.androdude.chatwho.Model_Class

class Chat {
    var msg : String ?= null
    var sender : String ?= null
    var reciver : String ?=null
    var isSeen : String ?= null

    constructor(){}

    constructor(msg: String?, sender: String?, reciver: String?, isSeen: String?) {
        this.msg = msg
        this.sender = sender
        this.reciver = reciver
        this.isSeen = isSeen
    }


}