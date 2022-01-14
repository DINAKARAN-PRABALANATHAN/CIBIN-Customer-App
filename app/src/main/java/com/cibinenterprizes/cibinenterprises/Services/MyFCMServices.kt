package com.cibinenterprizes.cibinenterprises.Services

import com.cibinenterprizes.cibinenterprises.Common.Common
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class MyFCMServices: FirebaseMessagingService() {

    var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onNewToken(pO: String) {
        super.onNewToken(pO)
        Common.updateToken(this, pO)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val dataTitle = remoteMessage.data["title"]
        val dataContent = remoteMessage.data["content"]
        Common.showNotification(this, Random().nextInt(), dataTitle, dataContent)
    }




}