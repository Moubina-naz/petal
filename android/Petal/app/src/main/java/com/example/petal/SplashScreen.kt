package com.example.petal

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay

// Colors
val Cream = Color(0xFFF5F0EB)
val DarkGreen = Color(0xFF1E3A2F)
val Terracotta = Color(0xFFCB6040)
val SubtleRed = Color(0xFFB85C5C)

@Composable
fun PetalSplashScreen(onFinished: () -> Unit = {}) {
    var visible by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = EaseInOut),
        label = "alpha"
    )

    val logoAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 1200, delayMillis = 300, easing = EaseInOut),
        label = "logoAlpha"
    )

    val bottomAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 700, easing = EaseInOut),
        label = "bottomAlpha"
    )

    LaunchedEffect(Unit) {
        visible = true
        delay(3000)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream),
        contentAlignment = Alignment.Center
    ) {
        // Top decorative line
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 60.dp)
                .alpha(alpha)
        ) {
            Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(1.5.dp)
                    .background(SubtleRed.copy(alpha = 0.6f))
            )
        }

        // Center content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.alpha(logoAlpha)
        ) {
            // Petal drop shape
            Canvas(
                modifier = Modifier.size(56.dp, 72.dp)
            ) {
                drawPetalShape(this)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // "Petal" serif title
            Text(
                text = "Petal",
                color = DarkGreen,
                fontSize = 52.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.Serif,
                letterSpacing = 0.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Subtitle
            Text(
                text = "WHERE MEMORIES LIVE",
                color = DarkGreen.copy(alpha = 0.7f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 4.sp,
                fontFamily = FontFamily.SansSerif
            )
        }

        // Bottom section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .alpha(bottomAlpha)
        ) {
            // Three dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .background(
                                color = DarkGreen.copy(alpha = if (index == 1) 0.5f else 0.25f),
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))


        }
    }
}

fun drawPetalShape(scope: DrawScope) {
    val path = Path().apply {
        val w = scope.size.width
        val h = scope.size.height

        // Shift the tip rightward by ~20% of width
        val tipX = w * 0.65f  // was w/2f (center), now shifted right
        val tipY = 0f

        moveTo(tipX, tipY)
        cubicTo(
            w * 1.2f, h * 0.25f,   // right control point (wider right lean)
            w * 1.05f, h * 0.75f,
            w / 2f, h              // base stays centered
        )
        cubicTo(
            w * -0.05f, h * 0.75f, // left side pulled in slightly
            w * 0.15f, h * 0.25f,  // left control pulled right (asymmetry)
            tipX, tipY             // back to shifted tip
        )
        close()
    }
    scope.drawPath(
        path = path,
        color = Terracotta
    )
}

@Composable
fun PetalIcon(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(72.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(34.dp, 44.dp)
        ) {
            drawPetalShape(this)
        }
    }
}

@Composable
@Preview
fun PetalSplashScreenPreview() {
    PetalSplashScreen()
}