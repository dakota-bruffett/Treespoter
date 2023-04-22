package com.example.trees

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val trees = "TreelistFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [trees_list_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class trees_list_fragment : Fragment() {
    private val treeViewModel: Tree_View_model by lazy {
        ViewModelProvider(requireActivity()).get(Tree_View_model::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val recycleViewer = inflater.inflate(R.layout.fragment_trees_list, container, false)
        if (recycleViewer !is RecyclerView) {
            throw RuntimeException("Tree Should be in Recycler viewer")
        }

            val trees = listOf<Tree>()
            val adapter = TreeRecycleViewer(trees) { tree, isfavorite ->

            }
        recycleViewer.layoutManager = LinearLayoutManager(context)
        recycleViewer.adapter = adapter
        treeViewModel.latestTree.observe(requireActivity()){treelist ->
            adapter.trees = treelist
            adapter.notifyDataSetChanged()
        }
            return recycleViewer

    }

    companion object {

        @JvmStatic
        fun newInstance() = trees_list_fragment

                }
            }


