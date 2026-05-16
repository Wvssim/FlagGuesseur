package com.example.quiz_app_wvssim.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationHelper {

    private const val CHANNEL_ID = "quiz_records"
    private const val PREFS_NAME = "quiz_records_prefs"

    fun createChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Records & Classement",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply { description = "Notifications quand tu bats un record" }
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun notifyIfRecord(context: Context, categoryName: String, score: Int, userName: String) {
        createChannel(context)
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val key = "best_$categoryName"
        val previous = prefs.getInt(key, 0)

        if (score > previous) {
            prefs.edit().putInt(key, score).apply()
            val notifId = categoryName.hashCode()
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.star_on)
                .setContentTitle("Nouveau record ! 🏆")
                .setContentText("$userName : $score pts en $categoryName")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
            try {
                NotificationManagerCompat.from(context).notify(notifId, builder.build())
            } catch (_: SecurityException) { /* permission non accordée */ }
        }
    }

    fun notifyLeaderboardBeaten(context: Context, beaterName: String, categoryName: String, newScore: Int) {
        createChannel(context)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.star_on)
            .setContentTitle("Ton record est battu !")
            .setContentText("$beaterName a marqué $newScore pts en $categoryName")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        try {
            NotificationManagerCompat.from(context).notify(newScore, builder.build())
        } catch (_: SecurityException) { }
    }
}
