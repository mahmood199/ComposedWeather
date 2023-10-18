package com.example.composedweather.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.composedweather.ui.theme.ComposedWeatherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposedWeatherAppBarUI(
    title: String,
    backButtonIcon: ImageVector,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.padding(horizontal = 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onBackPressed) {
                    Icon(
                        imageVector = backButtonIcon,
                        contentDescription = "TopBar Icon",
                    )
                }
                Text(text = title)
            }
        },
        modifier = modifier
            .zIndex(20f)
    )
}

@Preview
@Composable
fun ComposedWeatherAppBarUIPreview() {
    ComposedWeatherTheme {
        ComposedWeatherAppBarUI(
            title = "Home",
            backButtonIcon = Icons.Default.ArrowBack,
            onBackPressed = {}
        )
    }
}