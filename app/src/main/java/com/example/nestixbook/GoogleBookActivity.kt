package com.example.nestixbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.nestixbook.api.RetrofitClient
import com.example.nestixbook.storage.PreferenceManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_google_book.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoogleBookActivity : AppCompatActivity() {

    private lateinit var preference: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_book)

        preference = PreferenceManager(applicationContext)
        val token = preference.getPrefs()

        val book = intent.getParcelableExtra<Book>("bookFromGoogle")
        google_txtTitle.text = book?.title
        google_txtYear.text = book?.year
        google_txtGenre.text = book?.genre
        google_txtAuthor.text = book?.author
        google_txtIsbn.text = book?.isbn
        if (book?.desc != null) {
            google_txtDetail.text = book.desc.replace("<br />", "")
        } else {
            google_txtDetail.text = "No information for this media."
        }

        if (!book?.url.isNullOrEmpty()) {
            Picasso.get().load(book?.url).resize(180, 200).into(google_poster)
        } else {
            Picasso.get().load("https://laravel.sduytran.fr/public/img/no_image.png").resize(180, 200).into(google_poster)
        }

        google_proposition.setOnClickListener {
            val title = book?.title
            val year = book?.year
            val isbn = book?.isbn
            val detail = book.desc ?: "NULL"

            RetrofitClient.instance.proposeBook(token, title, year, isbn, detail ).enqueue(object: Callback<PropositionResponse> {
                override fun onFailure(call: Call<PropositionResponse>, t: Throwable) {
                    Log.i("GoogleBookActivity", "${t.message}")
                }

                override fun onResponse(
                    call: Call<PropositionResponse>,
                    response: Response<PropositionResponse>
                ) {
                    if (response.isSuccessful) {

                        val res = response.body()

                        if (res != null) {
                            if (res.error == false && res.message == "Create successful") {

                                Toast.makeText(applicationContext, "Thank you for your suggestion", Toast.LENGTH_LONG).show()
                                val intent = Intent(applicationContext, BookListActivity::class.java)
                                startActivity(intent)
                                finish()

                            } else {

                                Toast.makeText(applicationContext, "Sorry an error occurred ! }", Toast.LENGTH_LONG).show()
                                val intent = Intent(applicationContext, BookListActivity::class.java)
                                startActivity(intent)
                                finish()

                            }
                        }


                    }
                }

            })
        }


    }
}
