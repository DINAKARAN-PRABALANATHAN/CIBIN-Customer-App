package com.cibinenterprizes.cibinenterprises.Model

class BinExtra {

    var Username: String? = null
    var EmailId: String? = null
    var Mobile: String? = null
    var AuthId: String? = null

    constructor(){

    }
    constructor(Username: String?, EmailId: String?, Mobile: String?, AuthId: String?) {
        this.Username = Username
        this.EmailId = EmailId
        this.Mobile = Mobile
        this.AuthId = AuthId
    }
}