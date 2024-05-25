package com.example.docscanner.feature_createpdf.domain.usecase

import com.example.docscanner.feature_createpdf.domain.Pdf
import com.example.docscanner.feature_createpdf.domain.RealmDatabaseRepo
import org.mongodb.kbson.ObjectId

class GetPdfUseCase(private val repo: RealmDatabaseRepo) {
    operator fun invoke(id: ObjectId): Pdf?{
        return repo.getPdf(id)
    }
}