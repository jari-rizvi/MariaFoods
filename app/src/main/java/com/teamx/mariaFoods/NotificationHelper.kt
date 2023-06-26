package com.teamx.mariaFoods

import android.content.Context

object NotificationHelper {
    fun displayNotification(context: Context?, title: String?, description: String?) {
       /* val builder = NotificationCompat.Builder(
            context!!, "MainActivity.Channel_ID"
        )
            .setSmallIcon(R.drawable.ic_raseef)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        builder.setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        builder.setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)
        val notificationManagerCompat = NotificationManagerCompat.from(
            context
        )
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManagerCompat.notify(1, builder.build())*/



//        service!!.showNotification1(title!!, description.toString())
    }
}