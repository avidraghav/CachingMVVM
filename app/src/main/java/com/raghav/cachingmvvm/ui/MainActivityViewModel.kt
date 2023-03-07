package com.raghav.cachingmvvm.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.raghav.cachingmvvm.FakeWorker
import com.raghav.cachingmvvm.SyncWorker
import com.raghav.cachingmvvm.data.model.ArticlesResponseItem
import com.raghav.cachingmvvm.repository.Repository
import com.raghav.cachingmvvm.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    private val _articles: MutableStateFlow<List<ArticlesResponseItem>> = MutableStateFlow(
        emptyList()
    )
    val articles = _articles.asStateFlow()

    private val workManger by lazy {
        WorkManager.getInstance(getApplication<Application>().applicationContext)
    }
    val outputWorkInfos: LiveData<List<WorkInfo>> =
        workManger.getWorkInfosForUniqueWorkLiveData("Chained Work")

    init {
        getArticles()
    }

    private fun getArticles() {
        viewModelScope.launch {
            _articles.emit(repository.getArticles())
        }
    }

    fun schedulePeriodicArticlesSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        ).addTag("SyncWorker")
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                30_000L,
                TimeUnit.MILLISECONDS
            )
            .setInputData(
                workDataOf(Constants.INITIAL_INPUT to "initial input from app")
            )
            .build()

        workManger.enqueueUniquePeriodicWork(
            "sync_articles_from_server",
            ExistingPeriodicWorkPolicy.REPLACE,
            syncWorkRequest
        )
    }

    fun performChainedWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncWorkRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .addTag("SyncWorker")
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                30_000L,
                TimeUnit.MILLISECONDS
            )
            .setInputData(
                workDataOf(Constants.INITIAL_INPUT to "initial input from app")
            )
            .build()

        val fakeWorkRequest = OneTimeWorkRequestBuilder<FakeWorker>()
            .addTag("FakeWorker")
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                30_000L,
                TimeUnit.MILLISECONDS
            ).build()

        workManger.beginUniqueWork(
            "Chained Work",
            ExistingWorkPolicy.REPLACE,
            syncWorkRequest
        ).then(fakeWorkRequest).enqueue()
    }
}
