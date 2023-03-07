package com.raghav.cachingmvvm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.raghav.cachingmvvm.repository.Repository
import com.raghav.cachingmvvm.utils.Constants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlin.random.Random

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted ctx: Context,
    @Assisted params: WorkerParameters,
    private val repository: Repository
) : CoroutineWorker(ctx, params) {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            Random.nextInt(),
            NotificationCompat.Builder(appContext)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Worker has started")
                .setContentText("syncing data from server")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(LongArray(0))
                .build()
        )
    }

    private val appContext = applicationContext
    override suspend fun doWork(): Result {
        val sampleValue = inputData.getString(Constants.SAMPLE_KEY) ?: return Result.failure()
        showNotification(sampleValue)
        return try {
            val refreshSuccessful = repository.refreshArticlesInDb()
            if (refreshSuccessful) {
                Log.i(TAG, "Worker was successful")
                Result.success()
            } else {
                Log.i(TAG, "Worker was not successful will retry")
                Result.retry()
            }
        } catch (t: Throwable) {
            Log.e(TAG, "Worker failed:  ${t.localizedMessage}")
            t.printStackTrace()
            Result.failure()
        }
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
                    .setContentText("syncing data from server")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVibrate(LongArray(0))
                    .build()
            )
        )
    }

    companion object {
        private const val TAG = "SyncWorker"
    }
}
