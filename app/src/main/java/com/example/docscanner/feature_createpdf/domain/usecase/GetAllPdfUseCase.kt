package com.example.docscanner.feature_createpdf.domain.usecase

import com.example.docscanner.feature_createpdf.domain.Pdf
import com.example.docscanner.feature_createpdf.domain.RealmDatabaseRepo
import com.example.docscanner.feature_createpdf.domain.util.OrderType
import com.example.docscanner.feature_createpdf.domain.util.PdfOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllPdfUseCase(private val repo: RealmDatabaseRepo){

    operator fun invoke(pdfOrder: PdfOrder = PdfOrder.Date(OrderType.Descending)): Flow<List<Pdf>> {

        return repo.getPdfList().map { pdfList->
            when(pdfOrder.orderType){
                is OrderType.Ascending -> {
                    when(pdfOrder){
                        is PdfOrder.Date -> pdfList.sortedBy { it.timeStamp }
                        is PdfOrder.Description -> pdfList.sortedBy { it.description}
                        is PdfOrder.Title -> pdfList.sortedBy { it.titleName }
                    }
                }
                is OrderType.Descending -> {
                    when(pdfOrder){
                        is PdfOrder.Date -> pdfList.sortedByDescending { it.timeStamp }
                        is PdfOrder.Description -> pdfList.sortedByDescending { it.description }
                        is PdfOrder.Title -> pdfList.sortedByDescending { it.titleName }
                    }
                }

            }
        }
    }
}