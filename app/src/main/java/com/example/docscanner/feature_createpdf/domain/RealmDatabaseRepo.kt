package com.example.docscanner.feature_createpdf.domain

import com.example.docscanner.feature_createpdf.domain.util.PdfOrder
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface RealmDatabaseRepo {

    fun getPdfList() : Flow<List<Pdf>>
    fun filterPdfList(queryParam: String) : Flow<List<Pdf>>
    fun getPdf(id: ObjectId) : Pdf?
    fun getSearchedPdfList() : Flow<List<Pdf>>
    fun getArchivedPdfList() : Flow<List<Pdf>>
    suspend fun insertPdf(pdf: Pdf)
    suspend fun updatePdf(pdf: Pdf)
    suspend fun deletePdf(id: ObjectId)
}