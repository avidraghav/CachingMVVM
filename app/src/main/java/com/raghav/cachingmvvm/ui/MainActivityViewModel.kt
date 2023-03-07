package com.raghav.cachingmvvm.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.raghav.cachingmvvm.SyncWorker
import com.raghav.cachingmvvm.data.model.ArticlesResponseItem
import com.raghav.cachingmvvm.repository.Repository
import com.raghav.cachingmvvm.utils.AppApplication
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
                workDataOf(Constants.SAMPLE_KEY to "sample_value")
            )
            .build()

        workManger.enqueueUniquePeriodicWork(
            "sync_articles_from_server",
            ExistingPeriodicWorkPolicy.REPLACE,
            syncWorkRequest
        )
    }
}
