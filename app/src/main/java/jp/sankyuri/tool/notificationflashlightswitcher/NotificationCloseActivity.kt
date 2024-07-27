package jp.sankyuri.tool.notificationflashlightswitcher
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationCloseActivity: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        MainActivity.hideNotification( context!! )
    }


}



