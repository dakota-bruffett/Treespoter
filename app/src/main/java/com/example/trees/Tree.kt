package com.example.trees

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.GeoPoint
import java.util.*

data class Tree(
    val name: String? = null,
    val dateSpotted: Date? = null,
    val favorite: Boolean? = null,
    val location: GeoPoint? = null,
    @get:Exclude @set:Exclude var documentReference: DocumentReference? = null
) {
}