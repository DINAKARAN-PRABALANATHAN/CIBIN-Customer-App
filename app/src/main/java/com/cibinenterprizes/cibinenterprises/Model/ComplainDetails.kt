package com.cibinenterprizes.cibinenterprises.Model

class ComplainDetails {
    var EmailId: String? = null
    var BinID: String? = null
    var Area: String? = null
    var CompaintDescription: String? = null
    var Status: String? = null
    var ComplaintId: String? = null

    constructor(){

    }
    constructor(EmailId: String?, BinID: String?, Area: String?, CompaintDescription: String?,Status: String,ComplaintId: String) {
        this.EmailId = EmailId
        this.BinID = BinID
        this.Area = Area
        this.CompaintDescription = CompaintDescription
        this.Status = Status
        this.ComplaintId = ComplaintId
    }
}