package com.andresruiz.qrcraft.root

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.andresruiz.qrcraft.R
import com.andresruiz.qrcraft.core.presentation.ui.UiText
import com.andresruiz.qrcraft.create.CreateQRScreen
import com.andresruiz.qrcraft.create_details.CreateQRDetailsScreenRoot
import com.andresruiz.qrcraft.history.HistoryScreenRoot
import com.andresruiz.qrcraft.preview.QRPreviewScreenRoot
import com.andresruiz.qrcraft.scan.ScanScreenRoot

@Composable
fun NavigationRoot(
    closeApplication: () -> Unit,
) {

    val backStack = rememberNavBackStack(ScanQR)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when(key) {
                is ScanQR -> NavEntry(
                    key = key,
//                    metadata = NavDisplay.transitionSpec {
//
//                    }
                ) {
                    ScanScreenRoot(
                        navigateToScanResult = {
                            backStack.add(ScanResult(it))
                        },
                        navigateToCreate = {
                            backStack.add(CreateQR)
                        },
                        navigateToHistory = {
                            backStack.add(ScanHistory)
                        }
                    )
                }

                is ScanHistory -> NavEntry(
                    key = key,
                    metadata = NavDisplay.transitionSpec {
                        slideInHorizontally(initialOffsetX = { -it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { it })
                    } + NavDisplay.popTransitionSpec {
                        slideInHorizontally(initialOffsetX = { it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { -it })
                    } + NavDisplay.predictivePopTransitionSpec {
                        slideInHorizontally(initialOffsetX = { it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { -it })
                    }
                ) {
                    HistoryScreenRoot(
                        navigateToScan = {
                            backStack.removeIf { it != ScanQR }
                        },
                        navigateToCreate = {
                            if (backStack.contains(CreateQR)) {
                                val index = backStack.indexOf(CreateQR)
                                val page = backStack[index]
                                backStack.removeAt(index)
                                backStack.add(page)
                            } else {
                                backStack.add(CreateQR)
                            }
                        },
                        navigateToPreview = {
                            backStack.add(CreateQRPreview(it))
                        }
                    )
                }

                is CreateQR -> NavEntry(key) {
                    CreateQRScreen(
                        navigateToHistory = {
                            if (backStack.contains(ScanHistory)) {
                                val index = backStack.indexOf(ScanHistory)
                                val page = backStack[index]
                                backStack.removeAt(index)
                                backStack.add(page)
                            } else {
                                backStack.add(ScanHistory)
                            }
                        },
                        navigateToScan = {
                            backStack.removeIf { it != ScanQR }
                        },
                        navigateToDetails = {
                            backStack.add(CreateQRDetails(it))
                        }
                    )
                }

                is ScanResult -> NavEntry(key) {
                    QRPreviewScreenRoot(
                        title = UiText.StringResource(R.string.scan_result),
                        id = key.id,
                        onNavigateBack = {
                            val element = backStack.findLast { it is ScanResult }
                            element?.let {
                                backStack.remove(it)
                            }
                        }
                    )
                }

                is CreateQRDetails -> NavEntry(key) { test ->
                    CreateQRDetailsScreenRoot(
                        qrType = key.qrType,
                        onNavigateBack = {
                            val element = backStack.findLast { it is CreateQRDetails }
                            element?.let {
                                backStack.remove(it)
                            }
                        },
                        onCreateQR = {
                            backStack.add(CreateQRPreview(it))
                        }
                    )
                }

                is CreateQRPreview -> NavEntry(key) {
                    QRPreviewScreenRoot(
                        title = UiText.StringResource(R.string.preview),
                        id = key.id,
                        onNavigateBack = {
                            val element = backStack.findLast { it is CreateQRPreview }
                            element?.let {
                                backStack.remove(it)
                            }
                        }
                    )
                }

                else -> throw IllegalArgumentException("Invalid key: $key")

            }
        },
        transitionSpec = {
            // Slide in from right when navigating forward
            slideInHorizontally(initialOffsetX = { it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it })
        },
        popTransitionSpec = {
            // Slide in from left when navigating back
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            // Slide in from left when navigating back
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
    )

    PermissionsRequiredPopup(
        closeApplication = closeApplication
    )
}