package com.example.notifex

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class NotificationHelper(private val context: Context) {

    private val channelId = "my_channel_id"
    private val notificationId = 1

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "My Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val REQUEST_CODE_POST_NOTIFICATIONS = 1
    }

    fun createExpandedNotification() {
        // Verifica se a permissão foi concedida antes de criar a notificação
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED) {

            // Intent para a MainActivity ao clicar na notificação
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            // Intents para os botões de ação
            val actionIntent1 = Intent(context, MainActivity::class.java)
            val actionPendingIntent1 = PendingIntent.getActivity(context, 1, actionIntent1, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            val actionIntent2 = Intent(context, MainActivity::class.java)
            val actionPendingIntent2 = PendingIntent.getActivity(context, 2, actionIntent2, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notification_background)
                .setContentTitle("Título da Notificação")
                .setContentText("Texto da notificação")
                .setStyle(NotificationCompat.BigTextStyle().bigText("Poderia falar de muitas coisas nessa notificação porém só quero ver se ela está expandindo mesmo."))
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_iconaction1_background, "voltar para o app1", actionPendingIntent1)
                .addAction(R.drawable.ic_iconaction2_background, "voltar para o app2", actionPendingIntent2)
                .setAutoCancel(true) // A notificação desaparece ao clicar
                .build()

            with(NotificationManagerCompat.from(context)) {
                notify(notificationId, notification)
            }
        } else {
            // solicitar a permissão
            ActivityCompat.requestPermissions(context as Activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_CODE_POST_NOTIFICATIONS)
        }
    }
}
