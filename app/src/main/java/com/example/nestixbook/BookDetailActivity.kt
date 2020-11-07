package com.example.nestixbook

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.nestixbook.api.RetrofitClient
import com.example.nestixbook.storage.PreferenceManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_book_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class BookDetailActivity : AppCompatActivity() {

    private lateinit var preference: PreferenceManager


    // Method principal in one activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set layout for this activity
        setContentView(R.layout.activity_book_detail)
        // Get token secret from Shared preference
        preference = PreferenceManager(applicationContext)
        val token = preference.getPrefs()

        // verification if have intent, if yes we will get objet with parcelable
        val book = intent.getParcelableExtra<Book>("bookDetail")
        txtTitle.text = book?.title
        txtYear.text = book?.year
        txtGenre.text = book?.genre
        txtAuthor.text = book?.author
        txtIsbn.text = book?.isbn
        if (book?.desc != null) {
            txtDetail.text = book.desc.replace("<br />", "")
        } else {
            txtDetail.text = "No information for this media."
        }
        // check if object is not null
        if (!book?.url.isNullOrEmpty()) {
            Picasso.get().load("https://laravel.sduytran.fr/public/img/"+book?.url).resize(180, 200).into(poster)
        } else {
            Picasso.get().load("https://laravel.sduytran.fr/public/img/no_image.png").resize(180, 200).into(poster)
        }


        if (intent.hasExtra("collectionId") && intent.hasExtra("delete")) {
            val extra : Bundle? = intent.extras
            val collectionId = extra?.getInt("collectionId")
            add_collection.hide()
            del_collection.setOnClickListener {
                val dag = book?.title?.let { ConfirmDeleteBookCollection(it) }
                if (dag != null) {
                    dag.listener = object: ConfirmDeleteBookCollection.ConfirmDialog {
                        override fun onDialogPosClick() {
                            RetrofitClient.instance.deldBookCollection(token, collectionId, book.id, delete = true).enqueue(object: Callback<CollectionResponse> {
                                override fun onFailure(
                                    call: Call<CollectionResponse>,
                                    t: Throwable
                                ) {
                                    Log.i("collection", "Erro: ${t.message}")
                                }

                                override fun onResponse(
                                    call: Call<CollectionResponse>,
                                    response: Response<CollectionResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        val res = response.body()
                                        if (res?.error == false && res.message == "Deleted successful") {
                                            Toast.makeText(applicationContext, res.message, Toast.LENGTH_LONG).show()

                                            val intent = Intent(intent)
                                            intent.removeExtra("delete")
                                            intent.removeExtra("collectionId")
                                            finish();
                                            startActivity(intent)

                                        }
                                    }
                                }

                            })
                        }

                        override fun onDialogNegClick() {
                            Toast.makeText(applicationContext, "Delete cancelled", Toast.LENGTH_LONG).show()
                        }

                    }

                    dag.show(supportFragmentManager, "Confirmation")
                }
            }


        } else {

            del_collection.hide()
            add_collection.setOnClickListener {
                val dag = ConfirmBookCollection()
                dag.listener = object:ConfirmBookCollection.ConfirmDialog{
                    override fun onDialogPosClick() {
                        val intent = Intent(applicationContext, CollectionActivity::class.java)
                        intent.putExtra("bookId", book?.id)
                        startActivity(intent)
                    }

                    override fun onDialogNegClick() {
                        Toast.makeText(applicationContext, "Add cancelled", Toast.LENGTH_LONG).show()
                    }

                }
                dag.show(supportFragmentManager, "Confirmation")

            }

        }



        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


}
