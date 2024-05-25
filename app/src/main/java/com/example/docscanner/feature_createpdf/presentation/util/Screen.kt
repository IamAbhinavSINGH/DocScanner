package com.example.docscanner.feature_createpdf.presentation.util

sealed class Screen(val route: String) {
    data object CreatePdfScreen : Screen("create_pdf")
    data object SearchPdfScreen : Screen("search_pdf")
    data object WhatsNewScreen : Screen("whats_new_pdf")
    data object PdfViewScreen : Screen("pdf_view")
    data object BinPdfScreen : Screen("bin_pdf")
}