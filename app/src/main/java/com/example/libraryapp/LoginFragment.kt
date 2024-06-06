package com.example.libraryapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.libraryapp.databinding.FragmentLoginBinding
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Response

import android.util.Log

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (userLogin != "" && userID != "") {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }

        binding.goToRegisterFromLoginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.loginBtn.setOnClickListener {
            val loginValue = binding.loginInput.text.toString()
            val passwordValue = binding.passwordInput.text.toString()

            if (loginValue != "" && passwordValue != "") {
                login(loginValue, passwordValue)
            }
        }
    }

    private fun login(login: String, password: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

        val apiService = retrofit.create(ApiInterface::class.java)

        val body = mapOf(
            "login" to login,
            "password" to password,
        )

        apiService.login(body).enqueue(object: Callback<AuthResponse?> {
            override fun onResponse(
                call: Call<AuthResponse?>,
                response: Response<AuthResponse?>
            ) {
                if (response.body()?.id != null) {
                    userID = response.body()?.id.toString()
                    userLogin = login

                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                } else {
                    binding.loginError.text = "Wrong credentials"
                }
            }

            override fun onFailure(call: Call<AuthResponse?>, t: Throwable) {
                binding.loginError.text = "Something went wrong"
            }
        })
    }
}