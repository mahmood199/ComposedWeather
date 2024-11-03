package com.weather.forecastify.app.ui.common

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.weather.forecastify.app.ui.theme.ForecastifyTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun CustomSideDrawerOverlay(
    isDrawerOpen: Boolean,
    onDismiss: () -> Unit,
    drawerContent: @Composable ColumnScope.() -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    drawerWidth: Dp = 300.dp,
    animationDuration: Int = 300,
    maskColor: Color = Color.Black.copy(alpha = 0.5f),
    showMask: Boolean = false,
    drawerSide: DrawerSide = DrawerSide.RIGHT,
    cornerRadius: Dp = 32.dp,
    dragThresholdFraction: Float = 0.5f,
    enableSwipe: Boolean = true,
) {
    val scope = rememberCoroutineScope()

    val density = LocalDensity.current

    val drawerWidthPx = with(density) {
        drawerWidth.toPx()
    }

    val offsetX =
        remember {
            Animatable(
                initialValue = if (isDrawerOpen) 0f
                else
                    drawerWidthPx * (if (drawerSide == DrawerSide.LEFT) -1 else 1)
            )
        }

    LaunchedEffect(isDrawerOpen) {
        val targetOffsetX = if (isDrawerOpen) 0f
        else drawerWidthPx * (if (drawerSide == DrawerSide.LEFT) -1 else 1)
        offsetX.animateTo(
            targetValue = targetOffsetX,
            animationSpec = tween(durationMillis = animationDuration)
        )
    }

    if (isDrawerOpen) {
        BackHandler {
            onDismiss()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        content()

        if (isDrawerOpen and showMask) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(maskColor)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { onDismiss() })
                    }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(drawerWidth)
                .offset {
                    IntOffset(x = 2 * offsetX.value.roundToInt(), y = 0)
                }
                .align(if (drawerSide == DrawerSide.LEFT) Alignment.CenterStart else Alignment.CenterEnd)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = if (cornerRadius > 0.dp) {
                        if (drawerSide == DrawerSide.LEFT) {
                            RoundedCornerShape(topEnd = cornerRadius, bottomEnd = cornerRadius)
                        } else {
                            RoundedCornerShape(topStart = cornerRadius, bottomStart = cornerRadius)
                        }
                    } else {
                        RectangleShape
                    }
                )
                .pointerInput(Unit) {
                    if (enableSwipe) {
                        detectDragGestures (
                            onDragEnd = {
                                scope.launch {
                                    val shouldClose = when (drawerSide) {
                                        DrawerSide.LEFT -> offsetX.value < -drawerWidthPx * dragThresholdFraction
                                        DrawerSide.RIGHT -> offsetX.value > drawerWidthPx * dragThresholdFraction
                                    }

                                    val finalTarget = if (shouldClose) {
                                        drawerWidthPx * (if (drawerSide == DrawerSide.LEFT) -1 else 1)
                                    } else {
                                        0f
                                    }

                                    offsetX.animateTo(
                                        targetValue = finalTarget,
                                        animationSpec = tween(durationMillis = animationDuration)
                                    )

                                    if (shouldClose) {
                                        onDismiss()
                                    }
                                }
                            }
                        ) { change, dragAmount ->
                            change.consume()

                            scope.launch {
                                val newOffset = offsetX.value + dragAmount.x

                                val clampedOffset = when (drawerSide) {
                                    DrawerSide.LEFT -> newOffset.coerceIn(-drawerWidthPx, 0f)
                                    DrawerSide.RIGHT -> newOffset.coerceIn(0f, drawerWidthPx)
                                }

                                offsetX.snapTo(clampedOffset)
                            }
                        }
                    }
                }
                .then(
                    if (isDrawerOpen) Modifier.shadow(elevation = 16.dp, shape = RoundedCornerShape(cornerRadius))
                    else Modifier
                )        ) {
            drawerContent()

        }
    }


}

sealed interface DrawerSide {
    data object RIGHT : DrawerSide
    data object LEFT : DrawerSide
}

@Preview
@Composable
private fun CustomSideDrawerOverlayPreview() {
    ForecastifyTheme {
        CustomSideDrawerOverlay(
            isDrawerOpen = true,
            onDismiss = {},
            drawerContent = {},
            content = {}
        )
    }
}