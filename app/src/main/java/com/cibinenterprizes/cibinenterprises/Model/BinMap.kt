package com.cibinenterprizes.cibinenterprises.Model

class BinMap {
    var Lantitude: String? = null
    var Longitude: String? = null
    var BinId: Int?= null
    var Verification: String? = null
    var DriverName: String? = null

    constructor(){

    }

    constructor(Lantitude: String?, Longitude: String?, BinId: Int?, Verification: String?, DriverName: String) {
        this.Lantitude = Lantitude
        this.Longitude = Longitude
        this.BinId = BinId
        this.Verification = Verification
        this.DriverName = DriverName
    }
}