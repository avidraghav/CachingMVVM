package com.raghav.cachingmvvm.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.raghav.cachingmvvm.databinding.ActivityMainBinding
import com.raghav.cachingmvvm.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.composeView.setContent {
            val articles = viewModel.articles.collectAsState().value

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items = articles, key = { it.id }) {
                    ItemArticle(article = it)
                }
            }
        }
        viewModel.performChainedWork()

        viewModel.outputWorkInfos.observe(this) { workInfos ->

            val syncWorkInfo = workInfos.find {
                it.tags.contains("SyncWorker")
            }
            val fakeWorkerInfo = workInfos.find {
                it.tags.contains("FakeWorker")
            }

            Log.d(
                TAG,
                "SyncWorker " + syncWorkInfo?.state?.name.orEmpty()
            )
            Log.d(
                TAG,
                "FakeWorker " + fakeWorkerInfo?.state?.name.orEmpty()
            )
            Log.d(
                TAG,
                "SyncWorker " + syncWorkInfo?.outputData?.getString(Constants.OUTPUT_FROM_SYNC_WORKER)
                    .orEmpty()
            )
            Log.d(
                TAG,
                "FakeWorker " + fakeWorkerInfo?.outputData?.getString(Constants.OUTPUT_FROM_FAKE_WORKER)
                    .orEmpty()
            )
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
