package com.example.docscanner.feature_createpdf.presentation.searchpdf.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.docscanner.feature_createpdf.domain.Pdf

@Composable
fun HistoryPdfItem(
    modifier: Modifier = Modifier,
    pdf: Pdf,
    onClick : () -> Unit,
    onDelete : () -> Unit
){
    Row(
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp)
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row{
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = "history",
                tint = Color.White
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = pdf.titleName,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = { onDelete() }
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "delete history",
                tint = Color.DarkGray
            )
        }
    }
}