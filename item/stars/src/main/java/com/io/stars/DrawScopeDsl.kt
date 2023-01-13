package com.io.stars

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.*
import com.io.core.ui.Palette

internal fun DrawScope.drawStar(
    path: Path,
    starsPath: Path,
    topLeft: Offset,
    sizeOneStar: Size,
    palette: Palette,
){
    drawPath(
        path.star(topLeft, sizeOneStar)
            .also { currentStarPath -> starsPath.addPath(currentStarPath) },
        color = palette.contentSecondary,
        style = pathStarStroke
    )
}

private val pathStarStroke = Stroke(
    width = 3f
)