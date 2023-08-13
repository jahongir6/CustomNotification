package com.example.customnotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_NO_CREATE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.customnotification.databinding.ActivityMainBinding

/*notification telifon ekranda korinib turadigon ui companentalardan biri
  foydalanuvchiga eslatmalar oz vaqtida berish uchun sizning ilovangiz
  interfeysdan tashqarida korsatiladigon xabar xisoblanadi*/
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var notificationManager: NotificationManagerCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationManager = NotificationManagerCompat.from(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name) //qaysi app dan chiqishini bilish uchun
            val descriptionText = getString(R.string.app_name)//bunga ham app_name ni berip turipmiz
            val importanse = NotificationManager.IMPORTANCE_DEFAULT//muhimliligi importans default
            val channel = NotificationChannel("1", name, importanse).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }
        binding.hideNotification.setOnClickListener {

        }
        binding.showNotification.setOnClickListener {
            notificationManager.notify(1, notifyBuilder())
        }
    }


    private fun notifyBuilder(): Notification {
        val collapsedView = RemoteViews(
            packageName,
            R.layout.notification_collapsed
        )//kichkina notification turadigon joyi
        val expandedView =
            RemoteViews(packageName, R.layout.notification_expanded)//kengayadigon notification

        val clickIntent = Intent(this, NotificationReceiver::class.java)
        val clickPendingIntent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // bu yerda agar versiya kichik boladigon bolsa shuni ishlat aks holda uni
                PendingIntent.getBroadcast(this, 0, clickIntent, FLAG_IMMUTABLE)
            } else {
                PendingIntent.getBroadcast(this, 0, clickIntent, FLAG_NO_CREATE)
            }

        collapsedView.setTextViewText(
            R.id.textViewCollaps,
            "This is Collapse!"
        ) // callepsga shuni yoz deyapti

        expandedView.setImageViewResource(
            R.id.image_view_expanded,
            R.drawable.ic_launcher_background
        )//image ga esa shuni qoyasan deyapti
        expandedView.setOnClickPendingIntent(
            R.id.image_view_expanded,
            clickPendingIntent
        )//image viewbosilganda notificationResive ni ishlatgin deyapti

        return NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.drawable.ic_launcher_foreground).setCustomContentView(collapsedView)
            .setCustomBigContentView(expandedView)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle()).build()
    }
}