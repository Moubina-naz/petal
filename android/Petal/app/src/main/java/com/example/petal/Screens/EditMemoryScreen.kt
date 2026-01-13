package com.example.petal.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun EditMemoryScreen(
    navController: NavController
) {
    var isFavorite by rememberSaveable { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var title by rememberSaveable { mutableStateOf("Morning Quiet") }
    var body by rememberSaveable {
        mutableStateOf(
            "The light hitting the kitchen table was perfect today. It's funny how a single beam of dust-mote filled sun can change the entire frequency of a morning."
        )
    }

    var selectedMood by rememberSaveable { mutableStateOf("Calm") }

    val images = remember {
        mutableStateListOf(1, 2, 3)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF8F4))
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "cancel",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF615A57),
                modifier = Modifier.clickable { /* later */ }

            )
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "save",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF9E6F73),
                modifier = Modifier.clickable { /* later */ }

            )


        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFFECEEFF),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "October 24",
                        fontSize = 12.sp,
                        letterSpacing = 1.sp,
                        color = Color(0xFF9C8F86)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Color(0xFF9E6F73),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFFECEEFF),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Location",
                        fontSize = 12.sp,
                        letterSpacing = 1.sp,
                        color = Color(0xFF9C8F86)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = Color(0xFF9E6F73),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .background(
                    color = Color(0xFFFCD1D1),
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Time",
                    fontSize = 12.sp,
                    letterSpacing = 1.sp,
                    color = Color(0xFF9C8F86)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFF9E6F73),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        //title
        Text(
            text = "Morning Quiet",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF3E2F26),
            modifier = Modifier.clickable { }

        )
        Spacer(modifier = Modifier.height(16.dp))

        //mood
        Box(modifier = Modifier
            .background(
                color = Color(0xFFFCD1D1),
                shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "✳ Calm",
                    fontSize = 12.sp,
                    color = Color(0xFF8A5A5A)
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "down",
                    tint = Color(0xFF)
                )
            }
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
            modifier = Modifier.heightIn(max = 420.dp)
        ) {

            items(images) { img ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFEDE6DE))
                ) {
                    IconButton(
                        onClick = { images.remove(img) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .size(24.dp)
                            .background(Color.White, RoundedCornerShape(50))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove",
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            if (images.size < 5) {
                item {
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                1.dp,
                                Color(0xFFD6CCC2),
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { images.add(images.size + 1) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add image",
                            tint = Color(0xFFB0AAA3)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
@Preview
@Composable
fun EditMemoryScreenPreview(){
   // EditMemoryScreen()
}