package com.example.docscanner.feature_createpdf.presentation.searchpdf.searchViewModel

import com.example.docscanner.feature_createpdf.domain.Pdf

data class SearchPdfState(
    val searchQuery: String = "",
    val pdfList: List<Pdf> = emptyList(),
    val searchFocusChange: Boolean = false,
    val searchedPdfList: List<Pdf> = emptyList()
)