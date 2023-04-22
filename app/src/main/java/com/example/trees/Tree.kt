package com.example.trees

import android.location.Location
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import java.util.*

data class Tree(val name: String? = null,
                val dateSpotted: Date? = null,
                val favorite: Boolean? = null,
                val location:GeoPoint? = null,
                var documentReference: DocumentReference) {
}