package com.example.nestixbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nestixbook.api.RetrofitClient
import com.example.nestixbook.storage.PreferenceManager
import com.example.nestixbook.util.InputValidator
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class MainActivity : AppCompatActivity() {

    private lateinit var preference: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // init Token login
        preference = PreferenceManager(applicationContext)
        // Check token with web service, first check if preference is not null
        preference.getPrefs()?.let {
            RetrofitClient.instance.checkLogin(it)
                .enqueue(object: Callback<LoginResponse>{
                    // Do something if get error
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        // Show toast message error
                        Toast.makeText(applicationContext, "Problem connection", Toast.LENGTH_LONG).show()
                    }
                    // Response success
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        // if token valid , no need login, redirect to book list activity
                        if (response.isSuccessful) {
                            val loginResponse = response.body()
                            if(loginResponse?.error == false && loginResponse?.message == "Login successful") {
                                val intent = Intent(applicationContext, BookListActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }

                })
        }

        //Link to register
        lnkRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Button login
        btnLogin.setOnClickListener{
            val email = txtEmail.text.toString().trim()
            val password = txtPwd.text.toString().trim()

            if(email.isEmpty() || !InputValidator.isEmailValid(email)) {
                txtEmail.error = "Email is required"
                txtEmail.requestFocus()
                return@setOnClickListener

            }
            if (password.isEmpty()) {
                txtPwd.error = "Password is required"
                txtPwd.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.userLogin(email, password)
                .enqueue(object : Callback<LoginResponse>{
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.i("LoginActivity", t.message)
                    }

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            val loginResponse = response.body()

                            if (loginResponse?.error == false && loginResponse.message == "Login successful") {
                                Toast.makeText(applicationContext, loginResponse?.message, Toast.LENGTH_LONG).show()
                                val user = loginResponse.user
                                user?.token?.let {  preference.putPrefs(it) }
                                val intent = Intent(applicationContext, BookListActivity::class.java)
                                startActivity(intent)
                                finish()

                            }
                            else {

                                Toast.makeText(applicationContext, loginResponse?.message, Toast.LENGTH_LONG).show()
                            }

                        }

                    }

                })
        }

    }
}
