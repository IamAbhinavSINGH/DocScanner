package com.example.docscanner.feature_createpdf.presentation.viewpdf.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewerToolBar(
    modifier: Modifier = Modifier,
    text: String,
    onNavigateUp : () -> Unit,
    onShare : () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
){
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { onNavigateUp() }
            ) {
                Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "back")
            }
        },
        actions = {
            IconButton(
                onClick = { onShare() }
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "share")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.secondary,
            navigationIconContentColor = MaterialTheme.colorScheme.secondary,
            actionIconContentColor = MaterialTheme.colorScheme.secondary,
            scrolledContainerColor = MaterialTheme.colorScheme.primary
        ),
        scrollBehavior = scrollBehavior,
    )
}