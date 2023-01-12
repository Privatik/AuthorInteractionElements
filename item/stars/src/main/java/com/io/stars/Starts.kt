package com.io.stars

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private fun startPath(mid: Float, half: Float) = Path().apply {
        // top left
    moveTo(mid + half * 0.5f, half * 0.84f)
    // top right
    lineTo(mid + half * 1.5f, half * 0.84f)
    // bottom left
    lineTo(mid + half * 0.68f, half * 1.45f)
    // top tip
    lineTo(mid + half * 1.0f, half * 0.5f)
    // bottom right
    lineTo(mid + half * 1.32f, half * 1.45f)
    // top left
    lineTo(mid + half * 0.5f, half * 0.84f)

    close()
}

@Composable
fun Stars(
    modifier: Modifier = Modifier,
    countStars: Int = 5,
    sizeOnStar: Dp = 50.dp
){
    val mid = remember { sizeOnStar.value }
    val half = remember { 10f }

    Canvas(modifier = modifier){
        drawPath(
            path = startPath(mid, half),
            color = Color.Red,
            style = Stroke(
                width = 4f
            )
        )
    }
}