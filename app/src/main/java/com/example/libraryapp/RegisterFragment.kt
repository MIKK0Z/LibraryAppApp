package com.example.libraryapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.libraryapp.databinding.FragmentRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (userLogin != "" && userID != "") {
            findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
        }

        binding.goToLoginFromRegisterBtn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.registerBtn.setOnClickListener {
            val loginValue = binding.loginRInput.text.toString()
            val passwordValue = binding.passwordRInput.text.toString()

            if (loginValue != "" && passwordValue != "") {
                register(loginValue, passwordValue)
            }
        }
    }

    private fun register(login: String, password: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

        val apiService = retrofit.create(ApiInterface::class.java)

        val body = mapOf(
            "login" to login,
            "password" to password,
        )

        apiService.register(body).enqueue(object: Callback<AuthResponse?> {
            override fun onResponse(
                call: Call<AuthResponse?>,
                response: Response<AuthResponse?>,
            ) {
                if (response.body()?.id != null) {
                    userID = response.body()?.id.toString()
                    userLogin = login

                    findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                } else {
                    binding.registerError.text = "Such user exists"
                }
            }

            override fun onFailure(call: Call<AuthResponse?>, t: Throwable) {
                binding.registerError.text = "Something went wrong"
            }
        })
    }
}