package com.firebase

import android.os.Bundle


class FirebaseUtils {

    companion object {

        fun subscribeToTopic(topicName: String) {
//            when {
//                UserSessionManager.getCurrentLang().contentEquals("en") -> {
//                    FirebaseMessaging.getInstance().subscribeToTopic(topicName + "_en")
//                    FirebaseMessaging.getInstance().unsubscribeFromTopic(topicName + "_ar")
//                    FirebaseMessaging.getInstance().unsubscribeFromTopic(topicName + "_fr")
//                }
//                UserSessionManager.getCurrentLang().contentEquals("ar") -> {
//                    FirebaseMessaging.getInstance().subscribeToTopic(topicName + "_ar")
//                    FirebaseMessaging.getInstance().unsubscribeFromTopic(topicName + "_en")
//                    FirebaseMessaging.getInstance().unsubscribeFromTopic(topicName + "_fr")
//                }
//                else -> {
//                    FirebaseMessaging.getInstance().subscribeToTopic(topicName + "_fr")
//                    FirebaseMessaging.getInstance().unsubscribeFromTopic(topicName + "_ar")
//                    FirebaseMessaging.getInstance().unsubscribeFromTopic(topicName + "_en")
//                }
//            }
        }

        fun addEvent(eventName: String, key: String? = "", value: String? = "") {
            val bundle = Bundle()
            if (!key.isNullOrEmpty()) {
                bundle.putString(key, value)
            }
//            addFirebaseAnalytics(eventName, bundle)
        }

//        private fun addFirebaseAnalytics(eventName: String, data: Bundle) {
//            App.firebaseAnalytics.logEvent(eventName, data)
//        }
    }

}