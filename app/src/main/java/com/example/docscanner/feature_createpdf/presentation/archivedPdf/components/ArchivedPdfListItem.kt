package com.example.docscanner.feature_createpdf.presentation.archivedPdf.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.docscanner.feature_createpdf.domain.Pdf
import io.realm.kotlin.types.RealmInstant
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.Date

@Composable
fun ArchivedPdfListItem(
    modifier: Modifier = Modifier,
    pdf: Pdf,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onItemClick: () -> Unit,
    onDeleteClick : () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .pointerInput(Unit){
                detectTapGestures(
                    onLongPress = {
                        onSelect()
                        haptic.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.LongPress)
                    }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .clickable {
                    onItemClick()
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(
                width = 1.dp,
                color = Color.Black
            )
        ) {
            AsyncImage(
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp),
                model = pdf.thumbnailUri,
                contentDescription = pdf.titleName,
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                modifier = Modifier,
                text = pdf.titleName,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            pdf.archivedAt?.let {
                val timeRemaining: TimeRemaining = calculateTimeRemaining(it)
                val days = "${timeRemaining.days} days"
                Text(
                    modifier = Modifier,
                    text = days,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

data class TimeRemaining(val days: Long, val hours: Long, val minutes: Long)

fun calculateTimeRemaining(realmInstant: RealmInstant): TimeRemaining {
    // Convert RealmInstant to LocalDateTime
    val localDateTime = LocalDateTime.ofEpochSecond(realmInstant.epochSeconds, realmInstant.nanosecondsOfSecond, ZoneOffset.UTC)

    // Add 30 days to the LocalDateTime
    val futureDateTime = localDateTime.plusDays(30)

    // Get the current date and time
    val now = LocalDateTime.now()

    // Calculate the duration between now and the future date
    val duration = Duration.between(now, futureDateTime)

    // Extract the total days, hours, and minutes from the duration
    val totalMinutes = duration.toMinutes()
    val days = totalMinutes / (24 * 60)
    val hours = (totalMinutes % (24 * 60)) / 60
    val minutes = totalMinutes % 60

    return TimeRemaining(days, hours, minutes)
}


fun remainingDays(
    archivedAt: RealmInstant
): String{
    val currentDate = LocalDate.now()
    val archivedDate = LocalDate.ofEpochDay(archivedAt.epochSeconds)
    val deletionDate = archivedDate.plusDays(30)

    return ChronoUnit.DAYS.between(currentDate, deletionDate).toString()
}

fun getProgress(
    currentTime: Date,
    totalTime: Date
): Float{
    return ((currentTime.time/totalTime.time)/(1000 * 60 * 60 * 24)) * 100.toFloat()
}