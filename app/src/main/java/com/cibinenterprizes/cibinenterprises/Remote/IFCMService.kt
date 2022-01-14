package com.cibinenterprizes.cibinenterprises.Remote

import com.cibinenterprizes.cibinenterprises.Model.FCMResponse
import com.cibinenterprizes.cibinenterprises.Model.FCMSendData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface IFCMService {
    @Headers("Content-Type:application/json",
        "Authorization:Key=AAAAoBtFA5o:APA91bEukPnDhHlU2C81CKAEf4G2j2AgFJR-8sVrMj_42zz1sSs0M37ZJ2Ur0z5Tf3tshmg3aoJk7HbxaGlqnbDsnb1wN4T1HzvTiCpcLvtjObPEovSJxpD3e8cXLSvoX1Pc3BYdYYdB")
    @POST("fcm/send")

    fun sendNotification(@Body body: FCMSendData): Call<FCMResponse>

}