package com.example.novacut.ui.components

import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class HomeTab(val label: String) {
    EDITS("Edits"),
    IMAGE("Image"),
    AUDIO("Audio")
}

@Composable
fun HomeTabRow(
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = HomeTab.entries
    val selectedIndex = tabs.indexOf(selectedTab)

    SecondaryScrollableTabRow(
    selectedTabIndex = selectedTabIndex,
    edgePadding = 0.dp
) {
    tabs.forEachIndexed { index, tab ->
        Tab(
            selected = selectedTabIndex == index,
            onClick = { onTabSelected(tab) },
            text = { Text(tab.label) }
        )
    }
    }
