package com.example.docscanner.feature_createpdf.domain.util

sealed class PdfOrder(val orderType: OrderType) {
    class Title(orderType: OrderType): PdfOrder(orderType = orderType)
    class Description(orderType: OrderType): PdfOrder(orderType = orderType)
    class Date(orderType: OrderType): PdfOrder(orderType = orderType)

    fun copy(orderType: OrderType): PdfOrder{
        return when(this){
            is Description -> Description(orderType)
            is Date -> Date(orderType)
            is Title -> Title(orderType)
        }
    }
}
