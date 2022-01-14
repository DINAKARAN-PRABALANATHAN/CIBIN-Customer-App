package com.cibinenterprizes.cibinenterprises.Model

class BinDetails {
    var Area_Village: String? = null
    var Locality: String? = null
    var District: String? = null
    var LoadType: String? = null
    var CollectionPeriod: String? = null
    var Lantitude: String? = null
    var Longitude: String? = null
    var BinId: Int?= null
    var Verification: String? = null


    constructor(){

    }

    constructor(Area_Village: String?, Locality: String?,District: String?, LoadType: String?, CollectionPeriod: String?, Lantitude: String?, Longitude: String?, BinId: Int?, Verification: String?) {
        this.Area_Village = Area_Village
        this.Locality = Locality
        this.District = District
        this.LoadType = LoadType
        this.CollectionPeriod = CollectionPeriod
        this.Lantitude = Lantitude
        this.Longitude = Longitude
        this.BinId = BinId
        this.Verification = Verification
    }
}