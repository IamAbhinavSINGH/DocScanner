package com.example.docscanner.feature_createpdf.domain.usecase

import android.util.Log
import com.example.docscanner.feature_createpdf.domain.RealmDatabaseRepo
import org.mongodb.kbson.ObjectId

class DeletePdfUseCase(private val repo: RealmDatabaseRepo) {
    suspend operator fun invoke(id: ObjectId){
        repo.deletePdf(id = id)
    }
}