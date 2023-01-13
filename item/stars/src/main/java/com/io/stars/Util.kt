package com.io.stars

import android.graphics.Region
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.toAndroidRect

internal fun Path.star(
    topLeft: Offset,
    size: Size,
): Path {
    val borderRegion = Region(
        Rect(
            left = topLeft.x,
            top = topLeft.y,
            right = topLeft.x + size.width,
            bottom = topLeft.y + size.height,
        ).toAndroidRect()
    )

    val region = Region()

    reset()

    // top left
    moveTo(topLeft.x, topLeft.y + (size.height * 0.35f))
    // top right
    lineTo(topLeft.x + size.width, topLeft.y + (size.height * 0.35f))
    // bottom left
    lineTo(topLeft.x + (size.width * 0.17f), topLeft.y + size.height)
    // top tip
    lineTo(topLeft.x + (size.width * 0.5f), topLeft.y)
    // bottom right
    lineTo(topLeft.x + (size.width * 0.83f), topLeft.y + size.height)

    close()

    region.setPath(this.asAndroidPath(), borderRegion)
    return region.boundaryPath.asComposePath()
}

internal fun List<Animatable<Offset, AnimationVector2D>>.indexClickedOnStar(
    clickOffset: Offset,
    size: Size,
): Int{
    var index = -1
    var counter = 0
    while (index != 1 && counter != this.size){
        val currentOffset = get(counter).value
        if (clickOffset.x in currentOffset.x..(currentOffset.x + size.width)
            && clickOffset.y in currentOffset.y..(currentOffset.y + size.height)){
            index = counter
        }
        counter++
    }
    return index
}
