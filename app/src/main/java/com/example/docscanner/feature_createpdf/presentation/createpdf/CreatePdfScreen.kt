package com.example.docscanner.feature_createpdf.presentation.createpdf

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.docscanner.core.util.Constants.INTERNAL_DIR
import com.example.docscanner.core.util.FileManager
import com.example.docscanner.core.util.PdfParser
import com.example.docscanner.feature_createpdf.domain.Pdf
import com.example.docscanner.feature_createpdf.presentation.createpdf.components.CreatePdfDialog
import com.example.docscanner.feature_createpdf.presentation.createpdf.components.CreatePdfNavigationDrawer
import com.example.docscanner.feature_createpdf.presentation.createpdf.components.CreatePdfToolbar
import com.example.docscanner.feature_createpdf.presentation.createpdf.components.FloatingButton
import com.example.docscanner.feature_createpdf.presentation.createpdf.components.NavigationScreens
import com.example.docscanner.feature_createpdf.presentation.createpdf.components.PdfItem
import com.example.docscanner.feature_createpdf.presentation.createpdf.createPdfViewModel.CreatePdfEvent
import com.example.docscanner.feature_createpdf.presentation.createpdf.createPdfViewModel.CreatePdfViewModel
import com.example.docscanner.feature_createpdf.presentation.util.Screen
import com.google.mlkit.vision.documentscanner.GmsDocumentScanner
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import java.io.File

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreatePdfScreen(
    navController: NavController,
    activity: ComponentActivity,
    viewModel: CreatePdfViewModel = hiltViewModel(),
){
    val openCreatePdfDialog = remember{mutableStateOf(false)}
    val scannerResult = remember{
        mutableStateOf<GmsDocumentScanningResult?>(null)
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentScreenSelected = remember { mutableStateOf(NavigationScreens.HOME) }

    val scannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {
            if(it.resultCode == RESULT_OK){
                scannerResult.value = GmsDocumentScanningResult.fromActivityResultIntent(it.data)
                openCreatePdfDialog.value = true
            }
        }
    )

    if(openCreatePdfDialog.value){
        CreatePdfDialog(
            title = "Create PDF",
            onDismiss = {
                openCreatePdfDialog.value = false
                Toast.makeText(
                    activity.applicationContext,
                    "pdf couldn't save",
                    Toast.LENGTH_SHORT).show()
            },
            onSuccess ={title, description ->
                openCreatePdfDialog.value = false
                getPdfFromGmScannerResult(
                    viewModel = viewModel,
                    result = scannerResult.value,
                    title = title,
                    descriptionValue = description,
                    activity = activity
                )
            }
        )
    }

    CreatePdfNavigationDrawer(
        modifier = Modifier,
        drawerState = drawerState,
        onHomeClicked = {
            currentScreenSelected.value = NavigationScreens.HOME
            changeDrawerState(drawerState, scope)
        },
        onSettingsClicked = {
            currentScreenSelected.value = NavigationScreens.SETTINGS
            changeDrawerState(drawerState, scope)
        },
        onWhatsNewClicked = {
            currentScreenSelected.value = NavigationScreens.WHATS_NEW
            navController.navigate(Screen.WhatsNewScreen.route)
            changeDrawerState(drawerState, scope)
        },
        onTrashBinClicked = {
            currentScreenSelected.value = NavigationScreens.TRASH_BIN
            navController.navigate(Screen.BinPdfScreen.route)
            changeDrawerState(drawerState, scope)
        },
        currentlySelected = currentScreenSelected.value,
        content = {
            SetUpScaffold(
                navController = navController,
                viewModel = viewModel,
                scanner = createDocumentScanner(),
                activity = activity,
                scannerLauncher = scannerLauncher,
                onNavigationDrawerClicked = {
                    changeDrawerState(drawerState, scope)
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetUpScaffold(
    navController: NavController,
    viewModel: CreatePdfViewModel,
    scanner: GmsDocumentScanner,
    activity: ComponentActivity,
    scannerLauncher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
    onNavigationDrawerClicked: () -> Unit
){
    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            CreatePdfToolbar(
                modifier = Modifier.fillMaxWidth(),
                onSearchClicked = { navController.navigate(Screen.SearchPdfScreen.route) },
                onOrderClicked = {viewModel.onEvent(CreatePdfEvent.ToggleOrderSection)},
                onNavigationDrawerClicked = { onNavigationDrawerClicked() },
                onOrderChanged = {pdfOrder->
                    viewModel.onEvent(CreatePdfEvent.Order(pdfOrder))
                },
                isOrderVisible = viewModel.pdfState.value.isOrderVisible,
                scrollBehavior = scrollBehaviour,
                pdfOrder = viewModel.pdfState.value.pdfOrder
            )
        },
        floatingActionButton = {
            FloatingButton {
                scanner.getStartScanIntent(activity)
                    .addOnSuccessListener {
                        scannerLauncher.launch(
                            IntentSenderRequest.Builder(it)
                                .build()
                        )
                    }
                    .addOnFailureListener{
                        Toast.makeText(
                            activity.applicationContext,
                            it.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        },
        containerColor = MaterialTheme.colorScheme.primary,
        content = {paddingValues->
            PdfItemList(
                activity = activity,
                viewModel = viewModel,
                paddingValues = paddingValues,
                navController = navController
            )
        }
    )
}

@Composable
fun PdfItemList(
    activity: ComponentActivity,
    viewModel: CreatePdfViewModel,
    paddingValues: PaddingValues,
    navController: NavController
) {
    val isEditPdfDialogOpen = remember { mutableStateOf(false) }

    val isCopyToDeviceSelected = remember {
        mutableStateOf(false)
    }

    val isClicked = remember { mutableStateOf(false) }

    if(isEditPdfDialogOpen.value){
        EditPdf(
            viewModel = viewModel,
            onDismiss = { isEditPdfDialogOpen.value = false },
            onSuccess = { isEditPdfDialogOpen.value = false }
        )
    }

    if(isCopyToDeviceSelected.value){
        viewModel.pdfState.value.currentSelectedPdf?.let {
            PdfParser.RequestPermission(
                activity = activity,
                onDismiss = {
                    isCopyToDeviceSelected.value = false
                },
                permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
                action = {
                    val fileManager = FileManager(activity)
                    fileManager.copyToExternalDir(it)
                    Toast.makeText(activity.applicationContext, "${it.titleName}.pdf copied successfully", Toast.LENGTH_SHORT).show()
                    isCopyToDeviceSelected.value = false
                }
            )
        }
    }

    if(isClicked.value){
        PdfParser.RequestPermission(
            activity = activity,
            permission = Manifest.permission.READ_EXTERNAL_STORAGE,
            onDismiss = { isClicked.value = false },
            action = {
                navController.navigate(
                    Screen.PdfViewScreen.route +
                    "?_id=${viewModel.pdfState.value.currentSelectedPdf?._id?.toHexString()}"
                )
                isClicked.value = false
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ){
        items(viewModel.pdfState.value.pdfList){pdfItem ->
            PdfItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable {
                        viewModel.onEvent(CreatePdfEvent.SelectPdf(pdfItem))
                        isClicked.value = true
                    },
                pdfName = pdfItem.titleName,
                description = pdfItem.description,
                lastUpdated = pdfItem.timeStamp,
                thumbnailUri = pdfItem.thumbnailUri,
                onEdit = {
//              select the pdf so its item can change and then call the edit pdf Dialog to change the content
                    viewModel.onEvent(CreatePdfEvent.SelectPdf(pdfItem))
                    isEditPdfDialogOpen.value = true
                },
                onShare = {
                    sharePdf(activity, pdfItem)
                },
                onCopyToDevice = {
                    viewModel.onEvent(CreatePdfEvent.SelectPdf(pdfItem))
                    isCopyToDeviceSelected.value = true
                },
                onDelete = {
                    viewModel.onEvent(CreatePdfEvent.DeletePdf(pdfItem))
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(75.dp))
        }
    }
}

@Composable
fun EditPdf(viewModel: CreatePdfViewModel, onDismiss: () -> Unit, onSuccess: () -> Unit){
    CreatePdfDialog(
        title = "Edit PDF",
        onDismiss = {onDismiss()},
        onSuccess ={title, description->
            onSuccess()
            viewModel.onEvent(CreatePdfEvent.EditPdf(title = title, description = description))
        }
    )
}

fun createDocumentScanner(): GmsDocumentScanner{
    val options = GmsDocumentScannerOptions.Builder()
        .setScannerMode(SCANNER_MODE_FULL)
        .setGalleryImportAllowed(true)
        .setResultFormats(RESULT_FORMAT_JPEG , RESULT_FORMAT_PDF)
        .build()

    return  GmsDocumentScanning.getClient(options)
}

fun getPdfFromGmScannerResult(
    viewModel: CreatePdfViewModel,
    result: GmsDocumentScanningResult?,
    title: String,
    descriptionValue: String,
    activity: ComponentActivity
){
    val fileManager = FileManager(activity)
    val pdfUri = fileManager.copyPdfToInternalDir(resultPdf = result, title = title)
    val imageUri = fileManager.copyImageToInternalDir(resultPdf = result, title = title)


    val newPdf = Pdf().apply {
        _id = ObjectId.invoke()
        titleName = title
        description = descriptionValue
        uri = pdfUri
        thumbnailUri = imageUri
        timeStamp = RealmInstant.now()
    }

    result?.pdf?.uri?.let {
       fileManager.deleteFileFromDirectory("cache", getFileNameFromUri(it), isCache = true)
    }
    result?.pages?.forEach{page ->
        fileManager.deleteFileFromDirectory("cache", getFileNameFromUri(page.imageUri), isCache = true)
    }

    viewModel.onEvent(CreatePdfEvent.InsertPdf(newPdf))
}


fun sharePdf(activity: ComponentActivity, pdf: Pdf){
    try {

        Log.e("CreatePdfScreen", pdf.uri)
        val file = File(pdf.uri)

        val uri = FileProvider.getUriForFile(
            activity.applicationContext,
            "com.example.docscanner.fileprovider",
            file
        )

        Log.e("CreatePdfScreen", "uri -> $uri")

        val sharableIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, pdf.titleName)
            putExtra(Intent.EXTRA_TEXT, "Sharing this pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            type = "application/pdf"
        }

        activity.startActivity(Intent.createChooser(sharableIntent, "sharing pdf"))
    }catch (e: Exception){
        Log.e("CreatePdfScreen" , e.message.toString())
        e.printStackTrace()
    }
}

fun changeDrawerState(drawer: DrawerState,scope: CoroutineScope ){
    scope.launch {
        drawer.apply {
            if(isClosed) open()
            else close()
        }
    }
}

fun deletePdf(activity: ComponentActivity, pdfItem: Pdf, viewModel: CreatePdfViewModel){
    val fileManager = FileManager(activity)
    fileManager.deleteFileFromDirectory(
        INTERNAL_DIR,
        pdfItem.titleName + ".pdf",
        isCache = false
    )
    fileManager.deleteFileFromDirectory(
        INTERNAL_DIR,
        pdfItem.titleName + ".jpg",
        isCache = false
    )
}

fun getFileNameFromUri(uri: Uri):String{
    val tempNames = uri.toString().split("/")
    return tempNames[tempNames.size-1].replace(" " , "")
}