package com.example.petal

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun PetalSplashScreen(onFinished: () -> Unit = {}) {

    val colors = MaterialTheme.colorScheme

    var visible by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(1000)
    )

    val logoAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(1200, delayMillis = 300)
    )

    LaunchedEffect(Unit) {
        visible = true
        delay(2500)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
        contentAlignment = Alignment.Center
    ) {

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
                    .background(colors.primary.copy(alpha = 0.5f))
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.alpha(logoAlpha)
        ) {

            Canvas(modifier = Modifier.size(56.dp, 72.dp)) {
                drawPetalShape(this, colors.primary)
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Petal",
                style = MaterialTheme.typography.headlineLarge,
                color = colors.onBackground
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "WHERE MEMORIES LIVE",
                style = MaterialTheme.typography.labelSmall,
                color = colors.onSurfaceVariant
            )
        }
    }
}

fun drawPetalShape(scope: DrawScope, color: androidx.compose.ui.graphics.Color) {
    val path = Path().apply {
        val w = scope.size.width
        val h = scope.size.height

        val tipX = w * 0.65f

        moveTo(tipX, 0f)
        cubicTo(
            w * 1.2f, h * 0.25f,
            w * 1.05f, h * 0.75f,
            w / 2f, h
        )
        cubicTo(
            w * -0.05f, h * 0.75f,
            w * 0.15f, h * 0.25f,
            tipX, 0f
        )
        close()
    }

    scope.drawPath(path, color)
}


@Composable
fun PetalIcon(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = modifier.size(72.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(34.dp, 44.dp)) {
            drawPetalShape(this, colors.primary)
        }
    }
}

@Composable
@Preview
fun PetalSplashScreenPreview() {
    PetalSplashScreen()
}