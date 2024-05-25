package com.example.docscanner.feature_createpdf.domain.usecase

import com.example.docscanner.feature_createpdf.domain.Pdf
import com.example.docscanner.feature_createpdf.domain.RealmDatabaseRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllSearchedPdfUseCase(private val repo: RealmDatabaseRepo) {

    operator fun invoke(): Flow<List<Pdf>> {
        return repo.getSearchedPdfList().map { pdfList ->
            pdfList.sortedByDescending { it.searchedAt }
        }
    }
}