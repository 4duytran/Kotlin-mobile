package com.example.nestixbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nestixbook.api.RetrofitClient
import com.example.nestixbook.util.InputValidator
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnCreateAccount.setOnClickListener{
            val first_name = txtFirstName.text.toString().trim();
            val last_name = txtLastName.text.toString().trim()
            val email = txtEmail.text.toString().trim()
            val password = txtPwd.text.toString().trim()

            if (first_name.isEmpty() || !InputValidator.isInputValid(first_name)) {
                txtFirstName.error = "First name is required"
                txtFirstName.requestFocus()
                return@setOnClickListener
            }
            if (last_name.isEmpty() || !InputValidator.isInputValid(last_name)) {
                txtLastName.error = "Last name is required"
                txtLastName.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty() || !InputValidator.isEmailValid(email)) {
                txtEmail.error = "Emai missing or wrong format"
                txtEmail.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty() || !InputValidator.isInputPassValid(password)) {
                txtPwd.error = "Password is required or wrong format"
                txtPwd.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.register(first_name, last_name, email, password).enqueue(object : Callback<RegisterResponse>{
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {

                        val registerResponse = response.body()
                        if (registerResponse?.error == false && registerResponse.message == "Register successful") {
                            Toast.makeText(applicationContext, registerResponse.message, Toast.LENGTH_LONG).show()
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()

                        } else {
                            Toast.makeText(applicationContext, registerResponse?.message, Toast.LENGTH_LONG).show()
                        }

                    }
                }

            })
        }

        // Go to page Login
        lnkLogin.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
