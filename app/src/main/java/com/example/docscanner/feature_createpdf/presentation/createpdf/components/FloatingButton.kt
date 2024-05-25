package com.example.docscanner.feature_createpdf.presentation.createpdf.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FloatingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    FloatingActionButton(
        modifier = modifier,
        onClick = {onClick()},
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = Color.White,
        shape = RoundedCornerShape(50.dp)
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = Icons.Default.Create,
            contentDescription = "Create pdf",
            tint = Color.Black
        )
    }
}