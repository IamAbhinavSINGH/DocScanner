package com.example.docscanner.feature_createpdf.domain

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.FullText
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Pdf : RealmObject {

    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    @FullText
    var titleName: String = ""
    @FullText
    var description: String = ""

    var uri: String = ""

    var thumbnailUri: String = ""

    var timeStamp: RealmInstant = RealmInstant.now()

    var isSearched: Boolean = false

    var searchedAt: RealmInstant? = null

    var isArchived: Boolean = false

    var archivedAt: RealmInstant? = null
}