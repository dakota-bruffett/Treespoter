package com.example.trees

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Tree_View_model:ViewModel() {
    private val db = Firebase.firestore
    private val treeCollectionReference = db.collection("trees")

    val latestTree= MutableLiveData<List<Tree>>()

    private val latestTreeListener =  treeCollectionReference
       .orderBy("dataSpotted", Query.Direction.DESCENDING)
        .limit(10)
        .addSnapshotListener{snapshot,error ->
            if (error != null){
                Log.e(TAG, "This is a error", error)
        }
            else if (snapshot != null){
          //      val trees = snapshot.toObjects(Tree::class.java)
                val trees = mutableListOf<Tree>()
                for (treeDocument in snapshot){
                    val tree = treeDocument.toObject(Tree::class.java)
                tree.documentReference = treeDocument.reference
                trees.add(tree)
                Log.d(TAG, "Trees from Firebase , $trees")
            }
}
    fun setisFavorite(tree: Tree, favorite: Boolean?){
    Log.d(TAG,"Updateing the tree, $tree, to favorite $favorite")
        tree.documentReference?.update("favorite",favorite)
    }

        }
    fun addTree(tree: Tree){
        treeCollectionReference.add(tree)
            .addOnSuccessListener { treedocumentReference ->
                Log.d(TAG,"new Tree added at ${treedocumentReference.path}")
            }
            .addOnFailureListener{error->
                Log.e(TAG, "Error adding Trees $tree,$error")
            }
    }

}