package com.example.docscanner.feature_createpdf.presentation.archivedPdf

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.docscanner.feature_createpdf.domain.Pdf
import com.example.docscanner.feature_createpdf.domain.usecase.PdfUseCase
import com.example.docscanner.feature_createpdf.domain.util.OrderType
import com.example.docscanner.feature_createpdf.domain.util.PdfOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ArchivedPdfViewModel @Inject constructor(
    private val pdfUseCase: PdfUseCase
): ViewModel() {

    private val _state = MutableStateFlow(ArchivedPdfState())
    val state = _state.asStateFlow()

    init {
        getArchivedPdfList()
    }

    fun onEvent(event: ArchivedEvent){
        when(event){

            is ArchivedEvent.DeletePdf -> {
                viewModelScope.launch {
                    pdfUseCase.deletePdfUseCase.invoke(event.pdf._id)
                }
            }
            is ArchivedEvent.RestorePdf -> {
                viewModelScope.launch {
                    pdfUseCase.updatePdfUseCase.invoke(Pdf().apply {
                        _id = event.pdf._id
                        titleName = event.pdf.titleName
                        description = event.pdf.description
                        timeStamp = event.pdf.timeStamp
                        thumbnailUri = event.pdf.thumbnailUri
                        uri = event.pdf.uri
                        isSearched = event.pdf.isSearched
                        searchedAt = event.pdf.searchedAt
                        isArchived = false
                        archivedAt = null
                    }
                    )
                }
            }
            is ArchivedEvent.SelectPdf -> {
                _state.value.selectedPdfList.add(event.pdf)
                _state.value = _state.value.copy(
                    selectedPdfList = _state.value.selectedPdfList
                )
            }
            is ArchivedEvent.DeleteAllPdf -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO){
                        _state.value.archivedPdfList.forEach{pdf->
                            pdfUseCase.deletePdfUseCase.invoke(pdf._id)
                        }
                    }
                    _state.value = _state.value.copy(
                        archivedPdfList = emptyList(),
                        selectedPdfList = mutableListOf()
                    )
                }
            }
            is ArchivedEvent.RestoreAllPdf -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.value.selectedPdfList.forEach { pdf ->
                        Pdf().apply {
                            _id = pdf._id
                            titleName = pdf.titleName
                            description = pdf.description
                            timeStamp = pdf.timeStamp
                            thumbnailUri = pdf.thumbnailUri
                            uri = pdf.uri
                            isSearched = pdf.isSearched
                            searchedAt = pdf.searchedAt
                            isArchived = false
                            archivedAt = null
                        }
                    }
                }
            }

        }
    }

    fun isPdfSelected(pdf: Pdf): Boolean{
        Log.d("ArchivedPdfViewModel", "isPdfSelected: ${_state.value.selectedPdfList.contains(pdf)}")
        return _state.value.selectedPdfList.contains(pdf)
    }

    private fun getArchivedPdfList(order: PdfOrder = PdfOrder.Date(OrderType.Descending)) {
        pdfUseCase.getAllArchivedPdfUseCase.invoke(pdfOrder = order).onEach{pdfList->
            _state.value = state.value.copy(
                archivedPdfList = pdfList
            )
        }.launchIn(viewModelScope)
    }
}

data class ArchivedPdfState(
    val archivedPdfList : List<Pdf> = emptyList(),
    val selectedPdfList : MutableList<Pdf> = mutableListOf()
)

sealed class ArchivedEvent(){
    data class RestorePdf(val pdf: Pdf): ArchivedEvent()
    data class DeletePdf(val pdf: Pdf): ArchivedEvent()
    data class SelectPdf(val pdf: Pdf): ArchivedEvent()
    data object RestoreAllPdf: ArchivedEvent()
    data object DeleteAllPdf: ArchivedEvent()
}