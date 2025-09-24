package com.andresruiz.qrcraft.history.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.andresruiz.qrcraft.R
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryTabRow(
    pagerState: PagerState,
    selectedTab: Int,
    onTabClicked: (Int) -> Unit,
) {
    TabRow(
        selectedTabIndex = selectedTab,
        indicator = { tabPositions ->
            SecondaryIndicator(
                height = 2.dp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .pagerTabIndicatorOffset(pagerState, tabPositions)
                    .clip(RoundedCornerShape(topStart = 100f, topEnd = 100f))
            )
        },
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Tab(
            selected = selectedTab == 0,
            onClick = {
                onTabClicked(0)
            },
            text = {
                Text(
                    text = stringResource(R.string.scanned),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                )
            },
        )

        Tab(
            selected = selectedTab == 1,
            onClick = {
                onTabClicked(1)
            },
            text = {
                Text(
                    text = stringResource(R.string.generated),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                )
            },
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
fun Modifier.pagerTabIndicatorOffset(
    pagerState: PagerState,
    tabPositions: List<TabPosition>,
): Modifier = composed {
    // If there are no tabs, don't show the indicator
    if (tabPositions.isEmpty()) return@composed this

    val currentPage = pagerState.currentPage
    val offsetFraction = pagerState.currentPageOffsetFraction

    // Figure out which tab to animate to
    val targetPage = when {
        offsetFraction > 0 -> (currentPage + 1).coerceAtMost(tabPositions.lastIndex)
        offsetFraction < 0 -> (currentPage - 1).coerceAtLeast(0)
        else -> currentPage
    }

    val currentTab = tabPositions[currentPage]
    val targetTab = tabPositions[targetPage]

    // Linearly interpolate (lerp) between the start and end tabs
    val indicatorStart = lerp(currentTab.left, targetTab.left, abs(offsetFraction))
    val indicatorWidth = lerp(currentTab.width, targetTab.width, abs(offsetFraction))

    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorStart)
        .width(indicatorWidth)
}