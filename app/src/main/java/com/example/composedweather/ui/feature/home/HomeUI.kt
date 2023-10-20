package com.example.composedweather.ui.feature.home

import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.composedweather.data.models.request.Constants
import com.example.composedweather.data.models.response.Current
import com.example.composedweather.data.models.response.CurrentUnits
import com.example.composedweather.data.models.response.DailyForecast
import com.example.composedweather.data.models.response.HourlyForecast
import com.example.composedweather.ui.common.ComposedWeatherAppBarUI
import com.example.composedweather.ui.common.ContentLoaderUI
import com.example.composedweather.ui.common.SaveableLaunchedEffect
import com.example.composedweather.ui.theme.ComposedWeatherTheme
import com.example.composedweather.util.formatToAMPM
import com.example.composedweather.util.formatToDMMMY
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import java.text.DecimalFormat

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeUI(
    fusedLocationProviderClient: FusedLocationProviderClient,
    onBackPressed: () -> Unit,
    navigateToSearch: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    val dailyForecasts = viewModel.dailyForecasts
    val hourlyForecasts = viewModel.hourlyForecasts

    val todayWeather by viewModel.currentDayWeather.collectAsState()

    var showNetworkConnectivity by remember {
        mutableStateOf(false)
    }

    val permission = rememberPermissionState(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    val color by animateColorAsState(
        targetValue = if (state.isConnected) Color.Green else Color.DarkGray,
        label = "Bottom UI BG color"
    )

    SaveableLaunchedEffect(state.isConnected) {
        showNetworkConnectivity = true
        delay(1000)
        showNetworkConnectivity = false
    }

    Scaffold(
        topBar = {
            ComposedWeatherAppBarUI(
                title = "Home",
                onBackPressed = onBackPressed,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigateToSearch()
                },
                shape = RoundedCornerShape(25),
                containerColor = Color.DarkGray,
                elevation = FloatingActionButtonDefaults.elevation(12.dp),
                modifier = Modifier
                    .padding(end = 8.dp, bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Navigate to Search"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        snackbarHost = { SnackbarHost(snackBarHostState) },
        bottomBar = {
            AnimatedVisibility(
                visible = showNetworkConnectivity,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color)
                    .navigationBarsPadding()
                    .animateContentSize()
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    textAlign = TextAlign.Center,
                    text = if (state.isConnected) "Connected" else "Offline"
                )
            }
        }
    ) { paddingValues ->

        LaunchedEffect(state.error) {
            if (state.error != null) {
                val result = snackBarHostState.showSnackbar(
                    message =
                    if (state.isConnected.not())
                        "Please connect to a stable network"
                    else
                        state.error ?: "Something went wrong",
                    withDismissAction = true,
                    duration = SnackbarDuration.Short
                )
                when (result) {
                    SnackbarResult.ActionPerformed -> {
                        state.error = null
                    }

                    SnackbarResult.Dismissed -> {
                        state.error = null
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            HomeUiContent(
                state = state,
                permissionState = permission,
                hourlyForecasts = hourlyForecasts,
                dailyForecasts = dailyForecasts,
                todayWeather = todayWeather,
                modifyContent = {
                    viewModel.modifyState(state = it)
                },
                onPermissionGranted = {
                    fusedLocationProviderClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            location?.run {
                                viewModel.setLocationCoordinates(
                                    latitude = location.latitude,
                                    longitude = location.longitude,
                                )
                            }
                        }.addOnFailureListener {
                            viewModel.handleLocationError()
                        }
                },
            )
        }
    }
}

@OptIn(
    ExperimentalPermissionsApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun HomeUiContent(
    state: HomeViewState,
    permissionState: PermissionState,
    dailyForecasts: SnapshotStateList<DailyForecast>,
    hourlyForecasts: SnapshotStateList<HourlyForecast>,
    todayWeather: Pair<Current, CurrentUnits>?,
    modifyContent: (HomeViewState) -> Unit,
    onPermissionGranted: () -> Unit,
    modifier: Modifier = Modifier
) {
    SaveableLaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        val latitude = remember(state.weatherDataRequest.latitude) {
            DecimalFormat("#.##").format(state.weatherDataRequest.latitude)
        }

        val longitude = remember(state.weatherDataRequest.longitude) {
            DecimalFormat("#.##").format(state.weatherDataRequest.longitude)
        }

        val textToShow = remember(latitude, longitude) {
            if ((latitude.toDoubleOrNull() ?: 0.0) == 0.0 &&
                (longitude.toDoubleOrNull() ?: 0.0) == 0.0
            )
                "Please grant location access from settings or search for your location from below"
            else "Showing forecasts for Lat:- $latitude, Lon:- $longitude"
        }

        when (permissionState.status) {
            is PermissionStatus.Denied -> {
                Text(
                    text = textToShow,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(BorderStroke(2.dp, MaterialTheme.colorScheme.onPrimary))
                        .padding(16.dp)
                )
            }

            PermissionStatus.Granted -> {

                SaveableLaunchedEffect(state.weatherDataRequest.isLocationDetected) {
                    if (state.weatherDataRequest.isLocationDetected) {
                        onPermissionGranted()
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20))
                        .border(BorderStroke(4.dp, MaterialTheme.colorScheme.onPrimary))
                        .padding(16.dp)
                ) {
                    Text(
                        text = textToShow,
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .basicMarquee(100)
                    )
                }
            }
        }

        AnimatedContent(
            targetState = state.isLoading,
            label = "Forecast loading"
        ) { isLoading ->
            if (isLoading) {
                ContentLoaderUI()
            } else {
                WeatherContentUI(
                    state = state,
                    hourlyForecasts = hourlyForecasts,
                    dailyForecasts = dailyForecasts,
                    modifyContent = modifyContent,
                    todayWeather = todayWeather
                )
            }
        }
    }
}


@Composable
fun WeatherContentUI(
    state: HomeViewState,
    hourlyForecasts: SnapshotStateList<HourlyForecast>,
    dailyForecasts: SnapshotStateList<DailyForecast>,
    todayWeather: Pair<Current, CurrentUnits>?,
    modifyContent: (HomeViewState) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {

        var showTodaysInfo by rememberSaveable {
            mutableStateOf(true)
        }

        var showHourlyForecast by rememberSaveable {
            mutableStateOf(true)
        }

        Filters(
            state = state,
            showTodaysInfo = showTodaysInfo,
            showHourlyForecast = showHourlyForecast,
            toggleTodaysWeatherVisibility = {
                showTodaysInfo = showTodaysInfo.not()
            },
            toggleHourlyForecast = {
                showHourlyForecast = showHourlyForecast.not()
            },
            modifyContent = modifyContent
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            item(
                key = "TodaysWeatherInfo",
                contentType = {
                    "TodaysWeatherContent"
                }
            ) {
                AnimatedVisibility(
                    visible = (todayWeather != null && showTodaysInfo),
                    label = "Today Weather Header"
                ) {
                    todayWeather?.let {
                        TodayWeatherItem(it)
                    }
                }
            }

            item(
                key = "HourlyForecast",
                contentType = {
                    "HourlyForecastRow"
                }
            ) {
                AnimatedVisibility(
                    visible = showHourlyForecast,
                    label = "Hourly Forecast Row"
                ) {
                    HourlyForecastRow(
                        hourlyForecasts = hourlyForecasts,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            item(
                key = "WeeklyForecastHeader123",
                contentType = {
                    "Weekly Forecast Header"
                }
            ) {
                AnimatedVisibility(visible = dailyForecasts.isNotEmpty()) {
                    Text(
                        text = "Weekly Forecast",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                    )
                }
            }
            items(
                count = dailyForecasts.size,
                key = { index ->
                    dailyForecasts[index].time + index
                },
                contentType = {
                    "Weekly Forecast Info"
                }
            ) { index ->
                DailyWeatherItem(dailyForecasts[index])
            }
        }
    }
}

@Composable
fun HourlyForecastRow(
    hourlyForecasts: SnapshotStateList<HourlyForecast>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item(key = "HourlyForeCast Start Item") {
            AnimatedVisibility(hourlyForecasts.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Time")
                    Text("Temperature")
                    Text("Humidity")
                }
            }
        }
        items(
            count = hourlyForecasts.size,
            key = { index ->
                hourlyForecasts[index].time +
                        hourlyForecasts[index].temperature +
                        hourlyForecasts[index].relativeHumidity
            }, contentType = {
                "Hourly Forecast Item"
            }
        ) { index ->
            HourlyForecastItem(
                hourlyForecast = hourlyForecasts[index],
            )
        }
    }
}

@Composable
fun HourlyForecastItem(
    hourlyForecast: HourlyForecast,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        val time = remember(hourlyForecast) {
            hourlyForecast.time.formatToAMPM()
        }

        val temperature = remember(hourlyForecast) {
            "${hourlyForecast.temperature}${hourlyForecast.temperatureUnit}"
        }

        val relativeHumidity = remember(hourlyForecast) {
            "${hourlyForecast.relativeHumidity}${hourlyForecast.relativeHumidityUnit}"
        }

        Text(text = time)
        Text(text = temperature)
        Text(text = relativeHumidity)
    }
}

@Composable
fun TodayWeatherItem(
    todayWeather: Pair<Current, CurrentUnits>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .clip(RoundedCornerShape(12))
            .background(MaterialTheme.colorScheme.inversePrimary)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val current = remember(todayWeather) {
            todayWeather.first
        }
        val currentUnits = remember(todayWeather) {
            todayWeather.second
        }

        Text(
            text = "Currently",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold
        )

        Text(
            text = "${current.apparent_temperature}${currentUnits.apparent_temperature}",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold
        )

        Text(
            text = "Feels like ${current.temperature_2m}${currentUnits.temperature_2m}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun Filters(
    state: HomeViewState,
    showTodaysInfo: Boolean,
    showHourlyForecast: Boolean,
    toggleTodaysWeatherVisibility: () -> Unit,
    toggleHourlyForecast: () -> Unit,
    modifyContent: (HomeViewState) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "TemperatureUnitToggle") {
            FilterItem(
                text = state.weatherDataRequest.temperatureUnit,
                action = {
                    if (state.weatherDataRequest.temperatureUnit == Constants.CELSIUS)
                        modifyContent(
                            state.copy(
                                weatherDataRequest = state.weatherDataRequest.copy(
                                    temperatureUnit = Constants.FAHRENHEIT
                                )
                            )
                        )
                    else
                        modifyContent(
                            state.copy(
                                weatherDataRequest = state.weatherDataRequest.copy(
                                    temperatureUnit = Constants.CELSIUS
                                )
                            )
                        )
                }
            )
        }

        item(key = "TodaysWeatherToggle") {
            FilterItem(
                text = "Today",
                enabled = showTodaysInfo,
                action = {
                    toggleTodaysWeatherVisibility()
                }
            )
        }

        item(key = "HourlyForeCastToggle") {
            FilterItem(
                text = "Hourly",
                enabled = showHourlyForecast,
                action = {
                    toggleHourlyForecast()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterItem(
    text: String,
    action: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    FilterChip(
        shape = RoundedCornerShape(50),
        selected = enabled,
        onClick = {
            action()
        },
        label = {
            Text(
                text = text,
                modifier = Modifier.padding(4.dp)
            )
        },
        modifier = modifier
    )
}

@Composable
fun DailyWeatherItem(
    dailyForecast: DailyForecast,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val date = remember {
            dailyForecast.time.formatToDMMMY()
        }

        val temperatureMax = remember {
            "Max ${dailyForecast.temperature2mMax}${dailyForecast.temperature2mMaxUnit}"
        }

        val temperatureMin = remember {
            "Min ${dailyForecast.temperature2mMin}${dailyForecast.temperature2mMinUnit}"
        }


        val precipitationHours = remember {
            "Rain around ${dailyForecast.precipitationHours}hrs"
        }

        val precipitation = remember {
            "Rainfall - ${dailyForecast.precipitationSum}${dailyForecast.precipitationSumUnit}"
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = date,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = precipitation,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = precipitationHours,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = temperatureMax,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = temperatureMin,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun RequestPermissionUI(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text("Go to settings and allow permission")

        }
    }
}

@Preview
@Composable
fun HomeUIPreview() {
    ComposedWeatherTheme {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            LocalContext.current
        )
        HomeUI(
            fusedLocationProviderClient,
            onBackPressed = {},
            navigateToSearch = {}
        )
    }
}