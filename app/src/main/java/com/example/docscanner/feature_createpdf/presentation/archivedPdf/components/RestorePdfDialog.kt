package com.example.docscanner.feature_createpdf.presentation.archivedPdf.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.docscanner.feature_createpdf.domain.Pdf

@Composable
fun RestorePdfDialog(
    modifier: Modifier = Modifier,
    pdf: Pdf,
    onRestoreBtnClicked : () -> Unit,
    onCancelClicked: () -> Unit,
){
    Dialog(
        onDismissRequest = { onCancelClicked() },
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.secondary,
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.verticalScroll(
                    state = rememberScrollState(),
                    enabled = true
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Restore PDF",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        AsyncImage(
                            modifier = modifier.fillMaxSize(),
                            model = pdf.thumbnailUri,
                            contentDescription = pdf.titleName,
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
//                Text(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    text = pdf.titleName,
//                    color = MaterialTheme.colorScheme.secondary,
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Normal,
//                    textAlign = TextAlign.Start
//                )
//                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Do you want to restore this PDF?",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    TextButton(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onClick = { onCancelClicked() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = MaterialTheme.colorScheme.secondary,
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "No",
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onClick = { onRestoreBtnClicked() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.secondary,
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Restore",
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            }
        }
    }
}