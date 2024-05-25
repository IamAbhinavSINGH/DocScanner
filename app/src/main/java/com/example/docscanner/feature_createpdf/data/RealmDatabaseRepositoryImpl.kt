package com.example.docscanner.feature_createpdf.data

import android.util.Log
import com.example.docscanner.feature_createpdf.domain.Pdf
import com.example.docscanner.feature_createpdf.domain.RealmDatabaseRepo
import com.example.docscanner.feature_createpdf.domain.util.PdfOrder
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.delete
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class RealmDatabaseRepositoryImpl(private val realm: Realm): RealmDatabaseRepo {

    override fun getPdfList(): Flow<List<Pdf>> {
        return realm.query<Pdf>("isArchived == $0" , false).asFlow().map { it.list }
    }

    override fun filterPdfList(queryParam: String): Flow<List<Pdf>> {

        val filter = "titleName LIKE $0 OR description LIKE $0"

        return realm.query<Pdf>(filter, "*$queryParam*").asFlow().map { it.list }
    }

    override fun getSearchedPdfList(): Flow<List<Pdf>> {
        return realm.query<Pdf>("isSearched == $0" , true).asFlow().map { it.list }
    }

    override fun getArchivedPdfList(): Flow<List<Pdf>> {
        return realm.query<Pdf>("isArchived == $0" , true).asFlow().map { it.list }
    }

    override fun getPdf(id: ObjectId): Pdf? {
        return try{
            realm.query<Pdf>(query = "_id == $0" , id).first().find()
        }catch (e: Exception){
            Log.e("RealmDatabaseRepositoryImpl", e.message.toString())
            null
        }
    }

    override suspend fun insertPdf(pdf: Pdf) {
        try{
            realm.write {
                copyToRealm(
                    instance = pdf,
                    updatePolicy = UpdatePolicy.ALL
                )
            }
        }catch(e: Exception){
            e.printStackTrace()
            Log.e("RealmDatabaseRepositoryImpl", e.message.toString())
        }
    }

    override suspend fun updatePdf(pdf: Pdf) {
        val frozenSelectedPdf = realm.query<Pdf>(query = "_id == $0", pdf._id).first().find()
        realm.write {
            frozenSelectedPdf?.let {
               findLatest(it)?.let{selectedPdf->
                   selectedPdf.titleName = pdf.titleName
                   selectedPdf.description = pdf.description
                   selectedPdf.timeStamp = pdf.timeStamp
                   selectedPdf.thumbnailUri = pdf.thumbnailUri
                   selectedPdf.uri = pdf.uri
                   selectedPdf.isSearched = pdf.isSearched
                   selectedPdf.searchedAt = pdf.searchedAt
                   selectedPdf.isArchived = pdf.isArchived
                   selectedPdf.archivedAt = pdf.archivedAt
               }
            }
        }
    }

    override suspend fun deletePdf(id: ObjectId) {
        realm.write {
            val selectedPdf = realm.query<Pdf>(query = "_id == $0" , id).first().find()
            try {
                selectedPdf?.let {
                    findLatest(it)?.also {pdf->
                        delete(pdf)
                    }
                }
            }catch (e: Exception){
                Log.e("RealmDatabaseRepositoryImpl" , e.message.toString())
            }
        }
    }
}