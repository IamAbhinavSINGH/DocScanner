package com.example.docscanner.feature_createpdf.presentation.viewpdf

import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.docscanner.core.ui.theme.Grayish
import com.example.docscanner.core.util.PdfParser
import com.example.docscanner.feature_createpdf.presentation.viewpdf.components.PdfViewer
import com.example.docscanner.feature_createpdf.presentation.viewpdf.components.PdfViewerToolBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewScreen(
    viewModel: PdfViewerViewModel = hiltViewModel(),
    navController: NavController,
    activity: ComponentActivity,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val pdfState by viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            PdfViewerToolBar(
                text = pdfState.titleName,
                onNavigateUp = {
                    navController.navigateUp()
                },
                onShare = {

                },
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.secondary
    ) { paddingValues ->

        var imageList by rememberSaveable { mutableStateOf<List<Bitmap>?>(null) }

        LaunchedEffect(key1 = pdfState.uri) {
            val loadedImages = withContext(Dispatchers.Default){
                PdfParser(activity.applicationContext).getImagesFromPdf("file://${pdfState.uri}".toUri())
                    .toMutableList()
            }
            imageList = loadedImages
        }

        if (imageList == null) {
            Dialog(
                onDismissRequest = {},
                properties = DialogProperties(
                    dismissOnClickOutside = false,
                    dismissOnBackPress = true
                )
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.surface)
            }
        }else{
            PdfViewer(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Grayish),
                list = imageList!!
            )
        }
    }
}