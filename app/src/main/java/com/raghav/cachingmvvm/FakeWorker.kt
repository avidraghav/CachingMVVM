package com.raghav.cachingmvvm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.raghav.cachingmvvm.utils.Constants
import kotlinx.coroutines.delay
import kotlin.random.Random

// this worker is just to demonstrate chaining of Workers
class FakeWorker(
    ctx: Context,
    params: WorkerParameters
) : CoroutineWorker(ctx, params) {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            Random.nextInt(),
            NotificationCompat.Builder(appContext)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Worker has started")
                .setContentText("Fake Worker")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(LongArray(0))
                .build()
        )
    }

    private val appContext = applicationContext
    override suspend fun doWork(): Result {
        val input =
            inputData.getString(Constants.OUTPUT_FROM_SYNC_WORKER) ?: return Result.failure()
        showNotification(input)

        delay(5000)
        return Result.success(
            workDataOf(Constants.OUTPUT_FROM_FAKE_WORKER to "output from fake worker")
        )
    }

    private suspend fun showNotification(value: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val name = "default_channel"
            val description = "description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(Constants.CHANNEL_ID, name, importance)
            channel.description = description

            // Add the channel
            val notificationManager =
                appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)
        }
        setForeground(
            ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(appContext, Constants.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Worker has started $value")
                    .setContentText("Fake Worker")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVibrate(LongArray(0))
                    .build()
            )
        )
    }

    companion object {
        private const val TAG = "FakeWorker"
    }
}
