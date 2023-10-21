package com.example.composedweather.ui.feature.search

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.data.model.response.LocationResponseItem
import com.example.composedweather.ui.common.ComposedWeatherAppBarUI
import com.example.composedweather.ui.common.ContentLoaderUI
import com.example.composedweather.ui.theme.ComposedWeatherTheme
import java.text.DecimalFormat

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DetailUI(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchLocationViewModel = hiltViewModel()
) {

    val query by viewModel.uiQuery.collectAsState()
    val searchResult = viewModel.searchResults
    val state by viewModel.state.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val controller = LocalSoftwareKeyboardController.current
    var isEditMode by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }


    LaunchedEffect(Unit) {
        controller?.show()
        focusRequester.requestFocus()
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Blue)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                if (isEditMode) {
                    focusManager.clearFocus()
                }
            },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            ComposedWeatherAppBarUI(
                title = "Search Location",
                backButtonIcon = Icons.Rounded.ArrowBack,
                onBackPressed = onBackPressed
            )
        }
    ) { paddingValues ->


        LaunchedEffect(state.error) {
            if (state.error != null) {
                val message = if (state.isConnected.not()) "Please connect to a stable network" else state.error
                    ?: "Something went wrong.\n Please try again later."
                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.resetError()
            }
        }

        Column(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .fillMaxSize()
                .padding(horizontal = 12.dp),
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = {
                    viewModel.updateSearchQuery(it)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Leading Icon"
                    )
                },
                placeholder = { Text("Enter street, city, district or state") },
                maxLines = 1,
                trailingIcon = {
                    if (query.isNotBlank()) {
                        Icon(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.LightGray)
                                .clickable {
                                    viewModel.updateSearchQuery("")
                                },
                            tint = Color.Black,
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Clear Search Trailing Icon"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.searchLocation()
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 8.dp,
                        horizontal = 16.dp
                    )
                    .onFocusChanged {
                        isEditMode = it.isFocused
                    }
                    .focusRequester(focusRequester)
            )

            AnimatedContent(
                targetState = state.isLoading,
                label = "Content Loader"
            ) { isLoading ->
                if (isLoading) {
                    ContentLoaderUI()
                } else {
                    SearchResultContentUI(
                        query = query,
                        state = state,
                        searchResult = searchResult,
                        onItemSelected = { item, latitude, longitude ->
                            viewModel.setLocationCoordinates(
                                locationResponseItem = item
                            )
                            Toast.makeText(
                                context, "Location Selected: $latitude , $longitude", Toast.LENGTH_SHORT
                            ).show()
                            onBackPressed()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchResultContentUI(
    query: String,
    state: SearchLocationViewState,
    searchResult: SnapshotStateList<LocationResponseItem>,
    onItemSelected: (LocationResponseItem, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (query.isNotEmpty()) {
        if (searchResult.isEmpty() && state.isInSearchMode) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "No such location found",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else if (searchResult.isNotEmpty()) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    vertical = 6.dp,
                    horizontal = 16.dp
                ), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    count = searchResult.size,
                    key = { index ->
                        searchResult[index].placeId + searchResult[index].osmId
                    }, contentType = {
                        "Location Search Items"
                    }
                ) { index ->
                    val item = searchResult[index]
                    LocationItem(
                        item = item,
                        onItemClicked = { locationResponseItem, latitude, longitude ->
                            onItemSelected(locationResponseItem, latitude, longitude)
                        }
                    )
                }
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Search For a location",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun LocationItem(
    item: LocationResponseItem,
    onItemClicked: (LocationResponseItem, String, String) -> Unit,
    modifier: Modifier = Modifier
) {

    val latitude = remember(item.lat) {
        DecimalFormat("#.##").format(item.lat.toDouble())
    }

    val longitude = remember(item.lon) {
        DecimalFormat("#.##").format(item.lon.toDouble())
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .clickable {
                onItemClicked(item, latitude, longitude)
            }
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = item.displayName,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "Locality: ${item.name}",
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Area Type: ${item.addressType}, ${item.type}",
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Latitude: $latitude",
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "Longitude: $longitude",
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview
@Composable
fun HomeUIPreview() {
    ComposedWeatherTheme {
        DetailUI(onBackPressed = {})
    }
}