package com.example.nestixbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CollectionAdater(private val bookCollection: List<BookCollection>,
                       private val itemClickListener: CollectionAdater.CollectionItemListener): RecyclerView.Adapter<CollectionAdater.ViewHolder>(),
    View.OnClickListener {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardview = itemView.findViewById<CardView>(R.id.card_view_collection)
        val name = itemView.findViewById<TextView>(R.id.collection_title)

    }

    interface CollectionItemListener {

        fun onCollectionSelected(bookCollection: BookCollection)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewItem = inflater.inflate(R.layout.collection_list_item, parent, false)
        return ViewHolder(viewItem)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val collection = bookCollection[position]

        with(holder) {
            name.text = collection.name
            cardview.setOnClickListener(this@CollectionAdater)
            cardview.tag = collection
        }

    }

    override fun getItemCount(): Int {
        return bookCollection.size
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.card_view_collection -> itemClickListener.onCollectionSelected(view.tag as BookCollection)
        }
    }
}