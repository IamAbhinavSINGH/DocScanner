package com.example.docscanner.feature_createpdf.presentation.createpdf.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CreatePdfNavigationDrawer(
    modifier : Modifier = Modifier,
    drawerState: DrawerState,
    onHomeClicked : () -> Unit,
    onSettingsClicked: () -> Unit,
    onWhatsNewClicked: () -> Unit,
    onTrashBinClicked : () -> Unit,
    content: @Composable () -> Unit,
    currentlySelected: NavigationScreens
){
    ModalNavigationDrawer(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.primary),
        drawerContent = {
            NavigationDrawerSheet(
                modifier = Modifier.width(270.dp),
                onHomeClicked = { onHomeClicked() },
                onSettingsClicked = { onSettingsClicked() },
                onTrashBinClicked = { onTrashBinClicked() },
                onWhatsNewClicked = { onWhatsNewClicked() },
                currentlySelected = currentlySelected
            )
        },
        content = {content()},
        gesturesEnabled = true,
        drawerState = drawerState,
    )
}

@Composable
fun NavigationDrawerSheet(
    modifier: Modifier = Modifier,
    onHomeClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onTrashBinClicked: () -> Unit,
    onWhatsNewClicked: () -> Unit,
    currentlySelected: NavigationScreens
){
    ModalDrawerSheet(
        modifier = modifier,
        drawerContentColor = MaterialTheme.colorScheme.secondary,
        drawerContainerColor = MaterialTheme.colorScheme.primary,
        drawerTonalElevation = 20.dp,
    ) {
        Icon(
            modifier = Modifier
                .size(80.dp)
                .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 5.dp)
                .clickable {
                    onHomeClicked()
                },
            imageVector = Icons.Default.Sort,
            contentDescription = "App logo",
            tint = MaterialTheme.colorScheme.secondary
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 10.dp),
            text = "Doc Scanner",
            fontSize = 18.sp,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Divider()
        Spacer(modifier = Modifier.height(15.dp))
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {

            val isHomeSelected = currentlySelected == NavigationScreens.HOME
            val isSettingsSelected = currentlySelected == NavigationScreens.SETTINGS
            val isBinSelected = currentlySelected == NavigationScreens.TRASH_BIN
            val isWhatsNewSelected = currentlySelected == NavigationScreens.WHATS_NEW

            TextIconLayout(
                text = "Home",
                icon = Icons.Default.Home,
                onClick = {onHomeClicked()},
                isSelected = isHomeSelected
            )
            TextIconLayout(
                text = "Settings",
                icon = Icons.Default.Settings,
                onClick = {onSettingsClicked()},
                isSelected = isSettingsSelected
            )
            TextIconLayout(
                text = "Bin",
                icon = Icons.Default.RestoreFromTrash,
                onClick = {onTrashBinClicked()},
                isSelected = isBinSelected
            )
            TextIconLayout(
                text = "What's new",
                icon = Icons.Default.NewReleases,
                onClick = {onWhatsNewClicked()},
                isSelected = isWhatsNewSelected
            )
        }
    }
}

@Composable
fun TextIconLayout(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    isSelected: Boolean = false
){
    val color = if(isSelected)
        MaterialTheme.colorScheme.surface
    else
        MaterialTheme.colorScheme.primary

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = color,
            contentColor = MaterialTheme.colorScheme.secondary
        )
    ){
        Row(
            modifier = Modifier
                .background(color = color)
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = icon,
                contentDescription = "text",
                tint = MaterialTheme.colorScheme.secondary,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 17.sp
            )
        }
    }
}

enum class NavigationScreens {
    HOME, SETTINGS, TRASH_BIN, WHATS_NEW
}