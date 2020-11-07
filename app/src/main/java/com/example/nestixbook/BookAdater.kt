package com.example.nestixbook

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.security.AccessController.getContext

class BookAdater(private val book: List<Book>,
                 private val itemClickListener: BookAdater.BookItemListener): RecyclerView.Adapter<BookAdater.ViewHolder>(),
    View.OnClickListener {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardview = itemView.findViewById<CardView>(R.id.card_view)
        val pic = itemView.findViewById<ImageView>(R.id.icon)
        val title = itemView.findViewById<TextView>(R.id.book_title)
        val year = itemView.findViewById<TextView>(R.id.year)
        val genre = itemView.findViewById<TextView>(R.id.genre)

    }

    interface BookItemListener {

        fun onBookSelected(book: Book)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewItem = inflater.inflate(R.layout.book_list_item, parent, false)
        return ViewHolder(viewItem)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = book[position]
        holder.title.text = book.title
        holder.year.text = "Date sortie: "+book.year


        if (!book.genre.isNullOrEmpty()) {
            holder.genre.text = "Genre: "+book.genre
        }

        if (!book.url.isNullOrEmpty()) {
            Picasso.get().load("https://laravel.sduytran.fr/public/img/"+book.url).resize(180, 180).into(holder.pic)
        } else {
            Picasso.get().load("https://laravel.sduytran.fr/public/img/no_image.png").resize(180, 180).into(holder.pic)
        }

        holder.cardview?.setOnClickListener(this@BookAdater)
        holder.cardview.tag = book

    }

    override fun getItemCount(): Int {
        return book.size
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.card_view -> itemClickListener.onBookSelected(view.tag as Book)
        }
    }
}