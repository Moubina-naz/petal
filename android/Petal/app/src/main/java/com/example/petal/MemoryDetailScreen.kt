package com.example.petal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MemoryDetailScreen(

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF8F4))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment= Alignment.CenterVertically
        ){
            IconButton(onClick = {/*later*/}) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )

            }
            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { /* favorite */ }) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = Color(0xFFB07A7A)
                )
            }

            IconButton(onClick = { /* menu */ }) {
                Icon(Icons.Default.MoreVert, contentDescription = "More")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "OCTOBER 24 · SEASIDE CAFE",
            fontSize = 12.sp,
            letterSpacing = 1.sp,
            color = Color(0xFF9C8F86)
        ) //date

        Spacer(modifier = Modifier.height(16.dp))
//title
        Text(
            text = "Morning\nQuiet",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF3E2F26),
            modifier = Modifier.weight(1f)
        )
        //mood
        Box(modifier = Modifier
            .background(color = Color(0xFFFCD1D1),
                shape = RoundedCornerShape(16.dp)
            )) {
            Text(
                text = "✳ Calm",
                fontSize = 12.sp,
                color = Color(0xFF8A5A5A)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        //Body
        Text(
            text = """
The light hitting the kitchen table was perfect today. It's funny how a single beam of dust-mote filled sun can change the entire frequency of a morning.

I sat there for twenty minutes with my coffee, not scrolling, not planning, just watching the way the shadows moved across the worn wood. I felt a sense of calm I haven't felt in weeks—a rare, grounding stillness.

Sometimes the most important moments are the ones where absolutely nothing happens. Just the steam rising and the soft hum of the refrigerator. I want to remember this feeling when things get loud again.
            """.trimIndent(),
            fontSize = 16.sp,
            lineHeight = 26.sp,
            color = Color(0xFF5C5048)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "CAPTURED MOMENTS",
            fontSize = 12.sp,
            letterSpacing = 1.sp,
            color = Color(0xFF9C8F86)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.heightIn(max = 420.dp)        ){
            items(4){
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFEDE6DE))


                )
            }
        }
    }
}