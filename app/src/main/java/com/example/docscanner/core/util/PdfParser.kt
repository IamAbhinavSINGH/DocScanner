package com.example.docscanner.core.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toFile
import androidx.documentfile.provider.DocumentFile
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.io.File

class PdfParser(private val context: Context) {

    companion object{
        @OptIn(ExperimentalPermissionsApi::class)
        @Composable
        fun RequestPermission(
            activity: ComponentActivity,
            permission: String,
            onDismiss: () -> Unit,
            action: () -> Unit
        ){
            val permissionState = rememberPermissionState(permission = permission)

            if (permissionState.status.isGranted) {
                action()
            } else {
                Dialog(
                    onDismissRequest = {onDismiss()},
                    properties = DialogProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false
                    )
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ){
                            Text(
                                text = "Storage Permission Request",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            if(permissionState.status.shouldShowRationale){
                                val textToShow = "The Storage permission is used to store and read the pdf. " +
                                        "Please grant the permission."
                                Text(text = textToShow, style = MaterialTheme.typography.bodyMedium)
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        onClick = {
                                            onDismiss()
                                            permissionState.launchPermissionRequest()
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.surface,
                                            contentColor = MaterialTheme.colorScheme.secondary
                                        )
                                    ) {
                                        Text(
                                            text = "Request permission",
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                }
                            }
                            else{
                                val textToShow =  "Storage permission is required for this feature to be available. " +
                                        "Please grant the permission"
                                Text(text = textToShow, style = MaterialTheme.typography.bodyMedium)
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        onClick = {
                                            onDismiss()
                                            val intent = Intent(
                                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts("package", activity.packageName, null)
                                            )
                                            activity.startActivity(intent)
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.surface,
                                            contentColor = MaterialTheme.colorScheme.secondary
                                        )
                                    ) {
                                        Text(
                                            text = "open settings",
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    fun getImagesFromPdf(uri: Uri): List<Bitmap> {

        val documentFile = DocumentFile.fromFile(uri.toFile())

        val imageList = mutableListOf<Bitmap>()
        if (documentFile.exists()) {
            try{
                val parcelFileDescriptor =
                    context.contentResolver.openFileDescriptor(uri, "r") ?: return imageList
                val pdfRenderer = PdfRenderer(parcelFileDescriptor)

                val pageCount = pdfRenderer.pageCount

                for (i in 0 until pageCount) {
                    val page = pdfRenderer.openPage(i)
                    val width = page.width
                    val height = page.height

                    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    imageList.add(bitmap)
                    page.close()
                }

                pdfRenderer.close()
                parcelFileDescriptor.close()
            }catch (e: Exception){
                Log.e("pdfRenderer" , "Error rendering pdf : ${e.message}")
                e.printStackTrace()
            }
        }else{
            Log.e("pdfRenderer" , "DocumentFile is null or does not exist")
        }

        return imageList
    }
}