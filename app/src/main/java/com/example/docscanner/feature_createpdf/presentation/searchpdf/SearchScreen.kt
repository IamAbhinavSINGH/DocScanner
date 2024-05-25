package com.example.docscanner.feature_createpdf.presentation.searchpdf


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.docscanner.feature_createpdf.domain.Pdf
import com.example.docscanner.feature_createpdf.presentation.searchpdf.components.HistoryPdfItem
import com.example.docscanner.feature_createpdf.presentation.searchpdf.components.SearchBarPdfs
import com.example.docscanner.feature_createpdf.presentation.searchpdf.searchViewModel.SearchPdfEvent
import com.example.docscanner.feature_createpdf.presentation.searchpdf.searchViewModel.SearchViewModel
import com.example.docscanner.feature_createpdf.presentation.util.Screen
import io.realm.kotlin.types.RealmInstant

@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = hiltViewModel(),
) {
   Column(
       modifier = Modifier
           .fillMaxSize()
           .background(color = MaterialTheme.colorScheme.primary)
   ){
       SearchBarPdfs(
           query = searchViewModel.searchState.value.searchQuery,
           onValueChange = {
               searchViewModel.onEvent(SearchPdfEvent.ChangedSearchText(it))
               searchViewModel.onEvent(SearchPdfEvent.SearchQuery(it))
           },
           onSearchClicked = {
               searchViewModel.onEvent(SearchPdfEvent.SearchQuery(it))
           },
           isActive = searchViewModel.searchState.value.searchFocusChange,
           onActiveChange = {
               searchViewModel.onEvent(SearchPdfEvent.FocusChange(it))
           },
           onCloseSearch = {
               if (searchViewModel.searchState.value.searchQuery.isNotEmpty()) {
                   searchViewModel.onEvent(SearchPdfEvent.ChangedSearchText(""))
               } else {
                   searchViewModel.onEvent(SearchPdfEvent.FocusChange(false))
               }
           },
           onBackPressed = {
               navController.navigateUp()
           },
           onPdfClick = {pdf->
               searchViewModel.onEvent(SearchPdfEvent.SearchClicked(Pdf().apply {
                   _id = pdf._id
                   titleName = pdf.titleName
                   description = pdf.description
                   uri = pdf.uri
                   thumbnailUri = pdf.thumbnailUri
                   timeStamp = pdf.timeStamp
                   isSearched = true
                   searchedAt = RealmInstant.now()
                   isArchived = pdf.isArchived
                   archivedAt = pdf.archivedAt
               }))

               navController.navigate(
                   Screen.PdfViewScreen.route +
                   "?_id=${pdf._id.toHexString()}"
               )
           },
           pdfList = searchViewModel.searchState.value.pdfList
       )

       Spacer(modifier = Modifier.height(16.dp))

       Text(
           modifier = Modifier
               .fillMaxWidth()
               .align(Alignment.Start)
               .padding(horizontal = 16.dp),
           text = "Recent Searches",
           color = Color.White,
           fontWeight = FontWeight.Bold,
           fontSize = 20.sp
       )

       Spacer(modifier = Modifier.height(8.dp))

       LazyColumn(
           modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
           contentPadding = PaddingValues(8.dp)
       ){
           items(searchViewModel.searchState.value.searchedPdfList){pdf ->
               HistoryPdfItem(
                   modifier = Modifier.fillMaxWidth(),
                   pdf = pdf,
                   onClick = {
                        navController.navigate(
                            Screen.PdfViewScreen.route +
                                    "?_id=${pdf._id.toHexString()}"
                        )
                   },
                   onDelete = {
                        searchViewModel.onEvent(SearchPdfEvent.DeleteHistory(pdf))
                   }
               )
           }
       }
   }
}