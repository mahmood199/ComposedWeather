package com.weather.forecastify.app.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

sealed class UIResult {
    data object Success : UIResult()
    data object Failure : UIResult()
    data object Loading : UIResult()
}

class UserPreviewParameterProvider : PreviewParameterProvider<UIResult> {
    override val values: Sequence<UIResult>
        get() = sequenceOf(
            UIResult.Success,
            UIResult.Loading,
            UIResult.Failure
        )
}

@Preview(name = "UserPreview")
@Composable
private fun UserPreview(
    @PreviewParameter(provider = UserPreviewParameterProvider::class) uiResult: UIResult
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                when (uiResult) {
                    UIResult.Failure -> Color.Red
                    UIResult.Loading -> Color.Blue
                    UIResult.Success -> Color.Yellow
                }
            )
    ) {
        when (uiResult) {
            UIResult.Failure -> Text(text = "$uiResult")
            UIResult.Loading -> Text(text = "$uiResult")
            UIResult.Success -> Text(text = "$uiResult")
        }
    }
}