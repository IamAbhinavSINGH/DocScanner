package com.example.docscanner.feature_createpdf.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}