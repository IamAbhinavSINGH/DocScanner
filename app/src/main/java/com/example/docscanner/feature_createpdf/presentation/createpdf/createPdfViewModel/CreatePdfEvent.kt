package com.example.docscanner.feature_createpdf.presentation.createpdf.createPdfViewModel

import com.example.docscanner.feature_createpdf.domain.Pdf
import com.example.docscanner.feature_createpdf.domain.util.PdfOrder

sealed class CreatePdfEvent {
    data class InsertPdf(val pdf: Pdf): CreatePdfEvent()
    data class Order(val pdfOrder: PdfOrder): CreatePdfEvent()
    data class SelectPdf(val pdf: Pdf): CreatePdfEvent()
    data class EditPdf(val title: String, val description: String): CreatePdfEvent()
    data class DeletePdf(val pdf: Pdf) : CreatePdfEvent()
    object RestorePdf: CreatePdfEvent()
    object ToggleOrderSection: CreatePdfEvent()
}