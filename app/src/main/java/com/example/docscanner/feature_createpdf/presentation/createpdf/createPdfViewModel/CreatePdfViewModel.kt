package com.example.docscanner.feature_createpdf.presentation.createpdf.createPdfViewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docscanner.feature_createpdf.domain.Pdf
import com.example.docscanner.feature_createpdf.domain.usecase.PdfUseCase
import com.example.docscanner.feature_createpdf.domain.util.PdfOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePdfViewModel @Inject constructor(
    private val pdfUseCase: PdfUseCase
) : ViewModel(){

    private var _pdfState: MutableState<PdfState> = mutableStateOf(PdfState())
    val pdfState = _pdfState

    private var selectedPdf: Pdf? = null
    private var lastDeletedPdf: Pdf? = null

    init {
        getPdfs(pdfOrder = pdfState.value.pdfOrder)
    }

    fun onEvent(event: CreatePdfEvent){
        when(event){
            is CreatePdfEvent.InsertPdf -> {
                viewModelScope.launch(Dispatchers.IO) {
                    pdfUseCase.insertPdfUseCase(event.pdf)
                }
            }
            is CreatePdfEvent.EditPdf -> {
                viewModelScope.launch(Dispatchers.IO) {
                    if(selectedPdf != null) {
                        pdfUseCase.updatePdfUseCase.invoke( pdf = Pdf().apply {
                            titleName = event.title
                            description = event.description
                            _id = selectedPdf!!._id
                            timeStamp = selectedPdf?.timeStamp!!
                            uri = selectedPdf!!.uri
                            thumbnailUri = selectedPdf!!.thumbnailUri
                        })
                    }
                }
            }
            is CreatePdfEvent.Order -> {
                if(pdfState.value.pdfOrder::class == event.pdfOrder::class &&
                    pdfState.value.pdfOrder.orderType == event.pdfOrder.orderType){
                    return
                }

                getPdfs(event.pdfOrder)
            }
            is CreatePdfEvent.DeletePdf -> {
                viewModelScope.launch(Dispatchers.IO){
                    pdfUseCase.updatePdfUseCase.invoke( pdf = Pdf().apply {
                        _id = event.pdf._id
                        titleName = event.pdf.titleName
                        description = event.pdf.description
                        timeStamp = event.pdf.timeStamp
                        uri = event.pdf.uri
                        thumbnailUri = event.pdf.thumbnailUri
                        isArchived = true
                        archivedAt = RealmInstant.now()
                    })
//                    getPdfs(pdfOrder = pdfState.value.pdfOrder)
                }
            }
            is CreatePdfEvent.RestorePdf -> {
                if(lastDeletedPdf != null){
                    viewModelScope.launch {
                        pdfUseCase.insertPdfUseCase(lastDeletedPdf!!)
                    }
                }
            }
            is CreatePdfEvent.SelectPdf -> {
                selectedPdf = event.pdf
                _pdfState.value = _pdfState.value.copy(
                    currentSelectedPdf = event.pdf
                )
            }
            is CreatePdfEvent.ToggleOrderSection -> {
                _pdfState.value = _pdfState.value.copy(
                    isOrderVisible = !_pdfState.value.isOrderVisible
                )
            }
        }
    }

    private fun getPdfs(pdfOrder: PdfOrder){
        pdfUseCase.getAllPdfUseCase(pdfOrder).onEach { listOfPdf ->
            _pdfState.value = _pdfState.value.copy(
                pdfList = listOfPdf,
                pdfOrder = pdfOrder
            )
        }.launchIn(viewModelScope)
    }
}