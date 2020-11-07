package com.example.nestixbook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nestixbook.api.GoogleRetrofitClient
import com.example.nestixbook.api.RetrofitClient
import com.example.nestixbook.storage.PreferenceManager
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_book__list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BookListActivity : AppCompatActivity() {

    private lateinit var preference: PreferenceManager
    private lateinit var  recyclerView:RecyclerView
    private lateinit var adater:BookAdater

    //  method principal in activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set layout for this activity
        setContentView(R.layout.activity_book__list)
        // Get token secret from Shared preference
        preference = PreferenceManager(applicationContext)
        val token = preference.getPrefs()
        // Init recyclerview fot the book list
        recyclerView = findViewById<RecyclerView>(R.id.book_list)
        // Set layout for recyclerview
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)

        // Check if we got intent come from Collection
        if (intent.hasExtra("bookListFromCollection")) {
            val collection = intent.getParcelableExtra<BookCollection>("bookListFromCollection")
            val collectionId = collection?.id
            // Instance Retrofit object with request fro Api Class
            RetrofitClient.instance.bookListFromCollection(token,collectionId).enqueue(object : Callback<List<Book>>,
                BookAdater.BookItemListener {
                // Do something if get fail response
                override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                    Log.i("bookListFromCollection", "${t.message}")
                }
                // Do something if get success response
                override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                    if (response.isSuccessful) {
                        adater = response.body()?.let { BookAdater(it, this) }!!
                        recyclerView.adapter=adater

                    }

                }

                override fun onBookSelected(book: Book) {
                    val intent = Intent(applicationContext, BookDetailActivity::class.java)
                    intent.putExtra("bookDetail", book)
                    intent.putExtra("collectionId", collectionId)
                    intent.putExtra("delete", true)
                    startActivity(intent)

                }

            })

            del_collection_item.show() // Show button del in layout
            // Set onclick for the button
            del_collection_item.setOnClickListener {
                val dialog = collection?.name?.let {ConfirmDeleteCollection(it) }
                // Check if user choice yes or no
                if (dialog != null) {
                    dialog.listener = object : ConfirmDeleteCollection.ConfirmDialog{
                        // Method del collection
                        override fun onDialogPosClick() {
                            // Instance Retrofit client with param
                            RetrofitClient.instance.delCollection(token, collection.id, delete = true).enqueue(object: Callback<CollectionResponse>{
                                // Message if got fail response
                                override fun onFailure(
                                    call: Call<CollectionResponse>,
                                    t: Throwable
                                ) {
                                    Log.i("bookListDelCollection", "${t.message}")
                                }
                                // Message if got success response
                                override fun onResponse(
                                    call: Call<CollectionResponse>,
                                    response: Response<CollectionResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        val response = response.body()
                                        if (response?.error == false && response.message == "Deleted successful") {
                                            Toast.makeText(applicationContext, response.message, Toast.LENGTH_LONG).show()
                                            val intent = Intent(applicationContext, CollectionActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                    }
                                }

                            })
                        }

                        override fun onDialogNegClick() {
                            val intent = Intent(applicationContext, CollectionActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                    }

                    dialog.show(supportFragmentManager, "Confirm delete")
                }
            }

        }

        else if (intent.hasExtra("search")) {

            val extra: Bundle? = intent.extras
            val name = extra?.getString("search")
            val critere = extra?.getString("critere")

            RetrofitClient.instance.searchBook(token, name, critere).enqueue(object : Callback<List<Book>>,
                BookAdater.BookItemListener {
                override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                    Log.i("BookDetail", "${t.message}")
                }

                override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                    if (response.isSuccessful) {
                        if (response.body().isNullOrEmpty()) {
                            search_not_found.visibility = View.VISIBLE
                        } else {
                            adater = response.body()?.let { BookAdater(it, this) }!!
                            recyclerView.adapter=adater

                        }

                    }

                }

                override fun onBookSelected(book: Book) {
                    val intent = Intent(applicationContext, BookDetailActivity::class.java)
                    intent.putExtra("bookDetail", book)
                    startActivity(intent)

                }

            })

            del_collection_item.hide()

        }

        else {

            RetrofitClient.instance.bookList(token).enqueue(object : Callback<List<Book>>,
                BookAdater.BookItemListener {
                override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                    Log.i("booklist", "Error: ${t.message}")
                }

                override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                    if (response.isSuccessful) {
                        adater = response.body()?.let { BookAdater(it, this) }!!
                        recyclerView.adapter=adater


                    }

                }

                override fun onBookSelected(book: Book) {
                    val intent = Intent(applicationContext, BookDetailActivity::class.java)
                    intent.putExtra("bookDetail", book)
                    startActivity(intent)

                }

            })

            del_collection_item.hide()

        }



    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                preference.clear()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            R.id.action_bookmark -> {
                val intent = Intent(this, CollectionActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_home -> {
                val intent = Intent(applicationContext, BookListActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            R.id.action_search -> {
                val intent = Intent(applicationContext, SearchActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.action_scan -> {
                val scan = IntentIntegrator(this)
                scan.initiateScan()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                } else {

                    Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG)
                        .show()

                    // Test API Goole

                    GoogleRetrofitClient.instance.googleSearch(result.contents).enqueue(object: Callback<GoogleBookResponse> {
                        override fun onFailure(call: Call<GoogleBookResponse>, t: Throwable) {
                            Log.i("google", "Response: ${t.message}")
                        }

                        override fun onResponse(call: Call<GoogleBookResponse>, response: Response<GoogleBookResponse>) {

                            if (response.isSuccessful) {

                                if (!response.body()?.items.isNullOrEmpty()) {

                                    val title = response.body()?.items?.get(0)?.volumeInfo?.title
                                    val year = response.body()?.items?.get(0)?.volumeInfo?.publishedDate
                                    val genre = response.body()?.items?.get(0)?.volumeInfo?.categories?.joinToString()
                                    val author = response.body()?.items?.get(0)?.volumeInfo?.authors?.joinToString()
                                    val isbn = result.contents
                                    val image = response.body()?.items?.get(0)?.volumeInfo?.imageLinks?.thumbnail?.replace("http", "https")
                                    val desc = response.body()?.items?.get(0)?.volumeInfo?.description

                                    val googleBook = Book(0, title, isbn, year, genre, desc, author, image)
                                    val intent = Intent(applicationContext, GoogleBookActivity::class.java)

                                    intent.putExtra("bookFromGoogle", googleBook)
                                    startActivity(intent)


                                }

                            }

                        }
                    })
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }


}
