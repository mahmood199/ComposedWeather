package com.weather.forecastify.app.ui.feature.home

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
import androidx.compose.foundation.layout.aspectRatio
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
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.weather.forecastify.app.ui.common.ContentLoaderUI
import com.weather.forecastify.app.ui.common.ForecastfiyAppBarUI
import com.weather.forecastify.app.ui.common.SaveableLaunchedEffect
import com.weather.forecastify.app.ui.theme.ForecastifyTheme
import com.weather.forecastify.app.util.formatToAMPM
import com.weather.forecastify.app.util.formatToDMMMY
import com.weather.forecastify.data.model.request.Constants
import com.weather.forecastify.data.model.response.Current
import com.weather.forecastify.data.model.response.CurrentUnits
import com.weather.forecastify.data.model.response.DailyForecast
import com.weather.forecastify.data.model.response.HourlyForecast
import kotlinx.coroutines.delay
import java.text.DecimalFormat

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeUI(
    onBackPressed: () -> Unit,
    navigateToSearch: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current

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
            ForecastfiyAppBarUI(
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
                    val fusedLocationProviderClient =
                        LocationServices.getFusedLocationProviderClient(context)

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


fun getHourlyChartData(
    hourlyForecasts: SnapshotStateList<HourlyForecast>
): LineChartData {

    val pointsData: List<Point> = hourlyForecasts.mapIndexed { index, hourlyForecast ->
        Point((index + 1).toFloat(), hourlyForecast.temperature.toFloat())
    }

    val steps = hourlyForecasts.size - 1

    val xAxisData = AxisData.Builder()
        .axisStepSize(size = 100.dp)
        .backgroundColor(color = Color.Blue)
        .steps(count = steps)
        .labelData { it ->
            val item = hourlyForecasts[it]
            item.time.formatToAMPM()
        }
        .labelAndAxisLinePadding(padding = 15.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(count = steps)
        .backgroundColor(color = Color.Red)
        .labelAndAxisLinePadding(padding = 20.dp)
        .build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    lineStyle = LineStyle(),
                    intersectionPoint = IntersectionPoint(),
                    selectionHighlightPoint = SelectionHighlightPoint(),
                    shadowUnderLine = ShadowUnderLine(),
                    selectionHighlightPopUp = SelectionHighlightPopUp()
                )
            ),
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(),
        backgroundColor = Color.White
    )
    return lineChartData
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
    ForecastifyTheme {
        HomeUI(
            onBackPressed = {},
            navigateToSearch = {}
        )
    }
}