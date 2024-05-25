package com.example.docscanner.feature_createpdf.domain.usecase

import com.example.docscanner.feature_createpdf.domain.Pdf
import com.example.docscanner.feature_createpdf.domain.RealmDatabaseRepo

class InsertPdfUseCase(private val repo: RealmDatabaseRepo) {
    suspend operator fun invoke(pdf: Pdf){
        repo.insertPdf(pdf = pdf)
    }
}