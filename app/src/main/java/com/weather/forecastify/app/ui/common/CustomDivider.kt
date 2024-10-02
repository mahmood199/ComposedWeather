package com.weather.forecastify.app.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weather.forecastify.app.ui.theme.ForecastifyTheme

@Composable
fun CustomDivider(
    modifier: Modifier = Modifier
) {


}

@Composable
fun Demo_VerticalDivider(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(16.dp)
            .height(IntrinsicSize.Min)
    ) {
        Text("Item 1", fontSize = 18.sp)
        VerticalDivider(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .rotate(25f),
            thickness = 2.dp,
            color = Color.Blue
        )
        SegmentPathDivider()
        Text("Item 2", fontSize = 18.sp)
    }
}

@Composable
fun SegmentPathDivider(
    modifier: Modifier = Modifier,
    width: Dp = 20.dp,
    thickness: Dp = DividerDefaults.Thickness,
    color: Color = DividerDefaults.color,
) = Canvas(
    modifier
        .fillMaxHeight()
        .width(width)
) {
    drawLine(
        color = color,
        strokeWidth = thickness.toPx(),
        start = Offset(thickness.toPx() / 2 + width.toPx() * 0.25f, size.height),
        end = Offset(thickness.toPx() / 2 + width.toPx() * 0.75f, 0f)
    )
}

@Preview
@Composable
private fun CustomDividerPreview() {
    ForecastifyTheme {
        Demo_VerticalDivider()
    }
}