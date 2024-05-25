package com.example.docscanner.feature_createpdf.domain.usecase

import com.example.docscanner.feature_createpdf.domain.Pdf
import com.example.docscanner.feature_createpdf.domain.RealmDatabaseRepo

class UpdatePdfUseCase(private val repo: RealmDatabaseRepo) {
    suspend operator fun invoke(pdf: Pdf){
        repo.updatePdf(pdf = pdf)
    }
}