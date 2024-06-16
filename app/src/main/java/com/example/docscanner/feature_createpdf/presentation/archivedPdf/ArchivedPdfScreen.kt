package com.example.docscanner.feature_createpdf.presentation.archivedPdf

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.docscanner.feature_createpdf.domain.Pdf
import com.example.docscanner.feature_createpdf.presentation.archivedPdf.components.ArchivedPdfListItem
import com.example.docscanner.feature_createpdf.presentation.archivedPdf.components.ArchivedPdfTopBar
import com.example.docscanner.feature_createpdf.presentation.archivedPdf.components.RestorePdfDialog
import com.example.docscanner.feature_createpdf.presentation.viewpdf.PdfViewerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ArchivedPdfScreen(
    navController: NavController,
    viewModel : ArchivedPdfViewModel = hiltViewModel()
){
    val scrollState = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollState.nestedScrollConnection),
        topBar = {
            ArchivedPdfTopBar(
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = scrollState,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.secondary
    ) {paddingValues->

        val state by viewModel.state.collectAsState()

       Card(
           modifier = Modifier
               .fillMaxSize()
               .padding(paddingValues)
               .clip(
                   RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
               ),
           colors = CardDefaults.cardColors(
               containerColor = MaterialTheme.colorScheme.surface,
               contentColor = MaterialTheme.colorScheme.secondary
           )
       ) {
           if(state.archivedPdfList.isEmpty()){
               Box(
                   modifier = Modifier
                       .fillMaxSize(),
                   contentAlignment = Alignment.Center
               ){
                   Text(
                       modifier = Modifier
                           .fillMaxWidth()
                           .padding(24.dp),
                       text = "Your archived documents will be stored here for 30 days",
                       style = MaterialTheme.typography.labelMedium,
                       color = Color.Black,
                       fontSize = 18.sp,
                       fontWeight = FontWeight.Bold
                   )
               }
           }
           else{
              Column(
                  modifier = Modifier
                      .fillMaxSize(),
                  verticalArrangement = Arrangement.spacedBy(8.dp),
              ) {
                  Text(
                      modifier = Modifier
                          .fillMaxWidth()
                          .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                      text = "Your archived documents will be automatically deleted after 30 days",
                      style = MaterialTheme.typography.bodyMedium,
                      color = Color.Black,
                      fontSize = 16.sp,
                      textAlign = TextAlign.Center
                  )

                  var isRestoreDialogVisible by remember {
                      mutableStateOf(false)
                  }

                  var restorePdf by remember { mutableStateOf<Pdf?>(null)}

                  if(isRestoreDialogVisible && restorePdf != null){
                      HandlePdfClick(
                          pdf = restorePdf!!,
                          viewModel = viewModel,
                          onCancelClicked = {
                              isRestoreDialogVisible = false
                          }
                      )
                  }

                  LazyVerticalGrid(
                      modifier = Modifier
                          .fillMaxSize(),
                      columns = GridCells.Adaptive(minSize = 120.dp),
                      contentPadding = PaddingValues(16.dp),
                      horizontalArrangement = Arrangement.SpaceAround
                  ) {
                      items(state.archivedPdfList){pdf->
                          ArchivedPdfListItem(
                              modifier = Modifier
                                  .height(170.dp)
                                  .width(100.dp),
                              pdf = pdf,
                              isSelected = viewModel.isPdfSelected(pdf),
                              onItemClick = {
                                  isRestoreDialogVisible = true
                                  restorePdf = pdf
                              },
                              onDeleteClick = {
                                  viewModel.onEvent(ArchivedEvent.DeletePdf(pdf))
                              },
                              onSelect = {
                                  viewModel.onEvent(ArchivedEvent.SelectPdf(pdf))
                              }
                          )
                      }
                  }
              }
           }
       }
    }
}

@Composable
fun HandlePdfClick(
    pdf: Pdf,
    viewModel: ArchivedPdfViewModel,
    onCancelClicked: ()->Unit
){
    RestorePdfDialog(
        modifier = Modifier,
        pdf = pdf,
        onRestoreBtnClicked = {
            viewModel.onEvent(ArchivedEvent.RestorePdf(pdf))
            onCancelClicked()
        },
        onCancelClicked = {
            onCancelClicked()
        }
    )
}