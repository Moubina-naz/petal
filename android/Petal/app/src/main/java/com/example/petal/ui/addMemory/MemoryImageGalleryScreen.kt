package com.example.petal.ui.addMemory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import coil.compose.AsyncImage


class MemoryImageGalleryScreen(
    private val images: List<String>,
    private val initialIndex: Int = 0
) : Screen {

    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val pagerState = rememberPagerState(
            initialPage = initialIndex,
            initialPageOffsetFraction = 0f
        ) {
            images.size  // ← page count
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)  // dark bg for gallery feel
        ) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = "Memory image ${page + 1}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}