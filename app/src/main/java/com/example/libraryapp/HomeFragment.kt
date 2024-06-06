package com.example.libraryapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.libraryapp.databinding.FragmentHomeBinding
import io.github.g00fy2.quickie.ScanQRCode
import io.github.g00fy2.quickie.QRResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val scanQRCodeLauncher = registerForActivityResult(ScanQRCode()) { result ->
        if (result is QRResult.QRSuccess) {
            addBook(userID, result.content.rawValue.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



//    TODO()
        getBooks(userID)

        binding.openQrCodeScannerBtn.setOnClickListener { scanQRCodeLauncher.launch(null) }
    }

    private fun addBook(userID: String, bookID: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

        val apiService = retrofit.create(ApiInterface::class.java)

        val body = mapOf(
            "userID" to userID,
            "bookID" to bookID,
        )

        apiService.addBook(body).enqueue(object: Callback<BooksResponse> {
            override fun onResponse(
                call: Call<BooksResponse>,
                response: Response<BooksResponse>
            ) {
                books = response.body() ?: BooksResponse()
            }

            override fun onFailure(call: Call<BooksResponse>, t: Throwable) {
                Log.d("fetch", t.toString())
            }
        })
    }

    private fun getBooks(id: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

        val apiService = retrofit.create(ApiInterface::class.java)

        apiService.getBooks(id).enqueue(object: Callback<BooksResponse?> {
            override fun onResponse(
                call: Call<BooksResponse?>,
                response: Response<BooksResponse?>
            ) {
                books = response.body() ?: BooksResponse()
                Log.d("fetch", books.toString())
            }

            override fun onFailure(call: Call<BooksResponse?>, t: Throwable) {
                Log.d("fetch", "books fetch failed")
            }
        })
    }
}