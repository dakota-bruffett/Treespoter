package com.example.trees

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TreeRecycleViewer(var trees: List<Tree>, val SquireboxListener: (Tree, Boolean) -> Unit):
RecyclerView.Adapter<TreeRecycleViewer.ViewHolder>(){
    inner class ViewHolder (private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(tree: Tree) {
            view.findViewById<TextView>(R.id.tree_name).text = tree.name
            view.findViewById<TextView>(R.id.date_spotted).text = "${tree.dateSpotted}"
            view.findViewById<CheckBox>(R.id.Squire_Check).apply {
                isChecked= tree.favorite?: false
                setOnCheckedChangeListener{checkbox, ischecked ->
                    SquireboxListener(tree, ischecked)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_tree_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trees.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tree = trees[position]
        holder.bind(tree)
    }

}