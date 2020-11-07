package com.example.nestixbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nestixbook.api.RetrofitClient
import com.example.nestixbook.storage.PreferenceManager
import com.example.nestixbook.util.InputValidator
import kotlinx.android.synthetic.main.activity_collection.*
import kotlinx.android.synthetic.main.new_collection.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CollectionActivity : AppCompatActivity() {

    private lateinit var preference: PreferenceManager
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)


        preference = PreferenceManager(applicationContext)
        val token = preference.getPrefs()

        recyclerView = findViewById<RecyclerView>(R.id.collection_list)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)

        RetrofitClient.instance.collectionList(token).enqueue(object:
            Callback<List<BookCollection>>, CollectionAdater.CollectionItemListener {
            override fun onFailure(call: Call<List<BookCollection>>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(
                call: Call<List<BookCollection>>,
                response: Response<List<BookCollection>>
            ) {
                if (response.isSuccessful) {
                    val adater = response.body()?.let {CollectionAdater(it, this)}
                    recyclerView.adapter = adater

                }
            }

            override fun onCollectionSelected(bookCollection: BookCollection) {
                if (intent.hasExtra("bookId")) {
                    val extra:Bundle? = intent.extras
                    val bookId = extra?.getInt("bookId")
                    RetrofitClient.instance.addBookCollection(token, bookCollection.id, bookId).enqueue(object: Callback<CollectionResponse>{
                        override fun onFailure(call: Call<CollectionResponse>, t: Throwable) {
                            Log.i("CollectionActivity", "Error: ${t.message}")
                        }

                        override fun onResponse(
                            call: Call<CollectionResponse>,
                            response: Response<CollectionResponse>
                        ) {
                            if (response.isSuccessful) {
                            val response = response.body()
                            Toast.makeText(applicationContext, response?.message, Toast.LENGTH_LONG).show()
                        }
                        }

                    })
                }
                val intent = Intent(applicationContext, BookListActivity::class.java)
                intent.putExtra("bookListFromCollection", bookCollection)

                startActivity(intent)
                finish()

            }

        })

        create_collection.setOnClickListener {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.new_collection, null)
            val builder = AlertDialog.Builder(this)
                .setView(dialogView)
            val dialog = builder.show()

            dialogView.cancel_new_collection.setOnClickListener {
                dialog.dismiss()
            }

            dialogView.accept_new_collection.setOnClickListener {
                val collectionName = dialogView.txtNameCollection.text.toString()
                if (collectionName.isEmpty() || !InputValidator.isInputNameValid(collectionName)) {
                    dialogView.txtNameCollection.error = "Collection name is required"
                    dialogView.txtNameCollection.requestFocus()
                    return@setOnClickListener
                }

                RetrofitClient.instance.createNewCollection(token, collectionName, create = true).enqueue(object : Callback<CollectionResponse>{
                    override fun onFailure(call: Call<CollectionResponse>, t: Throwable) {
                        Log.i("collection", t.message)
                    }

                    override fun onResponse(
                        call: Call<CollectionResponse>,
                        response: Response<CollectionResponse>
                    ) {
                        if (response.isSuccessful) {
                            val response = response.body()
                            if(response?.error == false && response.message == "Create successful") {
                                Toast.makeText(applicationContext, response.message, Toast.LENGTH_LONG).show()
                                dialog.dismiss()
                                finish()
                                startActivity(intent)
                            }
                        }
                    }

                })

            }



        }



    }
}
