package dev.x001.foodies.model.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import dev.x001.foodies.R
import dev.x001.foodies.utils.Constants
import dev.x001.foodies.view.activities.MainActivity

class NotifyWorker(context: Context, params: WorkerParameters
    ): Worker(context, params) {

    override fun doWork(): Result {

        Log.i("Notify Worker", "doWork function is called...")
        sendNotification()

        return Result.success()
    }

    private fun sendNotification(){
        val notification_id = 0

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(Constants.NOTIFICATION_ID, notification_id)

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val titleNotification = "Checkout new dish recipes"
        val subtitleNotification = "Tap for more details"

        val bitmap = applicationContext.pngToBitmap(R.drawable.app_logo)

        val bigPicStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(bitmap)
            .bigLargeIcon(null)

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0 , intent, 0)

        val notification = NotificationCompat.Builder(applicationContext, Constants.NOTIFICATION_CHANNEL)
            .setContentTitle(titleNotification)
            .setContentText(subtitleNotification)
            .setLargeIcon(bitmap)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setStyle(bigPicStyle)
            .setAutoCancel(true)

        notification.priority = NotificationCompat.PRIORITY_MAX

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notification.setChannelId(Constants.NOTIFICATION_CHANNEL)

            //Setup the ringtone for notification
            val ringtoneManager =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes =
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            val channel =
                NotificationChannel(
                    Constants.NOTIFICATION_CHANNEL,
                    Constants.NOTIFICATION_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )

            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notification_id, notification.build())
    }

    private fun Context.pngToBitmap(drawableId: Int): Bitmap?{
        return try {
            val drawable = ContextCompat.getDrawable(this, drawableId)
            if (drawable != null) {
                val bitmap = Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bitmap
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}