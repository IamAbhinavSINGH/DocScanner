package com.example.docscanner.feature_createpdf.presentation.viewpdf

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.docscanner.feature_createpdf.domain.Pdf
import com.example.docscanner.feature_createpdf.domain.usecase.PdfUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

@HiltViewModel
class PdfViewerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val pdfUseCase: PdfUseCase
) : ViewModel(){

    private val _state = MutableStateFlow(Pdf())
    val state = _state.asStateFlow()

    init{
        val id = savedStateHandle.get<String>("_id")
        id?.let {
            val objectId = ObjectId(hexString = id)
            getPdf(objectId)
        }
    }

    private fun getPdf(id: ObjectId){
        pdfUseCase.getPdfUseCase.invoke(id)?.let {pdf->
            _state.update { pdf }
        }
    }
}