package jp.sankyuri.tool.notificationflashlightswitcher
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class FlashlightSwitchActivity: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        FlashlightSwitcher.turn(context!!, !FlashlightSwitcher.IsTorchOn)
    }


}



