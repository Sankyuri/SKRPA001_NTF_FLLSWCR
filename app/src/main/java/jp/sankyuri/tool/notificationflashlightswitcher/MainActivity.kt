package jp.sankyuri.tool.notificationflashlightswitcher
import android.Manifest
import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class MainActivity : AppCompatActivity() {

    private val reqCodePerm = 392392392

    private var ntfChId = "unused_on_older_than_oreo"




    companion object {

        fun hideNotification(
                context: Context )
        {
            FlashlightSwitcher.turn( context, false )
            val ntfMng = context.getSystemService( Context.NOTIFICATION_SERVICE ) as NotificationManager
            ntfMng.cancel( R.string.app_name )
        }

    } // companion object






    override fun onCreate(
            savedInstanceState: Bundle? )
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Oreo から必須のチャンネル設定
        createNotificationChannel()
    }




    fun btnHideOnClickHandler(
            view: View? )
    {
        hideNotification( applicationContext )
    }




    fun btnShowOnClickHandler(
            view: View? )
    {
        // Request notification permission.
        // If not allowed then return.
        if (!reqPerm()) {  return  }

        // Create MainActivity opener intent (main notification event)
        val ntfPndMain   = createMainPendingIntent()
        // Create switch torch mode activity intent
        val ntfPndSwitch = createEventPendingIntent( "SwitchTorch" )
        // Create close switcher notification activity intent
        val ntfPndClose  = createEventPendingIntent( "CloseApp"    )
        // Load custom notification and set event
        val rv = createRemoteViews( listOf(
                Pair( R.id.txvSwitch, ntfPndSwitch ),
                Pair( R.id.imbClose,  ntfPndClose  )
        ) )

        // Create notification
        val ntf = createNotification( ntfPndMain, rv )
        // Post notification
        notify( ntf )
    }








    // Create notification channel (requires on version >=O)
    private fun createNotificationChannel()
    {
        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            ntfChId = applicationContext.packageName + ".notification"
            val channel = NotificationChannel(
                    ntfChId,
                    getString( R.string.ntf_channelname ),
                    NotificationManager.IMPORTANCE_LOW )
            NotificationManagerCompat.from(this)
                .createNotificationChannel( channel )
        }
    }




    // Create MainActivity opener intent (main notification event)
    private fun createMainPendingIntent()
    : PendingIntent
    {
        val intent = Intent( this, MainActivity::class.java )
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY or
                           Intent.FLAG_ACTIVITY_NEW_TASK   or
                           Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.`package` = applicationContext.packageName
        return PendingIntent.getActivity( this, 0, intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT )
    }




    // Create activity event intent
    private fun createEventPendingIntent(
            action: String )
    : PendingIntent
    {
        val intent = Intent( action )
        intent.`package` = applicationContext.packageName
        return PendingIntent.getBroadcast( this, 0, intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT )
    }




    // Load custom notification and set event
    private fun createRemoteViews(
            onClickEvents: List<Pair<Int, PendingIntent>> )
    : RemoteViews
    {
        val rv = RemoteViews( packageName, R.layout.activity_switcher_notification )
        for (item in onClickEvents) {
            rv.setOnClickPendingIntent(item.first, item.second)
        }
        return rv
    }




    // Create notification
    private fun createNotification(
            ntfPndMain: PendingIntent,
            rv        : RemoteViews )
    : Notification
    {
        val ntf = NotificationCompat.Builder( applicationContext, ntfChId )
            .setSmallIcon( R.drawable.im_app_icon_small )
            .setPriority( NotificationCompat.PRIORITY_LOW )
            .setContentIntent( ntfPndMain )
            .setAutoCancel( false )
            .setSilent( true )
            .setCustomContentView( rv )
            .build()
        ntf.flags = ntf.flags or Notification.FLAG_ONGOING_EVENT
        return ntf
    }




    // Post notification
    private fun notify(
            ntf: Notification )
    {
        val ntfMng = getSystemService( Context.NOTIFICATION_SERVICE ) as NotificationManager
        ntfMng.notify( R.string.app_name, ntf )
    }


    // Request notification permission.
        // If not allowed then return false.
    private fun reqPerm()
    : Boolean
    {
        var valPerm = PackageManager.PERMISSION_GRANTED

        if (VERSION_CODES.TIRAMISU <= Build.VERSION.SDK_INT)
        {
            // Check permission granted.
            valPerm =
                checkSelfPermission( Manifest.permission.POST_NOTIFICATIONS )
            // If permission is not granted then request.
            if ( valPerm != PackageManager.PERMISSION_GRANTED )
            {
                showPermReqMsg()
            }

        }
        return valPerm == PackageManager.PERMISSION_GRANTED
    }


    @RequiresApi(VERSION_CODES.TIRAMISU)
    private fun showPermReqMsg()
    {
        AlertDialog.Builder( this )
            .setTitle( R.string.adl_perm_title )
            .setMessage( R.string.adl_perm_msg )
            .setPositiveButton( R.string.adl_perm_btn_ok ) { dialog, which ->
                requestPermissions( arrayOf( Manifest.permission.POST_NOTIFICATIONS ), reqCodePerm )
            }
            .setNegativeButton( R.string.adl_perm_btn_cancel ) { _, _ ->
                // dn
            }
            .show()
    }




}