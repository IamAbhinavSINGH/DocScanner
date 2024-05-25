package com.example.docscanner.feature_createpdf.presentation.searchpdf.searchViewModel

import com.example.docscanner.feature_createpdf.domain.Pdf

sealed class SearchPdfEvent {
    data class ChangedSearchText(val searchText: String): SearchPdfEvent()
    data class SearchQuery(val searchQuery: String): SearchPdfEvent()
    data class FocusChange(val focus: Boolean): SearchPdfEvent()
    data class SearchClicked(val pdf: Pdf): SearchPdfEvent()
    data class DeleteHistory(val pdf: Pdf): SearchPdfEvent()
}