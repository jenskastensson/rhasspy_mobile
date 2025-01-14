package org.rhasspy.mobile.android.configuration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FilterListOff
import androidx.compose.material.icons.filled.LowPriority
import androidx.compose.material.icons.filled.PlaylistRemove
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.StringResource
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import org.rhasspy.mobile.MR
import org.rhasspy.mobile.android.TestTag
import org.rhasspy.mobile.android.content.ServiceStateHeader
import org.rhasspy.mobile.android.content.elements.CustomDivider
import org.rhasspy.mobile.android.content.elements.Icon
import org.rhasspy.mobile.android.content.elements.LogListElement
import org.rhasspy.mobile.android.content.elements.Text
import org.rhasspy.mobile.android.main.LocalConfigurationNavController
import org.rhasspy.mobile.android.testTag
import org.rhasspy.mobile.android.theme.SetSystemColor
import org.rhasspy.mobile.logic.nativeutils.NativeApplication
import org.rhasspy.mobile.viewmodel.configuration.IConfigurationViewModel

/**
 * screen that's shown when a configuration is being tested
 */
@Composable
fun ConfigurationScreenTest(
    viewModel: IConfigurationViewModel,
    content: (@Composable () -> Unit)?
) {
    SetSystemColor(1.dp)

    val navController = LocalConfigurationNavController.current
    val nativeApplication = get<NativeApplication>()

    LaunchedEffect(Unit) {
        nativeApplication.isAppInBackground.collect {
            if (it) {
                navController.popBackStack()
            }
        }
    }

    Scaffold(
        topBar = {
            AppBar(
                viewModel = viewModel,
                title = MR.strings.test,
                onBackClick = navController::popBackStack
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = MR.strings.stop,
                )
            }
        },
    ) { paddingValues ->
        Surface(
            modifier = Modifier.padding(paddingValues),
            tonalElevation = 3.dp
        ) {
            ConfigurationScreenTestList(
                viewModel = viewModel,
                content = content
            )
        }
    }
}

/**
 * list and custom content
 */
@Composable
private fun ConfigurationScreenTestList(
    modifier: Modifier = Modifier,
    viewModel: IConfigurationViewModel,
    content: (@Composable () -> Unit)?
) {
    Column(modifier = modifier) {
        val coroutineScope = rememberCoroutineScope()
        val scrollState = rememberLazyListState()
        val logEventsList by viewModel.logEvents.collectAsState()

        if (viewModel.isListAutoscroll.collectAsState().value) {
            LaunchedEffect(logEventsList.size) {
                coroutineScope.launch {
                    if (logEventsList.isNotEmpty()) {
                        scrollState.animateScrollToItem(logEventsList.size - 1)
                    }
                }
            }
        }

        LazyColumn(
            state = scrollState,
            modifier = Modifier.weight(1f)
        ) {
            stickyHeader {
                ServiceStateHeader(viewModel)
            }

            items(logEventsList) { item ->
                LogListElement(item)
                CustomDivider()
            }
        }

        if (content != null) {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 0.dp,
            ) {
                content()
            }
        }

    }
}

/**
 * top app bar with title and back navigation button
 *
 * actions: autoscroll, expand and filter
 */
@Composable
private fun AppBar(
    viewModel: IConfigurationViewModel,
    title: StringResource,
    onBackClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                1.dp
            )
        ),
        title = {
            Text(
                resource = title,
                modifier = Modifier.testTag(TestTag.AppBarTitle)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.testTag(TestTag.AppBarBackButton),
                content = icon
            )
        },
        actions = {
            IconButton(onClick = viewModel::toggleListFiltered) {
                Icon(
                    imageVector = if (viewModel.isListFiltered.collectAsState().value) Icons.Filled.FilterListOff else Icons.Filled.FilterList,
                    contentDescription = MR.strings.filterList
                )
            }
            IconButton(onClick = viewModel::toggleListAutoscroll) {
                Icon(
                    imageVector = if (viewModel.isListAutoscroll.collectAsState().value) Icons.Filled.LowPriority else Icons.Filled.PlaylistRemove,
                    contentDescription = MR.strings.autoscrollList
                )
            }
        }
    )
}