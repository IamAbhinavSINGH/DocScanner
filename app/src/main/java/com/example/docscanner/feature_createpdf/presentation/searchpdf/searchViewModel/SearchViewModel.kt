package com.example.docscanner.feature_createpdf.presentation.searchpdf.searchViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docscanner.feature_createpdf.domain.Pdf
import com.example.docscanner.feature_createpdf.domain.usecase.PdfUseCase
import com.example.docscanner.feature_createpdf.domain.util.OrderType
import com.example.docscanner.feature_createpdf.domain.util.PdfOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val pdfUseCase: PdfUseCase,
) : ViewModel() {

    private val _searchState = mutableStateOf(SearchPdfState())
    val searchState = _searchState

    init {
        getSearchedPdfList()
    }

    fun onEvent(event: SearchPdfEvent) {
        when (event) {
            is SearchPdfEvent.ChangedSearchText -> {
                _searchState.value = _searchState.value.copy(
                    searchQuery = event.searchText
                )
            }

            is SearchPdfEvent.SearchQuery -> {
                getFilteredPdfList(PdfOrder.Date(OrderType.Descending), event.searchQuery)
            }

            is SearchPdfEvent.FocusChange -> {
                _searchState.value = _searchState.value.copy(
                    searchFocusChange = event.focus
                )
            }

            is SearchPdfEvent.SearchClicked -> {
                viewModelScope.launch(Dispatchers.IO) {
                    pdfUseCase.updatePdfUseCase(event.pdf)
                }
            }

            is SearchPdfEvent.DeleteHistory -> {
                viewModelScope.launch(Dispatchers.IO) {
                    pdfUseCase.updatePdfUseCase(Pdf().apply {
                        _id = event.pdf._id
                        titleName = event.pdf.titleName
                        description = event.pdf.description
                        uri = event.pdf.uri
                        thumbnailUri = event.pdf.thumbnailUri
                        timeStamp = event.pdf.timeStamp
                        isSearched = false
                        searchedAt = event.pdf.timeStamp
                        isArchived = event.pdf.isArchived
                        archivedAt = event.pdf.archivedAt
                    })
                }
            }
        }
    }

    private fun getSearchedPdfList() {
        pdfUseCase.getAllSearchedPdfUseCase.invoke().onEach { pdfList ->
            _searchState.value = _searchState.value.copy(
                searchedPdfList = pdfList
            )
        }.launchIn(viewModelScope)
    }

    private fun getFilteredPdfList(pdfOrder: PdfOrder, searchQuery: String) {
        pdfUseCase.getAllFilteredPdfUseCase.invoke(pdfOrder, searchQuery).onEach { pdfList ->
            _searchState.value = _searchState.value.copy(
                pdfList = pdfList,
                searchQuery = searchQuery
            )
        }.launchIn(viewModelScope)
    }
}