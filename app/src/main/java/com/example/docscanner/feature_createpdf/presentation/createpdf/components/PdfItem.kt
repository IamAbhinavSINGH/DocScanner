package com.example.docscanner.feature_createpdf.presentation.createpdf.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import io.realm.kotlin.types.RealmInstant
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

@Composable
fun PdfItem(
    modifier: Modifier = Modifier,
    pdfName: String,
    description: String,
    lastUpdated: RealmInstant,
    thumbnailUri: String,
    onEdit: () -> Unit,
    onShare: () -> Unit,
    onCopyToDevice: () -> Unit,
    onDelete : () -> Unit
){
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Start
            ) {

                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .heightIn(max = 80.dp)
                            .widthIn(max = 80.dp),
                        model = thumbnailUri,
                        contentDescription = "pdf thumbnail",
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column( 
                    modifier = Modifier.align(Alignment.CenterVertically),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    RowTextField(
                        key = "Name: ",
                        typeOfText = "title",
                        keyFontSize = 13,
                        value = pdfName,
                        valueFontSize = 15
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    RowTextField(
                        key = "Description: ",
                        typeOfText = "description",
                        keyFontSize = 13,
                        value = description,
                        valueFontSize = 15
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    RowTextField(
                        key = "Last updated: ",
                        typeOfText = "lastUpdated",
                        keyFontSize = 13,
                        value = "",
                        realmInstant = lastUpdated,
                        valueFontSize = 14
                    )
                }

            }

            val isMenuOpen = remember{ mutableStateOf(false)}

            IconButton(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.Bottom)
                    .padding(end = 10.dp, bottom = 10.dp)
                    .weight(0.1f),
                onClick = {
                    isMenuOpen.value = !isMenuOpen.value
                }) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "options button",
                    tint = Color.Black
                )
            }
            if(isMenuOpen.value) {
                DropDownOption(
                    isMenuOpen = isMenuOpen.value,
                    onDismiss = { isMenuOpen.value = false },
                    onEdit = {
                        isMenuOpen.value = false
                        onEdit()
                    },
                    onShare = {
                        isMenuOpen.value = false
                        onShare()
                    },
                    onCopyToDevice = {
                        isMenuOpen.value = false
                        onCopyToDevice()
                    },
                    onDelete = {
                        isMenuOpen.value = false
                        onDelete()
                    }
                )
            }
        }
    }
}


@Composable
fun RowTextField(
    modifier: Modifier = Modifier,
    typeOfText: String,
    key: String,
    keyFontSize: Int,
    value: String,
    realmInstant: RealmInstant? = null,
    valueFontSize: Int
){
    Row(
        modifier = modifier
    ) {
        Text(
            text = key,
            style = TextStyle(
                fontSize = keyFontSize.sp,
                color = MaterialTheme.colorScheme.primary
            ),
            textAlign = TextAlign.Start
        )
        if (typeOfText == "lastUpdated"){
            Text(
                text =  SimpleDateFormat("hh:mm a", Locale.getDefault())
                    .format(Date.from(realmInstant?.toInstant())).uppercase(),
                style = TextStyle(
                    fontSize = valueFontSize.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Start,
                ),
            )
        }else{
            val fontWeight =
                when (typeOfText) {
                    "title" -> FontWeight.Bold
                    "description" -> FontWeight.Normal
                    else -> FontWeight.Normal
                }

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontSize = valueFontSize.sp,
                maxLines = 2,
                fontWeight = fontWeight,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun DropDownOption(
    isMenuOpen: Boolean,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onShare: () -> Unit,
    onCopyToDevice: () -> Unit,
    onDelete : () -> Unit
){
    DropdownMenu(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.primary),
        expanded = isMenuOpen,
        onDismissRequest = {onDismiss()},
        offset = DpOffset(x = 200.dp, y = 0.dp),
        content = {
            DropDownMenuItem(
                onClick = {onEdit()},
                imageVector = Icons.Default.Edit,
                text = "Edit"
            )
            Spacer(modifier = Modifier.height(4.dp).background(color = MaterialTheme.colorScheme.primary))
            DropDownMenuItem(
                onClick = {onCopyToDevice()},
                imageVector = Icons.Default.Add,
                text = "Copy to device"
            )
            Spacer(modifier = Modifier.height(4.dp).background(color = MaterialTheme.colorScheme.primary))
            DropDownMenuItem(
                onClick = {onShare()},
                imageVector = Icons.Default.Share,
                text = "Share"
            )
            Spacer(modifier = Modifier.height(4.dp).background(color = MaterialTheme.colorScheme.primary))
            DropDownMenuItem(
                onClick = {onDelete()},
                imageVector = Icons.Default.Delete,
                text = "Delete"
            )
        }
    )
}

@Composable
fun DropDownMenuItem(
    onClick: () -> Unit,
    imageVector : ImageVector,
    text: String
){
    Row(
        modifier = Modifier.clickable { onClick() }
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
    ){
        Icon(
            modifier = Modifier.padding(8.dp),
            imageVector = imageVector ,
            tint = Color.LightGray,
            contentDescription = "drop down menu item"
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = text,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.LightGray
            )
        )
    }
}

@Preview
@Composable
fun PreviewPdfItem(){
    PdfItem(pdfName = "Hello Pdf",
        description = "This is the description of the pdf",
        lastUpdated = RealmInstant.now(),
        thumbnailUri = "",
        onCopyToDevice = {},
        onDelete = {},
        onEdit = {},
        onShare = {},
    )
}

fun RealmInstant.toInstant(): Instant {
    val sec: Long = this.epochSeconds
    val nano: Int = this.nanosecondsOfSecond
    return if (sec >= 0) {
        Instant.ofEpochSecond(sec, nano.toLong())
    } else {
        Instant.ofEpochSecond(sec - 1, 1_000_000 + nano.toLong())
    }
}
