package com.example.nestixbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nestixbook.util.InputValidator
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    private var checked:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        radio_group.setOnCheckedChangeListener {group, checkedId ->
            when(checkedId) {
                R.id.search_title -> checked = search_title.text.toString()
                R.id.search_artist -> checked = search_artist.text.toString()
                R.id.search_genre -> checked = search_genre.text.toString()

            }
        }

        submit_search.setOnClickListener {
            val search = txtsearch.text.toString()
            if( checked.isNullOrEmpty()) {
                search_title.error = "Required"
                search_genre.error = "Required"
                search_artist.error = "Required"
                search_title.requestFocus()
                return@setOnClickListener
            }
            if (search.isEmpty() && !InputValidator.isInputNameValid(search)) {
                txtsearch.error = "Required"
                txtsearch.requestFocus()
                return@setOnClickListener
            }

            val intent = Intent(this, BookListActivity::class.java)
            intent.putExtra("search", search)
            intent.putExtra("critere", checked)
            startActivity(intent)
            finish()

        }
    }
}
