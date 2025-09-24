package com.andresruiz.qrcraft.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andresruiz.qrcraft.R
import com.andresruiz.qrcraft.core.presentation.design_system.components.QRCraftNavigationBar
import com.andresruiz.qrcraft.core.presentation.ui.ChangeStatusBarColors
import com.andresruiz.qrcraft.history.components.FadedLazyColumnScreen
import com.andresruiz.qrcraft.history.components.HistoryModal
import com.andresruiz.qrcraft.history.components.HistoryTabRow
import com.andresruiz.qrcraft.history.components.ScanHistoryCell
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun HistoryScreenRoot(
    navigateToScan: () -> Unit,
    navigateToCreate: () -> Unit,
    navigateToPreview: (Long) -> Unit,
    viewModel: HistoryViewModel = koinViewModel()
) {

    ChangeStatusBarColors(true)

    val state by viewModel.state.collectAsStateWithLifecycle()

    HistoryScreen(
        state = state,
        onAction = viewModel::onAction,
        navigateToScan = navigateToScan,
        navigateToCreate = navigateToCreate,
        navigateToPreview = navigateToPreview
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    state: HistoryState,
    onAction: (HistoryAction) -> Unit,
    navigateToScan: () -> Unit,
    navigateToCreate: () -> Unit,
    navigateToPreview: (Long) -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        floatingActionButton = {
            QRCraftNavigationBar(
                navigateToHistory = { }, // empty since we are already on history
                navigateToCreate = navigateToCreate,
                navigateToScan = navigateToScan,
                onHistoryTab = true
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.scan_history),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .wrapContentWidth()
                    )
                }
            )
        }
    ) { innerPadding ->

        val scope = rememberCoroutineScope()
        val pagerState = rememberPagerState(
            pageCount = { 2 }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            HistoryTabRow(
                pagerState = pagerState,
                selectedTab = pagerState.currentPage,
                onTabClicked = {
                    scope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                },
            )

            HorizontalPager(
                state = pagerState,
                beyondViewportPageCount = 1,
            ) { page ->
                when (page) {
                    0 -> {
                        FadedLazyColumnScreen(
                            content = {
                                items(
                                    items = state.scannedQRs,
                                    key = { it.id }
                                ) {
                                    ScanHistoryCell(
                                        qr = it,
                                        onClick = {
                                            navigateToPreview(it.id)
                                        },
                                        onLongClick = {
                                            onAction(HistoryAction.QRLongPress(it.id))
                                        },
                                        onFavoriteClick = {
                                            onAction(HistoryAction.ToggleFavorite(it))
                                        },
                                        modifier = Modifier.animateItem()
                                    )
                                }
                            }
                        )
                    }

                    1 -> {
                        FadedLazyColumnScreen(
                            content = {
                                items(
                                    items = state.generatedQRs,
                                    key = { it.id }
                                ) {
                                    ScanHistoryCell(
                                        qr = it,
                                        onClick = {
                                            navigateToPreview(it.id)
                                        },
                                        onLongClick = {
                                            onAction(HistoryAction.QRLongPress(it.id))
                                        },
                                        onFavoriteClick = {
                                            onAction(HistoryAction.ToggleFavorite(it))
                                        },
                                        modifier = Modifier.animateItem()
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }

        state.currentQR?.let {
            HistoryModal(
                state = state,
                onAction = onAction
            )
        }
    }
}