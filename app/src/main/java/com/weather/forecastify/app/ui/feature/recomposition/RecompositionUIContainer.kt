package com.weather.forecastify.app.ui.feature.recomposition

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.weather.forecastify.app.ui.theme.ForecastifyTheme
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import okhttp3.internal.toImmutableList

@Composable
fun RecompositionUIContainer(
    modifier: Modifier = Modifier
) {
    val (isChecked, onChecked) = remember { mutableStateOf(false) }


    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(Color.White),
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onChecked
        )
        ContactList(
            contactListState = ContactListState(
                names = remember {
                    mutableListOf("name1", "name2", "name3", "name4").toPersistentList()
                },
            )
        )
    }
}

@Composable
fun ContactList(
    contactListState: ContactListState
) {
    if (contactListState.isLoading) {
        CircularProgressIndicator()
    } else {
        Text(text = contactListState.names.toString())
    }
}

@Stable
data class ContactListState(
    val names: PersistentList<String>,
    val isLoading: Boolean = false,
)

@Preview
@Composable
private fun RecompositionUIPreview() {
    ForecastifyTheme {
        RecompositionUIContainer(modifier = Modifier.fillMaxSize())
    }
}