package com.example.docscanner.feature_createpdf.presentation.createpdf.createPdfViewModel

import com.example.docscanner.feature_createpdf.domain.Pdf
import com.example.docscanner.feature_createpdf.domain.util.OrderType
import com.example.docscanner.feature_createpdf.domain.util.PdfOrder

data class PdfState(
    var pdfList : List<Pdf> = emptyList(),
    val isOrderVisible : Boolean = false,
    val pdfOrder: PdfOrder = PdfOrder.Date(OrderType.Descending),
    val currentSelectedPdf: Pdf? = null
)