package com.raghav.cachingmvvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.raghav.cachingmvvm.databinding.ActivityMainBinding
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
        viewModel.schedulePeriodicArticlesSync()
    }
}
