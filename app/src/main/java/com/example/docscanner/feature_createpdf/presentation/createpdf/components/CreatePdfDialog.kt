package com.example.docscanner.feature_createpdf.presentation.createpdf.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.docscanner.core.ui.theme.Greenish

@Composable
fun CreatePdfDialog(
    title: String,
    onDismiss : () -> Unit,
    onSuccess: (String, String) -> Unit,
){
    var titleValue by remember{ mutableStateOf("") }
    var descriptionValue by remember{ mutableStateOf("") }

    Dialog(
        onDismissRequest = {onDismiss()},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
            )
        ) {
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(24.dp),
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                InputTextForDialog(
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                    keyName = "Title",
                    value = titleValue,
                    onValueChange = {
                        titleValue = it
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                InputTextForDialog(
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                    keyName = "Description",
                    value = descriptionValue,
                    onValueChange ={
                        descriptionValue = it
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.End
                ){
                    TextButton(
                        modifier = Modifier.padding(16.dp),
                        onClick = {onDismiss()},
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Exit", color = Color.White, fontSize = 15.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    TextButton(
                        modifier = Modifier.padding(16.dp),
                        onClick = {
                            if (validateTitleAndDescription(titleValue, descriptionValue)){
                                onSuccess(titleValue, descriptionValue)
                            }
                        },
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Save", color = Color.White, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

fun validateTitleAndDescription(title: String, description: String): Boolean{
    return !((title.isEmpty()) or (description.isEmpty()))
}

@Composable
fun InputTextForDialog(
    modifier: Modifier = Modifier,
    keyName: String,
    value: String,
    onValueChange: (String) -> Unit,
){
    Column(
        modifier = modifier
    ) {
        Text(
            text = keyName,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.LightGray,
            fontSize = 15.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            maxLines = 2,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.primary,
                cursorColor = Greenish,
                focusedIndicatorColor = Color.DarkGray,
                unfocusedIndicatorColor = Color.DarkGray,
            )
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewCreatePdfDialog(){
    CreatePdfDialog(
        "Save pdf",
        onDismiss = {},
        onSuccess ={title, description ->
        } )
}