package com.example.docscanner.feature_createpdf.domain.usecase

import com.example.docscanner.feature_createpdf.domain.RealmDatabaseRepo

class PdfUseCase(repo: RealmDatabaseRepo) {

    val getPdfUseCase = GetPdfUseCase(repo)
    val getAllPdfUseCase = GetAllPdfUseCase(repo)
    val getAllFilteredPdfUseCase = GetAllFilteredPdfUseCase(repo)
    val insertPdfUseCase = InsertPdfUseCase(repo)
    val updatePdfUseCase = UpdatePdfUseCase(repo)
    val deletePdfUseCase = DeletePdfUseCase(repo)
    val getAllSearchedPdfUseCase = GetAllSearchedPdfUseCase(repo)
    val getAllArchivedPdfUseCase = GetAllArchivedPdfUseCase(repo)

}