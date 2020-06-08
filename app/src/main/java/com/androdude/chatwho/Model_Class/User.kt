package com.androdude.chatwho.Model_Class

class User {
    var user_id : String ?= null
    var name : String ?= null
    var email: String ?= null
    var status : String ?= null
    var image_url : String ?= null
    var user_remark : String? = null

    constructor(){}
    constructor(
        user_id: String?,
        name: String?,
        email: String?,
        status: String?,
        image_url: String?,
        user_remark: String?
    ) {
        this.user_id = user_id
        this.name = name
        this.email = email
        this.status = status
        this.image_url = image_url
        this.user_remark = user_remark
    }


}